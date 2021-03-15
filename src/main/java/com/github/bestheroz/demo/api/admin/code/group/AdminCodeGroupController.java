package com.github.bestheroz.demo.api.admin.code.group;

import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupRepository;
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
@RequestMapping(value = "api/admin/code/groups")
public class AdminCodeGroupController {
  @Resource private TableCodeGroupRepository tableCodeGroupRepository;
  @Resource private AdminCodeGroupService adminCodeGroupService;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.tableCodeGroupRepository.findAll());
  }

  @PostMapping
  ResponseEntity<ApiResult> post(@RequestBody final TableCodeGroupEntity payload) {
    return Result.created(this.tableCodeGroupRepository.save(payload));
  }

  @PutMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> put(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @RequestBody final TableCodeGroupEntity payload) {
    return Result.ok(
        this.tableCodeGroupRepository
            .findById(codeGroup)
            .map(
                (item) -> {
                  BeanUtils.copyProperties(payload, item);
                  return this.tableCodeGroupRepository.save(item);
                })
            .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS));
  }

  @DeleteMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup) {
    return Result.ok(this.adminCodeGroupService.delete(codeGroup));
  }
}
