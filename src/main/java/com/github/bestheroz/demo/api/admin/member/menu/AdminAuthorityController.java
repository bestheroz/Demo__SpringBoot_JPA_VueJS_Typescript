package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.authority.AuthorityEntity;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/authorities/")
@Slf4j
public class AdminAuthorityController {
  @Resource private AdminAuthorityService adminAuthorityService;

  @GetMapping()
  ResponseEntity<ApiResult> getItems() {
    return Result.ok(this.adminAuthorityService.getItems());
  }

  //  @GetMapping(value = "{id}")
  //  ResponseEntity<ApiResult> getItems(@PathVariable(value = "id") final Long id) {
  //    return Result.ok(
  //        this.authorityRepository.findAllById(id)
  //            authority, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")))
  //  }

  @PostMapping(value = "{id}")
  public ResponseEntity<ApiResult> save(@RequestBody final AuthorityEntity payload) {
    return Result.created(this.adminAuthorityService.save(payload));
  }
}
