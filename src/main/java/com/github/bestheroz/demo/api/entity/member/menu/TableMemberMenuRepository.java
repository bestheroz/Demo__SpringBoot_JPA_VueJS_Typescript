package com.github.bestheroz.demo.api.entity.member.menu;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMemberMenuRepository
    extends CrudRepository<TableMemberMenuEntity, TableMemberMenuEntityId> {
  List<TableMemberMenuEntity> findAllByAuthority(Integer authority, Sort sort);

  List<TableMemberMenuEntity> findAllById(Long id);

  void deleteByAuthorityAndIdNotIn(Integer authority, List<Long> ids);

  void deleteAllById(Long id);
}
