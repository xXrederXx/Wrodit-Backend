package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDTO(
        @NotBlank(message = "Username must not be empty")
        @Size(max = 50, message = "Username length must be not more than 45")
        String username,

        @NotBlank(message = "Email must not be empty")
        @Size(max = 255, message = "Email length must be not more than 255")
        String email,

        @NotBlank(message = "Password must not be empty")
        @Size(max = 255, message = "Password length must be not more than 255")
        String password
) {
}
