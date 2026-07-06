package ch.bbcag.wrodit.dto.response.backup;

import java.time.OffsetDateTime;

public record CommentBackupDTO(
    Integer id,
    Integer users_id,
    String content,
    OffsetDateTime created_at,
    Integer parent_comments_id,
    Integer posts_id) {}
