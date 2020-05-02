package movieland.validation.annotations.phonenumber;

import movieland.constants.entities.CinemaConstants;
import movieland.validation.annotations.ValidationStatements;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default ValidationStatements.PHONE_NUMBER_INVALID_VALUE;

    boolean nullable() default false;

    String pattern() default CinemaConstants.PHONE_NUMBER_PATTERN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
