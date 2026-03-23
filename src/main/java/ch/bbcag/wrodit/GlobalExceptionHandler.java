package ch.bbcag.wrodit;

import ch.bbcag.wrodit.util.ErrorResponseBuilder;
import ch.bbcag.wrodit.util.FailedValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
  public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    return new ErrorResponseBuilder<String>()
        .withBody("Unauthorized")
        .withStatus(HttpStatus.UNAUTHORIZED)
        .buildResponse();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
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
  public ResponseEntity<?> handleDataIntegrityViolationException(
      DataIntegrityViolationException ex) {
    logger.error("An error occurred: {}", ex.getMessage(), ex);
    Pattern pattern = Pattern.compile("Duplicate entry '(.*?)' for key ");
    Matcher matcher = pattern.matcher(ex.getMessage());
    if (matcher.find()) {
      return new ErrorResponseBuilder<String>()
          .withBody("Duplicate Entry: " + matcher.group(1))
          .withStatus(HttpStatus.CONFLICT)
          .buildResponse();
    }

    return new ErrorResponseBuilder<String>()
        .withBody("Data integrity violation")
        .withStatus(HttpStatus.CONFLICT)
        .buildResponse();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
    return new ErrorResponseBuilder<String>()
        .withBody("Access Denied: " + ex.getMessage())
        .withStatus(HttpStatus.FORBIDDEN)
        .buildResponse();
  }

  @ExceptionHandler(FailedValidationException.class)
  public ResponseEntity<?> handleFailedValidationException(FailedValidationException ex) {
    return new ErrorResponseBuilder<Map<String, List<String>>>().withBody(ex.getErrors()).withStatus(HttpStatus.BAD_REQUEST).buildResponse();
  }
}
