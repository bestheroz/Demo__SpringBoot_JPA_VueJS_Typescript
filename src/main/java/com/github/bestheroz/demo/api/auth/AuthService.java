package com.github.bestheroz.demo.api.auth;

import com.github.bestheroz.demo.api.entity.member.MemberRepository;
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
  @Resource private MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("No user found");
    }
    return this.memberRepository
        .findByUserId(username)
        .map(
            memberEntity ->
                new UserVO(
                    memberEntity.getId(),
                    memberEntity.getUserId(),
                    memberEntity.getName(),
                    memberEntity.getAuthorityId(),
                    memberEntity.getTheme()))
        .orElseThrow(() -> new UsernameNotFoundException("No user found by `" + username + "`"));
  }

  Map<String, String> login(final String userId, final String password) {
    return this.memberRepository
        .findByUserId(userId)
        .map(
            memberEntity -> {
              // 2. 패스워드를 생성한 적이 없으면
              if (StringUtils.isEmpty(memberEntity.getPassword())) {
                log.info(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD.toString());
                throw new BusinessException(ExceptionCode.SUCCESS_TRY_NEW_PASSWORD);
              }

              final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
              // 3. 패스워드가 틀리면
              if (!pbkdf2PasswordEncoder.matches(
                  memberEntity.getPassword(), pbkdf2PasswordEncoder.encode(password))) {
                memberEntity.setLoginFailCnt(memberEntity.getLoginFailCnt() + 1);
                this.memberRepository.save(memberEntity);
                throw new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
              }

              // 4. LOGIN_FAIL_CNT가 5회 이상 인가
              if (memberEntity.getLoginFailCnt() >= 5) {
                throw new BusinessException(ExceptionCode.FAIL_LOGIN_FAIL_CNT);
              }

              // 5. 계정 차단된 상태인가
              if (!memberEntity.getAvailable()
                  || memberEntity.getExpired().toEpochMilli() < Instant.now().toEpochMilli()) {
                throw new BusinessException(ExceptionCode.FAIL_LOGIN_CLOSED);
              }

              memberEntity.setLoginFailCnt(0);
              final UserVO userVO = new UserVO();
              BeanUtils.copyProperties(memberEntity, userVO);
              final String accessToken = JwtTokenProvider.createAccessToken(userVO);
              final String refreshToken = JwtTokenProvider.createRefreshToken(userVO);
              SecurityContextHolder.getContext()
                  .setAuthentication(JwtTokenProvider.getAuthentication(accessToken));
              memberEntity.setToken(refreshToken);
              this.memberRepository.save(memberEntity);
              return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER));
  }

  void logout() {
    this.memberRepository
        .findByUserId(AuthenticationUtils.getUserId())
        .ifPresent(
            item -> {
              item.setToken(null);
              this.memberRepository.save(item);
            });
  }

  String getNewAccessToken(final String refreshToken) {
    return this.memberRepository
        .findByUserIdAndToken(JwtTokenProvider.getUserId(refreshToken), refreshToken)
        .map(
            memberEntity -> {
              final UserVO userVO = new UserVO();
              BeanUtils.copyProperties(memberEntity, userVO);
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

  UserVO initPassword(final Long id, final String password) {
    return this.memberRepository
        .findById(id)
        .map(
            memberEntity -> {
              if (StringUtils.isNotEmpty(memberEntity.getPassword())) {
                log.warn(ExceptionCode.FAIL_INVALID_REQUEST.toString());
                throw new BusinessException(ExceptionCode.FAIL_INVALID_REQUEST);
              }

              memberEntity.setPassword(password);
              final UserVO userVO = new UserVO();
              BeanUtils.copyProperties(this.memberRepository.save(memberEntity), userVO);
              return userVO;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }
}
