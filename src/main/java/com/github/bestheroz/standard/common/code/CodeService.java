package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.demo.api.entity.code.TableCodeRepository;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
  @Resource private TableCodeRepository tableCodeRepository;

  public List<CodeVO> getCodeVOListByAuthority(final String groupName) {
    final Integer authority = AuthenticationUtils.getLoginVO().getAuthority();
    return this.tableCodeRepository
        .findAllByGroupName(groupName, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder"))
        .stream()
        .filter(item -> item.isAvailable() && item.getAuthority() <= authority)
        .map(item -> new CodeVO(item.getValue(), item.getName()))
        .collect(Collectors.toList());
  }

  public List<CodeVO> getCodeVOList(final String groupName) {
    return this.tableCodeRepository
        .findAllByGroupName(groupName, Sort.by(Sort.DEFAULT_DIRECTION, "displayOrder"))
        .stream()
        .map(item -> new CodeVO(item.getValue(), item.getName()))
        .collect(Collectors.toList());
  }
}
