package ch.bbcag.wrodit.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  private static User mockUser;

  @BeforeAll
  static void init() {
    mockUser = new User();
    mockUser.setId(1);
    mockUser.setUsername("Tester");
    mockUser.setEmail("test@test.com");
    mockUser.setPasswordHash("Some-Long-Hash");
    mockUser.setCreatedAt(OffsetDateTime.of(2026, 3, 20, 9, 13, 21, 67, ZoneOffset.UTC));
  }

  @Test
  void checkGetById_whenValidUser_thenIsReturned() throws Exception {
    Mockito.when(userService.findById(any(Integer.class))).thenReturn(mockUser);

    mockMvc
        .perform(get(UserController.PATH + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(mockUser.getId())))
        .andExpect(jsonPath("$.username", is(mockUser.getUsername())));
    // .andExpect(content().string(not(containsString("\"email\"")))); Uncomment once Issue Resolves
  }

  @Test
  void checkGetById_whenNoUser_then404Returned() throws Exception {
    Mockito.when(userService.findById(any(Integer.class))).thenThrow(EntityNotFoundException.class);

    mockMvc.perform(get(UserController.PATH + "/1")).andExpect(status().isNotFound());
  }

  @Test
  void checkGetAllById_whenValidUser_thenIsReturned() throws Exception {
    Mockito.when(userService.findById(any(Integer.class))).thenReturn(mockUser);

    mockMvc
        .perform(get(UserController.PATH + "/1/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(mockUser.getId())))
        .andExpect(jsonPath("$.email", is(mockUser.getEmail())))
        .andExpect(jsonPath("$.username", is(mockUser.getUsername())));
  }

  @Test
  void checkGetAllById_whenNoUser_then404Returned() throws Exception {
    Mockito.when(userService.findById(any(Integer.class))).thenThrow(EntityNotFoundException.class);

    mockMvc.perform(get(UserController.PATH + "/1/all")).andExpect(status().isNotFound());
  }
}
