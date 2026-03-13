package ch.bbcag.wrodit.dto.response;

import java.time.OffsetDateTime;

public record ThreadResponseDTO(
    Integer id, String name, String description, OffsetDateTime createdAt) {}
