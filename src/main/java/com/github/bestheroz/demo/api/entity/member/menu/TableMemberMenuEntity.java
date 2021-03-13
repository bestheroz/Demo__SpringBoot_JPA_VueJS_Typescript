package com.github.bestheroz.demo.api.entity.member.menu;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "MEMBER_MENU")
@IdClass(TableMemberMenuEntityId.class)
public class TableMemberMenuEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = 6518292219807880047L;
  @Id private Integer authority;
  @Id private Integer id;
  private String name;
  private String type;
  private Integer parentId;
  private Integer displayOrder;
  private String icon;
  private String url;
}