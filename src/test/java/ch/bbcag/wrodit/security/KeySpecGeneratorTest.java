package ch.bbcag.wrodit.security;

import static org.junit.jupiter.api.Assertions.*;

import ch.bbcag.wrodit.util.exception.InvalidSecretKeyException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeySpecGeneratorTest {
  private KeySpecGenerator keySpecGenerator;

  @BeforeEach
  void setUp() {
    keySpecGenerator = new KeySpecGenerator();
  }

  private void setInjectedSecret(String value) throws Exception {
    Field field = KeySpecGenerator.class.getDeclaredField("injectedSecret");
    field.setAccessible(true);
    field.set(keySpecGenerator, value);
  }

  @Test
  void checkInit_whenValidSecret_thenSetsSecret() throws Exception {
    setInjectedSecret("12345678901234567890123456789012"); // 32 chars

    keySpecGenerator.init();

    assertEquals("12345678901234567890123456789012", KeySpecGenerator.SECRET);
  }

  @Test
  void checkInit_whenNullSecret_thenThrowsInvalidSecretKeyException() {
    assertThrows(InvalidSecretKeyException.class, () -> keySpecGenerator.init());
  }

  @Test
  void checkInit_whenSecretTooShort_thenThrowsInvalidSecretKeyException() throws Exception {
    setInjectedSecret("shortsecret");

    assertThrows(InvalidSecretKeyException.class, () -> keySpecGenerator.init());
  }

  @Test
  void checkGetSecretKeySpec_returnsSecretKeySpec() {
    KeySpecGenerator.SECRET = "12345678901234567890123456789012";

    SecretKeySpec keySpec = KeySpecGenerator.getSecretKeySpec();

    assertNotNull(keySpec);
    assertEquals("HmacSHA256", keySpec.getAlgorithm());
    assertArrayEquals(
        KeySpecGenerator.SECRET.getBytes(StandardCharsets.UTF_8), keySpec.getEncoded());
  }
}
