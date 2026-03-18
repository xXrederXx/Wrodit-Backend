package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequestDTO(
    @NotBlank(message = "Title cant be empty")
        @Size(max = 255, message = "Title cant be longer than 255")
        String title,
    @NotBlank(message = "Content cant be empty") String content,
    @NotNull Integer threadId) {}
