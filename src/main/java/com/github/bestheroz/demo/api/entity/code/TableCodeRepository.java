package com.github.bestheroz.demo.api.entity.code;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeRepository extends CrudRepository<TableCodeEntity, Long> {
  List<TableCodeEntity> findAllByCodeGroup(String codeGroup);

  List<TableCodeEntity> findAllByCodeGroup(String codeGroup, Sort sort);

  Optional<TableCodeEntity> findByCodeGroupAndId(String codeGroup, Long id);

  void deleteByCodeGroup(String id);
}
