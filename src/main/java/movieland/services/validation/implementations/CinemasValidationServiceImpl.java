package movieland.services.validation.implementations;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.validation.CinemasValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.regex.Pattern;

import static movieland.constants.entities.CinemaConstants.*;

@Service
public class CinemasValidationServiceImpl implements CinemasValidationService {

    private boolean isNameValid(String name) {
        return name != null && name.trim().length() >= NAME_LENGTH_MIN_VALUE;
    }

    private boolean isAddressValid(String address) {
        return address != null;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber);
    }

    private boolean isEmailValid(String email) {
        return email != null && Pattern.matches(EMAIL_PATTERN, email);
    }

    private boolean isOpeningTimeBeforeClosingTime(LocalTime openingTime, LocalTime closingTime) {
        return openingTime.isBefore(closingTime);
    }

    @Override
    public boolean isValid(CinemaServiceModel cinemaServiceModel) {
        return isNameValid(cinemaServiceModel.getName())
                && isAddressValid(cinemaServiceModel.getAddress())
                && isPhoneNumberValid(cinemaServiceModel.getPhoneNumber())
                && isEmailValid(cinemaServiceModel.getEmail())
                && cinemaServiceModel.getOpeningTime() != null
                && cinemaServiceModel.getClosingTime() != null
                && isOpeningTimeBeforeClosingTime(cinemaServiceModel.getOpeningTime(), cinemaServiceModel.getClosingTime());
    }
}
