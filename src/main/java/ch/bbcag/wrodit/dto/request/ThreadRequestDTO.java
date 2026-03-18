package ch.bbcag.wrodit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ThreadRequestDTO(
    @NotBlank(message = "Thread name cant be empty")
        @Size(max = 150, message = "Thread names cant be longer than 150")
        String name,
    String description) {}
