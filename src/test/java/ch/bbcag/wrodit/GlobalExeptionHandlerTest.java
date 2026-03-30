package ch.bbcag.wrodit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bbcag.wrodit.util.exception.FailedValidationException;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;

class GlobalExceptionHandlerTest {
    private static GlobalExceptionHandler handler;

    @BeforeAll
    static void init() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void checkAllExceptions_returns500() {
        ResponseEntity<?> res = handler.handleAllExceptions(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

    @Test
    void checkAuthorizationDeniedException_returns401() {
        ResponseEntity<?> res =
                handler.handleAuthorizationDeniedException(new AuthorizationDeniedException(""));
        assertEquals(HttpStatus.UNAUTHORIZED, res.getStatusCode());
    }

    @Test
    void checkMethodArgumentNotValidException_returns400() {
        ResponseEntity<?> res = handler.handleAllExceptions(new Exception());
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void checkDataIntegrityViolationException_returns409() {
        ResponseEntity<?> res =
                handler.handleDataIntegrityViolationException(new DataIntegrityViolationException(""));
        assertEquals(HttpStatus.CONFLICT, res.getStatusCode());
    }

    @Test
    void checkAccessDeniedException_returns403() {
        ResponseEntity<?> res = handler.handleAllExceptions(new AccessDeniedException("Forbidden"));
        assertEquals(HttpStatus.FORBIDDEN, res.getStatusCode());
    }

    @Test
    void checkFailedValidationException_returns400() {
        ResponseEntity<?> res =
                handler.handleAllExceptions(new FailedValidationException(new HashMap<>(10)));
        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    void checkEntityNotFoundException_returns404() {
        ResponseEntity<?> res = handler.handleAllExceptions(new EntityNotFoundException());
        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }
}
