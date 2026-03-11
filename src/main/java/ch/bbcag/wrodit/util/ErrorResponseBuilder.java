package ch.bbcag.wrodit.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ErrorResponseBuilder<T> {
    private LocalDateTime timestamp;
    private T body;
    private HttpStatus status;


    public static <T> ErrorResponseBuilder<T> create() {
        return new ErrorResponseBuilder<>();
    }

    public ErrorResponseBuilder<T> withBody(T body) {
        this.body = body;
        return this;
    }

    public ErrorResponseBuilder<T> withStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ErrorResponseBuilder<T> withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ResponseEntity<ErrorResponseDTO<T>> buildResponse()
    {
        return new ResponseEntity<>(new ErrorResponseDTO<>(body, timestamp == null ? LocalDateTime.now() : timestamp), status);
    }
}
