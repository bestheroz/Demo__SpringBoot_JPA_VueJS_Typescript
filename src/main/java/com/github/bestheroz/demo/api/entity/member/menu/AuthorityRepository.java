package com.github.bestheroz.demo.api.entity.member.menu;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
  List<AuthorityEntity> findAllById(Long id);

  List<AuthorityEntity> findAllByIdOrderByDisplayOrderAsc(Long id);

  void deleteAllById(Long id);

  void deleteByAuthorityAndIdNotIn(Integer authority, List<Long> ids);
}
