package com.github.bestheroz.standard.common.authenticate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.bestheroz.standard.common.exception.BusinessException;
import com.github.bestheroz.standard.common.util.MapperUtils;
import java.sql.Date;
import java.time.OffsetDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

@Slf4j
@UtilityClass
public class JwtTokenProvider {
  private final Algorithm ALGORITHM = Algorithm.HMAC512("secret");
  private final Long expiresAtAccessToken = 60L;
  private final Long expiresAtRefreshToken = 2592000L; // 3600 * 24 * 30 == 1month

  public String createAccessToken(final UserVO userVO) {
    Assert.notNull(userVO, "userVO parameter must not be empty or null");
    Assert.hasText(userVO.getUserId(), "userId parameter must not be empty or null");
    Assert.hasText(userVO.getName(), "userName parameter must not be empty or null");
    Assert.notNull(userVO.getAuthority(), "authority parameter must not be empty or null");
    return JWT.create()
        .withClaim("userId", userVO.getUserId())
        .withClaim("userVO", MapperUtils.toString(userVO))
        .withExpiresAt(
            Date.from(
                OffsetDateTime.now().plusSeconds(expiresAtAccessToken.intValue()).toInstant()))
        .sign(ALGORITHM);
  }

  public String createRefreshToken(final UserVO userVO) {
    Assert.notNull(userVO, "userVO parameter must not be empty or null");
    Assert.hasText(userVO.getUserId(), "userId parameter must not be empty or null");
    return JWT.create()
        .withClaim("userId", userVO.getUserId())
        .withExpiresAt(
            Date.from(
                OffsetDateTime.now().plusSeconds(expiresAtRefreshToken.intValue()).toInstant()))
        .sign(ALGORITHM);
  }

  public Authentication getAuthentication(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    final UserVO userVO = getUserVO(token);
    final UserDetails userDetails =
        new UserVO(
            userVO.getId(),
            userVO.getUserId(),
            userVO.getName(),
            userVO.getAuthority(),
            userVO.getTheme());
    return new UsernamePasswordAuthenticationToken(
        userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
  }

  public String getUserId(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    try {
      return JWT.require(ALGORITHM)
          .acceptExpiresAt(expiresAtAccessToken)
          .build()
          .verify(token)
          .getClaims()
          .get("userId")
          .asString();
    } catch (final JWTVerificationException | NullPointerException e) {
      throw BusinessException.FAIL_TRY_LOGIN_FIRST;
    }
  }

  public UserVO getUserVO(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    try {
      return MapperUtils.getObjectMapper()
          .readValue(
              JWT.require(ALGORITHM)
                  .acceptExpiresAt(expiresAtAccessToken)
                  .build()
                  .verify(token)
                  .getClaims()
                  .get("userVO")
                  .asString(),
              UserVO.class);
    } catch (final JWTVerificationException | NullPointerException | JsonProcessingException e) {
      log.warn(ExceptionUtils.getStackTrace(e));
      throw BusinessException.FAIL_TRY_LOGIN_FIRST;
    }
  }

  public String resolveAccessToken(final HttpServletRequest request) {
    final String authorization = request.getHeader("Authorization");
    return StringUtils.equals(authorization, "null") ? null : authorization;
  }

  public boolean validateAccessToken(final String token) {
    Assert.hasText(token, "token parameter must not be empty or null");
    try {
      JWT.require(ALGORITHM).acceptExpiresAt(expiresAtAccessToken).build().verify(token);
      return true;
    } catch (final JWTVerificationException | NullPointerException e) {
      return false;
    }
  }
}
