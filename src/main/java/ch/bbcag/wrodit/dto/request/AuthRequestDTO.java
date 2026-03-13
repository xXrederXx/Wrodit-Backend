package ch.bbcag.wrodit.dto.request;

import ch.bbcag.wrodit.util.annotation.Password;
import ch.bbcag.wrodit.util.annotation.Username;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDTO(
    @Username String username,
    @NotBlank(message = "Email must not be empty")
        @Size(max = 255, message = "Email length must be not more than 255")
        String email,
    @Password String password) {}
