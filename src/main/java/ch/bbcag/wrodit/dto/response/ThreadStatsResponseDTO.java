package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ThreadStatsResponseDTO(
    Integer id,
    String name,
    String description,
    OffsetDateTime createdAt,
    Integer numberPosts,
    Integer numActiveUsers,
    List<UserResponseDTO> activeUsers,
    PostResponseDTO lastPost) {}
