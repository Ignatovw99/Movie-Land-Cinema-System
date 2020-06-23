package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Hall;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.HallServiceModel;
import movieland.errors.duplicate.HallAlreadyExistsException;
import movieland.errors.invalid.HallCinemaNotChangeableException;
import movieland.errors.invalid.InvalidHallException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.errors.notfound.HallNotFoundException;
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

    private static CinemaServiceModel DEFAULT_CINEMA_SERVICE_MODEL = CinemasServiceTest.initializeServiceModel();

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
        hallServiceModel.setCinema(DEFAULT_CINEMA_SERVICE_MODEL);
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
    }

    private void setupUpdateMethod() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(true);
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
        setupUpdateMethod();
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

    @Test
    public void update_WhenHallIsNotValid_ShouldThrowException() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidHallException.class,
                () -> hallsService.update(hallServiceModel.getId(), hallServiceModel)
        );

        verify(hallsValidationService).isValid(any(HallServiceModel.class));
    }

    @Test
    public void update_WhenHallIsValid_ShouldNotThrowException() {
        when(hallsValidationService.isValid(any(HallServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> hallsService.update(hallServiceModel.getId(), hallServiceModel));

        verify(hallsValidationService).isValid(any(HallServiceModel.class));
    }

    @Test
    public void update_WhenHallWithGivenIdDoesNotExist_ShouldThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                HallNotFoundException.class,
                () -> hallsService.update(hallServiceModel.getId(), hallServiceModel)
        );

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void update_WhenHallWithGivenIdExists_ShouldNotThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));

        assertDoesNotThrow(() -> hallsService.update(hallServiceModel.getId(), hallServiceModel));

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void update_WhenHallCinemaIsChanged_ShouldThrowException() {
        CinemaServiceModel newAssignedCinemaModel = new CinemaServiceModel();
        newAssignedCinemaModel.setId("New Cinema Id");
        hallServiceModel.setCinema(newAssignedCinemaModel);

        assertThrows(
                HallCinemaNotChangeableException.class,
                () -> hallsService.update(hallServiceModel.getId(), hallServiceModel)
        );
    }

    @Test
    public void update_WhenHallNameIsChangedAndAlreadyThereIsSuchHallNameInTheGivenCinema_ShouldThrowException() {
        hallServiceModel.setName("Changed Hall Name");
        when(cinemasRepository.existsByIdAndHallsName(anyString(), anyString()))
                .thenReturn(true);

        assertThrows(
                HallAlreadyExistsException.class,
                () -> hallsService.update(hallServiceModel.getId(), hallServiceModel)
        );

        verify(cinemasRepository).existsByIdAndHallsName(anyString(), anyString());
    }

    @Test
    public void update_WhenHallNameIsNotChanged_ShouldNotInvokeSearchInTheGivenCinema() {
        assertDoesNotThrow(() -> hallsService.update(hallServiceModel.getId(), hallServiceModel));
        verify(cinemasRepository, never()).existsByIdAndHallsName(anyString(), anyString());
    }

    @Test
    public void update_WhenHallIsUpdated_ShouldItBePersistedCorrectly() {
        hallServiceModel.setName("Hall name");
        hallServiceModel.setRows(12);
        hallServiceModel.setSoundSystem("Beats by Dre");

        assertEquals(DEFAULT_NAME, hall.getName());
        assertEquals(DEFAULT_ROWS, hall.getRows());
        assertEquals(DEFAULT_SOUND_SYSTEM, hall.getSoundSystem());

        HallServiceModel updatedHallServiceModel = hallsService.update(hallServiceModel.getId(), hallServiceModel);

        assertEquals(hall.getName(), updatedHallServiceModel.getName());
        assertEquals(hall.getRows(), updatedHallServiceModel.getRows());
        assertEquals(hall.getSoundSystem(), updatedHallServiceModel.getSoundSystem());
        assertEquals(hall.getCinema().getId(), updatedHallServiceModel.getCinema().getId());
        assertEquals(hall.getColumns(), updatedHallServiceModel.getColumns());
    }

    @Test
    public void delete_WhenHallWithTheGivenIdDoesNotExist_ShouldThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                HallNotFoundException.class,
                () -> hallsService.delete(hallServiceModel.getId())
        );

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void delete_WhenHallWithGivenIdExists_ShouldDeleteIt() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));

        HallServiceModel deletedHall = hallsService.delete(hallServiceModel.getId());

        verify(hallsRepository).delete(any(Hall.class));

        assertEquals(hall.getId(), deletedHall.getId());
        assertEquals(hall.getName(), deletedHall.getName());
    }

    @Test
    public void findById_WhenThereIsNotHallWithGivenId_ShouldThrowException() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                HallNotFoundException.class,
                () -> hallsService.findById(hallServiceModel.getId())
        );

        verify(hallsRepository).findById(anyString());
    }

    @Test
    public void findById_WhenThereIsHallWithGivenId_ShouldReturnTheCorrectHall() {
        when(hallsRepository.findById(anyString()))
                .thenReturn(Optional.of(hall));

        HallServiceModel actualHall = hallsService.findById(hallServiceModel.getId());

        verify(hallsRepository).findById(anyString());
        assertEquals(hall.getId(), actualHall.getId());
        assertEquals(hall.getName(), actualHall.getName());
    }
}
