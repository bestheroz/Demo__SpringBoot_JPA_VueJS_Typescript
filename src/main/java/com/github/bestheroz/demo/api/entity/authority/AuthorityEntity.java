package com.github.bestheroz.demo.api.entity.authority;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import com.github.bestheroz.demo.api.entity.authority.item.AuthorityItemEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "AUTHORITY")
public class AuthorityEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = 6518292219807880047L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;
  private String name;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "authorityId")
  private List<AuthorityItemEntity> items;
}
