package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Genre;
import movieland.domain.entities.Movie;
import movieland.domain.models.service.GenreServiceModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.errors.duplicate.MovieAlreadyExistsException;
import movieland.errors.invalid.InvalidMovieException;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.errors.notfound.MovieNotFoundException;
import movieland.repositories.GenresRepository;
import movieland.repositories.MoviesRepository;
import movieland.services.interfaces.MoviesService;
import movieland.services.validation.MoviesValidationService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoviesServiceTest extends TestBase {

    @MockBean
    private MoviesRepository moviesRepository;

    @MockBean
    private MoviesValidationService moviesValidationService;

    @MockBean
    private GenresRepository genresRepository;

    @Autowired
    private MoviesService moviesService;

    private Movie movie;

    private MovieServiceModel movieServiceModel;

    private static final String DEFAULT_ID = UUID.randomUUID().toString();

    private static final String DEFAULT_TITLE = "Fast and Furious 9";

    private static final String DEFAULT_DESCRIPTION = "F9 (alternatively known as Fast & Furious 9) is an upcoming American action film directed by Justin Lin and written by Daniel Casey.";

    private static final String DEFAULT_DIRECTOR = "Justin Lin";

    private static final List<String> DEFAULT_CAST = Arrays.asList("Vin Diesel", "John Cena", "Jordana Brewster");

    private static final Integer DEFAULT_RUNNING_TIME = 200;

    private static final Integer DEFAULT_YEAR_OF_PRODUCTION = 2020;

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.now().plusYears(4);

    private static final Integer DEFAULT_AGE_RESTRICTION = 18;

    public static Movie initializeEntity() {
        Movie movie = new Movie();
        movie.setId(DEFAULT_ID);
        movie.setTitle(DEFAULT_TITLE);
        movie.setDescription(DEFAULT_DESCRIPTION);
        movie.setDirector(DEFAULT_DIRECTOR);
        movie.setCast(new HashSet<>(DEFAULT_CAST));
        movie.setRunningTime(DEFAULT_RUNNING_TIME);
        movie.setYearOfProduction(DEFAULT_YEAR_OF_PRODUCTION);
        movie.setReleaseDate(DEFAULT_RELEASE_DATE);
        movie.setAgeRestriction(DEFAULT_AGE_RESTRICTION);
        movie.setGenre(GenresServiceTest.initializeEntity());
        return movie;
    }

    public static MovieServiceModel initializeServiceModel() {
        MovieServiceModel movieServiceModel = new MovieServiceModel();
        movieServiceModel.setId(DEFAULT_ID);
        movieServiceModel.setTitle(DEFAULT_TITLE);
        movieServiceModel.setDescription(DEFAULT_DESCRIPTION);
        movieServiceModel.setDirector(DEFAULT_DIRECTOR);
        movieServiceModel.setCast(new HashSet<>(DEFAULT_CAST));
        movieServiceModel.setRunningTime(DEFAULT_RUNNING_TIME);
        movieServiceModel.setYearOfProduction(DEFAULT_YEAR_OF_PRODUCTION);
        movieServiceModel.setReleaseDate(DEFAULT_RELEASE_DATE);
        movieServiceModel.setAgeRestriction(DEFAULT_AGE_RESTRICTION);
        movieServiceModel.setGenre(GenresServiceTest.initializeServiceModel());
        return movieServiceModel;
    }

    @Override
    protected void before() {
        movie = MoviesServiceTest.initializeEntity();
        movieServiceModel = MoviesServiceTest.initializeServiceModel();
    }

    private void setupCreateMethod() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(true);
        when(moviesRepository.existsByTitle(anyString()))
                .thenReturn(false);
        when(genresRepository.findById(any()))
                .thenReturn(Optional.of(GenresServiceTest.initializeEntity()));
        when(moviesRepository.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void setupUpdateMethod() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(true);
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));
    }

    private void setupDeleteMethod() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
        setupUpdateMethod();
        setupDeleteMethod();
    }

    @Test
    public void create_WhenInvalidServiceModel_ShouldThrowException() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidMovieException.class,
                () -> moviesService.create(movieServiceModel)
        );

        verify(moviesValidationService).isValid(any(MovieServiceModel.class));
    }

    @Test
    public void create_WhenValidServiceModel_ShouldNotThrowException() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> moviesService.create(movieServiceModel));

        verify(moviesValidationService).isValid(any(MovieServiceModel.class));
    }

    @Test
    public void create_WhenMovieTitleAlreadyExists_ShouldThrowException() {
        when(moviesRepository.existsByTitle(anyString()))
                .thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () -> moviesService.create(movieServiceModel)
        );

        verify(moviesRepository).existsByTitle(anyString());
    }

    @Test
    public void create_WhenMovieTitleDoesNotExist_ShouldNotThrowException() {
        when(moviesRepository.existsByTitle(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> moviesService.create(movieServiceModel));

        verify(moviesRepository).existsByTitle(anyString());
    }

    @Test
    public void create_WhenAssignedGenreDoesNotExist_ShouldThrowException() {
        when(genresRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> moviesService.create(movieServiceModel)
        );

        verify(genresRepository).findById(any());
    }

    @Test
    public void create_WhenAssignedGenreIsNull_ShouldThrowException() {
        when(genresRepository.findById(null))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> moviesService.create(movieServiceModel)
        );

        verify(genresRepository).findById(any());
    }

    @Test
    public void create_WhenAssignedGenreExists_ShouldNotThrowException() {
        when(genresRepository.findById(any()))
                .thenReturn(Optional.of(GenresServiceTest.initializeEntity()));

        assertDoesNotThrow(() -> moviesService.create(movieServiceModel));

        verify(genresRepository).findById(any());
    }

    @Test
    public void create_WhenAssignedGenreExists_ShouldBeAssignedToTheCreatedMovie() {
        Genre genre = GenresServiceTest.initializeEntity();
        when(genresRepository.findById(any()))
                .thenReturn(Optional.of(genre));

        MovieServiceModel createdMovie = moviesService.create(movieServiceModel);

        assertEquals(genre.getId(), createdMovie.getGenre().getId());
        assertEquals(genre.getName(), createdMovie.getGenre().getName());
    }

    @Test
    public void create_WhenMovieIsCorrect_ShouldPersistAndReturnMovie() {
        MovieServiceModel persistedMovie = moviesService.create(movieServiceModel);

        verify(moviesRepository).save(any(Movie.class));

        assertEquals(movie.getId(), persistedMovie.getId());
        assertEquals(movie.getTitle(), persistedMovie.getTitle());
    }

    @Test
    public void update_WhenMovieServiceModelIsInvalid_ShouldThrowException() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(false);

        assertThrows(
                InvalidMovieException.class,
                () -> moviesService.update(movieServiceModel.getId(), movieServiceModel)
        );

        verify(moviesValidationService).isValid(any(MovieServiceModel.class));
    }

    @Test
    public void update_WhenMovieServiceModelIsValid_ShouldNotThrowException() {
        when(moviesValidationService.isValid(any(MovieServiceModel.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> moviesService.update(movieServiceModel.getId(), movieServiceModel));

        verify(moviesValidationService).isValid(any(MovieServiceModel.class));
    }

    @Test
    public void update_WhenMovieWithTheGivenIdDoesNotExist_ShouldThrowException() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> moviesService.update(UUID.randomUUID().toString(), movieServiceModel)
        );

        verify(moviesRepository).findById(any(String.class));
    }

    @Test
    public void update_WhenMovieWithTheGivenIdExists_ShouldNotThrowException() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> moviesService.update(DEFAULT_ID, movieServiceModel));

        verify(moviesRepository).findById(any(String.class));
    }

    @Test
    public void update_WhenUpdatedMovieTitleAlreadyExists_ShouldThrowException() {
        movieServiceModel.setTitle("Updated Title");
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));
        when(moviesRepository.existsByTitle(anyString()))
                .thenReturn(true);

        assertThrows(
                MovieAlreadyExistsException.class,
                () -> moviesService.update(DEFAULT_ID, movieServiceModel)
        );

        verify(moviesRepository).findById(any(String.class));
        verify(moviesRepository).existsByTitle(anyString());
    }

    @Test
    public void update_WhenUpdatedMovieTitleDoesNotExist_ShouldNotThrowException() {
        movieServiceModel.setTitle("Updated Title");
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));
        when(moviesRepository.existsByTitle(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> moviesService.update(DEFAULT_ID, movieServiceModel));

        verify(moviesRepository).findById(any(String.class));
        verify(moviesRepository).existsByTitle(anyString());
    }

    @Test
    public void update_WhenMovieTitleWasNotChanged_ShouldNotThrowException() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> moviesService.update(DEFAULT_ID, movieServiceModel));

        verify(moviesRepository).findById(any(String.class));
        verify(moviesRepository, never()).existsByTitle(anyString());
    }

    @Test
    public void update_WhenMovieGenreWasNotUpdated_ShouldNotCheckIfGenreExists() {
        moviesService.update(DEFAULT_ID, movieServiceModel);
        verify(genresRepository, never()).findById(any(String.class));
    }

    @Test
    public void update_WhenUpdatedMovieGenreDoesNotExist_ShouldThrowException() {
        movieServiceModel.getGenre().setId(UUID.randomUUID().toString());
        when(genresRepository.findById(any(String.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> moviesService.update(DEFAULT_ID, movieServiceModel)
        );

        verify(genresRepository).findById(any(String.class));
    }

    @Test
    public void update_WhenUpdatedGenreExists_ShouldChangeMovieGenre() {
        Genre updatedGenre = GenresServiceTest.initializeEntity();
        String genreIdUpdated = UUID.randomUUID().toString();
        updatedGenre.setId(genreIdUpdated);
        movieServiceModel.getGenre().setId(genreIdUpdated);
        when(genresRepository.findById(any(String.class)))
                .thenReturn(Optional.of(updatedGenre));
        when(moviesRepository.save(any(Movie.class)))
                .then(invocation -> invocation.getArgument(0));

        assertNotEquals(updatedGenre.getId(), movie.getGenre().getId());

        MovieServiceModel updatedMovie = moviesService.update(DEFAULT_ID, movieServiceModel);

        assertEquals(updatedGenre.getId(), updatedMovie.getGenre().getId());
    }

    @Test
    public void update_WhenOnlyTitleAndDescriptionAreUpdatedAndOtherFieldsAreNull_ShouldNotMapAnotherNullValues() {
        MovieServiceModel movieToUpdate = new MovieServiceModel();
        movieToUpdate.setId(DEFAULT_ID);
        movieToUpdate.setTitle("Updated title");
        movieToUpdate.setDescription("Updated description");
        movieToUpdate.setGenre(new GenreServiceModel());

        assertEquals(DEFAULT_TITLE, movie.getTitle());
        assertEquals(DEFAULT_DESCRIPTION, movie.getDescription());

        MovieServiceModel updatedMovie = moviesService.update(DEFAULT_ID, movieToUpdate);

        assertEquals("Updated title", updatedMovie.getTitle());
        assertEquals("Updated description", updatedMovie.getDescription());
        assertEquals(DEFAULT_DIRECTOR, updatedMovie.getDirector());
    }

    @Test
    public void delete_WhenMovieWithTheGivenIdDoesNotExist_ShouldThrowException() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.empty());

        assertThrows(
                MovieNotFoundException.class,
                () -> moviesService.delete(DEFAULT_ID)
        );

        verify(moviesRepository).findById(any(String.class));
    }

    @Test
    public void delete_WhenMovieWithTheGivenIdExists_ShouldNotThrowException() {
        when(moviesRepository.findById(any(String.class)))
                .thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> moviesService.delete(DEFAULT_ID));

        verify(moviesRepository).findById(any(String.class));
    }

    @Test
    public void delete_WhenMovieExists_ShouldBeDeleted() {
        MovieServiceModel deletedMovie = moviesService.delete(DEFAULT_ID);

        verify(moviesRepository).delete(movie);

        assertEquals(movie.getId(), deletedMovie.getId());
        assertEquals(movie.getTitle(), deletedMovie.getTitle());
    }
}