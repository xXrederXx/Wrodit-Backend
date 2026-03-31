package ch.bbcag.wrodit.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.repos.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class WroditUserDetailsServiceTest {
  private static UserRepository mockRepo;
  private static WroditUserDetailsService service;

  @BeforeAll
  static void init() {
    mockRepo = mock(UserRepository.class);
    service = new WroditUserDetailsService(mockRepo);
  }

  @Test
  void checkLoadUser_whenValid_thenSuccess() {
    User mockUser = TestingUtil.generateUser();
    when(mockRepo.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

    var result = service.loadUserByUsername("tester");

    assertEquals(mockUser.getUsername(), result.getUsername());
    assertEquals(mockUser.getPasswordHash(), result.getPassword());
  }

  @Test
  void checkLoadUser_whenInvalid_thenThrow() {
    when(mockRepo.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("tester"));
  }
}
