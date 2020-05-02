package movieland.validation.annotations.email;

import movieland.constants.entities.CinemaConstants;
import movieland.validation.annotations.ValidationStatements;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    String message() default ValidationStatements.EMAIL_INVALID_VALUE;

    boolean nullable() default false;

    String pattern() default CinemaConstants.EMAIL_PATTERN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
