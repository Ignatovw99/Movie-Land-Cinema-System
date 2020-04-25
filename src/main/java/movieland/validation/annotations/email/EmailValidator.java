package movieland.validation.annotations.email;

import movieland.validation.annotations.ValidationStatements;
import movieland.validation.annotations.ValidatorUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, CharSequence> {

    private boolean nullable;

    private String message;

    private Pattern pattern;

    @Override
    public void initialize(Email emailAnnotation) {
        nullable = emailAnnotation.nullable();
        message = emailAnnotation.message();
        pattern = Pattern.compile(emailAnnotation.pattern());
    }

    @Override
    public boolean isValid(CharSequence inputValue, ConstraintValidatorContext validatorContext) {
        if (inputValue == null) {
            if (nullable) {
                return true;
            } else {
                ValidatorUtils.setErrorMessage(validatorContext, ValidationStatements.EMAIL_CAN_NOT_BE_NULL);
                return false;
            }
        }

        String email = inputValue.toString();

        if (!pattern.matcher(email).matches()) {
            ValidatorUtils.setErrorMessage(validatorContext, message);
            return false;
        }

        return true;
    }
}
