package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;

public record CommentResponseDTO(
    Integer id, String content, OffsetDateTime createdAt, Integer parentId) {}
