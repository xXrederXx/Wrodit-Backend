package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.util.exception.InvalidSecretKeyException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeySpecGenerator {
  public static String SECRET;
  public static final String ALGORITHM = "HmacSHA256";

  @Value("${jwt.secret}")
  private String injectedSecret;

  @PostConstruct
  public void init() {
    if (injectedSecret == null) {
      throw new InvalidSecretKeyException("JWT secret not set");
    }
    if (injectedSecret.length() < 32) {
      throw new InvalidSecretKeyException(
          "JWT secret too short, current length is only " + injectedSecret.length());
    }
    SECRET = injectedSecret;
  }

  public static SecretKeySpec getSecretKeySpec() {
    return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  }
}
