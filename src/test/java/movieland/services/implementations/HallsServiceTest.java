package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Hall;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.HallServiceModel;
import movieland.errors.duplicate.HallAlreadyExistsException;
import movieland.errors.invalid.InvalidHallException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.HallsRepository;
import movieland.services.interfaces.HallsService;
import movieland.services.validation.HallsValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HallsServiceTest extends TestBase {

    @MockBean
    private HallsRepository hallsRepository;

    @MockBean
    private HallsValidationService hallsValidationService;

    @MockBean
    private CinemasRepository cinemasRepository;

    @Autowired
    private HallsService hallsService;

    private Hall hall;

    private HallServiceModel hallServiceModel;

    private static String DEFAULT_ID = UUID.randomUUID().toString();

    private static String DEFAULT_NAME = "VIP-Hall";

    private static Integer DEFAULT_ROWS = 14;

    private static Integer DEFAULT_COLUMNS = 10;

    private static String DEFAULT_FILM_TECHNOLOGY = "IMAX";

    private static String DEFAULT_SOUND_SYSTEM = "JBL";

    private static Cinema DEFAULT_CINEMA = CinemasServiceTest.initializeEntity();

    public static Hall initializeEntity() {
        Hall hall = new Hall();
        hall.setId(DEFAULT_ID);
        hall.setName(DEFAULT_NAME);
        hall.setRows(DEFAULT_ROWS);
        hall.setColumns(DEFAULT_COLUMNS);
        hall.setFilmTechnology(DEFAULT_FILM_TECHNOLOGY);
        hall.setSoundSystem(DEFAULT_SOUND_SYSTEM);
        hall.setCinema(DEFAULT_CINEMA);
        return hall;
    }

    public static HallServiceModel initializeServiceModel() {
        HallServiceModel hallServiceModel = new HallServiceModel();
        hallServiceModel.setId(DEFAULT_ID);
        hallServiceModel.setName(DEFAULT_NAME);
        hallServiceModel.setRows(DEFAULT_ROWS);
        hallServiceModel.setColumns(DEFAULT_COLUMNS);
        hallServiceModel.setFilmTechnology(DEFAULT_FILM_TECHNOLOGY);
        hallServiceModel.setSoundSystem(DEFAULT_SOUND_SYSTEM);
        hallServiceModel.setCinema(new CinemaServiceModel());
        return hallServiceModel;
    }

    @Override
    protected void before() {
        hall = HallsServiceTest.initializeEntity();
        hallServiceModel = HallsServiceTest.initializeServiceModel();
    }

    private void setupCreateMethod() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(true);
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_CINEMA));
        when(cinemasRepository.existsByIdAndHallsName(anyString(), anyString()))
                .thenReturn(false);
        when(hallsRepository.save(any(Hall.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        hallServiceModel.getCinema().setId(UUID.randomUUID().toString());
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
    }

    @Test
    public void create_WhenHallIsInvalid_ShouldThrowException() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidHallException.class,
                () -> hallsService.create(hallServiceModel)
        );

        verify(hallsValidationService).isValid(any(HallServiceModel.class));
    }

    @Test
    public void create_WhenHallIsValid_ShouldNotThrowAnyException() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> hallsService.create(hallServiceModel));

        verify(hallsValidationService).isValid(any(HallServiceModel.class));
    }

    @Test
    public void create_WhenAssignedCinemaDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> hallsService.create(hallServiceModel)
        );

        verify(cinemasRepository).findById(anyString());
    }

    @Test
    public void create_WhenHallAlreadyExistsInTheGivenCinema_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_CINEMA));
        when(cinemasRepository.existsByIdAndHallsName(anyString(), anyString()))
                .thenReturn(true);

        assertThrows(
                HallAlreadyExistsException.class,
                () -> hallsService.create(hallServiceModel)
        );

        verify(cinemasRepository).existsByIdAndHallsName(anyString(), anyString());
    }

    @Test
    public void create_WhenHallAlreadyExistsInTheDatabaseButInAnotherCinema_ShouldNotThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(DEFAULT_CINEMA));
        when(cinemasRepository.existsByIdAndHallsName(anyString(), anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> hallsService.create(hallServiceModel));

        verify(cinemasRepository).existsByIdAndHallsName(anyString(), anyString());
    }

    @Test
    public void create_WhenHallIsCorrect_ShouldBePersistedInTheDatabase() {
        HallServiceModel createdHall = hallsService.create(this.hallServiceModel);

        verify(hallsRepository).save(any(Hall.class));

        assertEquals(hall.getId(), createdHall.getId());
        assertEquals(hall.getName(), createdHall.getName());
        assertNotNull(createdHall.getCinema());
    }
}
