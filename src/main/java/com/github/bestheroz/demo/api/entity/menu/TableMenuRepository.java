package com.github.bestheroz.demo.api.entity.menu;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMenuRepository extends CrudRepository<TableMenuEntity, Long> {
  List<TableMenuEntity> findAll(Sort sort);
}
