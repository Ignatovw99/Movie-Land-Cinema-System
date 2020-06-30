package movieland.services.validation.implementations;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.services.validation.ProgrammesValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ProgrammesValidationServiceImpl implements ProgrammesValidationService {

    private final Clock clock;

    @Autowired
    public ProgrammesValidationServiceImpl(Clock clock) {
        this.clock = clock;
    }

    private boolean isCinemaValid(CinemaServiceModel cinema) {
        return cinema != null;
    }

    private boolean isProgrammeAtLeastOneWeekLong(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays() >= 7;
    }

    private boolean isStartDateAfterToday(LocalDate startDate) {
        return startDate.isAfter(LocalDate.now(clock));
    }

    @Override
    public boolean isValid(ProgrammeServiceModel programmeServiceModel) {
        return isStartDateAfterToday(programmeServiceModel.getStartDate())
                && isProgrammeAtLeastOneWeekLong(programmeServiceModel.getStartDate(), programmeServiceModel.getEndDate())
                && isCinemaValid(programmeServiceModel.getCinema());
    }
}
