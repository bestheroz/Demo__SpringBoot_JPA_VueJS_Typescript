package com.github.bestheroz.standard.common.authenticate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
public class UserVO implements UserDetails, Serializable {
  private static final long serialVersionUID = -3806331610004769750L;
  private String id;
  private String name;
  private Integer authority;
  private String theme;

  public UserVO(final String id, final String name, final Integer authority, final String theme) {
    this.id = id;
    this.name = name;
    this.authority = authority;
    this.theme = theme;
    //    this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
  }

  //  @ElementCollection(targetClass = SimpleGrantedAuthority.class, fetch = FetchType.EAGER)
  //  @JsonDeserialize(as = SimpleGrantedAuthority.class)
  //  private Set<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return this.getId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
