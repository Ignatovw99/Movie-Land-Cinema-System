package movieland.services.implementations;

import movieland.TestBase;
import movieland.constants.entities.ProjectionConstants;
import movieland.domain.entities.*;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.invalid.InvalidProjectionException;
import movieland.errors.notfound.HallNotFoundException;
import movieland.errors.notfound.MovieNotFoundException;
import movieland.repositories.*;
import movieland.services.interfaces.ProjectionsService;
import movieland.services.validation.ProjectionsValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectionsServiceTest extends TestBase {

    @MockBean
    private ProjectionsRepository projectionsRepository;

    @MockBean
    private ProjectionsValidationService projectionsValidationService;

    @MockBean
    private MoviesRepository moviesRepository;

    @MockBean
    private HallsRepository hallsRepository;

    @MockBean
    private ProgrammesRepository programmesRepository;

    @MockBean
    private SeatsRepository seatsRepository;

    @MockBean
    private Clock clock;

    @Autowired
    private ProjectionsService projectionsService;

    private static String DEFAULT_ID = UUID.randomUUID().toString();

    private static Movie DEFAULT_MOVIE = MoviesServiceTest.initializeEntity();

    private static Hall DEFAULT_HALL = HallsServiceTest.initializeEntity();

    private static Programme DEFAULT_PROGRAMME = ProgrammesServiceTest.initializeEntity();

    private static LocalDateTime DEFAULT_STARTING_TIME = LocalDateTime.of(2020, 7, 21, 15, 45);

    private static LocalDate MOCK_TODAY = LocalDate.of(2020, 7, 19);

    private Projection projection;

    private ProjectionServiceModel projectionServiceModel;

    public static Projection initializeEntity() {
        Projection projection = new Projection();
        projection.setId(DEFAULT_ID);
        projection.setMovie(DEFAULT_MOVIE);
        projection.setHall(DEFAULT_HALL);
        projection.setProgramme(DEFAULT_PROGRAMME);
        projection.setStartingTime(DEFAULT_STARTING_TIME);
        projection.setEndingTime(DEFAULT_STARTING_TIME.plusMinutes(DEFAULT_MOVIE.getRunningTime()));
        DEFAULT_PROGRAMME.setProjections(Set.of(projection));
        return projection;
    }

    public static ProjectionServiceModel initializeServiceModel() {
        ProjectionServiceModel projectionServiceModel = new ProjectionServiceModel();
        projectionServiceModel.setId(DEFAULT_ID);
        projectionServiceModel.setMovie(MoviesServiceTest.initializeServiceModel());
        projectionServiceModel.setHall(HallsServiceTest.initializeServiceModel());
        projectionServiceModel.setProgramme(ProgrammesServiceTest.initializeServiceModel());
        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME);
        return projectionServiceModel;
    }

    @Override
    protected void before() {
        Clock fixedClock = Clock.fixed(MOCK_TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        projection = ProjectionsServiceTest.initializeEntity();
        projectionServiceModel = ProjectionsServiceTest.initializeServiceModel();
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
    }

    private void setupCreateMethod() {
        when(projectionsValidationService.isValid(any(ProjectionServiceModel.class)))
                .thenReturn(true);
        when(moviesRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_MOVIE));
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_HALL));
        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(LocalDate.class)))
                .thenReturn(Optional.of(DEFAULT_PROGRAMME));

        projection.getHall().setCinema(CinemasServiceTest.initializeEntity());

        Integer movieRunningTime = DEFAULT_MOVIE.getRunningTime();
        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME.plusMinutes(movieRunningTime + 1));

        when(projectionsRepository.save(any(Projection.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    @Test
    public void create_WhenProjectionModelIsInvalid_ShouldThrowException() {
        when(projectionsValidationService.isValid(any(ProjectionServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        verify(projectionsValidationService).isValid(any(ProjectionServiceModel.class));
    }

    @Test
    public void create_WhenProjectionModelIsValid_ShouldNotThrowException() {
        when(projectionsValidationService.isValid(any(ProjectionServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));

        verify(projectionsValidationService).isValid(any(ProjectionServiceModel.class));
    }

    @Test
    public void create_WhenMovieDoesNotExist_ShouldThrowException() {
        when(moviesRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        verify(moviesRepository).findById(anyString());
    }

    @Test
    public void create_WhenMovieExists_ShouldNotThrowException() {
        when(moviesRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_MOVIE));

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));

        verify(moviesRepository).findById(anyString());
    }

    @Test
    public void create_WhenHallDoesNotExist_ShouldThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                HallNotFoundException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void create_WhenHallExists_ShouldNotThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_HALL));

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void create_WhenTheCinemaDoesNotHaveAnActiveProgramme_ShouldThrowException() {
        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidProgrammeException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        verify(programmesRepository).findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(LocalDate.class));
    }

    @Test
    public void create_WhenTheCinemaHasAnActiveProgramme_ShouldNotThrowException() {
        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(LocalDate.class)))
                .thenReturn(Optional.of(DEFAULT_PROGRAMME));

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));

        verify(programmesRepository).findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(LocalDate.class));
    }

    @Test
    public void create_WhenProjectionStartsBeforeTheOpeningHourOfCinema_ShouldThrowException() {
        DEFAULT_HALL.getCinema().setOpeningTime(projectionServiceModel.getStartingTime().plusHours(1).toLocalTime());

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );
    }

    @Test
    public void create_WhenProjectionStartsAfterTheClosingHourOfCinema_ShouldThrowException() {
        DEFAULT_HALL.getCinema().setClosingTime(DEFAULT_STARTING_TIME.minusHours(1).toLocalTime());

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );
    }

    @Test
    public void create_WhenProjectionStartsInRangeOfWorkingHoursOfCinema_ShouldNotThrowException() {
        Integer runningTime = projection.getMovie().getRunningTime();
        projection.getHall().getCinema().setClosingTime(projectionServiceModel.getStartingTime().plusMinutes(runningTime + 1).toLocalTime());

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));

        projection.getHall().getCinema().setOpeningTime(DEFAULT_STARTING_TIME.toLocalTime());
        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));
    }

    @Test
    public void create_WhenHallIsNotFreeInTheGivenPeriodOfTime_ShouldThrowException() {
        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME.minusHours(1).minusMinutes(15));

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME.plusMinutes(35));

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );
    }

    @Test
    public void create_WhenHallIsFreeInTheGivenPeriodOfTime_ShouldNotThrowException() {
        Integer movieRunningTime = DEFAULT_MOVIE.getRunningTime();
        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME.plusMinutes(movieRunningTime + 1));

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));
    }

    @Test
    public void create_WhenAlreadyThereIsSuchMovieProjectionInTheCinemaAtTheGivenTime_ShouldThrowException() {
        Hall hall = HallsServiceTest.initializeEntity();
        hall.setId("NEW ID");
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));
        when(seatsRepository.existsByProjectionAndIsFree(any(Projection.class), anyBoolean()))
                .thenReturn(true);

        projectionServiceModel.setStartingTime(projection.getStartingTime());

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        projectionServiceModel.setStartingTime(projection.getStartingTime().plusMinutes(25));

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );

        projectionServiceModel.setStartingTime(projection.getStartingTime().minusMinutes(29));

        assertThrows(
                InvalidProjectionException.class,
                () -> projectionsService.create(projectionServiceModel)
        );
    }

    @Test
    public void create_WhenThereIsNotSuchMovieProjectionInTheCinemaAtTheGivenTime_ShouldNotThrowException() {
        Hall hall = HallsServiceTest.initializeEntity();
        hall.setId("NEW ID");
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));

        projectionServiceModel.setStartingTime(projection.getStartingTime().plusMinutes(ProjectionConstants.MAX_MINUTES_DIFFERENCE + 5));

        assertDoesNotThrow(() -> projectionsService.create(projectionServiceModel));
    }

    @Test
    public void create_WhenThereIsAlreadySuchMovieProjectionInTheCinemaAtTheGivenTimeAndTheProjectionIsBookedOut_ShouldCreateAnotherOne() {
        Hall hall = HallsServiceTest.initializeEntity();
        hall.setId("NEW ID");
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));

        when(seatsRepository.existsByProjectionAndIsFree(any(Projection.class), anyBoolean()))
                .thenReturn(false);

        projectionServiceModel.setStartingTime(DEFAULT_STARTING_TIME);

        assertDoesNotThrow(() -> {
            ProjectionServiceModel createdProjection = projectionsService.create(this.projectionServiceModel);

            assertEquals(projection.getMovie().getTitle(), createdProjection.getMovie().getTitle());
        });
    }

//TODO Tests
}
