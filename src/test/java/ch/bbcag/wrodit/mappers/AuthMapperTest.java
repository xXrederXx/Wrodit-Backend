package ch.bbcag.wrodit.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bbcag.wrodit.TestingUtil;
import ch.bbcag.wrodit.dto.request.AuthRequestDTO;
import ch.bbcag.wrodit.dto.response.AuthResponseDTO;
import ch.bbcag.wrodit.entities.User;
import ch.bbcag.wrodit.mapper.AuthMapper;
import org.junit.jupiter.api.Test;

class AuthMapperTest {
  @Test
  void checkToDto_whenValidEntity_thenDto() {
    User mockUser = TestingUtil.generateUser();
    AuthResponseDTO dto = AuthMapper.toDTO(mockUser);

    assertEquals(mockUser.getId(), dto.id());
    assertEquals(mockUser.getUsername(), dto.username());
  }

  @Test
  void checkFromDto_whenValidEntity_thenDto() {
    AuthRequestDTO dto = generateDto();
    User user = AuthMapper.fromDTO(dto);

    assertEquals(dto.username(), user.getUsername());
    assertEquals(dto.email(), user.getEmail());
    assertEquals(dto.password(), user.getPasswordHash());
  }

  private AuthRequestDTO generateDto() {
    return new AuthRequestDTO("Tester", "test@test.com", "Strong+Password7");
  }
}
