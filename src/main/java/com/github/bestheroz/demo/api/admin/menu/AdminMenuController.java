package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/admin/menus")
public class AdminMenuController {
  @Resource private AdminMenuService adminMenuService;
  @Resource private TableMenuRepository tableMenuRepository;
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  @GetMapping
  ResponseEntity<ApiResult> getItems() {
    if (List.of(900, 999).contains(AuthenticationUtils.getLoginVO().getAuthority())) {
      return Result.ok(
          this.tableMenuRepository.findAll(
              Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")));
    } else {
      return Result.ok(
          this.tableMemberMenuRepository.findAllByAuthority(
              AuthenticationUtils.getLoginVO().getAuthority(),
              Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")));
    }
  }

  @PostMapping
  public ResponseEntity<ApiResult> post(@RequestBody final TableMenuEntity payload) {
    this.tableMenuRepository.save(payload);
    return Result.created(payload);
  }

  @PutMapping(value = "{id}")
  public ResponseEntity<ApiResult> put(
      @PathVariable(value = "id") final Integer id,
      @RequestBody final TableMenuEntity tableMenuEntity) {
    return Result.ok(this.adminMenuService.put(tableMenuEntity, id));
  }

  @DeleteMapping(value = "{id}")
  public ResponseEntity<ApiResult> delete(@PathVariable(value = "id") final Integer id) {
    return Result.ok(this.adminMenuService.delete(id));
  }

  @PostMapping(value = "save")
  public ResponseEntity<ApiResult> save(@RequestBody final List<TableMenuEntity> menus) {
    menus.forEach(menu -> this.tableMenuRepository.save(menu));
    return Result.created();
  }
}
