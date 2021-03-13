package com.github.bestheroz.demo.api.admin.code;

import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeEntityId;
import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
      @RequestBody final TableCodeEntity tableCodeEntity) {
    tableCodeEntity.setCodeGroup(codeGroup);
    this.tableCodeRepository.save(tableCodeEntity);
    return Result.created();
  }

  @PutMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> put(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @PathVariable(value = "code") final String code,
      @RequestBody final TableCodeEntity tableCodeEntity) {
    tableCodeEntity.setCodeGroup(codeGroup);
    tableCodeEntity.setCode(code);
    this.tableCodeRepository.save(tableCodeEntity);
    return Result.ok();
  }

  @DeleteMapping(value = "{codeGroup}/{code}")
  public ResponseEntity<ApiResult> delete(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @PathVariable(value = "code") final String code) {
    this.tableCodeRepository.deleteById(new TableCodeEntityId(codeGroup, code));
    return Result.ok();
  }
}
