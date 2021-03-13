package com.github.bestheroz.standard.common.code;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CodeVO implements Serializable {
  private static final long serialVersionUID = 272726757907169621L;
  @Id private String value;
  private String text;
}
