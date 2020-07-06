package movieland.services.implementations;

import movieland.constants.entities.CinemaConstants;
import movieland.constants.entities.HallConstants;
import movieland.constants.entities.MovieConstants;
import movieland.constants.entities.ProgrammeConstants;
import movieland.domain.entities.*;
import movieland.domain.models.service.HallServiceModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.invalid.InvalidProjectionException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.errors.notfound.HallNotFoundException;
import movieland.errors.notfound.MovieNotFoundException;
import movieland.errors.notfound.ProjectionNotFoundException;
import movieland.repositories.*;
import movieland.services.interfaces.ProjectionsService;
import movieland.services.interfaces.SeatsService;
import movieland.services.validation.ProjectionsValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static movieland.constants.entities.ProjectionConstants.*;

@Service
public class ProjectionsServiceImpl implements ProjectionsService {

    private final ProjectionsRepository projectionsRepository;

    private final ProjectionsValidationService projectionsValidationService;

    private final MoviesRepository moviesRepository;

    private final HallsRepository hallsRepository;

    private final ProgrammesRepository programmesRepository;

    private final SeatsRepository seatsRepository;

    private final SeatsService seatsService;

    private final CinemasRepository cinemasRepository;

    private final ModelMapper modelMapper;

    private final Clock clock;

    @Autowired
    public ProjectionsServiceImpl(ProjectionsRepository projectionsRepository, ProjectionsValidationService projectionsValidationService, MoviesRepository moviesRepository, HallsRepository hallsRepository, ProgrammesRepository programmesRepository, SeatsRepository seatsRepository, SeatsService seatsService, CinemasRepository cinemasRepository, ModelMapper modelMapper, Clock clock) {
        this.projectionsRepository = projectionsRepository;
        this.projectionsValidationService = projectionsValidationService;
        this.moviesRepository = moviesRepository;
        this.hallsRepository = hallsRepository;
        this.programmesRepository = programmesRepository;
        this.seatsRepository = seatsRepository;
        this.seatsService = seatsService;
        this.cinemasRepository = cinemasRepository;
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

        Programme cinemaCurrentActiveProgramme = programmesRepository.findCurrentActiveProgrammeOfCinema(projectionCinema, projectionServiceModel.getStartingTime().toLocalDate())
                .orElseThrow(() -> new InvalidProgrammeException(ProgrammeConstants.CINEMA_DOES_NOT_HAVE_ACTIVE_PROGRAMMES));

        LocalDateTime projectionStartingTime = projectionServiceModel.getStartingTime();
        LocalDateTime projectionEndingTime = projectionStartingTime.plusMinutes(movie.getRunningTime());

        if (!isHallFree(cinemaCurrentActiveProgramme, hall, projectionStartingTime, projectionEndingTime)) {
            throw new InvalidProjectionException(HALL_IS_NOT_FREE_IN_THE_GIVEN_PERIOD);
        }

        boolean[] areAllMovieProjectionsBookedOut = { true };
        boolean isMovieAlreadyProjected = isMovieAlreadyProjectedInCinemaAtTheGivenTime(movie, cinemaCurrentActiveProgramme, projectionServiceModel.getStartingTime(), areAllMovieProjectionsBookedOut, null);

        if (isMovieAlreadyProjected && !areAllMovieProjectionsBookedOut[0]){
            throw new InvalidProjectionException(SUCH_MOVIE_PROJECTION_ALREADY_EXISTS);
        }

        Projection projection = modelMapper.map(projectionServiceModel, Projection.class);
        projection.setEndingTime(projectionEndingTime);
        projection.setProgramme(cinemaCurrentActiveProgramme);
        projection = projectionsRepository.save(projection);

        ProjectionServiceModel createdProjection = modelMapper.map(projection, ProjectionServiceModel.class);

        // Asynchronous seats generation
        seatsService.generateProjectionSeats(createdProjection);

        return createdProjection;
    }

    @Override
    public ProjectionServiceModel update(String id, ProjectionServiceModel projectionServiceModel) {
        if (!projectionsValidationService.isValid(projectionServiceModel)) {
            throw new InvalidProjectionException(PROJECTION_MODEL_NOT_VALID);
        }

        Projection projectionToUpdate = projectionsRepository.findById(id)
                .orElseThrow(() -> new ProjectionNotFoundException(PROJECTION_NOT_FOUND));

        Hall projectionHall = projectionToUpdate.getHall();
        Movie projectionMovie = projectionToUpdate.getMovie();

        if (isHallChanged(projectionHall, projectionServiceModel.getHall()) || isMovieChanged(projectionMovie, projectionServiceModel.getMovie())) {
            throw new InvalidProjectionException(MOVIE_AND_HALL_ARE_NOT_UPDATABLE);
        }

        if (!isStaringTimeInRangeOfWorkingHours(projectionHall.getCinema(), projectionServiceModel.getStartingTime())) {
            throw new InvalidProjectionException(PROJECTION_SHOULD_START_IN_CINEMA_WORKING_HOURS_RANGE);
        }

        LocalDateTime projectionNewStartingTime = projectionServiceModel.getStartingTime();
        LocalDateTime projectionNewEndingTime = projectionNewStartingTime.plusMinutes(projectionMovie.getRunningTime());

        if (!isHallFree(projectionToUpdate.getProgramme(), projectionHall, projectionNewStartingTime, projectionNewEndingTime)) {
            throw new InvalidProjectionException(HALL_IS_NOT_FREE_IN_THE_GIVEN_PERIOD);
        }
        boolean[] areAllMovieProjectionsBookedOut = { true };
        boolean isMovieAlreadyProjected = isMovieAlreadyProjectedInCinemaAtTheGivenTime(projectionMovie, projectionToUpdate.getProgramme(), projectionServiceModel.getStartingTime(), areAllMovieProjectionsBookedOut, projectionToUpdate.getId());

        if (isMovieAlreadyProjected && !areAllMovieProjectionsBookedOut[0]){
            throw new InvalidProjectionException(SUCH_MOVIE_PROJECTION_ALREADY_EXISTS);
        }

        projectionToUpdate.setStartingTime(projectionNewStartingTime);
        projectionToUpdate.setEndingTime(projectionNewEndingTime);

        projectionsRepository.save(projectionToUpdate);

        return modelMapper.map(projectionToUpdate, ProjectionServiceModel.class);
    }

