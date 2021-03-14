package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuEntityId;
import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMemberMenuService {
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  @Transactional
  public List<TableMemberMenuEntity> save(
      final Integer authority, final List<TableMemberMenuEntity> payload) {
    this.tableMemberMenuRepository.deleteByAuthorityAndIdNotIn(
        authority, payload.stream().map(TableMemberMenuEntity::getId).collect(Collectors.toList()));
    return payload.stream()
        .map(
            menu -> {
              return this.tableMemberMenuRepository
                  .findById(new TableMemberMenuEntityId(authority, menu.getId()))
                  .map(
                      (item -> {
                        item.setAuthority(authority);
                        item.setId(menu.getId());
                        this.tableMemberMenuRepository.save(item);
                        return item;
                      }))
                  .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
            })
        .collect(Collectors.toList());
  }
}
