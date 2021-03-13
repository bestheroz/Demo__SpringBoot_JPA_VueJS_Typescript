package com.github.bestheroz.demo.api.admin.member;

import com.github.bestheroz.demo.api.entity.member.TableMemberEntity;
import com.github.bestheroz.demo.api.entity.member.TableMemberRepository;
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
  @Resource private TableMemberRepository tableMemberRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems(final DataTableFilterDTO dataTableFilterDTO) {
    // TODO: dataTableFilter
    // final int count = this.tableMemberRepository.countForDataTable(dataTableFilterDTO);
    // return Result.ok(
    //        count > 0 ? this.tableMemberRepository.getItemsForDataTable(dataTableFilterDTO) :
    // List.of(),
    //        count);
    return Result.ok(this.tableMemberRepository.findAll(), this.tableMemberRepository.count());
  }

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItem(@PathVariable(value = "id") final String id) {
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
  public ResponseEntity<ApiResult> post(@RequestBody final TableMemberEntity tableMemberEntity) {
    this.tableMemberRepository.save(tableMemberEntity);
    return Result.created();
  }

  @PatchMapping(value = "{id}")
  public ResponseEntity<ApiResult> patch(
      @PathVariable(value = "id") final String id,
      @RequestBody final TableMemberEntity tableMemberEntity) {
    this.tableMemberRepository
        .findById(id)
        .ifPresentOrElse(
            (item) -> {
              item.setName(tableMemberEntity.getName());
              item.setAuthority(tableMemberEntity.getAuthority());
              item.setExpired(tableMemberEntity.getExpired());
              item.setAvailable(tableMemberEntity.isAvailable());
              this.tableMemberRepository.save(item);
            },
            () -> {
              throw new BusinessException(BusinessException.FAIL_NO_DATA_SUCCESS);
            });
    return Result.ok();
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final String id) {
    this.tableMemberRepository.deleteById(id);
    return Result.ok();
  }

  @PostMapping(value = "{id}/resetPassword")
  public ResponseEntity<ApiResult> resetPassword(@PathVariable(value = "id") final String id) {
    this.tableMemberRepository
        .findById(id)
        .ifPresentOrElse(
            (item) -> {
              item.setPassword(null);
              this.tableMemberRepository.save(item);
            },
            () -> {
              throw new BusinessException(BusinessException.FAIL_NO_DATA_SUCCESS);
            });
    return Result.ok();
  }
}
