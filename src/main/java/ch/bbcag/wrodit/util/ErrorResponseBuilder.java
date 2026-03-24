package ch.bbcag.wrodit.util;

import ch.bbcag.wrodit.util.exception.IllegalStatusException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseBuilder<T> {
  private final Logger logger = LoggerFactory.getLogger(ErrorResponseBuilder.class);

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

  public ResponseEntity<ErrorResponseDTO<T>> buildResponse() {
    if (status == null || !status.isError()) {
      throw new IllegalStatusException(
          "Cant build an Error Response with a Non-Error Status code", status);
    }

    if (body == null) {
      logger.warn("A Response was build without a body. Consider adding one for the client.");
    }

    return new ResponseEntity<>(
        new ErrorResponseDTO<>(body, timestamp == null ? LocalDateTime.now() : timestamp), status);
  }
}
