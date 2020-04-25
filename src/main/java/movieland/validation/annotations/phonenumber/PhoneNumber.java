package movieland.validation.annotations.phonenumber;

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

    String pattern() default "^\\+[0-9]{11}$";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