    @Override
    public ProjectionServiceModel delete(String id) {
        Projection projection = projectionsRepository.findById(id)
                .orElseThrow(() -> new ProjectionNotFoundException(PROJECTION_NOT_FOUND));

        projectionsRepository.delete(projection);

        return modelMapper.map(projection, ProjectionServiceModel.class);
    }

    @Override
    public ProjectionServiceModel findById(String id) {
        Projection projection = projectionsRepository.findById(id)
                .orElseThrow(() -> new ProjectionNotFoundException(PROJECTION_NOT_FOUND));

        return modelMapper.map(projection, ProjectionServiceModel.class);
    }

    @Override
    public List<ProjectionServiceModel> findAll() {
        return projectionsRepository.findAll()
                .stream()
                .map(projection -> modelMapper.map(projection, ProjectionServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isStaringTimeInRangeOfWorkingHours(String cinemaId, LocalDateTime projectionDateAndStartingTime) {
        Cinema cinema = cinemasRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException(CinemaConstants.CINEMA_NOT_FOUND));

        return isStaringTimeInRangeOfWorkingHours(cinema, projectionDateAndStartingTime);
    }

    @Override
    public boolean isHallFree(String hallId, String movieId, LocalDateTime projectionStartingTime) {
        Hall hall = hallsRepository.findById(hallId)
                .orElseThrow(() -> new HallNotFoundException(HallConstants.HALL_NOT_FOUND));

        Programme programme = programmesRepository.findCurrentActiveProgrammeOfCinema(hall.getCinema(), projectionStartingTime.toLocalDate())
                .orElseThrow(() -> new InvalidProgrammeException(ProgrammeConstants.CINEMA_DOES_NOT_HAVE_PROGRAMMES));

        Movie movie = moviesRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(MovieConstants.MOVIE_NOT_FOUND));

        return isHallFree(programme, hall, projectionStartingTime, projectionStartingTime.plusMinutes(movie.getRunningTime()));
    }

    @Override
    public boolean isMovieAlreadyProjectedInCinemaAtTheGivenTime(String movieId, String cinemaId, LocalDateTime projectionStartingTime) {
        boolean[] areAllMovieProjectionsBookedOut = {true};

        Movie movie = moviesRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(MovieConstants.MOVIE_NOT_FOUND));

        Cinema cinema = cinemasRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException(CinemaConstants.CINEMA_NOT_FOUND));

        Programme programme = programmesRepository.findCurrentActiveProgrammeOfCinema(cinema, projectionStartingTime.toLocalDate())
                .orElseThrow(() -> new InvalidProgrammeException(ProgrammeConstants.CINEMA_DOES_NOT_HAVE_PROGRAMMES));

        boolean isMovieAlreadyProjected = isMovieAlreadyProjectedInCinemaAtTheGivenTime(movie, programme, projectionStartingTime, areAllMovieProjectionsBookedOut, null);

        return isMovieAlreadyProjected && !areAllMovieProjectionsBookedOut[0];
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

    private boolean isMovieAlreadyProjectedInCinemaAtTheGivenTime(Movie movie, Programme programme, LocalDateTime projectionStartingTime, boolean[] areAllMovieProjectionsBookedOut, String projectionIdToUpdate) {
        boolean isAlreadyProjected = false;
        for (Projection projection : programme.getProjections()) {
            if (projection.getMovie().equals(movie) && areDatesEqual(projection.getStartingTime(), projectionStartingTime) && isMinutesDeviationAdmissible(projection.getStartingTime(), projectionStartingTime)) {
                //Check if it is applied on update, because the projection to update should not be taken into consideration.
                if (projection.getId().equals(projectionIdToUpdate)) {
                    continue;
                }
                isAlreadyProjected = true;
                if (seatsRepository.existsByProjectionAndIsFree(projection, true)) {
                    areAllMovieProjectionsBookedOut[0] = false;
                    break;
                }
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

    private boolean isMovieChanged(Movie movie, MovieServiceModel movieServiceModel) {
        return !movie.getId().equals(movieServiceModel.getId());
    }

    private boolean isHallChanged(Hall hall, HallServiceModel hallServiceModel) {
        return !hall.getId().equals(hallServiceModel.getId());
    }
}
