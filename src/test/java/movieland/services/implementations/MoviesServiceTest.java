package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Genre;
import movieland.domain.entities.Movie;
import movieland.domain.models.service.MovieServiceModel;
import movieland.errors.duplicate.MovieAlreadyExistsException;
import movieland.errors.invalid.InvalidMovieException;
import movieland.errors.notfound.GenreNotFoundException;
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

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
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
}