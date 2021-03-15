package com.github.bestheroz.demo.api.admin.code;

import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeEntityId;
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
@RequestMapping(value = "api/admin/codes")
public class AdminCodeController {
  @Resource private TableCodeRepository tableCodeRepository;

  @GetMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> getItems(@PathVariable(value = "codeGroup") final String codeGroup) {
    return Result.ok(this.tableCodeRepository.findAllByCodeGroup(codeGroup));
  }

  @PostMapping(value = "{codeGroup}")
  public ResponseEntity<ApiResult> post(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @RequestBody final TableCodeEntity payload) {
    payload.setCodeGroup(codeGroup);
    return Result.created(this.tableCodeRepository.save(payload));
  }

  @PutMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> put(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @PathVariable(value = "code") final String code,
      @RequestBody final TableCodeEntity payload) {
    return Result.ok(
        this.tableCodeRepository
            .findById(new TableCodeEntityId(codeGroup, code))
            .map(
                (item) -> {
                  BeanUtils.copyProperties(payload, item);
                  return this.tableCodeRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @DeleteMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> delete(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @PathVariable(value = "code") final String code) {
    return Result.ok(
        this.tableCodeRepository
            .findById(new TableCodeEntityId(codeGroup, code))
            .map(
                (item) -> {
                  this.tableCodeRepository.deleteById(new TableCodeEntityId(codeGroup, code));
                  return item;
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }
}
