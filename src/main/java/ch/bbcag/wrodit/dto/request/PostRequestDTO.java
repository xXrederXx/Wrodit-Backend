package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
    @NotBlank(message = "Title cant be empty")
        @Size(max = 255, message = "Title cant be longer than 255")
        String title,
    @NotBlank(message = "Content cant be empty") String content) {}
