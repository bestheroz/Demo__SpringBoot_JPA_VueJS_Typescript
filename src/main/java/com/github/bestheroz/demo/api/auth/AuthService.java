package com.github.bestheroz.demo.api.auth;

import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.authenticate.UserVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.time.Instant;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class AuthService implements UserDetailsService {
  @Resource private TableMemberRepository tableMemberRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("No user found");
    }
    return this.tableMemberRepository
        .findById(username)
        .map(
            tableMemberEntity ->
                new UserVO(
                    tableMemberEntity.getId(),
                    tableMemberEntity.getName(),
                    tableMemberEntity.getAuthority(),
                    tableMemberEntity.getTheme()))
        .orElseThrow(() -> new UsernameNotFoundException("No user found by `" + username + "`"));
  }

  Map<String, String> login(final String id, final String password) {
    return this.tableMemberRepository
        .findById(id)
        .map(
            tableMemberEntity -> {
              // 2. 패스워드를 생성한 적이 없으면
              if (StringUtils.isEmpty(tableMemberEntity.getPassword())) {
                log.info(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD.toString());
                throw new BusinessException(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD);
              }

              final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
              // 3. 패스워드가 틀리면
              if (!pbkdf2PasswordEncoder.matches(
                  tableMemberEntity.getPassword(), pbkdf2PasswordEncoder.encode(password))) {
                tableMemberEntity.setLoginFailCnt(tableMemberEntity.getLoginFailCnt() + 1);
                this.tableMemberRepository.save(tableMemberEntity);
                throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
              }

              // 4. LOGIN_FAIL_CNT가 5회 이상 인가
              if (tableMemberEntity.getLoginFailCnt() >= 5) {
                throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
              }

              // 5. 계정 차단된 상태인가
              if (!tableMemberEntity.isAvailable()
                  || tableMemberEntity.getExpired().toEpochMilli() < Instant.now().toEpochMilli()) {
                throw new BusinessException(ExceptionCode.FAIL_LOGIN_CLOSED);
              }

              tableMemberEntity.setLoginFailCnt(0);
              final UserVO userVO = new UserVO();
              BeanUtils.copyProperties(tableMemberEntity, userVO);
              final String accessToken = JwtTokenProvider.createAccessToken(userVO);
              final String refreshToken = JwtTokenProvider.createRefreshToken(userVO);
              SecurityContextHolder.getContext()
                  .setAuthentication(JwtTokenProvider.getAuthentication(accessToken));
              tableMemberEntity.setToken(refreshToken);
              this.tableMemberRepository.save(tableMemberEntity);
              return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
            })
        .orElseThrow(
            () -> {
              // 1. 유저가 없으면
              return new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
            });
  }

  void logout() {
    this.tableMemberRepository
        .findById(AuthenticationUtils.getUserPk())
        .ifPresent(
            item -> {
              item.setToken(null);
              this.tableMemberRepository.save(item);
            });
  }

  String getNewAccessToken(final String refreshToken) {
    return this.tableMemberRepository
        .findByIdAndToken(JwtTokenProvider.getUserPk(refreshToken), refreshToken)
        .map(
            tableMemberEntity -> {
              final UserVO userVO = new UserVO();
              BeanUtils.copyProperties(tableMemberEntity, userVO);
              final String newAccessToken = JwtTokenProvider.createAccessToken(userVO);
              SecurityContextHolder.getContext()
                  .setAuthentication(JwtTokenProvider.getAuthentication(newAccessToken));
              return newAccessToken;
            })
        .orElseThrow(
            () -> {
              log.info("invalid refresh-token");
              return BusinessException.FAIL_TRY_LOGIN_FIRST;
            });
  }

  void initPassword(final String id, final String password) {
    this.tableMemberRepository
        .findById(id)
        .ifPresentOrElse(
            tableMemberEntity -> {
              if (StringUtils.isNotEmpty(tableMemberEntity.getPassword())) {
                log.warn(ExceptionCode.FAIL_INVALID_REQUEST.toString());
                throw new BusinessException(ExceptionCode.FAIL_INVALID_REQUEST);
              }

              tableMemberEntity.setPassword(password);
              this.tableMemberRepository.save(tableMemberEntity);
            },
            () -> {
              // 1. 유저가 없으면
              log.warn(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER.toString());
              throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
            });
  }
}
