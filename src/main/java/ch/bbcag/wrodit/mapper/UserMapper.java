package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.response.UserResponseDTO;
import ch.bbcag.wrodit.entitys.User;

public class UserMapper {
  private UserMapper() { // hide ctor
  }

  public static UserResponseDTO toDto(User user, boolean includeEmail) {
    return new UserResponseDTO(
        user.getId(), user.getUsername(), includeEmail ? user.getEmail() : "", user.getCreatedAt());
  }

}
