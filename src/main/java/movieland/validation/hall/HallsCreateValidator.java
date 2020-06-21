package movieland.validation.hall;

import movieland.domain.models.binding.HallCreateBindingModel;
import movieland.repositories.CinemasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.HallConstants.*;

@movieland.validation.Validator
public class HallsCreateValidator implements Validator {

    private final CinemasRepository cinemasRepository;

    @Autowired
    public HallsCreateValidator(CinemasRepository cinemasRepository) {
        this.cinemasRepository = cinemasRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return HallCreateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HallCreateBindingModel hallCreateBindingModel = (HallCreateBindingModel) o;

        if (hallCreateBindingModel.getName() == null || hallCreateBindingModel.getName().trim().length() == 0) {
            errors.rejectValue(NAME_FIELD, NULL_ERROR_VALUE, NAME_NOT_EMPTY);
        } else if (hallCreateBindingModel.getName().trim().length() < 4) {
            errors.rejectValue(NAME_FIELD, INVALID_LENGTH_ERROR, NAME_CHARACTERS_LENGTH);
        } else if (cinemasRepository.existsByIdAndHallsName(hallCreateBindingModel.getCinemaId(), hallCreateBindingModel.getName())) {
            errors.rejectValue(NAME_FIELD, ALREADY_EXISTS_ERROR, ALREADY_EXISTS_WITH_SUCH_NAME);
        }

        if (hallCreateBindingModel.getRows() == null) {
            errors.rejectValue(ROWS_FIELD, NULL_ERROR_VALUE, ROWS_NOT_NULL);
        } else if (hallCreateBindingModel.getRows() <= 0) {
            errors.rejectValue(ROWS_FIELD, INVALID_VALUE, ROWS_NOT_NEGATIVE_OR_ZERO);
        }

        if (hallCreateBindingModel.getColumns() == null) {
            errors.rejectValue(COLUMNS_FIELD, NULL_ERROR_VALUE, COLUMNS_NOT_NULL);
        } else if (hallCreateBindingModel.getColumns() <= 0) {
            errors.rejectValue(COLUMNS_FIELD, INVALID_VALUE, COLUMNS_NOT_NEGATIVE_OR_ZERO);
        }

        if (hallCreateBindingModel.getFilmTechnology() == null) {
            errors.rejectValue(FILM_TECHNOLOGY_FIELD, NULL_ERROR_VALUE, FILM_TECHNOLOGY_NOT_NULL);
        }

        if (hallCreateBindingModel.getSoundSystem() == null) {
            errors.rejectValue(SOUND_SYSTEM_FIELD, NULL_ERROR_VALUE, SOUND_SYSTEM_NOT_NULL);
        }

        if (hallCreateBindingModel.getCinemaId() == null) {
            errors.rejectValue(CINEMA_ID_FIELD, NULL_ERROR_VALUE, CINEMA_NOT_NULL);
        }
    }
}
