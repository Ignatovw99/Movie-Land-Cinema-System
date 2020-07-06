package movieland.services.implementations;

import movieland.constants.entities.CinemaConstants;
import movieland.domain.entities.Cinema;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    private boolean isStartDateNextDayAfterTheLastAvailableProgramme(Programme availableProgramme, LocalDate startDate) {
        return isTheNextDay(availableProgramme.getEndDate(), startDate);
    }

    private boolean isStartDateTomorrow(LocalDate startDate) {
        return isTheNextDay(LocalDate.now(clock), startDate);
    }

    private boolean hasCinemaActiveProgramme(Cinema cinema) {
        Optional<Programme> activeProgrammeCandidate = programmesRepository.findCurrentActiveProgrammeOfCinema(cinema, LocalDate.now(clock));
        if (activeProgrammeCandidate.isEmpty()) {
            return false;
        }
        return isProgrammeActive(activeProgrammeCandidate.get());
    }

    private ProgrammeServiceModel createActiveWeeklyProgrammeFor(Cinema cinema) {
        Programme programme = new Programme();
        programme.setStartDate(LocalDate.now(clock));
        programme.setEndDate(LocalDate.now(clock).plusDays(7));
        programme.setCinema(cinema);
        programme = programmesRepository.save(programme);

        return modelMapper.map(programme, ProgrammeServiceModel.class);
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

        Optional<Programme> lastAvailableProgrammeCandidate = programmesRepository.findFirstByCinemaIdOrderByStartDateDesc(cinemaId);

        if (lastAvailableProgrammeCandidate.isEmpty() && !isStartDateTomorrow(programmeServiceModel.getStartDate())) {
            throw new InvalidProgrammeException(START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME);
        } else if (lastAvailableProgrammeCandidate.isPresent() && !isProgrammeActive(lastAvailableProgrammeCandidate.get())) {
            Programme lastActiveProgramme = lastAvailableProgrammeCandidate.get();
            if (isProgrammeOver(lastActiveProgramme) && isStartDateTomorrow(lastActiveProgramme.getStartDate())) {
                throw new InvalidProgrammeException(START_DATE_SHOULD_BE_TOMORROW_WHEN_NO_ACTIVE_PROGRAMME);
            } else if (!isStartDateNextDayAfterTheLastAvailableProgramme(lastActiveProgramme, programmeServiceModel.getStartDate())){
                throw new InvalidProgrammeException(START_DATE_SHOULD_BE_THE_NEXT_DAY_AFTER_CURRENT_ACTIVE_PROGRAMME);
            }
        } else if (lastAvailableProgrammeCandidate.isPresent() && isProgrammeActive(lastAvailableProgrammeCandidate.get()) && !isStartDateNextDayAfterTheLastAvailableProgramme(lastAvailableProgrammeCandidate.get(), programmeServiceModel.getStartDate())) {
            throw new InvalidProgrammeException(START_DATE_SHOULD_BE_THE_NEXT_DAY_AFTER_CURRENT_ACTIVE_PROGRAMME);
        }

        Programme programme = modelMapper.map(programmeServiceModel, Programme.class);
        programme = programmesRepository.save(programme);

        return modelMapper.map(programme, ProgrammeServiceModel.class);
    }

    private boolean isProgrammeOver(Programme lastActiveProgramme) {
        return LocalDate.now(clock).isAfter(lastActiveProgramme.getEndDate());
    }

    @Override
    public boolean isProgrammeActive(Programme programme) {
        LocalDate today = LocalDate.now(clock);
        LocalDate startDate = programme.getStartDate();
        LocalDate endDate = programme.getEndDate();
        return (startDate.isEqual(today) || startDate.isBefore(today)) && (endDate.isEqual(today) || endDate.isAfter(today));
    }

    //This is scheduled task, which has to be executed every day at midnight (12am)
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void createAnActiveProgrammeForAllCinemasWithInactiveOnes() {
        cinemasRepository.findAll()
                .forEach(cinema -> {
                    if (!hasCinemaActiveProgramme(cinema)) {
                        createActiveWeeklyProgrammeFor(cinema);
                    }
                });
    }

    @Scheduled(cron = "0 0 12 * * SUN")
    @Transactional
    @Override
    public void deleteInactiveProgrammesOlderThanOneYear() {
        LocalDate lastYearDate = LocalDate.now(clock).minusYears(1);
        programmesRepository.deleteByEndDateBefore(lastYearDate);
    }

    @Override
    public LocalDate getFirstPossibleStartDateForCinema(String cinemaId) {
        Optional<Programme> lastCinemaProgrammeCandidate = programmesRepository.findFirstByCinemaIdOrderByEndDateDesc(cinemaId);

        LocalDate possibleStartDate;
        LocalDate todayDate = LocalDate.now(clock);

        if (lastCinemaProgrammeCandidate.isEmpty()) {
            possibleStartDate = todayDate.plusDays(1);
        } else {
            LocalDate endDateOfLastCinemaProgramme = lastCinemaProgrammeCandidate.get().getEndDate();
            if (endDateOfLastCinemaProgramme.isAfter(todayDate)) {
                possibleStartDate = endDateOfLastCinemaProgramme.plusDays(1);
            } else {
                possibleStartDate = todayDate.plusDays(1);
            }
        }

        return possibleStartDate;
    }
}
