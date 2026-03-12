package ch.bbcag.wrodit.util;

import java.time.LocalDateTime;

public record ErrorResponseDTO<T>(T body, LocalDateTime timestamp) {}
