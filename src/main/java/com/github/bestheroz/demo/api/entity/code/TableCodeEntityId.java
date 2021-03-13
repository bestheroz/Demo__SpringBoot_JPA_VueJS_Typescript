package com.github.bestheroz.demo.api.entity.code;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableCodeEntityId implements Serializable {
  private static final long serialVersionUID = -4897596840013190393L;
  @Id private String codeGroup;
  @Id private String code;
}
