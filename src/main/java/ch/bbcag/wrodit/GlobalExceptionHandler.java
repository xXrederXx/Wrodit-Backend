package ch.bbcag.wrodit;

import ch.bbcag.wrodit.util.ErrorResponseBuilder;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleAllExceptions(Exception ex) {
    logger.error("An error occurred: {}", ex.getMessage(), ex);
    return new ErrorResponseBuilder<String>()
        .withBody("An unexpected error occurred")
        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        .buildResponse();
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<?> handleAuthDeniedExceptions(Exception ex) {
    return new ErrorResponseBuilder<String>()
        .withBody("Unautherized")
        .withStatus(HttpStatus.UNAUTHORIZED)
        .buildResponse();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleResourceNotFound(MethodArgumentNotValidException ex) {
    final Map<String, List<String>> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              final String fieldName = ((FieldError) error).getField();
              final String errorMessage = error.getDefaultMessage();

              if (!errors.containsKey(fieldName)) errors.put(fieldName, new ArrayList<>());

              errors.get(fieldName).add(errorMessage);
            });
    return new ErrorResponseBuilder<Map<String, List<String>>>()
        .withBody(errors)
        .withStatus(HttpStatus.BAD_REQUEST)
        .buildResponse();
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    return new ErrorResponseBuilder<String>()
        .withBody("Data integrity violation")
        .withStatus(HttpStatus.CONFLICT)
        .buildResponse();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
    return new ResponseEntity<>("Access Denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
  }
}
