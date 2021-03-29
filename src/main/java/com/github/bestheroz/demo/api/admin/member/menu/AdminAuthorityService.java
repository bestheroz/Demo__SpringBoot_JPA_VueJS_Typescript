package com.github.bestheroz.demo.api.admin.member.menu;

import com.github.bestheroz.demo.api.entity.authority.AuthorityEntity;
import com.github.bestheroz.demo.api.entity.authority.AuthorityRepository;
import com.github.bestheroz.demo.api.entity.authority.item.AuthorityItemEntity;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AdminAuthorityService {
  @Resource private AuthorityRepository authorityRepository;

  @Transactional
  public List<AuthorityEntity> getItems() {
    return this.authorityRepository.findAllByOrderByNameAsc();
  }

  @Transactional
  public AuthorityEntity save(final AuthorityEntity payload) {
    for (final AuthorityItemEntity entity : payload.getItems()) {
      entity.setAuthority(payload);
    }
    return this.authorityRepository.save(payload);
  }
}
