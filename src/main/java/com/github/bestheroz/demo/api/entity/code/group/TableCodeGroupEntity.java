package com.github.bestheroz.demo.api.entity.code.group;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE_GROUP")
@Data
public class TableCodeGroupEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = -9216318893256632523L;
  @Id private String codeGroup;
  private String name;
}
