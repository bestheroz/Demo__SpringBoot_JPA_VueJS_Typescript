package com.github.bestheroz.demo.api.entity.member;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MEMBER")
public class TableMemberEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = 7280716056600887400L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String userId;

  private String password;
  private String name;
  private Integer authority;
  private Integer loginFailCnt;
  private boolean available;
  private String theme;
  private String token;
  private Instant expired;
}
