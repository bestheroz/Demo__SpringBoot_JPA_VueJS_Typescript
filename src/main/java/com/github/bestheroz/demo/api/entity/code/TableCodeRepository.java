package com.github.bestheroz.demo.api.entity.code;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeRepository extends CrudRepository<TableCodeEntity, TableCodeEntityId> {
  List<TableCodeEntity> findAllByCodeGroup(String id);

  List<TableCodeEntity> findAllByCodeGroup(String id, Sort sort);

  void deleteByCodeGroup(String id);
}
