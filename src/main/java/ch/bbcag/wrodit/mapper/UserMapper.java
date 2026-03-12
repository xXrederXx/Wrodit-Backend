package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.UserRequestDTO;
import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.entitys.User;

public class UserMapper {
  private UserMapper() { // hide ctor
  }

  public static User fromDto(UserRequestDTO dto) {
    User user = new User();
    user.setUsername(dto.username());
    user.setPasswordHash(dto.password());
    return user;
  }

  public static UserResponseDTO toDto(User user, boolean includeEmail) {
    return new UserResponseDTO(
        user.getId(), user.getUsername(), includeEmail ? user.getEmail() : "");
  }
}
