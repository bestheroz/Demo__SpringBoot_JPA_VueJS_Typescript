package com.github.bestheroz.demo.api.entity.member.menu;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMemberMenuRepository
    extends CrudRepository<TableMemberMenuEntity, TableMemberMenuEntityId> {
  List<TableMemberMenuEntity> findAllByAuthority(Integer authority, Sort sort);

  void deleteByAuthorityAndIdNotIn(Integer authority, List<Integer> ids);

  @Query("DELETE FROM MEMBER_MENU WHERE id = (:id)")
  void deleteAllById(Integer id);
}
