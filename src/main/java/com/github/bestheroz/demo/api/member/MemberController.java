package com.github.bestheroz.demo.api.member;

import com.github.bestheroz.demo.api.entity.member.TableMemberEntity;
import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
import com.github.bestheroz.standard.common.code.CodeRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.exception.ExceptionCode;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
@Slf4j
public class MemberController {
  @Resource private TableMemberRepository tableMemberRepository;
  @Resource private CodeRepository codeRepository;

  @GetMapping(value = "codes")
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.codeRepository.getMembers());
  }

  @GetMapping(value = "mine")
  ResponseEntity<ApiResult> getMyInfo() {
    return Result.ok(
        this.tableMemberRepository
            .findByUserId(AuthenticationUtils.getUserId())
            .map(
                item -> {
                  item.setPassword(null);
                  return item;
                })
            .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER)));
  }

  @PatchMapping("mine")
  public ResponseEntity<ApiResult> editMe(@RequestBody final TableMemberEntity payload) {
    return this.tableMemberRepository
        .findByUserId(AuthenticationUtils.getUserId())
        .map(
            tableMemberEntity -> {
              final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
              // 패스워드가 틀리면
              if (!pbkdf2PasswordEncoder.matches(
                  tableMemberEntity.getPassword(),
                  pbkdf2PasswordEncoder.encode(payload.getPassword()))) {
                throw new BusinessException(ExceptionCode.FAIL_MATCH_PASSWORD);
              }
              tableMemberEntity.setName(payload.getName());
              this.tableMemberRepository.save(tableMemberEntity);
              return Result.ok();
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER));
  }

  @PostMapping(value = "mine/changePassword")
  public ResponseEntity<ApiResult> changePassword(@RequestBody final Map<String, String> payload) {
    return this.tableMemberRepository
        .findByUserId(AuthenticationUtils.getUserId())
        .map(
            tableMemberEntity -> {
              final Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
              // 패스워드가 틀리면
              if (!pbkdf2PasswordEncoder.matches(
                  tableMemberEntity.getPassword(),
                  pbkdf2PasswordEncoder.encode(payload.get("oldPassword")))) {
                throw new BusinessException(ExceptionCode.FAIL_MATCH_OLD_PASSWORD);
              }
              tableMemberEntity.setPassword(payload.get("newPassword"));
              this.tableMemberRepository.save(tableMemberEntity);
              return Result.ok();
            })
        .orElseThrow(
            () -> {
              // 1. 유저가 없으면
              return new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER);
            });
  }

  @PostMapping(value = "mine/changeTheme")
  public ResponseEntity<ApiResult> changeTheme(@RequestBody final Map<String, String> payload) {
    return this.tableMemberRepository
        .findByUserId(AuthenticationUtils.getUserId())
        .map(
            tableMemberEntity -> {
              tableMemberEntity.setTheme(payload.get("theme"));
              this.tableMemberRepository.save(tableMemberEntity);
              return Result.ok();
            })
        .orElseThrow(() -> new BusinessException(ExceptionCode.FAIL_NOT_ALLOWED_MEMBER));
  }
}
