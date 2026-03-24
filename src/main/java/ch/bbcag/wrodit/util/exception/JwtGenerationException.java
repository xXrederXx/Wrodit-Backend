package ch.bbcag.wrodit.util.exception;

public class JwtGenerationException extends RuntimeException {
    public JwtGenerationException(String message) {
        super("Wasn't able to generate the Jwt.\n" + message);
    }
}
