package ch.bbcag.wrodit.util;

import org.springframework.http.HttpStatus;

public class IllegalStatusException extends RuntimeException {
  public IllegalStatusException(String message, HttpStatus status) {
    super(message + "\n\tStatus: " + status);
  }
}
