package com.github.bestheroz.demo.api.entity.member;

import com.github.bestheroz.demo.api.entity.AbstractCreatedUpdateEntity;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MEMBER")
public class TableMemberEntity extends AbstractCreatedUpdateEntity implements Serializable {
  private static final long serialVersionUID = 7280716056600887400L;
  @Id private String id;
  private String password;
  private String name;
  private Integer authority;
  private Integer loginFailCnt;
  private boolean available;
  private String theme;
  private String token;
  private Instant expired;
}
