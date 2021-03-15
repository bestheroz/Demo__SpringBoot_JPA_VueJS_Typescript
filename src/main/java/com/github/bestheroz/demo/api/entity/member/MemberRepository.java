package com.github.bestheroz.demo.api.entity.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, Long> {
  Page<MemberEntity> findAll(Pageable pageable);

  Optional<MemberEntity> findByUserIdAndToken(String userId, String token);

  Optional<MemberEntity> findByUserId(String userId);

  List<MemberEntity> findAll(Sort sort);
}
