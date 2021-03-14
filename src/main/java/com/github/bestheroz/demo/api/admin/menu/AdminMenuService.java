package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMenuService {
  @Resource private TableMenuRepository tableMenuRepository;
  @Resource private TableMemberMenuRepository tableMemberMenuRepository;

  @Transactional
  public void put(final TableMenuEntity tableMenuEntity, final Integer id) {
    this.tableMenuRepository.save(tableMenuEntity);
    this.tableMemberMenuRepository
        .findAllById(id)
        .forEach(
            item -> {
              BeanUtils.copyProperties(tableMenuEntity, item);
              this.tableMemberMenuRepository.save(item);
            });
  }

  @Transactional
  public void delete(final Integer id) {
    this.tableMenuRepository.deleteById(id);
    this.tableMemberMenuRepository.deleteAllById(id);
  }
}
