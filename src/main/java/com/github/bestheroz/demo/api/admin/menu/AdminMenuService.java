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
  public TableMenuEntity put(final TableMenuEntity payload, final Long id) {
    return this.tableMenuRepository
        .findById(id)
        .map(
            (item) -> {
              BeanUtils.copyProperties(payload, item);
              this.tableMemberMenuRepository
                  .findAllById(id)
                  .forEach(
                      memberMenu -> {
                        BeanUtils.copyProperties(payload, memberMenu);
                        this.tableMemberMenuRepository.save(memberMenu);
                      });
              return this.tableMenuRepository.save(item);
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }

  @Transactional
  public TableMenuEntity delete(final Long id) {
    return this.tableMenuRepository
        .findById(id)
        .map(
            (item) -> {
              this.tableMenuRepository.delete(item);
              this.tableMemberMenuRepository.deleteAllById(id);
              return item;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }
}
