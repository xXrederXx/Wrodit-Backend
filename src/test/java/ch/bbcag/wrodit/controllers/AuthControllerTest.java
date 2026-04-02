package ch.bbcag.wrodit.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.security.JWTGenerator;
import ch.bbcag.wrodit.services.UserService;
import ch.bbcag.wrodit.util.URIHelper;
import ch.bbcag.wrodit.util.exception.JwtGenerationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private AuthenticationManager authenticationManager;

  private static User mockUser;

  @BeforeAll
  static void init() {
    mockUser = TestingUtil.generateUser();
  }

  @Test
  void checkSignUp_whenValid_thenCreated() throws Exception {
    when(userService.insert(any(User.class))).thenReturn(mockUser);

    mockMvc
        .perform(
            post(URIHelper.join(AuthController.PATH, "signup"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                        {
                        "username":"admin",
                        "email":"admin@wrodit.ch",
                        "password":"Admin123+"
                        }
                        """))
        .andExpect(status().isCreated());
  }

  @Test
  void checkSignUp_whenInvalid_thenBadRequest() throws Exception {
    when(userService.insert(any(User.class))).thenReturn(mockUser);

    mockMvc
        .perform(
            post(URIHelper.join(AuthController.PATH, "signup"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                        {
                        "usernameNO":"admin",
                        "email":"admin@wrodit.ch",
                        "password":"Admin123+"
                        }
                        """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void checkSignUp_whenConflict_thenConflict() throws Exception {
    when(userService.insert(any(User.class))).thenThrow(DataIntegrityViolationException.class);

    mockMvc
        .perform(
            post(URIHelper.join(AuthController.PATH, "signup"))
                .contentType(TestingUtil.CONTENT_TYPE_JSON)
                .content(
                    """
                        {
                        "username":"admin",
                        "email":"admin@wrodit.ch",
                        "password":"Admin123+"
                        }
                        """))
        .andExpect(status().isConflict());
  }

  @Test
  void checkSignIn_whenValid_thenOk() throws Exception {
    String fakeToken = "FAKE_JWT_TOKEN";
    Authentication authentication = Mockito.mock(Authentication.class);
    when(userService.findByUsername(anyString())).thenReturn(AuthControllerTest.mockUser);
    when(authentication.getName()).thenReturn(mockUser.getUsername());
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    try (MockedStatic<JWTGenerator> jwtMock = Mockito.mockStatic(JWTGenerator.class)) {
      jwtMock
          .when(() -> JWTGenerator.generateJwtToken(anyInt(), anyString()))
          .thenReturn(fakeToken);
      mockMvc
          .perform(
              post("/auth/signin")
                  .contentType("application/json")
                  .content(
                      """
                                                                                            {
                                                                         "username":"admin",
                        "email":"admin@wrodit.ch",
                        "password":"Admin123+"
                                                                                            }"""))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.accessToken").value(fakeToken));
    }
  }

  @Test
  void checkSignIn_whenInvalid_thenBadRequest() throws Exception {
    String fakeToken = "FAKE_JWT_TOKEN";
    Authentication authentication = Mockito.mock(Authentication.class);
    when(userService.findByUsername(anyString())).thenReturn(AuthControllerTest.mockUser);
    when(authentication.getName()).thenReturn(mockUser.getUsername());
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    try (MockedStatic<JWTGenerator> jwtMock = Mockito.mockStatic(JWTGenerator.class)) {
      jwtMock
          .when(() -> JWTGenerator.generateJwtToken(anyInt(), anyString()))
          .thenReturn(fakeToken);
      mockMvc
          .perform(
              post("/auth/signin")
                  .contentType("application/json")
                  .content(
                      """
                                                                                                            {
                                                                                         "username!!!!":"admin",
                                        "email":"admin@wrodit.ch",
                                        "password":"Admin123+"
                                                                                                            }"""))
          .andExpect(status().isBadRequest());
    }
  }

  @Test
  void checkSignIn_whenWrongPasswd_thenUnauthorized() throws Exception {
    String fakeToken = "FAKE_JWT_TOKEN";
    Authentication authentication = Mockito.mock(Authentication.class);

    when(userService.findByUsername(anyString())).thenReturn(mockUser);
    when(authentication.getName()).thenReturn(mockUser.getUsername());
    when(authentication.isAuthenticated()).thenReturn(false);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    try (MockedStatic<JWTGenerator> jwtMock = Mockito.mockStatic(JWTGenerator.class)) {
      jwtMock
          .when(() -> JWTGenerator.generateJwtToken(anyInt(), anyString()))
          .thenReturn(fakeToken);
      mockMvc
          .perform(
              post("/auth/signin")
                  .contentType("application/json")
                  .content(
                      """
                                                                                                            {
                                                                                         "username!!!!":"admin",
                                        "email":"admin@wrodit.ch",
                                        "password":"Admin123+"
                                                                                                            }"""))
          .andExpect(status().isBadRequest());
    }
  }

  @Test
  void checkSignIn_whenJwtFails_then500() throws Exception {
    Authentication authentication = Mockito.mock(Authentication.class);
    when(userService.findByUsername(anyString())).thenReturn(AuthControllerTest.mockUser);
    when(authentication.getName()).thenReturn(mockUser.getUsername());
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    try (MockedStatic<JWTGenerator> jwtMock = Mockito.mockStatic(JWTGenerator.class)) {
      jwtMock
          .when(() -> JWTGenerator.generateJwtToken(anyInt(), anyString()))
          .thenThrow(JwtGenerationException.class);
      mockMvc
          .perform(
              post("/auth/signin")
                  .contentType("application/json")
                  .content(
                      """
                                                        {
                                                        "username":"admin",
                                                        "email":"admin@wrodit.ch",
                                                        "password":"Admin123+"
                                                        }"""))
          .andExpect(status().isInternalServerError());
    }
  }
}
