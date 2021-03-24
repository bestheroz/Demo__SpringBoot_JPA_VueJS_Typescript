package com.github.bestheroz.demo.api.entity.authority.item;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import com.github.bestheroz.demo.api.entity.menu.MenuEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "AUTHORITY_ITEM")
@TypeDef(name = "jsonString", typeClass = JsonStringType.class)
public class AuthorityItemEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = 6518292219807880047L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long authorityId;
  private Integer displayOrder;

  @Type(type = "jsonString")
  private List<String> typesJson;

  @OneToOne
  @JoinColumn(name = "menuId", referencedColumnName = "id", nullable = false)
  private MenuEntity menu;

  //  @ManyToOne
  //  @JoinColumn(name = "authorityId", insertable = false, updatable = false, nullable = false)
  //  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  //  @JoinColumn(name = "authorityId", nullable = false)
  //  private AuthorityEntity authority;
}
