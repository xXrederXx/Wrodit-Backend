package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.controllers.AuthController;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;

public class SecurityConstants {
  private SecurityConstants() {
    // hide ctor
  }

  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  public static final String SECRET;

  static {
    String secretFromEnv = System.getenv("JWT_SECRET");
    if (secretFromEnv == null || secretFromEnv.length() < 32) {
      throw new RuntimeException("JWT_SECRET environment variable not set or too short");
    }
    SECRET = secretFromEnv;
  }

  public static final SecretKeySpec SECRET_KEY_SPEC =
      new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

  public static final String ALGORITHM = "HmacSHA256";

  public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 2 Weeks

  public static final String AUTH_ENDPOINTS = AuthController.PATH + "/**";
  public static final String[] DOCS_ENDPOINTS = {
    "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
  };
}
