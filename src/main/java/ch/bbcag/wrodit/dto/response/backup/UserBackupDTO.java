package ch.bbcag.wrodit.dto.response.backup;

import java.time.OffsetDateTime;

public record UserBackupDTO(
    Integer id,
    String email,
    String username,
    String passwordHash,
    String profile_image_path,
    OffsetDateTime created_at) {}
