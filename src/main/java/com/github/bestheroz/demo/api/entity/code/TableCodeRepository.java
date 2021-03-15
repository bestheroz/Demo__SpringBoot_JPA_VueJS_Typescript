package com.github.bestheroz.demo.api.entity.code;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeRepository extends CrudRepository<TableCodeEntity, Long> {
  List<TableCodeEntity> findAllByGroupName(String groupName);

  List<TableCodeEntity> findAllByGroupName(String groupName, Sort sort);

  Optional<TableCodeEntity> findByGroupNameAndId(String groupName, Long id);
}
