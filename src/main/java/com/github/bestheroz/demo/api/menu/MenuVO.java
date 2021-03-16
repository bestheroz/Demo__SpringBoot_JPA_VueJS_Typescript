package com.github.bestheroz.demo.api.menu;

import com.github.bestheroz.demo.api.entity.member.menu.MemberMenuEntity;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuVO extends MemberMenuEntity implements Serializable {
  private static final long serialVersionUID = 5783239806154575625L;
  private List<MenuVO> children;
}
