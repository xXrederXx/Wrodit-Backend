package ch.bbcag.wrodit.util.exception;

import java.util.List;
import java.util.Map;

public class FailedValidationException extends RuntimeException {
  private final Map<String, List<String>> errors;

  public FailedValidationException(Map<String, List<String>> errors) {
    this.errors = errors;
  }

  public Map<String, List<String>> getErrors() {
    return errors;
  }
}
