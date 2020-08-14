package movieland.services.validation.implementations;

import movieland.TestBase;
import movieland.constants.entities.HallConstants;
import movieland.domain.models.service.HallServiceModel;
import movieland.services.implementations.HallsServiceTest;
import movieland.services.validation.HallsValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HallsValidationServiceTest extends TestBase {

    @Autowired
    private HallsValidationService hallsValidationService;

    private HallServiceModel hallServiceModel;

    @Override
    protected void before() {
        hallServiceModel = HallsServiceTest.initializeServiceModel();
    }

    @Test
    public void isValid_WhenNameIsNull_ShouldReturnFalse() {
        hallServiceModel.setName(null);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenNameLengthIsLessThanRequiredMinLength_ShouldReturnFalse() {
        hallServiceModel.setName("A".repeat(HallConstants.NAME_LENGTH_MIN_VALUE - 1));
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenNameMeetsTheRequirements_ShouldReturnTrue() {
        hallServiceModel.setName("VIP Hall");
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenRowsIsNull_ShouldReturnFalse() {
        hallServiceModel.setRows(null);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenRowsIsNotPositive_ShouldReturnFalse() {
        hallServiceModel.setRows(-3);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
        hallServiceModel.setRows(0);
        isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenRowsIsPositive_ShouldReturnTrue() {
        hallServiceModel.setRows(8);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenColumnsIsNull_ShouldReturnFalse() {
        hallServiceModel.setColumns(null);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenColumnsIsNotPositive_ShouldReturnFalse() {
        hallServiceModel.setColumns(-5);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
        hallServiceModel.setColumns(0);
        isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenColumnsIsPositive_ShouldReturnTrue() {
        hallServiceModel.setColumns(10);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenFilmTechnologyIsNull_ShouldReturnFalse() {
        hallServiceModel.setFilmTechnology(null);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenFillTechnologyIsNotNull_ShouldReturnTrue() {
        hallServiceModel.setSoundSystem("IMAX");
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertTrue(isValid);
    }

    @Test
    public void isValid_WhenSoundSystemIsNull_ShouldReturnFalse() {
        hallServiceModel.setSoundSystem(null);
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenSoundSystemIsNotNull_ShouldReturnTrue() {
        hallServiceModel.setSoundSystem("Doubly");
        boolean isValid = hallsValidationService.isValid(hallServiceModel);
        assertTrue(isValid);
    }
}
