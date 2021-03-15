package com.github.bestheroz.demo.api.entity.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableMemberRepository extends CrudRepository<TableMemberEntity, Long> {
  Page<TableMemberEntity> findAll(Pageable pageable);

  Optional<TableMemberEntity> findByUserIdAndToken(String userId, String token);

  Optional<TableMemberEntity> findByUserId(String userId);

  List<TableMemberEntity> findAll(Sort sort);
}
