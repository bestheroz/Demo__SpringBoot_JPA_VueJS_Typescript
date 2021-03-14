package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/member/menus/{authority}")
public class AdminMemberMenuController {
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;
  @Resource private AdminMemberMenuService adminMemberMenuService;

  @GetMapping
  ResponseEntity<ApiResult> getItems(@PathVariable(value = "authority") final Integer authority) {
    return Result.ok(
        this.tableMemberMenuRepository.findAllByAuthority(
            authority, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")));
  }

  @PostMapping(value = "save")
  public ResponseEntity<ApiResult> save(
      @PathVariable(value = "authority") final Integer authority,
      @RequestBody final List<TableMemberMenuEntity> payload) {
    return Result.created(this.adminMemberMenuService.save(authority, payload));
  }
}
