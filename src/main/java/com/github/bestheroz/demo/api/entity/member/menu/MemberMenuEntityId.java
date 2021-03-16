package com.github.bestheroz.demo.api.entity.member.menu;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberMenuEntityId implements Serializable {
  private static final long serialVersionUID = -4897596840013190393L;
  @Id private Integer authority;
  @Id private Long id;
}
