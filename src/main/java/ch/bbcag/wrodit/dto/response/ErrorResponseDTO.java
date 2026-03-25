package ch.bbcag.wrodit.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO<T>(T body, LocalDateTime timestamp) {}
