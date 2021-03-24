package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.authority.AuthorityRepository;
import com.github.bestheroz.demo.api.entity.menu.MenuRepository;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
  @Resource private MenuRepository menuRepository;
  @Resource private AuthorityRepository authorityRepository;

  //  public List<MenuVO> getDrawerList(final Integer authority) {
  //    final List<MenuVO> parents;
  //    final List<AuthorityEntity> items;
  //    if (authority.equals(999)) {
  //      items =
  //          this.menuRepository
  //              .findAll(Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name"))
  //              .stream()
  //              .map(
  //                  entity -> {
  //                    final AuthorityEntity menuEntity = new AuthorityEntity();
  //                    BeanUtils.copyProperties(entity, menuEntity);
  //                    return menuEntity;
  //                  })
  //              .collect(Collectors.toList());
  //
  //      parents =
  //          items.stream()
  //              .filter(menu -> menu.getParentId().equals(0L))
  //              .map(
  //                  item -> {
  //                    final MenuVO menuVO = new MenuVO();
  //                    BeanUtils.copyProperties(item, menuVO);
  //                    return menuVO;
  //                  })
  //              .collect(Collectors.toList());
  //    } else {
  //      items =
  //          this.authorityRepository.findAllByAuthority(
  //              authority, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name"));
  //      parents =
  //          items.stream()
  //              .filter(menu -> menu.getParentId().equals(0L))
  //              .map(
  //                  item -> {
  //                    final MenuVO menuVO = new MenuVO();
  //                    BeanUtils.copyProperties(item, menuVO);
  //                    return menuVO;
  //                  })
  //              .collect(Collectors.toList());
  //    }
  //
  //    return parents.stream()
  //        .peek(
  //            parent ->
  //                parent.setChildren(
  //                    items.stream()
  //                        .filter(menu -> menu.getParentId().equals(parent.getId()))
  //                        .map(
  //                            item -> {
  //                              final MenuVO menuVO = new MenuVO();
  //                              BeanUtils.copyProperties(item, menuVO);
  //                              return menuVO;
  //                            })
  //                        .collect(Collectors.toList())))
  //        .collect(Collectors.toList());
  //  }
}
