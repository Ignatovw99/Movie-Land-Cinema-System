package movieland.validation.hall;

import movieland.domain.entities.Hall;
import movieland.domain.models.binding.hall.HallUpdateBindingModel;
import movieland.repositories.CinemasRepository;
import movieland.repositories.HallsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.HallConstants.*;

@movieland.validation.Validator
public class HallsUpdateValidator implements Validator {

    private final HallsRepository hallsRepository;

    private final CinemasRepository cinemasRepository;

    @Autowired
    public HallsUpdateValidator(HallsRepository hallsRepository, CinemasRepository cinemasRepository) {
        this.hallsRepository = hallsRepository;
        this.cinemasRepository = cinemasRepository;
    }

    private boolean isHallNameTheSame(String hallId, String newHallName) {
        Optional<Hall> hall = hallsRepository.findById(hallId);
        if (hall.isEmpty()) {
            return false;
        }
        return hall.get().getName().equals(newHallName);
    }

    private boolean isHallCinemaChanged(String hallId, String cinemaId) {
        Optional<Hall> hall = hallsRepository.findById(hallId);
        if (hall.isEmpty()) {
            return false;
        }
        return !hall.get().getCinema().getId().equals(cinemaId);
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return HallUpdateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HallUpdateBindingModel hallUpdateBindingModel = (HallUpdateBindingModel) o;

        if (hallUpdateBindingModel.getName() == null || hallUpdateBindingModel.getName().trim().length() == 0) {
            errors.rejectValue(NAME_FIELD, NULL_ERROR_VALUE, NAME_NOT_EMPTY);
        } else if (hallUpdateBindingModel.getName().trim().length() < 4) {
            errors.rejectValue(NAME_FIELD, INVALID_LENGTH_ERROR, NAME_CHARACTERS_LENGTH);
        } else if (!isHallNameTheSame(hallUpdateBindingModel.getId(), hallUpdateBindingModel.getName()) && cinemasRepository.existsByIdAndHallsName(hallUpdateBindingModel.getCinemaId(), hallUpdateBindingModel.getName())) {
            errors.rejectValue(NAME_FIELD, ALREADY_EXISTS_ERROR, ALREADY_EXISTS_WITH_SUCH_NAME);
        }

        if (hallUpdateBindingModel.getRows() == null) {
            errors.rejectValue(ROWS_FIELD, NULL_ERROR_VALUE, ROWS_NOT_NULL);
        } else if (hallUpdateBindingModel.getRows() <= 0) {
            errors.rejectValue(ROWS_FIELD, INVALID_VALUE, ROWS_NOT_NEGATIVE_OR_ZERO);
        }

        if (hallUpdateBindingModel.getColumns() == null) {
            errors.rejectValue(COLUMNS_FIELD, NULL_ERROR_VALUE, COLUMNS_NOT_NULL);
        } else if (hallUpdateBindingModel.getColumns() <= 0) {
            errors.rejectValue(COLUMNS_FIELD, INVALID_VALUE, COLUMNS_NOT_NEGATIVE_OR_ZERO);
        }

        if (hallUpdateBindingModel.getFilmTechnology() == null) {
            errors.rejectValue(FILM_TECHNOLOGY_FIELD, NULL_ERROR_VALUE, FILM_TECHNOLOGY_NOT_NULL);
        }

        if (hallUpdateBindingModel.getSoundSystem() == null) {
            errors.rejectValue(SOUND_SYSTEM_FIELD, NULL_ERROR_VALUE, SOUND_SYSTEM_NOT_NULL);
        }

        if (hallUpdateBindingModel.getCinemaId() == null) {
            errors.rejectValue(CINEMA_ID_FIELD, NULL_ERROR_VALUE, CINEMA_NOT_NULL);
        } else if (isHallCinemaChanged(hallUpdateBindingModel.getId(), hallUpdateBindingModel.getCinemaId())) {
            errors.rejectValue(CINEMA_ID_FIELD, INVALID_VALUE, HALL_CAN_NOT_CHANGE_ITS_CINEMA);
        }
    }
}
