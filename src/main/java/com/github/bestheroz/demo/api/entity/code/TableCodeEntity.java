package com.github.bestheroz.demo.api.entity.code;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE")
@IdClass(TableCodeEntityId.class)
@Data
public class TableCodeEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = -6076508411557466173L;
  @Id private String codeGroup;
  @Id private String code;
  private String name;
  private boolean available;
  private Integer displayOrder;
  private Integer authority;
}
