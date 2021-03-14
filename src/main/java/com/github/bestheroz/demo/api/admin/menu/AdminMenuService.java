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
  public TableMenuEntity put(final TableMenuEntity payload, final Integer id) {
    return this.tableMenuRepository
        .findById(id)
        .map(
            (item) -> {
              BeanUtils.copyProperties(payload, item);
              this.tableMenuRepository.save(item);
              this.tableMemberMenuRepository
                  .findAllById(id)
                  .forEach(
                      memberMenu -> {
                        BeanUtils.copyProperties(payload, memberMenu);
                        this.tableMemberMenuRepository.save(memberMenu);
                      });
              return item;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }

  @Transactional
  public TableMenuEntity delete(final Integer id) {
    return this.tableMenuRepository
        .findById(id)
        .map(
            (item) -> {
              this.tableMenuRepository.deleteById(id);
              this.tableMemberMenuRepository.deleteAllById(id);
              return item;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }
}
