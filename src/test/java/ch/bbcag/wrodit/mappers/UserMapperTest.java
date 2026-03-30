package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.*;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class UserMapperTest {
  @Test
  void checkToDto_whenValidEntityWithEMail_thenDto() {
    User mockUser = TestingUtil.generateUser();
    UserResponseDTO dto = UserMapper.toDto(mockUser, true);

    assertNotNull(dto.email());

    assertEquals(mockUser.getId(), dto.id());
    assertEquals(mockUser.getUsername(), dto.username());
    assertEquals(mockUser.getEmail(), dto.email());
    assertEquals(mockUser.getCreatedAt(), dto.createdAt());
  }

  @Test
  void checkToDto_whenValidEntityWithoutEMail_thenDto() {
    User mockUser = TestingUtil.generateUser();
    UserResponseDTO dto = UserMapper.toDto(mockUser, false);

    assertTrue(StringUtils.isBlank(dto.email()));

    assertEquals(mockUser.getId(), dto.id());
    assertEquals(mockUser.getUsername(), dto.username());
    assertEquals(mockUser.getCreatedAt(), dto.createdAt());
  }
}
