package movieland.services.implementations;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.entities.Programme;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.errors.invalid.InvalidProgrammeException;
import movieland.errors.notfound.CinemaNotFoundException;
import movieland.repositories.CinemasRepository;
import movieland.repositories.ProgrammesRepository;
import movieland.services.interfaces.ProgrammesService;
import movieland.services.validation.ProgrammesValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static movieland.constants.entities.ProgrammeConstants.*;

@Service
public class ProgrammesServiceImpl implements ProgrammesService {

    private final ProgrammesRepository programmesRepository;

    private final ProgrammesValidationService programmesValidationService;

    private final CinemasRepository cinemasRepository;

    private final Clock clock;

    private final ModelMapper modelMapper;

    @Autowired
    public ProgrammesServiceImpl(ProgrammesRepository programmesRepository, ProgrammesValidationService programmesValidationService, CinemasRepository cinemasRepository, Clock clock, ModelMapper modelMapper) {
        this.programmesRepository = programmesRepository;
        this.programmesValidationService = programmesValidationService;
        this.cinemasRepository = cinemasRepository;
        this.clock = clock;
        this.modelMapper = modelMapper;
    }

    private boolean isTheNextDay(LocalDate day, LocalDate nextDayCandidate) {
        return Period.between(day, nextDayCandidate).getDays() == 1;
    }

    private boolean isStartDateNextDayAfterTheActiveProgramme(Programme activeProgramme, LocalDate startDate) {
        return isTheNextDay(activeProgramme.getEndDate(), startDate);
    }

    private boolean isStartDateTomorrow(LocalDate startDate) {
        return isTheNextDay(LocalDate.now(clock), startDate);
    }

    @Override
    public ProgrammeServiceModel createNext(ProgrammeServiceModel programmeServiceModel) {
        if (!programmesValidationService.isValid(programmeServiceModel)) {
            throw new InvalidProgrammeException(PROGRAMME_MODEL_NOT_VALID);
        }

        String cinemaId = programmeServiceModel.getCinema().getId();

        if (!cinemasRepository.existsById(cinemaId)) {
            throw new CinemaNotFoundException(CinemaConstants.CINEMA_NOT_FOUND);
        }

        Optional<Programme> activeProgrammeCandidate = programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(cinemaId);

        if (activeProgrammeCandidate.isEmpty() && !isStartDateTomorrow(programmeServiceModel.getStartDate())) {
            throw new InvalidProgrammeException(START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME);
        } else if (activeProgrammeCandidate.isPresent() && !isProgrammeActive(activeProgrammeCandidate.get()) && !isStartDateTomorrow(programmeServiceModel.getStartDate())) {
            throw new InvalidProgrammeException(START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME);
        } else if (activeProgrammeCandidate.isPresent() && isProgrammeActive(activeProgrammeCandidate.get()) && !isStartDateNextDayAfterTheActiveProgramme(activeProgrammeCandidate.get(), programmeServiceModel.getStartDate())) {
            throw new InvalidProgrammeException(START_DATE_SHOULD_BE_THE_NEXT_DAY_AFTER_CURRENT_ACTIVE_PROGRAMME);
        }

        Programme programme = modelMapper.map(programmeServiceModel, Programme.class);
        programme = programmesRepository.save(programme);

        return modelMapper.map(programme, ProgrammeServiceModel.class);
    }

    @Override
    public boolean isProgrammeActive(Programme programme) {
        LocalDate today = LocalDate.now(clock);
        return programme.getStartDate().isBefore(today) && programme.getEndDate().isAfter(today);
    }
}
