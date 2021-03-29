package com.github.bestheroz.demo.api.entity.authority;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {
  List<AuthorityEntity> findAllById(Long id);

  List<AuthorityEntity> findAllByOrderByNameAsc();

  void deleteAllById(Long id);

  @Query(
      value = "SELECT A.ID AS VALUE, A.NAME AS TEXT FROM AUTHORITY A ORDER BY A.NAME ASC",
      nativeQuery = true)
  List<Object[]> getCodes();
}
