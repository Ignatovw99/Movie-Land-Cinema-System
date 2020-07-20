package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Movie;
import movieland.domain.entities.Programme;
import movieland.domain.entities.Projection;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.domain.models.view.movie.MovieViewModel;
import movieland.domain.models.view.programme.CinemaProgrammeDateViewModel;
import movieland.domain.models.view.projection.ProjectionViewModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.errors.notfound.ProgrammeNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.ProgrammesRepository;
import movieland.services.interfaces.ProgrammesService;
import movieland.services.validation.ProgrammesValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProgrammesServiceTest extends TestBase {

    @MockBean
    private ProgrammesRepository programmesRepository;

    @MockBean
    private ProgrammesValidationService programmesValidationService;

    @MockBean
    private CinemasRepository cinemasRepository;

    @MockBean
    private Clock clock;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ProgrammesService programmesService;

    private Programme programme;

    private ProgrammeServiceModel programmeServiceModel;

    private static final String DEFAULT_ID = UUID.randomUUID().toString();

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2020, 6, 30);

    private static final LocalDate MOCK_TODAY = LocalDate.of(2020, 7, 2);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.of(2020, 7, 6);

    private static final Cinema DEFAULT_CINEMA = CinemasServiceTest.initializeEntity();

    private static final CinemaServiceModel DEFAULT_CINEMA_SERVICE_MODEL = CinemasServiceTest.initializeServiceModel();

    public static Programme initializeEntity() {
        Programme programme = new Programme();
        programme.setId(DEFAULT_ID);
        programme.setStartDate(DEFAULT_START_DATE);
        programme.setEndDate(DEFAULT_END_DATE);
        programme.setCinema(DEFAULT_CINEMA);
        return programme;
    }

    public static ProgrammeServiceModel initializeServiceModel() {
        ProgrammeServiceModel programmeServiceModel = new ProgrammeServiceModel();
        programmeServiceModel.setId(DEFAULT_ID);
        programmeServiceModel.setStartDate(DEFAULT_START_DATE);
        programmeServiceModel.setEndDate(DEFAULT_END_DATE);
        programmeServiceModel.setCinema(DEFAULT_CINEMA_SERVICE_MODEL);
        return programmeServiceModel;
    }

    @Override
    protected void before() {
        //tell your tests to return the specified MOCK_TODAY when calling LocalDate.now(clock)
        Clock fixedClock = Clock.fixed(MOCK_TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        programme = ProgrammesServiceTest.initializeEntity();
        programmeServiceModel = ProgrammesServiceTest.initializeServiceModel();
    }

    @BeforeEach
    public void cacheEvict() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    private void setupCreateNextMethod() {
        when(programmesValidationService.isValid(any(ProgrammeServiceModel.class)))
                .thenReturn(true);
        when(cinemasRepository.existsById(anyString()))
                .thenReturn(true);
        Programme activeProgramme = new Programme();
        activeProgramme.setStartDate(DEFAULT_START_DATE.minusDays(10));
        activeProgramme.setEndDate(DEFAULT_START_DATE.minusDays(1));
        when(programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(anyString()))
                .thenReturn(Optional.of(activeProgramme));
        when(programmesRepository.save(any(Programme.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private void setupGetCurrantActiveCinemaProgrammeWithItsProjections() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_CINEMA));
        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), eq(MOCK_TODAY)))
                .thenReturn(Optional.of(programme));

        programme.setProjections(new LinkedHashSet<>());

        for (int i = 0; i < 3; i++) {
            Projection projection = ProjectionsServiceTest.initializeEntity();
            projection.setId(UUID.randomUUID().toString());
            programme.getProjections().add(projection);
        }
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateNextMethod();
        setupGetCurrantActiveCinemaProgrammeWithItsProjections();
    }

    @Test
    public void createNext_WhenServiceModelIsNotValid_ShouldThrowException() {
        when(programmesValidationService.isValid(any(ProgrammeServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidProgrammeException.class,
                () -> programmesService.createNext(programmeServiceModel)
        );

        verify(programmesValidationService).isValid(any(ProgrammeServiceModel.class));
    }

    @Test
    public void createNext_WhenACinemaWithTheGivenIdDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.existsById(anyString()))
                .thenReturn(false);

        assertThrows(
                CinemaNotFoundException.class,
                () -> programmesService.createNext(programmeServiceModel)
        );

        verify(cinemasRepository).existsById(anyString());
    }

    @Test
    public void createNext_WhenThereIsNotAnyProgrammesForAGivenCinema_StartDateShouldBeTomorrow() {
        when(programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(anyString()))
                .thenReturn(Optional.empty());

        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(1));

        ProgrammeServiceModel createdProgramme = programmesService.createNext(programmeServiceModel);

        assertEquals(1, Period.between(MOCK_TODAY, createdProgramme.getStartDate()).getDays());

        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(3));
        assertThrows(
                InvalidProgrammeException.class,
                () -> programmesService.createNext(programmeServiceModel)
        );
    }

    @Test
    public void createNext_WhenThereIsNotActiveProgramme_StartDateShouldBeTomorrow() {
        LocalDate startDate = MOCK_TODAY.minusDays(21);
        LocalDate endDate = MOCK_TODAY.minusDays(1);
        Programme programme = new Programme();
        programme.setStartDate(startDate);
        programme.setEndDate(endDate);

        when(programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(anyString()))
                .thenReturn(Optional.of(programme));

        programmeServiceModel.setStartDate(programme.getEndDate().plusDays(1));

        ProgrammeServiceModel createdProgramme = programmesService.createNext(programmeServiceModel);

        assertEquals(1, Period.between(programme.getEndDate(), createdProgramme.getStartDate()).getDays());
        assertEquals(programmeServiceModel.getStartDate(), createdProgramme.getStartDate());

        programmeServiceModel.setStartDate(programme.getEndDate().plusDays(3));
        assertThrows(
                InvalidProgrammeException.class,
                () -> programmesService.createNext(programmeServiceModel)
        );
    }

    @Test
    public void createNext_WhenThereIsCurrentActiveProgramme_StartDateShouldBeAfterTheDayOfTheEndDate() {
        LocalDate startDateOfActiveProgramme = LocalDate.of(2020, 7, 1);
        LocalDate endDateOfActiveProgramme = LocalDate.of(2020, 7, 10);
        Programme activeProgramme = new Programme();
        activeProgramme.setStartDate(startDateOfActiveProgramme);
        activeProgramme.setEndDate(endDateOfActiveProgramme);

        when(programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(anyString()))
                .thenReturn(Optional.of(activeProgramme));

        programmeServiceModel.setStartDate(endDateOfActiveProgramme.plusDays(1));

        ProgrammeServiceModel createdProgramme = programmesService.createNext(programmeServiceModel);

        assertEquals(1, Period.between(activeProgramme.getEndDate(), createdProgramme.getStartDate()).getDays());
        assertEquals(programmeServiceModel.getStartDate(), createdProgramme.getStartDate());

        programmeServiceModel.setStartDate(activeProgramme.getEndDate().plusDays(3));
        assertThrows(
                InvalidProgrammeException.class,
                () -> programmesService.createNext(programmeServiceModel)
        );
    }

    @Test
    public void getProgrammeByCinemaIdAndDate_WhenCinemaDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> programmesService.getProgrammeByCinemaIdAndDate(anyString(), MOCK_TODAY)
        );

        verify(cinemasRepository).findById(anyString());
    }

    @Test
    public void getProgrammeByCinemaIdAndDate_WhenThereIsNotAnyProgrammeInTheGivenPeriod_ShouldThrowException() {
        Cinema cinema = new Cinema();
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));

        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), eq(MOCK_TODAY)))
                .thenReturn(Optional.empty());

        assertThrows(
                ProgrammeNotFoundException.class,
                () -> programmesService.getProgrammeByCinemaIdAndDate(anyString(), MOCK_TODAY)
        );

        verify(programmesRepository).findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), eq(MOCK_TODAY));
    }

    @Test
    public void getProgrammeByCinemaIdAndDate_WhenThereIsProgrammeInTheGivenPeriod_ShouldBeReturned() {
        Cinema cinema = new Cinema();
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));

        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), eq(MOCK_TODAY)))
                .thenReturn(Optional.of(programme));

        ProgrammeServiceModel actualProgrammeByCinemaIdAndDate = programmesService.getProgrammeByCinemaIdAndDate(anyString(), MOCK_TODAY);

        assertNotNull(actualProgrammeByCinemaIdAndDate);
        assertEquals(programme.getId(), actualProgrammeByCinemaIdAndDate.getId());
    }

    @Test
    public void isProgrammeActive_WhenItIsAlreadyInThePast_ShouldReturnFalse() {
        programme.setEndDate(MOCK_TODAY.minusDays(1));
        assertFalse(programmesService.isProgrammeActive(programme));
    }

    @Test
    public void isProgrammeActive_WhenTodayIsAfterStartAndBeforeEndDate_ShouldReturnTrue() {
        assertTrue(programmesService.isProgrammeActive(programme));
    }

    @Test
    public void getFirstPossibleStartDateForCinema_WhenTheActiveProgrammeEndsAfterToday_ShouldReturnTheDayAfterEndDay() {
        when(programmesRepository.findFirstByCinemaIdOrderByEndDateDesc(anyString()))
                .thenReturn(Optional.of(programme));

        assertTrue(programme.getEndDate().isAfter(MOCK_TODAY));
        LocalDate firstPossibleStartDateForCinema = programmesService.getFirstPossibleStartDateForCinema(programme.getCinema().getId());
        assertEquals(1, Period.between(programme.getEndDate(), firstPossibleStartDateForCinema).getDays());
    }

    @Test
    public void getFirstPossibleStartDateForCinema_WhenTheActiveProgrammeEndsBeforeToday_ShouldReturnTheTomorrowDate() {
        programme.setEndDate(MOCK_TODAY.minusDays(1));
        when(programmesRepository.findFirstByCinemaIdOrderByEndDateDesc(anyString()))
                .thenReturn(Optional.of(programme));

        assertTrue(programme.getEndDate().isBefore(MOCK_TODAY));
        LocalDate firstPossibleStartDateForCinema = programmesService.getFirstPossibleStartDateForCinema(programme.getCinema().getId());
        assertEquals(1, Period.between(MOCK_TODAY, firstPossibleStartDateForCinema).getDays());
    }

    @Test
    public void getFirstPossibleStartDateForCinema_WhenTheActiveProgrammeEndsToday_ShouldReturnTheTomorrowDate() {
        programme.setEndDate(MOCK_TODAY);
        when(programmesRepository.findFirstByCinemaIdOrderByEndDateDesc(anyString()))
                .thenReturn(Optional.of(programme));

        assertTrue(programme.getEndDate().isEqual(MOCK_TODAY));
        LocalDate firstPossibleStartDateForCinema = programmesService.getFirstPossibleStartDateForCinema(programme.getCinema().getId());
        assertEquals(1, Period.between(MOCK_TODAY, firstPossibleStartDateForCinema).getDays());
    }

    @Test
    public void getCurrantActiveCinemaProgrammeWithItsProjections_WhenCinemaDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> programmesService.getCurrantActiveCinemaProgrammeWithItsProjections("")
        );

        verify(cinemasRepository).findById(any());
    }

    @Test
    public void getCurrantActiveCinemaProgrammeWithItsProjections_WhenThereIsNotAnyActiveProgramme_ShouldThrowException() {
        when(programmesRepository.findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(MOCK_TODAY.getClass())))
                .thenReturn(Optional.empty());

        assertThrows(
                ProgrammeNotFoundException.class,
                () -> programmesService.getCurrantActiveCinemaProgrammeWithItsProjections("id")
        );

        verify(programmesRepository).findProgrammeOfCinemaInGivenPeriod(any(Cinema.class), any(MOCK_TODAY.getClass()));
    }

    @Test
    public void getCurrantActiveCinemaProgrammeWithItsProjections_WhenGetMoviesByDate_ShouldReturnOnlyProjectionsOnTheGivenDate() {
        LocalTime startingTime = ProjectionsServiceTest.initializeEntity().getStartingTime().toLocalTime();
        LocalTime endingTime = ProjectionsServiceTest.initializeEntity().getEndingTime().toLocalTime();

        LocalDate firstProjectionStartingTime = MOCK_TODAY.plusDays(1);
        LocalDate secondProjectionStartingTime = MOCK_TODAY.minusDays(1);
        LocalDate thirdProjectionStartingTime = MOCK_TODAY;

        LocalDate[] projectionsDates = { firstProjectionStartingTime, secondProjectionStartingTime, thirdProjectionStartingTime };

        int i = 0;

        for (Projection projection : programme.getProjections()) {
            projection.setStartingTime(LocalDateTime.of(projectionsDates[i], startingTime));
            projection.setEndingTime(LocalDateTime.of(projectionsDates[i++], endingTime));
        }

        Map<LocalDate, CinemaProgrammeDateViewModel> moviesWithTheirProjectionsByDate = programmesService.getCurrantActiveCinemaProgrammeWithItsProjections(DEFAULT_CINEMA.getId());
        CinemaProgrammeDateViewModel cinemaProgrammeDateViewModel = moviesWithTheirProjectionsByDate.get(firstProjectionStartingTime);

        assertEquals(DEFAULT_CINEMA.getName(), cinemaProgrammeDateViewModel.getCinemaName());
        assertEquals(firstProjectionStartingTime, cinemaProgrammeDateViewModel.getDate());

        cinemaProgrammeDateViewModel = moviesWithTheirProjectionsByDate.get(secondProjectionStartingTime);

        assertEquals(DEFAULT_CINEMA.getName(), cinemaProgrammeDateViewModel.getCinemaName());
        assertEquals(secondProjectionStartingTime, cinemaProgrammeDateViewModel.getDate());
    }

    @Test
    public void getCurrantActiveCinemaProgrammeWithItsProjections_WhenGetMoviesByDate_ShouldSortProjectionByStartingTimeAscending() {
        LocalTime time = LocalTime.of(13, 45);
        for (Projection projection : programme.getProjections()) {
            projection.setStartingTime(LocalDateTime.of(MOCK_TODAY, time));
            time = time.minusHours(1);
        }

        Map<LocalDate, CinemaProgrammeDateViewModel> moviesWithTheirProjectionsByDate = programmesService.getCurrantActiveCinemaProgrammeWithItsProjections(DEFAULT_CINEMA.getId());
        CinemaProgrammeDateViewModel cinemaProgrammeDateViewModel = moviesWithTheirProjectionsByDate.get(MOCK_TODAY);

        for (Map.Entry<MovieViewModel, Set<ProjectionViewModel>> movieProjection : cinemaProgrammeDateViewModel.getMovieProjections().entrySet()) {

            LocalDateTime expected = null;
            for (ProjectionViewModel projection : movieProjection.getValue()) {
                if (expected == null) {
                    expected = projection.getStartingTime();
                } else {
                    assertTrue(expected.isBefore(projection.getStartingTime()));
                }
            }
        }
    }

    @Test
    public void getCurrantActiveCinemaProgrammeWithItsProjections_MoviesShouldBeSortedByMovieTitleAscending() {
        String[] unsortedTitles = { "F&F 9", "Best", "Alice" };

        String[] sortedTitles = { "Alice", "Best", "F&F 9" };

        Set<Projection> projections = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Projection projection = ProjectionsServiceTest.initializeEntity();
            projection.setId(UUID.randomUUID().toString());
            Movie movie = new Movie();
            movie.setId(UUID.randomUUID().toString());
            movie.setTitle(unsortedTitles[i]);
            projection.setMovie(movie);
            projection.setStartingTime(LocalDateTime.of(MOCK_TODAY, LocalTime.of(0, 0)));
            projection.setEndingTime(LocalDateTime.of(MOCK_TODAY, LocalTime.of(3, 0)));
            projections.add(projection);
        }
        programme.setProjections(projections);

        Map<LocalDate, CinemaProgrammeDateViewModel> moviesWithTheirProjectionsByDate = programmesService.getCurrantActiveCinemaProgrammeWithItsProjections(DEFAULT_CINEMA.getId());
        CinemaProgrammeDateViewModel cinemaProgrammeDateViewModel = moviesWithTheirProjectionsByDate.get(MOCK_TODAY);

        int index = 0;
        for (Map.Entry<MovieViewModel, Set<ProjectionViewModel>> movieProjection : cinemaProgrammeDateViewModel.getMovieProjections().entrySet()) {
            String movieTitle = movieProjection.getKey().getTitle();
            assertEquals(sortedTitles[index++], movieTitle);
        }
    }
}
