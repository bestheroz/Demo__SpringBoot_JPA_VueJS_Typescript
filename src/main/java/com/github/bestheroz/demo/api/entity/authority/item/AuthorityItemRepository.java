package com.github.bestheroz.demo.api.entity.authority.item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityItemRepository extends CrudRepository<AuthorityItemEntity, Long> {}
