package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.standard.common.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends CrudRepository<CodeVO, String> {
  @Query(
      value = "SELECT M.ID AS VALUE, M.NAME AS TEXT FROM MEMBER M ORDER BY M.ID ASC",
      nativeQuery = true)
  List<CodeVO> getMembers() throws BusinessException;
}
