package com.github.bestheroz.demo.api.admin.code.group;

import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupEntity;
import com.github.bestheroz.demo.api.entity.code.group.TableCodeGroupRepository;
import com.github.bestheroz.standard.common.exception.BusinessException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdminCodeGroupService {
  @Resource private TableCodeGroupRepository tableCodeGroupRepository;
  @Resource private TableCodeRepository tableCodeRepository;

  @Transactional
  public TableCodeGroupEntity delete(final String codeGroup) {
    return this.tableCodeGroupRepository
        .findById(codeGroup)
        .map(
            (item) -> {
              this.tableCodeRepository.deleteByCodeGroup(codeGroup);
              this.tableCodeGroupRepository.deleteById(codeGroup);
              return item;
            })
        .orElseThrow(() -> BusinessException.FAIL_NO_DATA_SUCCESS);
  }
}
