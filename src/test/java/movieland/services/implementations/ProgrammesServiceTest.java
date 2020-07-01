package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Programme;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.ProgrammesRepository;
import movieland.services.interfaces.ProgrammesService;
import movieland.services.validation.ProgrammesValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    protected void setupMockBeansActions() {
        setupCreateNextMethod();
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

        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(1));

        ProgrammeServiceModel createdProgramme = programmesService.createNext(programmeServiceModel);

        assertEquals(1, Period.between(MOCK_TODAY, createdProgramme.getStartDate()).getDays());
        assertEquals(programmeServiceModel.getStartDate(), createdProgramme.getStartDate());

        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(3));
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
}
