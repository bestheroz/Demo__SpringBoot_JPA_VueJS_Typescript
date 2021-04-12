package com.github.bestheroz.demo.api.entity.code;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends CrudRepository<CodeEntity, Long> {
  @Query(value = "SELECT DISTINCT C.TYPE FROM CODE C ORDER BY C.TYPE ASC", nativeQuery = true)
  List<String> getTypes();

  List<CodeEntity> findAllByTypeOrderByDisplayOrderAsc(String type);

  @Query(
      "SELECT new com.github.bestheroz.standard.common.code.CodeVO(C.value, C.name) "
          + "from CODE C INNER JOIN CODE_AUTHORITY CA on CA.code = C "
          + "WHERE C.type = :type AND CA.authorityId= :authorityId "
          + "ORDER BY C.displayOrder ASC")
  List<CodeVO<String>> getCodesByTypeAndAuthorityId(String type, Long authorityId);

  @Query(
      value =
          "SELECT new com.github.bestheroz.standard.common.code.CodeVO(C.value, C.name) "
              + "FROM CODE C WHERE C.type = :type ORDER BY C.displayOrder ASC")
  List<CodeVO<String>> getCodesByType(String type);

  @Query(
      value =
          "SELECT new com.github.bestheroz.standard.common.code.CodeVO(M.userId, M.name) "
              + "FROM MEMBER M ORDER BY M.id ASC")
  List<CodeVO<String>> getMembers() throws BusinessException;
}
