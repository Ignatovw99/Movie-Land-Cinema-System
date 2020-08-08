package movieland.validation.projection;

import movieland.domain.entities.Cinema;
import movieland.domain.entities.Programme;
import movieland.domain.models.binding.projection.ProjectionCreateBindingModel;
import movieland.repositories.CinemasRepository;
import movieland.repositories.HallsRepository;
import movieland.repositories.MoviesRepository;
import movieland.repositories.ProgrammesRepository;
import movieland.services.interfaces.ProjectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static movieland.constants.ValidationErrorCodes.*;
import static movieland.constants.entities.ProjectionConstants.*;

@movieland.validation.Validator
public class ProjectionsCreateValidator implements Validator {

    private final MoviesRepository moviesRepository;

    private final CinemasRepository cinemasRepository;

    private final HallsRepository hallsRepository;

    private final ProgrammesRepository programmesRepository;

    private final ProjectionsService projectionsService;

    @Autowired
    public ProjectionsCreateValidator(MoviesRepository moviesRepository, CinemasRepository cinemasRepository, HallsRepository hallsRepository, ProgrammesRepository programmesRepository, ProjectionsService projectionsService) {
        this.moviesRepository = moviesRepository;
        this.cinemasRepository = cinemasRepository;
        this.hallsRepository = hallsRepository;
        this.programmesRepository = programmesRepository;
        this.projectionsService = projectionsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProjectionCreateBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProjectionCreateBindingModel projectionCreateBindingModel = (ProjectionCreateBindingModel) o;

        if (projectionCreateBindingModel.getMovieId() == null || projectionCreateBindingModel.getMovieId().equals("")) {
            errors.rejectValue(MOVIE_FILED, NULL_ERROR_VALUE, MOVIE_NOT_NULL);
        } else if (!moviesRepository.existsById(projectionCreateBindingModel.getMovieId())) {
            errors.rejectValue(MOVIE_FILED, NOT_FOUND_ERROR, MOVIE_DOES_NOT_EXIST);
        }

        if (projectionCreateBindingModel.getCinemaId() == null || projectionCreateBindingModel.getCinemaId().equals("")) {
            errors.rejectValue(CINEMA_FIELD, NULL_ERROR_VALUE, CINEMA_NOT_NULL);
        } else if (!cinemasRepository.existsById(projectionCreateBindingModel.getCinemaId())) {
            errors.rejectValue(CINEMA_FIELD, NOT_FOUND_ERROR, CINEMA_DOES_NOT_EXIST);
        }

        if (projectionCreateBindingModel.getHallId() == null || projectionCreateBindingModel.getHallId().equals("")) {
            errors.rejectValue(HALL_FIELD, NULL_ERROR_VALUE, HALL_NOT_NULL);
        } else if (!hallsRepository.existsById(projectionCreateBindingModel.getHallId())) {
            errors.rejectValue(HALL_FIELD, NOT_FOUND_ERROR, HALL_DOES_NOT_EXIST);
        }

        if (projectionCreateBindingModel.getStartingTime() == null) {
            errors.rejectValue(STARTING_DATE_FILED, NULL_ERROR_VALUE, START_DATE_NOT_NULL);
        } else if (LocalDate.now().isAfter(projectionCreateBindingModel.getStartingTime().toLocalDate())) {
            if (LocalTime.now().isAfter(projectionCreateBindingModel.getStartingTime().toLocalTime())) {
                errors.rejectValue(STARTING_DATE_FILED, INVALID_VALUE, String.format(STARTING_DATE_CAN_BE_IN_PAST, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));
            }
        } else if (!projectionsService.isStaringTimeInRangeOfWorkingHours(projectionCreateBindingModel.getCinemaId(), projectionCreateBindingModel.getStartingTime())) {
            errors.rejectValue(STARTING_DATE_FILED, INVALID_VALUE, STARTING_TIME_NOT_IN_CINEMA_WORKING_HOURS);
        } else if (!isStartingTimeInRangeOfAvailableProgramme(projectionCreateBindingModel.getStartingTime(), projectionCreateBindingModel.getCinemaId())) {
            errors.rejectValue(STARTING_DATE_FILED, INVALID_VALUE, NO_AVAILABLE_PROGRAMME_AT_THIS_TIME);
        } else if (projectionCreateBindingModel.getHallId() != null && projectionCreateBindingModel.getMovieId() != null && !projectionsService.isHallFree(projectionCreateBindingModel.getHallId(), projectionCreateBindingModel.getMovieId(), projectionCreateBindingModel.getStartingTime())) {
            errors.rejectValue(STARTING_DATE_FILED, INVALID_VALUE, HALL_NOT_AVAILABLE_DURING_GIVEN_TIME);
        } else if (projectionCreateBindingModel.getMovieId() != null && projectionsService.isMovieAlreadyProjectedInCinemaAtTheGivenTime(projectionCreateBindingModel.getMovieId(), projectionCreateBindingModel.getCinemaId(), projectionCreateBindingModel.getStartingTime())) {
            errors.rejectValue(STARTING_DATE_FILED, INVALID_VALUE, PROJECTION_WITH_THIS_MOVIE_EXISTS_AT_THIS_TIME);
        }
    }

    private boolean isStartingTimeInRangeOfAvailableProgramme(LocalDateTime startingTime, String cinemaId) {
        Optional<Cinema> cinemaCandidate = cinemasRepository.findById(cinemaId);
        if (cinemaCandidate.isEmpty()) {
            return false;
        }
        Cinema cinema = cinemaCandidate.get();
        Optional<Programme> programmeCandidate = programmesRepository.findProgrammeOfCinemaInGivenPeriod(cinema, startingTime.toLocalDate());

        return programmeCandidate.isPresent();
    }
}
