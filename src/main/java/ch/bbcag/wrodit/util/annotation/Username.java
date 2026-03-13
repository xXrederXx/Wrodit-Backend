package ch.bbcag.wrodit.util.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
// Java annotations
@NotBlank(message = "Username must not be empty")
@Size(max = 50, message = "Username length must be not more than 50")
public @interface Username {
  String message() default "Invalid username";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};
}
