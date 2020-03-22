package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Genre;
import movieland.domain.entities.enumerations.Classification;
import movieland.domain.models.service.GenreServiceModel;
import movieland.errors.duplicate.GenreAlreadyExistsException;
import movieland.errors.invalid.InvalidGenreModelException;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.repositories.GenresRepository;
import movieland.services.interfaces.GenresService;
import movieland.services.validation.GenresValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GenresServiceTest extends TestBase {

    @MockBean
    private GenresRepository genresRepository;

    @MockBean
    private GenresValidationService genresValidationService;

    @Autowired
    private GenresService genresService;

    private Genre genre;

    private GenreServiceModel genreServiceModel;

    private static final String DEFAULT_ID = UUID.randomUUID().toString();

    private static final String DEFAULT_NAME = "Comedy";

    private static final Classification DEFAULT_CLASSIFICATION = Classification.A;

    private static final Integer DEFAULT_AGE_RESTRICTION = 18;

    @Override
    public void before() {
        genre = new Genre();
        genre.setId(DEFAULT_ID);
        genre.setName(DEFAULT_NAME);
        genre.setClassification(DEFAULT_CLASSIFICATION);
        genre.setAgeRestriction(DEFAULT_AGE_RESTRICTION);

        genreServiceModel = new GenreServiceModel();
        genreServiceModel.setName(DEFAULT_NAME);
        genreServiceModel.setClassification(DEFAULT_CLASSIFICATION);
        genreServiceModel.setAgeRestriction(DEFAULT_AGE_RESTRICTION);
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
        setupUpdateMethod();
    }

    private void setupCreateMethod() {
        when(genresValidationService.isValid(any()))
                .thenReturn(true);
        when(genresRepository.existsByName(anyString()))
                .thenReturn(false);
        when(genresRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private void setupUpdateMethod() {
        genreServiceModel.setId(DEFAULT_ID);
        when(genresValidationService.isValid(any()))
                .thenReturn(true);
        when(genresRepository.existsById(anyString()))
                .thenReturn(true);
        when(genresRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void create_WhenInvalidServiceModel_ShouldThrowException() {
        when(genresValidationService.isValid(any()))
            .thenReturn(false);

        assertThrows(
                InvalidGenreModelException.class,
                () -> genresService.create(genreServiceModel)
        );
    }

    @Test
    public void create_WhenValidServiceModel_ShouldNotThrowException() {
        when(genresValidationService.isValid(any()))
                .thenReturn(true);

        assertDoesNotThrow(() -> genresService.create(genreServiceModel));
    }

    @Test
    public void create_WhenGenreNameAlreadyExists_ShouldThrowException() {
        when(genresRepository.existsByName(anyString()))
                .thenReturn(true);

        assertThrows(
                GenreAlreadyExistsException.class,
                () -> genresService.create(genreServiceModel)
        );
    }

    @Test
    public void create_WhenGenreNameDoesNotExist_ShouldNotThrowException() {
        when(genresRepository.existsByName(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> genresService.create(genreServiceModel));
    }

    @Test
    public void create_WhenValidServiceModelAndGenreNameDoesNotExist_ShouldPersistGenre() {
        GenreServiceModel persistedGenre = genresService.create(genreServiceModel);

        verify(genresRepository).save(any(Genre.class));

        assertEquals(genreServiceModel.getName(), persistedGenre.getName());
        assertEquals(genreServiceModel.getClassification(), persistedGenre.getClassification());
        assertEquals(genreServiceModel.getAgeRestriction(), persistedGenre.getAgeRestriction());
    }

    @Test
    public void update_WhenInvalidServiceModel_ShouldThrowException() {
        when(genresValidationService.isValid(any()))
                .thenReturn(false);

        assertThrows(
                InvalidGenreModelException.class,
                () -> genresService.update(DEFAULT_ID, genreServiceModel)
        );
    }

    @Test
    public void update_WhenValidServiceModel_ShouldNotThrowException() {
        when(genresValidationService.isValid(any()))
                .thenReturn(true);

        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreDoesNotExist_ShouldThrowException() {
        when(genresRepository.existsById(anyString()))
                .thenReturn(false);

        assertThrows(
                GenreNotFoundException.class,
                () -> genresService.update(DEFAULT_ID, genreServiceModel)
        );
    }

    @Test
    public void update_WhenGenreExists_ShouldNotThrowException() {
        when(genresRepository.existsById(anyString()))
                .thenReturn(true);

        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreNameAlreadyExists_ShouldThrowException() {
        when(genresRepository.existsByName(anyString()))
                .thenReturn(true);

        assertThrows(
                GenreAlreadyExistsException.class,
                () -> genresService.update(DEFAULT_ID, genreServiceModel)
        );
    }

    @Test
    public void update_WhenGenreNameDoesNotExist_ShouldNotThrowException() {
        when(genresRepository.existsByName(anyString()))
                .thenReturn(false);

        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreServiceModelIsValidAndGenreExists_ShouldUpdateEntry() {
        GenreServiceModel updatedGenre = genresService.update(DEFAULT_ID, genreServiceModel);

        verify(genresRepository).save(any(Genre.class)); // verify if this method is invoked

        assertEquals(genreServiceModel.getName(), updatedGenre.getName());
        assertEquals(genreServiceModel.getClassification(), updatedGenre.getClassification());
        assertEquals(genreServiceModel.getAgeRestriction(), updatedGenre.getAgeRestriction());
    }

    @Test
    public void delete_WhenGenreDoesNotExist_ShouldThrowException() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> genresService.delete(DEFAULT_ID)
        );
    }

    @Test
    public void delete_WhenGenreExists_ShouldDeleteGenre() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.of(genre));

        GenreServiceModel deletedGenre = genresService.delete(DEFAULT_ID);

        verify(genresRepository).delete(genre);

        assertEquals(genre.getId(), deletedGenre.getId());
        assertEquals(genre.getName(), deletedGenre.getName());
    }
}