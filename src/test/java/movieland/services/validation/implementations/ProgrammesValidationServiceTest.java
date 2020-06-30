package movieland.services.validation.implementations;

import movieland.TestBase;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.services.implementations.ProgrammesServiceTest;
import movieland.services.validation.ProgrammesValidationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProgrammesValidationServiceTest extends TestBase {

    @MockBean
    private Clock clock;

    @Autowired
    private ProgrammesValidationService programmesValidationService;

    private ProgrammeServiceModel programmeServiceModel;

    private static final LocalDate MOCK_TODAY = LocalDate.of(2020, 6, 30);

    @Override
    protected void before() {
        //tell your tests to return the specified MOCK_TODAY when calling LocalDate.now(clock)
        Clock fixedClock = Clock.fixed(MOCK_TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();

        programmeServiceModel = ProgrammesServiceTest.initializeServiceModel();
        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(1));
        programmeServiceModel.setEndDate(MOCK_TODAY.plusDays(8));
    }

    @Test
    public void isValid_WhenStartDateIsBeforeToday_ShouldReturnFalse() {
        programmeServiceModel.setStartDate(MOCK_TODAY.minusDays(1));
        boolean isValid = programmesValidationService.isValid(programmeServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenProgrammeIsLessThanOneWeekLong_ShouldReturnFalse() {
        programmeServiceModel.setStartDate(MOCK_TODAY.plusDays(1));
        programmeServiceModel.setEndDate(MOCK_TODAY.plusDays(5));
        boolean isValid = programmesValidationService.isValid(programmeServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_WhenAssignedCinemaIsNull_ShouldReturnFalse() {
        programmeServiceModel.setCinema(null);
        boolean isValid = programmesValidationService.isValid(programmeServiceModel);
        assertFalse(isValid);
    }

    @Test
    public void isValid_ItIsCorrect_ShouldReturnTrue() {
        boolean isValid = programmesValidationService.isValid(programmeServiceModel);
        assertTrue(isValid);
    }
}
