package com.github.bestheroz.demo.api.entity.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMemberRepository extends CrudRepository<TableMemberEntity, String> {
  Optional<TableMemberEntity> findByIdAndToken(String id, String token);

  List<TableMemberEntity> findAll(Sort sort);
}
