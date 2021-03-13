package com.github.bestheroz.demo.api.admin.code.group;

import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupRepository;
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
@RequestMapping(value = "api/admin/code/groups")
public class AdminCodeGroupController {
  @Resource private TableCodeGroupRepository tableCodeGroupRepository;
  @Resource private AdminCodeGroupService adminCodeGroupService;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.tableCodeGroupRepository.findAll());
  }

  @PostMapping
  ResponseEntity<ApiResult> post(@RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
    this.tableCodeGroupRepository.save(tableCodeGroupEntity);
    return Result.created();
  }

  @PutMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> put(
      @PathVariable(value = "codeGroup") final String codeGroup,
      @RequestBody final TableCodeGroupEntity tableCodeGroupEntity) {
    tableCodeGroupEntity.setCodeGroup(codeGroup);
    this.tableCodeGroupRepository.save(tableCodeGroupEntity);
    return Result.ok();
  }

  @DeleteMapping(value = "{codeGroup}")
  ResponseEntity<ApiResult> delete(@PathVariable(value = "codeGroup") final String codeGroup) {
    this.adminCodeGroupService.delete(codeGroup);
    return Result.ok();
  }
}
