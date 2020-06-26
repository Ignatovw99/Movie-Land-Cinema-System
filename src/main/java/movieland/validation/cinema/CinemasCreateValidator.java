package movieland.validation.cinema;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.models.binding.cinema.CinemaCreateBindingModel;
import movieland.repositories.CinemasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.CinemaConstants.*;

@movieland.validation.Validator
public class CinemasCreateValidator implements Validator {

    private final static Pattern PHONE_NUMBER_PATTERN = Pattern.compile(CinemaConstants.PHONE_NUMBER_PATTERN);

    private final static Pattern EMAIL_PATTERN = Pattern.compile(CinemaConstants.EMAIL_PATTERN);

    private final CinemasRepository cinemasRepository;

    @Autowired
    public CinemasCreateValidator(CinemasRepository cinemasRepository) {
        this.cinemasRepository = cinemasRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return CinemaCreateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CinemaCreateBindingModel cinemaCreateBindingModel = (CinemaCreateBindingModel) o;

        if (cinemaCreateBindingModel.getName() == null || cinemaCreateBindingModel.getName().trim().length() == 0) {
            errors.rejectValue(NAME_FIELD, NULL_ERROR_VALUE, NAME_NOT_EMPTY);
        } else if (cinemaCreateBindingModel.getName().trim().length() < 4) {
            errors.rejectValue(NAME_FIELD, INVALID_LENGTH_ERROR, NAME_CHARACTERS_LENGTH);
        } else if (cinemasRepository.existsByName(cinemaCreateBindingModel.getName())) {
            errors.rejectValue(NAME_FIELD, ALREADY_EXISTS_ERROR, ALREADY_EXISTS_WITH_SUCH_NAME);
        }

        if (cinemaCreateBindingModel.getAddress() == null || cinemaCreateBindingModel.getAddress().trim().length() == 0) {
            errors.rejectValue(ADDRESS_FIELD, NULL_ERROR_VALUE, ADDRESS_NOT_NULL);
        }

        if (cinemaCreateBindingModel.getPhoneNumber() == null || cinemaCreateBindingModel.getPhoneNumber().trim().length() == 0) {
            errors.rejectValue(PHONE_NUMBER_FIELD, NULL_ERROR_VALUE, PHONE_NUMBER_NOT_NULL);
        } else if (!PHONE_NUMBER_PATTERN.matcher(cinemaCreateBindingModel.getPhoneNumber()).matches()) {
            errors.rejectValue(PHONE_NUMBER_FIELD, INVALID_VALUE, PHONE_NUMBER_INVALID_VALUE);
        }

        if (cinemaCreateBindingModel.getEmail() == null || cinemaCreateBindingModel.getEmail().trim().length() == 0) {
            errors.rejectValue(EMAIL_FIELD, NULL_ERROR_VALUE, EMAIL_NOT_NULL);
        } else if (!EMAIL_PATTERN.matcher(cinemaCreateBindingModel.getEmail()).matches()) {
            errors.rejectValue(EMAIL_FIELD, INVALID_VALUE, EMAIL_INVALID_VALUE);
        }

        boolean isOpeningTimeNull = false;

        if (cinemaCreateBindingModel.getOpeningTime() == null) {
            isOpeningTimeNull = true;
            errors.rejectValue(OPENING_TIME_FIELD, NULL_ERROR_VALUE, OPENING_TIME_NOT_NULL);
        }

        if (cinemaCreateBindingModel.getClosingTime() == null) {
            errors.rejectValue(CLOSING_TIME_FIELD, NULL_ERROR_VALUE, CLOSING_TIME_NOT_NULL);
        } else if (!isOpeningTimeNull && !cinemaCreateBindingModel.getOpeningTime().isBefore(cinemaCreateBindingModel.getClosingTime())) {
            errors.rejectValue(CLOSING_TIME_FIELD, INVALID_VALUE, String.format(CLOSING_TIME_SHOULD_BE_AFTER_OPENING_TIME, DateTimeFormatter.ofPattern(TIME_12_HOUR_FORMAT_PATTERN).format(cinemaCreateBindingModel.getOpeningTime())));
        }
    }
}
