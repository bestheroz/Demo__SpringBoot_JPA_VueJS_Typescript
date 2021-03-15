package com.github.bestheroz.demo.api.admin.code;

import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/admin/code-groups/{groupName}/codes")
public class AdminCodeController {
  @Resource private TableCodeRepository tableCodeRepository;

  @GetMapping()
  ResponseEntity<ApiResult> getItems(@PathVariable(value = "groupName") final String groupName) {
    return Result.ok(this.tableCodeRepository.findAllByGroupName(groupName));
  }

  @PostMapping()
  public ResponseEntity<ApiResult> post(
      @PathVariable(value = "groupName") final String groupName,
      @RequestBody final TableCodeEntity payload) {
    payload.setGroupName(groupName);
    return Result.created(this.tableCodeRepository.save(payload));
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult> put(
      @PathVariable(value = "groupName") final String groupName,
      @PathVariable(value = "id") final Long id,
      @RequestBody final TableCodeEntity payload) {
    return Result.ok(
        this.tableCodeRepository
            .findByGroupNameAndId(groupName, id)
            .map(
                (item) -> {
                  BeanUtils.copyProperties(payload, item);
                  return this.tableCodeRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(
      @PathVariable(value = "groupName") final String groupName,
      @PathVariable(value = "id") final Long id) {
    return Result.ok(
        this.tableCodeRepository
            .findByGroupNameAndId(groupName, id)
            .map(
                (item) -> {
                  this.tableCodeRepository.delete(item);
                  return item;
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }
}
