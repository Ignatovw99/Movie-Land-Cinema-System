package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Cinema;
import movieland.domain.entities.Hall;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.HallServiceModel;
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
import java.util.*;

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

    @Test
    public void delete_WhenCinemaWithGivenIdDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> cinemasService.delete(cinemaServiceModel.getId())
        );
    }

    @Test
    public void delete_WhenCinemaExists_ShouldDeleteCorrectly() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));

        assertDoesNotThrow(() -> {
            CinemaServiceModel deletedCinema = cinemasService.delete(cinemaServiceModel.getId());
            assertEquals(cinema.getId(), deletedCinema.getId());
        });
    }

    @Test
    public void findById_WhenCinemaWithGivenIdDoesNotExist_ShouldThrowException() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                CinemaNotFoundException.class,
                () -> cinemasService.findById(cinemaServiceModel.getId())
        );
    }

    @Test
    public void findById_WhenCinemaWithGivenIdExists_ShouldReturnTheCorrectEntry() {
        when(cinemasRepository.findById(anyString()))
                .thenReturn(Optional.of(cinema));

        CinemaServiceModel foundCinema = cinemasService.findById(cinemaServiceModel.getId());

        verify(cinemasRepository).findById(anyString());
        assertEquals(cinema.getId(), foundCinema.getId());
        assertEquals(cinema.getName(), foundCinema.getName());
    }

    @Test
    public void findAll_WhenThereAreNoCinemasInDatabase_ShouldReturnEmptyCollection() {
        when(cinemasRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<CinemaServiceModel> emptyResult = cinemasService.findAll();

        verify(cinemasRepository).findAll();
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    public void findAll_WhenThereAreMoreThanOneEntriesInDatabase_ShouldReturnAllOfThem() {
        when(cinemasRepository.findAll())
                .thenReturn(List.of(cinema, cinema, cinema, cinema));

        List<CinemaServiceModel> allCinemas = cinemasService.findAll();

        verify(cinemasRepository).findAll();
        assertEquals(4, allCinemas.size());
    }

    @Test
    public void findAll_WhenResultIsReturned_ShouldNotBeModifiable() {
        when(cinemasRepository.findAll())
                .thenReturn(List.of(cinema, cinema, cinema, cinema));

        List<CinemaServiceModel> allCinemas = cinemasService.findAll();

        assertEquals(4, allCinemas.size());

        assertThrows(
                UnsupportedOperationException.class,
                () -> allCinemas.add(new CinemaServiceModel())
        );

        assertEquals(4, allCinemas.size());
    }

    @Test
    public void findAllCinemasWithoutGivenHallName_WhenHallWithGivenNameDoesNotExist_ShouldReturnAllCinemas() {
        Cinema first = new Cinema();
        Hall firstHall = new Hall();
        firstHall.setName("First");
        first.setHalls(new HashSet<>(List.of(firstHall)));

        Cinema second = new Cinema();
        Hall secondHall = new Hall();
        secondHall.setName("Second");
        second.setHalls(new HashSet<>(List.of(secondHall)));

        Cinema third = new Cinema();
        Hall thirdHall = new Hall();
        thirdHall.setName("Third");
        third.setHalls(new HashSet<>(List.of(thirdHall)));

        when(cinemasRepository.findAllCinemasWithoutGivenHallName(anyString()))
                .thenReturn(List.of(first, second, third));

        String givenHallName = "Another";

        List<CinemaServiceModel> availableCinemas = cinemasService.findAllCinemasWithoutGivenHallName(givenHallName);

        for (CinemaServiceModel availableCinema : availableCinemas) {
            for (HallServiceModel hall : availableCinema.getHalls()) {
                assertNotEquals(givenHallName, hall.getName());
            }
        }
    }

    @Test
    public void findAllCinemasWithoutGivenHallName_WhenHallWithGivenNameExistsAndIsAssignedToACinema_ShouldReturnAllCinemasExceptAssignedCinema() {
        Cinema first = new Cinema();
        first.setName("First Cinema");
        Hall firstHall = new Hall();
        firstHall.setName("First");
        first.setHalls(new HashSet<>(List.of(firstHall)));

        Cinema second = new Cinema();
        second.setName("Second Cinema");
        Hall secondHall = new Hall();
        secondHall.setName("Second");
        second.setHalls(new HashSet<>(List.of(secondHall)));

        String givenHallName = "Given hall name";
        Cinema third = new Cinema();
        third.setName("Third Cinema");
        Hall thirdHall = new Hall();
        thirdHall.setName(givenHallName);
        third.setHalls(new HashSet<>(List.of(thirdHall)));

        when(cinemasRepository.findAllCinemasWithoutGivenHallName(anyString()))
                .thenReturn(List.of(first, second));

        List<CinemaServiceModel> availableCinemas = cinemasService.findAllCinemasWithoutGivenHallName(givenHallName);

        assertEquals(2, availableCinemas.size());

        for (CinemaServiceModel availableCinema : availableCinemas) {
            assertNotEquals(third.getName(), availableCinema.getName());
            for (HallServiceModel hall : availableCinema.getHalls()) {
                assertNotEquals(givenHallName, hall.getName());
            }
        }
    }
}
