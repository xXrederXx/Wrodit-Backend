package ch.bbcag.wrodit.security;

import ch.bbcag.wrodit.controllers.AuthController;

public class SecurityConstants {
  private SecurityConstants() {
    // hide ctor
  }

  public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

  public static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14L; // 2 Weeks

  public static final String AUTH_ENDPOINTS = AuthController.PATH + "/**";
  public static final String[] DOCS_ENDPOINTS = {
    "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
  };
}
