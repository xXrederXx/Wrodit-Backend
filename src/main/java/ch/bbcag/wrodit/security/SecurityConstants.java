package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.controllers.AuthController;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

public class SecurityConstants {
  private SecurityConstants() {
    // hide ctor
  }

  public static final SecurityConstants getInstance = new SecurityConstants();

  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  @Value("${jwt.secret}")
  private String secret;

  public SecretKeySpec getSecretKeySpec() {
    return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  }

  public static final String ALGORITHM = "HmacSHA256";

  public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 2 Weeks

  public static final String AUTH_ENDPOINTS = AuthController.PATH + "/**";
  public static final String[] DOCS_ENDPOINTS = {
    "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
  };
}
