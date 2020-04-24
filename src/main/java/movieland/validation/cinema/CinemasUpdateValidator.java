package movieland.validation.cinema;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@movieland.validation.Validator
public class CinemasUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
