package ch.bbcag.wrodit.services;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.entitys.User;
import ch.bbcag.wrodit.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
  private UserRepository mockRepo;
  private PasswordEncoder mockEncoder;
  private UserService userService;

  private User mockUser;

  @BeforeEach
  void setup() {
    mockRepo = Mockito.mock(UserRepository.class);
    mockEncoder = Mockito.mock(PasswordEncoder.class);
    userService = new UserService(mockRepo, mockEncoder);
    mockUser = TestingUtil.generateUser();
  }

  @Test
  void checkFindById_whenValidId_thenReturn() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.of(mockUser));

    Assertions.assertEquals(mockUser, userService.findById(1));
  }

  @Test
  void checkFindById_whenInvalidId_thenThrow() {
    when(mockRepo.findById(anyInt())).thenReturn(Optional.empty());

    Assertions.assertThrows(EntityNotFoundException.class, () -> userService.findById(1));
  }

  @Test
  void checkFindByUsername_whenValidUsername_thenReturn() {
    when(mockRepo.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

    Assertions.assertEquals(mockUser, userService.findByUsername("user"));
  }

  @Test
  void checkFindByUsername_whenInvalidUsername_thenThrow() {
    when(mockRepo.findByUsername(anyString())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        EntityNotFoundException.class, () -> userService.findByUsername("user"));
  }

  @Test
  void checkInsert_whenValidUser_thenHashAndSave() {
    User user = TestingUtil.generateUser();
    user.setPasswordHash("plain");

    when(mockEncoder.encode("plain")).thenReturn("encoded");
    when(mockRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User result = userService.insert(user);

    Assertions.assertEquals("encoded", result.getPasswordHash());
    Assertions.assertTrue(
        OffsetDateTime.now().toEpochSecond() - result.getCreatedAt().toEpochSecond()
            < TestingUtil.MAX_TIME_CHECK_DIFF);
    verify(mockEncoder).encode(anyString());
    verify(mockRepo).save(user);
  }
}
