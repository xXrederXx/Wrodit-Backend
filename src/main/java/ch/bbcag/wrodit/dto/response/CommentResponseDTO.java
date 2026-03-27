package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;

public record CommentResponseDTO(
    Integer id,
   
    String content,
   
    Integer votes,
   
    OffsetDateTime createdAt,
   
    Integer parentId,
    Integer postId,
    Integer userId) {}
