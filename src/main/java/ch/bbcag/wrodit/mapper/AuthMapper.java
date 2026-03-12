package ch.bbcag.wrodit.mapper;

import ch.bbcag.wrodit.dto.request.AuthRequestDTO;
import ch.bbcag.wrodit.dto.response.AuthResponseDTO;
import ch.bbcag.wrodit.entitys.User;

public class AuthMapper {
    private AuthMapper() {// hide ctor
    }

    public static User fromDTO(AuthRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPasswordHash(dto.password());
        return user;
    }

    public static AuthResponseDTO toDTO(User user) {
        return new AuthResponseDTO(user.getId(), user.getUsername());
    }
}
