package ch.bbcag.wrodit.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;

public class JWTGenerator {
  public static String generateJwtToken(String username) {
    try {
      SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), buildJWTClaimsSet(username));
      jwt.sign(new MACSigner(SecurityConstants.SECRET_KEY_SPEC));
      return jwt.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private static JWTClaimsSet buildJWTClaimsSet(String username) {
    return new JWTClaimsSet.Builder()
        .subject(username)
        .expirationTime(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .build();
  }
}
