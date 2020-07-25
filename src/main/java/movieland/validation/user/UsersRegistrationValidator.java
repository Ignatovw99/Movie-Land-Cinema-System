package movieland.validation.user;

import movieland.constants.entities.UserConstants;
import movieland.domain.models.binding.user.UserRegisterBindingModel;
import movieland.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.UserConstants.*;

@movieland.validation.Validator
public class UsersRegistrationValidator implements Validator {

    private final static Pattern EMAIL_PATTERN = Pattern.compile(UserConstants.EMAIL_PATTERN);

    private final UsersRepository usersRepository;

    @Autowired
    public UsersRegistrationValidator(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean supports(Class<?> model) {
        return UserRegisterBindingModel.class.equals(model);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) o;

        if (userRegisterBindingModel.getEmail() == null || userRegisterBindingModel.getEmail().equals("")) {
            errors.rejectValue(EMAIL_FILED, NULL_ERROR_VALUE, EMAIL_NOT_NULL);
        } else if (!EMAIL_PATTERN.matcher(userRegisterBindingModel.getEmail()).matches()) {
            errors.rejectValue(EMAIL_FILED, INVALID_VALUE, EMAIL_NOT_VALID);
        } else if (usersRepository.existsByUsername(userRegisterBindingModel.getEmail())) {
            errors.rejectValue(EMAIL_FILED, ALREADY_EXISTS_ERROR, EMAIL_EXISTS);
        }

        if (userRegisterBindingModel.getPassword() == null || userRegisterBindingModel.getPassword().equals("")) {
            errors.rejectValue(PASSWORD_FILED, NULL_ERROR_VALUE, PASSWORD_NOT_NULL);
        } else if (userRegisterBindingModel.getPassword().length() < PASSWORD_MIN_SIZE) {
            errors.rejectValue(PASSWORD_FILED, INVALID_LENGTH_ERROR, String.format(PASSWORD_INVALID_LENGTH, PASSWORD_MIN_SIZE));
        } else {
            if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
                errors.rejectValue(CONFIRM_PASSWORD_FIELD, INVALID_VALUE, CONFIRM_PASS_NOT_VALID);
            }
        }
    }
}
