package movieland.services.validation.implementations;

import movieland.TestBase;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.implementations.CinemasServiceTest;
import movieland.services.validation.CinemasValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static movieland.constants.entities.CinemaConstants.NAME_LENGTH_MIN_VALUE;
import static movieland.constants.entities.CinemaConstants.PHONE_NUMBER_LENGTH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CinemasValidationServiceTest extends TestBase {

    @Autowired
    private CinemasValidationService cinemasValidationService;

    private CinemaServiceModel cinemaServiceModel;

    @Override
    protected void before() {
        cinemaServiceModel = CinemasServiceTest.initializeServiceModel();
    }

    @Test
    public void isValid_WhenNameIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setName(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenNameLengthIsLessThanFourCharacters_ShouldReturnFalse() {
        cinemaServiceModel.setName("A".repeat(NAME_LENGTH_MIN_VALUE - 1));
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenNameIsCorrect_ShouldReturnTrue() {
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenAddressIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setAddress(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenAddressIsCorrect_ShouldReturnTrue() {
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenPhoneNumberIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setPhoneNumber(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenPhoneNumberContainsALetter_ShouldReturnFalse() {
        cinemaServiceModel.setPhoneNumber("9643285749v");
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenPhoneNumberContainsLessThanRequiredSymbols_ShouldReturnFalse() {
        cinemaServiceModel.setPhoneNumber("8".repeat(PHONE_NUMBER_LENGTH - 1));
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenPhoneNumberContainsMoreThanRequiredSymbols_ShouldReturnFalse() {
        cinemaServiceModel.setPhoneNumber("8".repeat(PHONE_NUMBER_LENGTH + 1));
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenPhoneNumberIsCorrect_ShouldReturnTrue() {
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);

        cinemaServiceModel.setPhoneNumber("43275978479");
        isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenEmailIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setEmail(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenEmailIsNotValid_ShouldReturnFalse() {
        cinemaServiceModel.setEmail("email.gmail.com");
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);

        cinemaServiceModel.setEmail("email@gmail");
        isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);

        cinemaServiceModel.setEmail("email@gmail.bgdfds");
        isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenEmailIsValid_ShouldReturnTrue() {
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenOpeningTimeIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setOpeningTime(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenOpeningTimeIsAfterClosingTime_ShouldReturnFalse() {
        cinemaServiceModel.setOpeningTime(LocalTime.of(21, 24));
        cinemaServiceModel.setClosingTime(LocalTime.of(5, 4));
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenClosingTimeIsNull_ShouldReturnFalse() {
        cinemaServiceModel.setClosingTime(null);
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenOpeningAndClosingTimeAreCorrect_ShouldReturnTrue() {
        boolean isValid = cinemasValidationService.isValid(cinemaServiceModel);
        assertTrue(isValid);
    }
}
