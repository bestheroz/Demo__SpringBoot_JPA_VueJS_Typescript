package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.member.menu.AuthorityEntity;
import com.github.bestheroz.demo.api.entity.member.menu.AuthorityRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthorityService {
  @Resource private AuthorityRepository authorityRepository;

  @Transactional
  public List<AuthorityEntity> save(final Integer authority, final List<AuthorityEntity> payload) {
    this.authorityRepository.deleteByAuthorityAndIdNotIn(
        authority, payload.stream().map(AuthorityEntity::getId).collect(Collectors.toList()));
    return payload.stream()
        .map(menu -> this.authorityRepository.save(menu))
        .collect(Collectors.toList());
  }
}
