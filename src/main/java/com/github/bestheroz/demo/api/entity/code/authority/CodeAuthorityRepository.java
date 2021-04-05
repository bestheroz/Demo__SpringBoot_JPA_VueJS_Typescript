package com.github.bestheroz.demo.api.entity.code.authority;

import com.github.bestheroz.demo.api.entity.authority.item.AuthorityItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeAuthorityRepository extends CrudRepository<AuthorityItemEntity, Long> {}
