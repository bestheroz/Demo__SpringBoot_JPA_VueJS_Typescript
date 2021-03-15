package com.github.bestheroz.demo.api.entity.code.group;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableCodeGroupRepository extends CrudRepository<TableCodeGroupEntity, Long> {
  Optional<TableCodeGroupEntity> findByCodeGroup(String codeGroup);
}
