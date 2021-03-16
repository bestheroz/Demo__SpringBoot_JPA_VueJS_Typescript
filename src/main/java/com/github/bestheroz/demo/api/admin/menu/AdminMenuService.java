package com.github.bestheroz.demo.api.admin.menu;

import com.github.bestheroz.demo.api.entity.member.menu.MemberMenuRepository;
import com.github.bestheroz.demo.api.entity.menu.MenuEntity;
import com.github.bestheroz.demo.api.entity.menu.MenuRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMenuService {
  @Resource private MenuRepository menuRepository;
  @Resource private MemberMenuRepository memberMenuRepository;

  @Transactional
  public MenuEntity put(final MenuEntity payload, final Long id) {
    return this.menuRepository
        .findById(id)
        .map(
            (item) -> {
              BeanUtils.copyProperties(payload, item);
              this.memberMenuRepository
                  .findAllById(id)
                  .forEach(
                      memberMenu -> {
                        BeanUtils.copyProperties(payload, memberMenu);
                        this.memberMenuRepository.save(memberMenu);
                      });
              return this.menuRepository.save(item);
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }

  @Transactional
  public MenuEntity delete(final Long id) {
    return this.menuRepository
        .findById(id)
        .map(
            (item) -> {
              this.menuRepository.delete(item);
              this.memberMenuRepository.deleteAllById(id);
              return item;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }
}
