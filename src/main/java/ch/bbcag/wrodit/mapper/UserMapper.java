package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.UserRequestDTO;
import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.dto.response.UserValidateResponseDTO;
import ch.bbcag.wrodit.entitys.User;

import java.util.Optional;

public class UserMapper {
  private UserMapper() { // hide ctor
  }

  public static User fromDto(UserRequestDTO dto) {
    User user = new User();
    user.setUsername(dto.username());
    user.setPasswordHash(dto.password());
    return user;
  }

  public static UserResponseDTO toUserDto(User user, boolean includeEmail) {
    return new UserResponseDTO(
        user.getId(), user.getUsername(), includeEmail ? user.getEmail() : "");
  }

  public static UserValidateResponseDTO toValidateDTO(Optional<User> user) {
      return user.map(value -> new UserValidateResponseDTO(value.getId(), true)).orElseGet(() -> new UserValidateResponseDTO(null, false));
  }
}
