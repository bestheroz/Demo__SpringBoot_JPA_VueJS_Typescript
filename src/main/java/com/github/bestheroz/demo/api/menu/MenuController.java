package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.menu.MenuRepository;
import com.github.bestheroz.standard.common.response.ApiResult;
import com.github.bestheroz.standard.common.response.Result;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/menus")
public class MenuController {
  @Resource private MenuService menuService;
  @Resource private MenuRepository menuRepository;

  @GetMapping(value = "drawer")
  ResponseEntity<ApiResult> getItems() {
    //    return Result.ok(
    //        this.menuService.getDrawerList(AuthenticationUtils.getLoginVO().getAuthority()));
    return Result.ok(
        this.menuRepository.findAll(Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name")));
  }
}
