package ch.bbcag.wrodit.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.bbcag.wrodit.util.exception.JwtGenerationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class JWTGeneratorTest {
  @BeforeAll
  static void init() {
    KeySpecGenerator.SECRET = "qwertzuiopasdfghjklöäsdcvbnm,.e5rft6hgzjiukolp65r7ftzuijko";
  }

  @Test
  void generateJwtToken_validInput_returnsToken() {
    String token = JWTGenerator.generateJwtToken(1, "testuser");

    assertNotNull(token);
    assertFalse(token.isEmpty());
    assertEquals(3, token.split("\\.").length); // JWT has 3 parts
  }

  @Test
  void generateJwtToken_signingFails_throwsJwtGenerationException() throws JOSEException {
    try (MockedConstruction<SignedJWT> mocked =
        mockConstruction(
            SignedJWT.class,
            (mock, context) ->
                doThrow(new JOSEException("Signing failed")).when(mock).sign(any()))) {

      assertThrows(
          JwtGenerationException.class, () -> JWTGenerator.generateJwtToken(1, "testuser"));
    }
  }
}
