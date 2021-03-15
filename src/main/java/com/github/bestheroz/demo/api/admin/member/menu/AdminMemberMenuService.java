package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMemberMenuService {
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;
  @Resource private TableMenuRepository tableMenuRepository;

  @Transactional
  public List<TableMemberMenuEntity> save(
      final Integer authority, final List<TableMemberMenuEntity> payload) {
    this.tableMemberMenuRepository.deleteByAuthorityAndIdNotIn(
        authority, payload.stream().map(TableMemberMenuEntity::getId).collect(Collectors.toList()));
    return payload.stream()
        .map(
            menu -> {
              return this.tableMenuRepository
                  .findById(menu.getId())
                  .map(
                      (item -> {
                        BeanUtils.copyProperties(item, menu);
                        menu.setAuthority(authority);
                        this.tableMemberMenuRepository.save(menu);
                        return menu;
                      }))
                  .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
            })
        .collect(Collectors.toList());
  }
}
