package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.errors.duplicate.CinemaAlreadyExistsException;
import movieland.errors.invalid.InvalidCinemaException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.services.interfaces.CinemasService;
import movieland.services.validation.CinemasValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CinemasServiceTest extends TestBase {

    @MockBean
    private CinemasRepository cinemasRepository;

    @MockBean
    private CinemasValidationService cinemasValidationService;

    @Autowired
    private CinemasService cinemasService;

    private Cinema cinema;

    private CinemaServiceModel cinemaServiceModel;

    private static String DEFAULT_ID = UUID.randomUUID().toString();

    private static String DEFAULT_NAME = "Movie Land";

    private static String DEFAULT_ADDRESS = "4267 Spirit Drive";

    private static String DEFAULT_EMAIL = "movie_land@gmail.abc";

    private static String DEFAULT_PHONE_NUMBER = "+35442131244";

    private static LocalTime DEFAULT_OPENING_TIME = LocalTime.of(8, 30);

    private static LocalTime DEFAULT_CLOSING_TIME = LocalTime.of(22, 30);

    public static Cinema initializeEntity() {
        Cinema cinema = new Cinema();
        cinema.setId(DEFAULT_ID);
        cinema.setName(DEFAULT_NAME);
        cinema.setAddress(DEFAULT_ADDRESS);
        cinema.setEmail(DEFAULT_EMAIL);
        cinema.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        cinema.setOpeningTime(DEFAULT_OPENING_TIME);
        cinema.setClosingTime(DEFAULT_CLOSING_TIME);
        return cinema;
    }

    public static CinemaServiceModel initializeServiceModel() {
        CinemaServiceModel cinemaServiceModel = new CinemaServiceModel();
        cinemaServiceModel.setId(DEFAULT_ID);
        cinemaServiceModel.setName(DEFAULT_NAME);
        cinemaServiceModel.setAddress(DEFAULT_ADDRESS);
        cinemaServiceModel.setEmail(DEFAULT_EMAIL);
        cinemaServiceModel.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        cinemaServiceModel.setOpeningTime(DEFAULT_OPENING_TIME);
        cinemaServiceModel.setClosingTime(DEFAULT_CLOSING_TIME);
        return cinemaServiceModel;
    }

    @Override
    protected void before() {
        cinema = CinemasServiceTest.initializeEntity();
        cinemaServiceModel = CinemasServiceTest.initializeServiceModel();
    }

    private void setupCreateMethod() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(true);
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(false);
        when(cinemasRepository.save(any(Cinema.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void setupUpdateMethod() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(true);
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(false);
        when(cinemasRepository.save(any(Cinema.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
        setupUpdateMethod();
    }

    @Test
    public void create_WhenCinemaIsInvalid_ShouldThrowException() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidCinemaException.class,
                () -> cinemasService.create(cinemaServiceModel)
        );

        verify(cinemasValidationService).isValid(any(CinemaServiceModel.class));
    }

    @Test
    public void create_WhenCinemaIsValid_ShouldNotThrowException() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> cinemasService.create(cinemaServiceModel));

        verify(cinemasValidationService).isValid(any(CinemaServiceModel.class));
    }

    @Test
    public void create_WhenCinemaAlreadyExists_ShouldThrowException() {
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(true);

        assertThrows(
                CinemaAlreadyExistsException.class,
                () -> cinemasService.create(cinemaServiceModel)
        );

        verify(cinemasRepository).existsByName(anyString());
    }

    @Test
    public void create_WhenCinemaDoesNotExist_ShouldNotThrowException() {
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> cinemasService.create(cinemaServiceModel));

        verify(cinemasRepository).existsByName(anyString());
    }

    @Test
    public void create_WhenCinemaIsCorrect_ShouldPersistCinema() {
        CinemaServiceModel persistedCinema = cinemasService.create(this.cinemaServiceModel);

        verify(cinemasRepository).save(any(Cinema.class));

        assertNotNull(persistedCinema.getId());
        assertEquals(cinema.getName(), persistedCinema.getName());
    }

    @Test
    public void update_WhenCinemaIsInvalid_ShouldThrowException() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidCinemaException.class,
                () -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel)
        );
    }

    @Test
    public void update_WhenCinemaIsValid_ShouldNotThrowAnyException() {
        when(cinemasValidationService.isValid(any(CinemaServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel));
    }

    @Test
    public void update_WhenCinemaDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel)
        );
    }

    @Test
    public void update_WhenCinemaExist_ShouldNotThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));

        assertDoesNotThrow(() -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel));

        verify(cinemasRepository).findById(anyString());
    }

    @Test
    public void update_WhenCinemaNameToUpdateIsAlreadyAssignedToAnotherEntry_ShouldThrowException() {
        cinemaServiceModel.setName("Already assigned cinema name");
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(true);

        assertThrows(
                CinemaAlreadyExistsException.class,
                () -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel)
        );
    }

    @Test
    public void update_WhenCinemaNameToUpdateIsNotAssignedToAnyOtherEntry_ShouldNotThrowException() {
        cinemaServiceModel.setName("Not assigned name to another entry");
        when(cinemasRepository.existsByName(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel));

        verify(cinemasRepository).existsByName(anyString());
    }

    @Test
    public void update_WhenCinemaNameToUpdateIsTheSameAsBefore_ShouldNotCheckIfSuchCinemaExists() {
        verify(cinemasRepository, never()).existsByName(anyString());
    }

    @Test
    public void update_WhenCinemaModelIsValidAndItExists_ShouldUpdateTheEntry() {
        cinema.setName("Movie Land 2");
        cinema.setClosingTime(LocalTime.of(23, 30));

        cinemaServiceModel.setName("Movie Land 2");
        cinemaServiceModel.setClosingTime(LocalTime.of(23, 30));
        CinemaServiceModel updatedCinema = cinemasService.update(cinemaServiceModel.getId(), cinemaServiceModel);

        assertEquals(cinemaServiceModel.getName(), updatedCinema.getName());
        assertEquals(cinemaServiceModel.getClosingTime(), updatedCinema.getClosingTime());
        assertEquals(cinemaServiceModel.getEmail(), updatedCinema.getEmail());
    }
}
