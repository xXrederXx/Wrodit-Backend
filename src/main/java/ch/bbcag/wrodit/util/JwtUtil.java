package ch.bbcag.wrodit.util;

import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;

public class JwtUtil {

  private JwtUtil() {}

  public static Integer extractUserId(String token) {
    try {
      SignedJWT jwt = SignedJWT.parse(token);
      return jwt.getJWTClaimsSet().getIntegerClaim("userId");
    } catch (ParseException e) {
      throw new RuntimeException("Invalid JWT token");
    }
  }
}
