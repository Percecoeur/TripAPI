package com.amadeus.trip.utils;

import com.amadeus.trip.model.exception.AuthenticationException;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@UtilityClass
public class Utils {

  /**
   * @param basicAuth a basic authentication string like : Basic YWRtaW46cGFzc3dvcmQ=
   * @return the user within the authentication
   */
  public String extractUserFromBasic(String basicAuth) throws AuthenticationException {
    if (basicAuth != null && basicAuth.startsWith(Constants.BASIC)) {
      String base64Credentials = basicAuth.substring(Constants.BASIC.length()).trim();
      byte[] byteCredentials = Base64.getDecoder().decode(base64Credentials);
      String credentials = new String(byteCredentials, StandardCharsets.UTF_8);
      // credentials = username:password
      final String[] values = credentials.split(":", 2);

      return values[0];
    }

    throw new AuthenticationException("Cannot retrieve user from authentication headers.");
  }

}
