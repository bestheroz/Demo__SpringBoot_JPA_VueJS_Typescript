package com.github.bestheroz.demo.api.entity.code;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE")
@Data
public class TableCodeEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = -6076508411557466173L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String codeGroup;
  private String code;
  private String name;
  private boolean available;
  private Integer displayOrder;
  private Integer authority;
}
