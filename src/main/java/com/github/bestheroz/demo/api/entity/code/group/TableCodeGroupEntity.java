package com.github.bestheroz.demo.api.entity.code.group;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import com.github.bestheroz.demo.api.entity.code.TableCodeEntity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CODE_GROUP")
@Table(name = "CODE_GROUP")
@Data
public class TableCodeGroupEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = -9216318893256632523L;

  @Id
  @Column(name = "name")
  private String name;

  private String description;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  //  @JoinTable(
  //      name = "CODE",
  //      joinColumns = @JoinColumn(name = "name"),
  //      inverseJoinColumns = @JoinColumn(name = "group_name"))
  private List<TableCodeEntity> codes;
}
