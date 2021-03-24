package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.AuthorityEntity;
import com.github.bestheroz.demo.api.entity.member.menu.AuthorityRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/authorities/")
public class AdminAuthorityController {
  @Resource private AuthorityRepository authorityRepository;
  @Resource private AdminAuthorityService adminAuthorityService;

  @GetMapping(value = "{id}")
  ResponseEntity<ApiResult> getItems(@PathVariable(value = "id") final Integer id) {
    return Result.ok(
        this.authorityRepository.findAllById(id)
            authority, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")))
  }

  @PostMapping
  public ResponseEntity<ApiResult> save(
      @RequestParam(value = "authority") final Integer authority,
      @RequestBody final List<AuthorityEntity> payload) {
    return Result.created(this.adminAuthorityService.save(authority, payload));
  }
}
