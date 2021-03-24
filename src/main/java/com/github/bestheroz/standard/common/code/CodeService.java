package com.github.bestheroz.standard.common.code;

import com.github.bestheroz.demo.api.entity.code.CodeRepository;
import com.github.bestheroz.standard.common.util.AuthenticationUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CodeService {
  @Resource private CodeRepository codeRepository;

  public List<CodeVO> getCodesByTypeByAuthority(final String type) {
    final Integer authority = AuthenticationUtils.getLoginVO().getAuthorityId();
    return this.codeRepository.findAllByTypeOrderByDisplayOrderAsc(type).stream()
        .filter(item -> item.getAvailable() && item.getAuthorityId() <= authority)
        .map(item -> new CodeVO(item.getValue(), item.getName()))
        .collect(Collectors.toList());
  }

  public List<CodeVO> getCodesByType(final String type) {
    return this.codeRepository.getCodes(type).stream()
        .map(code -> new CodeVO((String) code[0], (String) code[1]))
        .collect(Collectors.toList());
  }
}
