package com.microservice.todolist.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The JwtTokenUtil is responsible for performing
 * JWT operations like creation and validation.
 * @author vidhya.sama
 *
 */
@Component
public class JwtTokenUtil implements Serializable {

  private static final long serialVersionUID = -2550185165626007488L;

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  public static final long MILLI_SECONDS = 1000;

  public static final int SKIP = 7;

  @Value("${jwt.secret}")
  private String secret;

  /**
   * Retrieve username from jwt token.
   *
   */
  public String getUsernameFromToken(final String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  /**
   * Retrieve expiration date from jwt token.
   *
   * @param token Jwt token
   * @return all claims.
   *
   */
  public Date getExpirationDateFromToken(final String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /** retrieve all claims. */
  public <T> T getClaimFromToken(final String token,
      final Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * for retrieveing any information from token we will need the secret key.
   * @param token Jwt token
   *
   *
   */
  private Claims getAllClaimsFromToken(final String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  /**
   * check if the token has expired.
   * @param token Jwt token
   * @return true if the instant of time
   * represented by this Date object is strictly
   * earlier than the instant represented by when;
   * false otherwise.
   */
  private Boolean isTokenExpired(final String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * generate token for user.
   * @param userDetails Provides core user information.
   * @return token Jwt token.
   */
  public String generateToken(final UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * creating the token.
   * @param claims the JWT claims to be set as the JWT body.
   * @param subject the subject
   * value or null to remove the property from the Claims map.
   * @return claims of the token, like Issuer,
   * Expiration,Subject, and the ID Sign the JWT using the HS512 algorithm and
   * secret key.
   */
  private String doGenerateToken(final Map<String, Object> claims,
      final String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(
            System.currentTimeMillis() + JWT_TOKEN_VALIDITY * MILLI_SECONDS))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  /** Validate token . */
  public Boolean validateToken(final String token,
      final UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  /** Parse token . */
  public String parseJwt(final String token) {
    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
      return token.substring(SKIP, token.length());
    }

    return null;
  }
}
