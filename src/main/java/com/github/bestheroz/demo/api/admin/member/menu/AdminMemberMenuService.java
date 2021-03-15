package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.MemberMenuEntity;
import com.github.bestheroz.demo.api.entity.member.menu.MemberMenuRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMemberMenuService {
  @Resource private MemberMenuRepository tableMemberMenuRepository;

  @Transactional
  public List<MemberMenuEntity> save(
      final Integer authority, final List<MemberMenuEntity> payload) {
    this.tableMemberMenuRepository.deleteByAuthorityAndIdNotIn(
        authority, payload.stream().map(MemberMenuEntity::getId).collect(Collectors.toList()));
    return payload.stream()
        .map(menu -> this.tableMemberMenuRepository.save(menu))
        .collect(Collectors.toList());
  }
}
