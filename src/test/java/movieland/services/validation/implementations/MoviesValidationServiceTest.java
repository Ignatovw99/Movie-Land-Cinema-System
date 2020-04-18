package movieland.services.validation.implementations;

import movieland.TestBase;
import movieland.domain.models.service.MovieServiceModel;
import movieland.services.implementations.MoviesServiceTest;
import movieland.services.validation.MoviesValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static movieland.constants.entities.MovieConstants.*;

public class MoviesValidationServiceTest extends TestBase {

    @Autowired
    private MoviesValidationService moviesValidationService;

    private MovieServiceModel movieServiceModel;

    @Override
    protected void before() {
        movieServiceModel = MoviesServiceTest.initializeServiceModel();
    }

    @Test
    public void isValid_WhenTitleIsNull_ShouldReturnFalse() {
        movieServiceModel.setTitle(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenTitleLengthIsLessThanMinValue_ShouldReturnFalse() {
        movieServiceModel.setTitle("A".repeat(TITLE_MIN_LENGTH - 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenTitleLengthEqualsMinValue_ShouldReturnTrue() {
        movieServiceModel.setTitle("A".repeat(TITLE_MIN_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenTitleLengthIsGreaterThanMaxValue_ShouldReturnFalse() {
        movieServiceModel.setTitle("A".repeat(TITLE_MAX_LENGTH + 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenTitleLengthEqualsMaxValue_ShouldReturnTrue() {
        movieServiceModel.setTitle("A".repeat(TITLE_MAX_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenTitleLengthInRange_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDescriptionIsNull_ShouldReturnFalse() {
        movieServiceModel.setDescription(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDescriptionLengthIsLessThanMinValue_ShouldReturnFalse() {
        movieServiceModel.setDescription("A".repeat(DESCRIPTION_MIN_LENGTH - 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDescriptionLengthIsEqualMinValue_ShouldReturnTrue() {
        movieServiceModel.setDescription("A".repeat(DESCRIPTION_MIN_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDescriptionLengthIsGreaterThanMaxValue_ShouldReturnFalse() {
        movieServiceModel.setDescription("A".repeat(DESCRIPTION_MAX_LENGTH + 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDescriptionLengthIsEqualMaxValue_ShouldReturnTrue() {
        movieServiceModel.setDescription("A".repeat(DESCRIPTION_MAX_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDescriptionLengthIsInRange_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDirectorIsNull_ShouldReturnFalse() {
        movieServiceModel.setDirector(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDirectorLengthIsLessThanMinValue_ShouldReturnFalse() {
        movieServiceModel.setDirector("A".repeat(DIRECTOR_MIN_LENGTH - 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDirectorLengthIsEqualMinValue_ShouldReturnTrue() {
        movieServiceModel.setDirector("A".repeat(DIRECTOR_MIN_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDirectorLengthIsGreaterThanMaxValue_ShouldReturnFalse() {
        movieServiceModel.setDirector("A".repeat(DIRECTOR_MAX_LENGTH + 1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenDirectorLengthIsEqualMaxValue_ShouldReturnTrue() {
        movieServiceModel.setDirector("A".repeat(DIRECTOR_MAX_LENGTH));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenDirectorLengthIsInRange_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenCastIsNull_ShouldReturnFalse() {
        movieServiceModel.setCast(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenCastIsEmpty_ShouldReturnFalse() {
        movieServiceModel.setCast(new HashSet<>());
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenCastIsNotEmpty_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsNull_ShouldReturnFalse() {
        movieServiceModel.setRunningTime(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsLessThanMinValue_ShouldReturnFalse() {
        movieServiceModel.setRunningTime(RUNNING_TIME_MIN - 1);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsEqualMinValue_ShouldReturnTrue() {
        movieServiceModel.setRunningTime(RUNNING_TIME_MIN);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsGreaterThanMaxValue_ShouldReturnFalse() {
        movieServiceModel.setRunningTime(RUNNING_TIME_MAX + 1);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsEqualMaxValue_ShouldReturnTrue() {
        movieServiceModel.setRunningTime(RUNNING_TIME_MAX);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenRunningTimeIsInRange_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenYearOfProductionIsNull_ShouldReturnFalse() {
        movieServiceModel.setYearOfProduction(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenYearOfProductionIsInTheFuture_ShouldReturnFalse() {
        movieServiceModel.setYearOfProduction(LocalDate.now().plusYears(1).getYear());
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenYearOfProductionIsLessTheCurrentYear_ShouldReturnTrue() {
        movieServiceModel.setYearOfProduction(LocalDate.now().minusYears(1).getYear());
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenYearOfProductionIsEqualTheCurrentYear_ShouldReturnTrue() {
        movieServiceModel.setYearOfProduction(LocalDate.now().getYear());
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenReleaseDayIsNull_ShouldReturnFalse() {
        movieServiceModel.setReleaseDate(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenReleaseDayIsTomorrow_ShouldReturnTrue() {
        movieServiceModel.setReleaseDate(LocalDate.now().plusDays(1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenReleaseDayIsToday_ShouldReturnFalse() {
        movieServiceModel.setReleaseDate(LocalDate.now());
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenReleaseDayIsInThePast_ShouldReturnFalse() {
        movieServiceModel.setReleaseDate(LocalDate.now().minusDays(1));
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenReleaseDayIsInTheFuture_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenGenreIsNull_ShouldReturnFalse() {
        movieServiceModel.setGenre(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenGenreIsNotNull_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsNull_ShouldReturnFalse() {
        movieServiceModel.setAgeRestriction(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenGenreDoesNotRequireAgeRestrictionAndAgeRestrictionIsNull_ShouldReturnTrue() {
        movieServiceModel.getGenre().setIsAgeRestrictionRequired(false);
        movieServiceModel.setAgeRestriction(null);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsLessThanMinValue_ShouldReturnFalse() {
        movieServiceModel.setAgeRestriction(AGE_RESTRICTION_MIN - 1);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsEqualMinValue_ShouldReturnTrue() {
        movieServiceModel.setAgeRestriction(AGE_RESTRICTION_MIN);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsGreaterThanMaxValue_ShouldReturnFalse() {
        movieServiceModel.setAgeRestriction(AGE_RESTRICTION_MAX + 1);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsEqualMaxValue_ShouldReturnTrue() {
        movieServiceModel.setAgeRestriction(AGE_RESTRICTION_MAX);
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenGenreRequiresAgeRestrictionAndAgeRestrictionIsInRange_ShouldReturnTrue() {
        boolean isValid = moviesValidationService.isValid(movieServiceModel);
        assertTrue(isValid);
    }
}