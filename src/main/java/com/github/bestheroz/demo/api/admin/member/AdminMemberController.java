package com.github.bestheroz.demo.api.admin.member;

import com.github.bestheroz.demo.api.entity.member.MemberEntity;
import com.github.bestheroz.demo.api.entity.member.MemberRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.filter.DataTableFilterDTO;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
  @Resource private MemberRepository tableMemberRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems(final DataTableFilterDTO dataTableFilterDTO) {
    return Result.ok(this.tableMemberRepository.findAll(dataTableFilterDTO.getPageRequest()));
  }

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItem(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.tableMemberRepository
            .findById(id)
            .map(
                item -> {
                  item.setPassword(null);
                  return item;
                }));
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(@RequestBody final MemberEntity payload) {
    return Result.created(this.tableMemberRepository.save(payload));
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult> patch(
      @PathVariable(value = "id") final Long id, @RequestBody final MemberEntity payload) {
    return Result.ok(
        this.tableMemberRepository
            .findById(id)
            .map(
                (item) -> {
                  item.setName(payload.getName());
                  item.setAuthority(payload.getAuthority());
                  item.setExpired(payload.getExpired());
                  item.setAvailable(payload.isAvailable());
                  return this.tableMemberRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.tableMemberRepository
            .findById(id)
            .map(
                (item) -> {
                  this.tableMemberRepository.delete(item);
                  return item;
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @PostMapping(value = "{id}/resetPassword")
  public ResponseEntity<ApiResult> resetPassword(@PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.tableMemberRepository
            .findById(id)
            .map(
                (item) -> {
                  item.setPassword(null);
                  return this.tableMemberRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }
}
