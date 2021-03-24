package com.github.bestheroz.demo.api.entity.authority;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
  List<AuthorityEntity> findAllById(Long id);

  List<AuthorityEntity> findAllByOrderByNameAsc();

  void deleteAllById(Long id);

  //  void deleteByAuthorityAndIdNotIn(Integer authority, List<Long> ids);
}
