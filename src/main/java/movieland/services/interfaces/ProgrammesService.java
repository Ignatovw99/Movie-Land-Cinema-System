package movieland.services.interfaces;

import movieland.domain.entities.Programme;
import movieland.domain.models.service.ProgrammeServiceModel;

public interface ProgrammesService {

    ProgrammeServiceModel createNext(ProgrammeServiceModel programmeServiceModel);

    boolean isProgrammeActive(Programme programme);

    void createAnActiveProgrammeForAllCinemasWithInactiveOnes();

    void deleteInactiveProgrammesOlderThanOneYear();
}
