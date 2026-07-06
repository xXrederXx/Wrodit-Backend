package ch.bbcag.wrodit.dto.response.backup;

import java.time.OffsetDateTime;

public record ThreadBackupDTO(
    Integer id,
    String name,
    String description,
    String banner_image_path,
    String icon_image_path,
    OffsetDateTime created_at) {}
