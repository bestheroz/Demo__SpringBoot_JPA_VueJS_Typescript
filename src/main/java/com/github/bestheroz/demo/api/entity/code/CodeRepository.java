package com.github.bestheroz.demo.api.entity.code;

import com.github.bestheroz.standard.common.code.CodeVO;
import com.github.bestheroz.standard.common.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends CrudRepository<CodeEntity, Long> {
  @Query(value = "SELECT DISTINCT  C.TYPE FROM CODE C ORDER BY C.TYPE ASC", nativeQuery = true)
  List<String> getTypes();

  List<CodeEntity> findAllByTypeOrderByDisplayOrderAsc(String type);

  @Query(
      value =
          "SELECT C.VALUE AS VALUE, C.NAME AS TEXT FROM CODE C WHERE C.TYPE = :type ORDER BY C.DISPLAY_ORDER ASC",
      nativeQuery = true)
  List<Object[]> getCodes(String type);

  @Query(
      value =
          "SELECT new com.github.bestheroz.standard.common.code.CodeVO(M.userId, M.name) FROM MEMBER M ORDER BY M.id ASC")
  List<CodeVO<String>> getMembers() throws BusinessException;
}
