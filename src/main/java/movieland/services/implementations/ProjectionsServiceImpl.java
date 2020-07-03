package movieland.services.implementations;

import movieland.constants.entities.HallConstants;
import movieland.constants.entities.MovieConstants;
import movieland.constants.entities.ProgrammeConstants;
import movieland.domain.entities.*;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.invalid.InvalidProjectionException;
import movieland.errors.notfound.HallNotFoundException;
import movieland.errors.notfound.MovieNotFoundException;
import movieland.repositories.*;
import movieland.services.interfaces.ProjectionsService;
import movieland.services.validation.ProjectionsValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

import static movieland.constants.entities.ProjectionConstants.*;

@Service
public class ProjectionsServiceImpl implements ProjectionsService {

    private final ProjectionsRepository projectionsRepository;

    private final ProjectionsValidationService projectionsValidationService;

    private final MoviesRepository moviesRepository;

    private final HallsRepository hallsRepository;

    private final ProgrammesRepository programmesRepository;

//    private final SeatsRepository seatsRepository;

    private final ModelMapper modelMapper;

    private final Clock clock;

    @Autowired
    public ProjectionsServiceImpl(ProjectionsRepository projectionsRepository, ProjectionsValidationService projectionsValidationService, MoviesRepository moviesRepository, HallsRepository hallsRepository, ProgrammesRepository programmesRepository, ModelMapper modelMapper, Clock clock) {
        this.projectionsRepository = projectionsRepository;
        this.projectionsValidationService = projectionsValidationService;
        this.moviesRepository = moviesRepository;
        this.hallsRepository = hallsRepository;
        this.programmesRepository = programmesRepository;
        this.modelMapper = modelMapper;
        this.clock = clock;
    }

    @Override
    public ProjectionServiceModel create(ProjectionServiceModel projectionServiceModel) {
        if (!projectionsValidationService.isValid(projectionServiceModel)) {
            throw new InvalidProjectionException(PROJECTION_MODEL_NOT_VALID);
        }

        Movie movie = moviesRepository.findById(projectionServiceModel.getMovie().getId())
                .orElseThrow(() -> new MovieNotFoundException(MovieConstants.MOVIE_NOT_FOUND));

        Hall hall = hallsRepository.findById(projectionServiceModel.getHall().getId())
                .orElseThrow(() -> new HallNotFoundException(HallConstants.HALL_NOT_FOUND));

        Cinema projectionCinema = hall.getCinema();

        if (!isStaringTimeInRangeOfWorkingHours(projectionCinema, projectionServiceModel.getStartingTime())) {
            throw new InvalidProjectionException(PROJECTION_SHOULD_START_IN_CINEMA_WORKING_HOURS_RANGE);
        }

        Programme cinemaCurrentActiveProgramme = programmesRepository.findCurrentActiveProgrammeOfCinema(projectionCinema, LocalDate.now(clock))
                .orElseThrow(() -> new InvalidProgrammeException(ProgrammeConstants.CINEMA_DOES_NOT_HAVE_ACTIVE_PROGRAMMES));

        LocalDateTime projectionStartingTime = projectionServiceModel.getStartingTime();
        LocalDateTime projectionEndingTime = projectionStartingTime.plusMinutes(movie.getRunningTime());

        if (!isHallFree(cinemaCurrentActiveProgramme, hall, projectionStartingTime, projectionEndingTime)) {
            throw new InvalidProjectionException(HALL_IS_NOT_FREE_IN_THE_GIVEN_PERIOD);
        }

        boolean[] areAllMovieProjectionsBookedOut = { false };

        if (isMovieAlreadyProjectedInCinemaAtTheGivenTime(movie, cinemaCurrentActiveProgramme, projectionServiceModel.getStartingTime(), areAllMovieProjectionsBookedOut)){
            throw new InvalidProjectionException(SUCH_MOVIE_PROJECTION_ALREADY_EXISTS);
        }
        // TODO: -------------> It is also possible the same projections(with same movie) to start at the same time but in diff halls if only the first projection is sold out and there is free hall;

        //TODO: when generating seats think of the is emergency state??
        return null;
    }

    @Override
    public ProjectionServiceModel update(String id, ProjectionServiceModel serviceModel) {
        return null;
    }

    @Override
    public ProjectionServiceModel delete(String id) {
        return null;
    }

    @Override
    public ProjectionServiceModel findById(String id) {
        return null;
    }

    @Override
    public List<ProjectionServiceModel> findAll() {
        return null;
    }

    private boolean isStaringTimeInRangeOfWorkingHours(Cinema cinema, LocalDateTime projectionDateAndStartingTime) {
        LocalTime projectionStartingTime = projectionDateAndStartingTime.toLocalTime();
        LocalTime cinemaOpeningTime = cinema.getOpeningTime();
        LocalTime cinemaClosingTime = cinema.getClosingTime();

        return (cinemaOpeningTime.isBefore(projectionStartingTime) || cinemaOpeningTime.equals(projectionStartingTime))
                && (cinemaClosingTime.isAfter(projectionStartingTime) || cinemaClosingTime.equals(projectionStartingTime));
    }

    private boolean isTimeOverlapping(LocalDateTime hallProjectionStartingTime, LocalDateTime hallProjectionEndingTime, LocalDateTime projectionStartingTime, LocalDateTime projectionEndingTime) {
        return !hallProjectionStartingTime.isAfter(projectionEndingTime) && !projectionStartingTime.isAfter(hallProjectionEndingTime);
    }

    private boolean isHallFree(Programme programme, Hall hall, LocalDateTime projectionStartingTime, LocalDateTime projectionEndingTime) {
        for (Projection projection : programme.getProjections()) {
            if (!projection.getHall().equals(hall)) {
                continue;
            }
            if (isTimeOverlapping(projection.getStartingTime(), projection.getEndingTime(), projectionStartingTime, projectionEndingTime)) {
                return false;
            }
        }
        return true;
    }

    private boolean isMovieAlreadyProjectedInCinemaAtTheGivenTime(Movie movie, Programme programme, LocalDateTime projectionStartingTime, boolean[] areAllMovieProjectionsBookedOut) {
        boolean isAlreadyProjected = false;
        for (Projection projection : programme.getProjections()) {
            if (projection.getMovie().equals(movie) && areDatesEqual(projection.getStartingTime(), projectionStartingTime) && isMinutesDeviationAdmissible(projection.getStartingTime(), projectionStartingTime)) {
                // TODO check if all seats are sold out
                areAllMovieProjectionsBookedOut[0] = true;
                isAlreadyProjected = true;
            }
        }
        return isAlreadyProjected;
    }

    private boolean isMinutesDeviationAdmissible(LocalDateTime projectionStartingDate, LocalDateTime newProjectionStartingDate) {
        return Math.abs(Duration.between(projectionStartingDate.toLocalTime(), newProjectionStartingDate.toLocalTime()).toMinutes()) <= MAX_MINUTES_DIFFERENCE;
    }

    private boolean areDatesEqual(LocalDateTime projectionStartingDate, LocalDateTime newProjectionStartingDate) {
        return projectionStartingDate.toLocalDate().isEqual(newProjectionStartingDate.toLocalDate());
    }
}
