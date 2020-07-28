package movieland.services.validation.implementations;

import movieland.constants.entities.UserConstants;
import movieland.domain.models.service.UserServiceModel;
import movieland.services.validation.UsersValidationService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UsersValidationServiceImpl implements UsersValidationService {

    private boolean isPasswordValid(String password) {
        return password != null
                && password.length() >= UserConstants.PASSWORD_MIN_SIZE;
    }

    private boolean isEmailValid(String email) {
        return email != null
                && email.isEmpty()
                && Pattern.compile(UserConstants.EMAIL_PATTERN).matcher(email).matches();
    }
    @Override
    public boolean isValid(UserServiceModel userServiceModel) {
        return userServiceModel != null
                && isEmailValid(userServiceModel.getEmail())
                && isPasswordValid(userServiceModel.getPassword());
    }
}
