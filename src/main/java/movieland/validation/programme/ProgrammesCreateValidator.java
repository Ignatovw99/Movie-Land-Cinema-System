package movieland.validation.programme;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.models.binding.programme.ProgrammeCreateBindingModel;
import movieland.repositories.CinemasRepository;
import movieland.services.interfaces.ProgrammesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.ProgrammeConstants.*;

@movieland.validation.Validator
public class ProgrammesCreateValidator implements Validator {

    private final ProgrammesService programmesService;

    private final CinemasRepository cinemasRepository;

    @Autowired
    public ProgrammesCreateValidator(ProgrammesService programmesService, CinemasRepository cinemasRepository) {
        this.programmesService = programmesService;
        this.cinemasRepository = cinemasRepository;
    }

    @Override
    public boolean supports(Class<?> modelClass) {
        return ProgrammeCreateBindingModel.class.equals(modelClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProgrammeCreateBindingModel programmeCreateBindingModel = (ProgrammeCreateBindingModel) o;

        LocalDate requiredStartDate = programmesService.getFirstPossibleStartDateForCinema(programmeCreateBindingModel.getCinemaId());

        if (programmeCreateBindingModel.getCinemaId() == null) {
            errors.rejectValue(CINEMA_ID_FIELD, NULL_ERROR_VALUE, CINEMA_NOT_NULL);
        } else if (!cinemasRepository.existsById(programmeCreateBindingModel.getCinemaId())) {
            errors.rejectValue(CINEMA_ID_FIELD, INVALID_VALUE, CinemaConstants.CINEMA_NOT_FOUND);
        }

        if (programmeCreateBindingModel.getStartDate() == null) {
            errors.rejectValue(START_DATE_FILED, NULL_ERROR_VALUE, START_DATE_NOT_NULL);
        } else if (!requiredStartDate.isEqual(programmeCreateBindingModel.getStartDate())) {
            errors.rejectValue(START_DATE_FILED, INVALID_VALUE, String.format(START_DATE_NOT_VALID, requiredStartDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN))));
        }

        LocalDate startDate = programmeCreateBindingModel.getStartDate();

        if (programmeCreateBindingModel.getEndDate() == null) {
            errors.rejectValue(END_DATE_FILED, NULL_ERROR_VALUE, END_DATE_NOT_NULL);
        } else if (startDate != null && !startDate.isBefore(programmeCreateBindingModel.getEndDate())) {
            errors.rejectValue(END_DATE_FILED, INVALID_LENGTH_ERROR, END_DATE_AFTER_START_DATE);
        } else if (startDate != null && (Period.between(startDate, programmeCreateBindingModel.getEndDate()).getDays() < 7 || Period.between(startDate, programmeCreateBindingModel.getEndDate()).getDays() > 14)) {
            errors.rejectValue(END_DATE_FILED, INVALID_VALUE, PROGRAMME_LENGTH_NOT_VALID);
        }
    }
}
