package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.TableMemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.TableMenuEntity;
import com.github.bestheroz.demo.api.entity.menu.TableMenuRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
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
    this.tableMenuRepository
        .findById(id)
        .ifPresentOrElse(
            (item) -> {
              BeanUtils.copyProperties(tableMenuEntity, item);
              this.tableMenuRepository.save(item);
              this.tableMemberMenuRepository
                  .findAllById(id)
                  .forEach(
                      memberMenu -> {
                        BeanUtils.copyProperties(tableMenuEntity, memberMenu);
                        this.tableMemberMenuRepository.save(memberMenu);
                      });
            },
            () -> {
              throw BusinessException.FAIL_NO_DATA_SUCCESS;
            });
  }

  @Transactional
  public void delete(final Integer id) {
    this.tableMenuRepository
        .findById(id)
        .ifPresentOrElse(
            (item) -> {
              this.tableMenuRepository.deleteById(id);
              this.tableMemberMenuRepository.deleteAllById(id);
            },
            () -> {
              throw BusinessException.FAIL_NO_DATA_SUCCESS;
            });
  }
}
