package com.github.bestheroz.demo.api.admin.member;

import com.github.bestheroz.demo.api.entity.member.MemberEntity;
import com.github.bestheroz.demo.api.entity.member.MemberRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.filter.DataTableFilterDTO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.lang.reflect.Method;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/members")
public class AdminMemberController {
  @Resource private MemberRepository memberRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems(final DataTableFilterDTO dataTableFilterDTO) {
    log.debug("{}", dataTableFilterDTO.getFilter());

    final MemberEntity filterEntity = new MemberEntity();
    final ExampleMatcher matcher = ExampleMatcher.matching();
    Optional.ofNullable(dataTableFilterDTO.getFilter())
        .ifPresent(
            filter ->
                filter.forEach(
                    (key, value) -> {
                      final String columnName = StringUtils.substringBefore(key, ":");
                      final String methodString =
                          "set"
                              + columnName.substring(0, 1).toUpperCase()
                              + columnName.substring(1);
                      final Method[] methods = filterEntity.getClass().getDeclaredMethods();
                      for (final Method method : methods) {
                        if (methodString.equals(method.getName())) {
                          try {
                            method.invoke(filterEntity, value);
                          } catch (final Exception e) {
                            e.printStackTrace();
                          }
                        }
                      }
                      final GenericPropertyMatcher genericPropertyMatcher;
                      if ("equals"
                          .equals(
                              StringUtils.defaultString(
                                  StringUtils.substringAfter(key, ":"), "equals"))) {
                        genericPropertyMatcher = GenericPropertyMatchers.exact();
                      } else {
                        genericPropertyMatcher = GenericPropertyMatchers.contains().ignoreCase();
                      }

                      matcher.withMatcher(columnName, genericPropertyMatcher);
                    }));
    return Result.ok(this.memberRepository.findAll(Example.of(filterEntity, matcher)));
  }

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItem(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.memberRepository
            .findById(id)
            .map(
                item -> {
                  item.setPassword(null);
                  return item;
                }));
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(@RequestBody final MemberEntity payload) {
    return Result.created(this.memberRepository.save(payload));
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult> patch(
      @PathVariable(value = "id") final Long id, @RequestBody final MemberEntity payload) {
    return Result.ok(
        this.memberRepository
            .findById(id)
            .map(
                (item) -> {
                  item.setName(payload.getName());
                  item.setAuthority(payload.getAuthority());
                  item.setExpired(payload.getExpired());
                  item.setAvailable(payload.isAvailable());
                  return this.memberRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.memberRepository
            .findById(id)
            .map(
                (item) -> {
                  this.memberRepository.delete(item);
                  return item;
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @PostMapping(value = "{id}/resetPassword")
  public ResponseEntity<ApiResult> resetPassword(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.memberRepository
            .findById(id)
            .map(
                (item) -> {
                  item.setPassword(null);
                  return this.memberRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }
}
