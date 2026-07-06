package ch.bbcag.wrodit.dto.response.backup;

import java.time.OffsetDateTime;

public record PostBackupDTO(
    Integer id,
    String title,
    String content,
    Integer users_id,
    Integer threads_id,
    OffsetDateTime created_at) {}
