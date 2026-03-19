package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.controllers.AuthController;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;

public class SecurityConstants {
  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  public static final String AUTH_HEADER_PASSWORD = "WI-Auth-Passwd";
  public static final String AUTH_HEADER_ID = "WI-Auth-Id";

  public static final String SECRET = "Secret Key to generate JWT's (min 256 Bits)";
  public static final String ALGORITHM = "HmacSHA256";
  public static final SecretKeySpec SECRET_KEY_SPEC =
      new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  ;
  public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 2 Weeks

  public static final String AUTH_ENDPOINTS = AuthController.PATH + "/**";
  public static final String[] DOCS_ENDPOINTS = {
    "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
  };
}
