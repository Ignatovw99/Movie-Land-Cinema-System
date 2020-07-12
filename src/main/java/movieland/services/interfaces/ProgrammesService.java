package movieland.services.interfaces;

import movieland.domain.entities.Programme;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.domain.models.view.programme.CinemaProgrammeDateViewModel;

import java.time.LocalDate;
import java.util.Map;

public interface ProgrammesService {

    ProgrammeServiceModel createNext(ProgrammeServiceModel programmeServiceModel);

    ProgrammeServiceModel getProgrammeByCinemaIdAndDate(String cinemaId, LocalDate date);

    boolean isProgrammeActive(Programme programme);

    void createAnActiveProgrammeForAllCinemasWithInactiveOnes();

    void deleteInactiveProgrammesOlderThanOneYear();

    LocalDate getFirstPossibleStartDateForCinema(String cinemaId);

    Map<LocalDate, CinemaProgrammeDateViewModel> getCurrantActiveCinemaProgrammeWithItsProjections(String cinemaId);
}
