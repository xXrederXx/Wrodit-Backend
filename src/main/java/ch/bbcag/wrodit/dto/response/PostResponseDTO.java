package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;

public record PostResponseDTO(
    Integer id,
    String title,
    String content,
    Integer vote,
    OffsetDateTime createdAt,
    UserResponseDTO user,
    Integer threadId) {}
