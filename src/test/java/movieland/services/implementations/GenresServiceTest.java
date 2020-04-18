package movieland.services.implementations;

import movieland.TestBase;
import movieland.domain.entities.Genre;
import movieland.domain.entities.enumerations.Classification;
import movieland.domain.models.service.GenreServiceModel;
import movieland.errors.duplicate.GenreAlreadyExistsException;
import movieland.errors.invalid.InvalidGenreException;
import movieland.errors.notfound.GenreNotFoundException;
import movieland.repositories.GenresRepository;
import movieland.services.interfaces.GenresService;
import movieland.services.validation.GenresValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
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

    private static final Boolean DEFAULT_IS_AGE_RESTRICTION_REQUIRED = true;

    public static Genre initializeEntity() {
        Genre genre = new Genre();
        genre.setId(DEFAULT_ID);
        genre.setName(DEFAULT_NAME);
        genre.setClassification(DEFAULT_CLASSIFICATION);
        genre.setIsAgeRestrictionRequired(DEFAULT_IS_AGE_RESTRICTION_REQUIRED);
        return genre;
    }

    public static GenreServiceModel initializeServiceModel() {
        GenreServiceModel genreServiceModel = new GenreServiceModel();
        genreServiceModel.setName(DEFAULT_NAME);
        genreServiceModel.setClassification(DEFAULT_CLASSIFICATION);
        genreServiceModel.setIsAgeRestrictionRequired(DEFAULT_IS_AGE_RESTRICTION_REQUIRED);
        return genreServiceModel;
    }

    @Override
    public void before() {
        genre = GenresServiceTest.initializeEntity();
        genreServiceModel = GenresServiceTest.initializeServiceModel();
    }

    @Override
    protected void setupMockBeansActions() {
        setupCreateMethod();
        setupUpdateMethod();
        setupDeleteMethod();
        setupFindAllMethod();
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
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.of(genre));
        when(genresRepository.existsByName(anyString()))
                .thenReturn(false);
        when(genresRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void setupDeleteMethod() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.of(genre));
    }

    private void setupFindAllMethod() {
        when(genresRepository.findAll())
                .thenReturn(List.of(genre, genre, genre, genre));
    }

    @Test
    public void create_WhenInvalidServiceModel_ShouldThrowException() {
        when(genresValidationService.isValid(any()))
            .thenReturn(false);

        assertThrows(
                InvalidGenreException.class,
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
        assertEquals(genreServiceModel.getIsAgeRestrictionRequired(), persistedGenre.getIsAgeRestrictionRequired());
    }

    @Test
    public void update_WhenInvalidServiceModel_ShouldThrowException() {
        when(genresValidationService.isValid(any()))
                .thenReturn(false);

        assertThrows(
                InvalidGenreException.class,
                () -> genresService.update(DEFAULT_ID, genreServiceModel)
        );
    }

    @Test
    public void update_WhenValidServiceModel_ShouldNotThrowException() {
        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreDoesNotExist_ShouldThrowException() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> genresService.update(DEFAULT_ID, genreServiceModel)
        );
    }

    @Test
    public void update_WhenGenreExists_ShouldNotThrowException() {
        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreNameToUpdateIsAssignedToTheGenreToUpdate_ShouldNotThrowException() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.of(genre));

        assertDoesNotThrow(() -> genresService.update(DEFAULT_ID, genreServiceModel));
    }

    @Test
    public void update_WhenGenreNameToUpdateIsAlreadyAssignedToAnotherEntry_ShouldThrowException() {
        genreServiceModel.setName("Name assigned to another genre");
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.of(genre));
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
        assertEquals(genreServiceModel.getIsAgeRestrictionRequired(), updatedGenre.getIsAgeRestrictionRequired());
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
        GenreServiceModel deletedGenre = genresService.delete(DEFAULT_ID);

        verify(genresRepository).delete(genre);

        assertEquals(genre.getId(), deletedGenre.getId());
        assertEquals(genre.getName(), deletedGenre.getName());
    }

    @Test
    public void findById_WhenGenreWithGivenIdDoesNotExist_ShouldThrowException() {
        when(genresRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                GenreNotFoundException.class,
                () -> genresService.findById(DEFAULT_ID)
        );
    }

    @Test
    public void findById_WhenGenreWithSuchIdExists_ShouldReturnTheCorrectGenre() {
        when(genresRepository.findById(any()))
                .thenReturn(Optional.of(genre));

        GenreServiceModel returnedGenre = genresService.findById(DEFAULT_ID);

        verify(genresRepository).findById(anyString());
        assertEquals(genre.getId(), returnedGenre.getId());
        assertEquals(genre.getName(), returnedGenre.getName());
    }

    @Test
    public void findAll_WhenThereAreNoGenresInDb_ShouldNotReturnNull() {
        when(genresRepository.findAll())
                .thenReturn(new ArrayList<>());

        assertNotNull(genresService.findAll());
        verify(genresRepository).findAll();
    }

    @Test
    public void findAll_WhenGenreDbTableIsEmpty_ShouldReturnEmptyList() {
        when(genresRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<GenreServiceModel> emptyGenresList = genresService.findAll();

        verify(genresRepository).findAll();
        assertEquals(0, emptyGenresList.size());
        assertTrue(emptyGenresList.isEmpty());
    }

    @Test
    public void findAll_WhenThereAreMoreThanZeroEntriesInDb_ResultShouldNotBeEmpty() {
        List<GenreServiceModel> allFoundGenres = genresService.findAll();

        assertFalse(allFoundGenres.isEmpty());
    }

    @Test
    public void findAll_WhenThereAreManyEntriesInDb_ShouldReturnAllOfThem() {
        List<GenreServiceModel> allFoundGenres = genresService.findAll();

        verify(genresRepository).findAll();
        assertEquals(4, allFoundGenres.size());
    }

    @Test
    public void findAll_WhenResultIsReturned_ShouldBeUnmodifiable() {
        when(genresRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<GenreServiceModel> foundGenres = genresService.findAll();

        assertTrue(foundGenres.isEmpty());

        assertThrows(
                UnsupportedOperationException.class,
                () -> foundGenres.add(genreServiceModel)
        );

        assertTrue(foundGenres.isEmpty());
    }
}