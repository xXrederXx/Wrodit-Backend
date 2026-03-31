package ch.bbcag.wrodit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

class ThrowHelperTest {
  @Test
  void checkThrowAuth_whenEqual_thenNothing() {
    Assertions.assertDoesNotThrow(() -> ThrowHelper.throwAuthorizationIfNotEqual(1, 1));
  }

  @Test
  void checkThrowAuth_whenNotEqual_thenThrows() {
    Assertions.assertThrows(
        AuthorizationDeniedException.class, () -> ThrowHelper.throwAuthorizationIfNotEqual(1, 2));
  }

  @Test
  void checkThrowAccess_whenEqual_thenNothing() {
    Assertions.assertDoesNotThrow(() -> ThrowHelper.throwAccessDeniedIfNotEqual(1, 1));
  }

  @Test
  void checkThrowAccess_whenNotEqual_thenThrows() {
    Assertions.assertThrows(
        AccessDeniedException.class, () -> ThrowHelper.throwAccessDeniedIfNotEqual(1, 2));
  }
}
