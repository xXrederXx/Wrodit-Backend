package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;

public record UserResponseDTO(
    Integer id, String username, String email, OffsetDateTime createdAt) {}
