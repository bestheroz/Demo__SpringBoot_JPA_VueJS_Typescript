package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
  @Resource private TableMenuRepository tableMenuRepository;
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  public List<MenuVO> getDrawerList(final Integer authority) {
    final List<MenuVO> parents;
    final List<TableMemberMenuEntity> items;
    if (authority.equals(999)) {
      items =
          this.tableMenuRepository
              .findAll(Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name"))
              .stream()
              .map(
                  entity -> {
                    final TableMemberMenuEntity tableMenuEntity = new TableMemberMenuEntity();
                    BeanUtils.copyProperties(entity, tableMenuEntity);
                    return tableMenuEntity;
                  })
              .collect(Collectors.toList());

      parents =
          items.stream()
              .filter(menu -> menu.getParentId().equals(0L))
              .map(
                  item -> {
                    final MenuVO menuVO = new MenuVO();
                    BeanUtils.copyProperties(item, menuVO);
                    return menuVO;
                  })
              .collect(Collectors.toList());
    } else {
      items =
          this.tableMemberMenuRepository.findAllByAuthority(
              authority, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder", "name"));
      parents =
          items.stream()
              .filter(menu -> menu.getParentId().equals(0L))
              .map(
                  item -> {
                    final MenuVO menuVO = new MenuVO();
                    BeanUtils.copyProperties(item, menuVO);
                    return menuVO;
                  })
              .collect(Collectors.toList());
    }

    return parents.stream()
        .peek(
            parent ->
                parent.setChildren(
                    items.stream()
                        .filter(menu -> menu.getParentId().equals(parent.getId()))
                        .map(
                            item -> {
                              final MenuVO menuVO = new MenuVO();
                              BeanUtils.copyProperties(item, menuVO);
                              return menuVO;
                            })
                        .collect(Collectors.toList())))
        .collect(Collectors.toList());
  }
}
