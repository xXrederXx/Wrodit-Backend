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
  private String secret;

  @PostConstruct
  public void init() {
    if (secret == null) {
      throw new InvalidSecretKeyException("JWT secret not set");
    }
    if (secret.length() < 32) {
      throw new InvalidSecretKeyException("JWT secret too short, current length is only " + secret.length());
    }
    SECRET = secret;
  }

  public static SecretKeySpec getSecretKeySpec() {
    return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), ALGORITHM);
  }
}
