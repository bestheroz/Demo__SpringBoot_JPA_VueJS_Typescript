package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.authority.AuthorityEntity;
import com.github.bestheroz.demo.api.entity.authority.AuthorityRepository;
import com.github.bestheroz.demo.api.entity.authority.item.AuthorityItemEntity;
import com.github.bestheroz.demo.api.entity.authority.item.AuthorityItemRepository;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminAuthorityService {
  @Resource private AuthorityRepository authorityRepository;
  @Resource private AuthorityItemRepository authorityItemRepository;

  @Transactional
  public List<AuthorityEntity> getItems() {
    return this.authorityRepository.findAllByOrderByNameAsc();
  }

  @Transactional
  public AuthorityEntity save(final AuthorityEntity payload) {
    final List<AuthorityItemEntity> items = new ArrayList<>(payload.getItems());
    payload.getItems().clear();
    final AuthorityEntity authorityEntity = this.authorityRepository.save(payload);
    log.debug("{}", items);
    for (final AuthorityItemEntity entity : items) {
      entity.setAuthorityId(authorityEntity.getId());
      this.authorityItemRepository.save(entity);
    }
    return authorityEntity;
    //    this.authorityRepository.deleteByAuthorityAndIdNotIn(
    //        authority, payload.stream().map(AuthorityEntity::getId).collect(Collectors.toList()));
    //    return payload.stream()
    //        .map(menu -> this.authorityRepository.save(menu))
    //        .collect(Collectors.toList());
  }
}
