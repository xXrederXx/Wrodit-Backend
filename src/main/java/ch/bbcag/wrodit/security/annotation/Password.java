package ch.bbcag.wrodit.security.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
// Java annotations
@NotBlank(message = "Password must not be empty")
@Size(min = 8, max = 255, message = "Password length must be not more than 255")
@Pattern(regexp = ".*[a-z].*", message = "Password must contain a lower case letter")
@Pattern(regexp = ".*[A-Z].*", message = "Password must contain a upper case letter")
@Pattern(regexp = ".*\\d.*", message = "Password must contain a number")
@Pattern(regexp = ".*\\W.*", message = "Password must contain a symbol")
public @interface Password {}
