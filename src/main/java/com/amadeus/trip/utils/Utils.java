package com.amadeus.trip.utils;

import com.amadeus.trip.model.UserDetailsImpl;
import com.amadeus.trip.model.exception.AuthenticationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@UtilityClass
@Log4j2
public class Utils {

  /**
   * @param basicAuth a basic authentication string like : Basic YWRtaW46cGFzc3dvcmQ=
   * @return the user within the authentication
   */
  public String extractUserFromBasic(String basicAuth) throws AuthenticationException {

    if (StringUtils.hasText(basicAuth) && basicAuth.startsWith(Constants.BASIC)) {
      String base64Credentials = basicAuth.substring(Constants.BASIC.length()).trim();
      byte[] byteCredentials = Base64.getDecoder().decode(base64Credentials);
      String credentials = new String(byteCredentials, StandardCharsets.UTF_8);
      // credentials = username:password
      final String[] values = credentials.split(":", 2);

      return values[0];
    }

    throw new AuthenticationException("Cannot retrieve user from authentication headers.");
  }

  public String extractTokenFromJwt(String bearerAuth) {

    if (StringUtils.hasText(bearerAuth) && bearerAuth.startsWith(Constants.BEARER)) {
      // credentials = Bearer <token>
      return bearerAuth.substring(Constants.BEARER.length()).trim();
    }

    return null;
  }

  public String createToken(Authentication authentication, int expiration, String secret) throws AuthenticationException {

    UserDetailsImpl userPrincipal = (UserDetailsImpl)authentication.getPrincipal();

    Instant now = Instant.now();

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusSeconds(expiration)))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public boolean isValidToken(String authToken, String secret) {
    if (StringUtils.hasText(authToken)) {
      try {
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(authToken);
        return true;
      } catch (Exception e) {
        log.error("Jwt token is not valid");
      }
    }

    return false;
  }

  public String getUserNameFromJwtToken(String token, String secret) {
    return Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
