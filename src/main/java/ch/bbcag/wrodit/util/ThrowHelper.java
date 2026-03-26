package ch.bbcag.wrodit.util;

import org.springframework.security.authorization.AuthorizationDeniedException;

public class ThrowHelper {
  private ThrowHelper() {
    // hide ctor
  }

  public static void throwAuthorizationIfNotEqual(Integer idA, Integer idB) {
    if (!idA.equals(idB)) {
      throw new AuthorizationDeniedException("Forbidden");
    }
  }
}
