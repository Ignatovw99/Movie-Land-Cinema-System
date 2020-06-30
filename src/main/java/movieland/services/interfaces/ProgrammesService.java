package movieland.services.interfaces;

import movieland.domain.entities.Programme;
import movieland.domain.models.service.ProgrammeServiceModel;

public interface ProgrammesService {

    /**
     * When a programmes became inactive it should be added a new weekly programme for a cinema
     *
     * Delete all inactive programmes which are older than one year
     */

    ProgrammeServiceModel createNext(ProgrammeServiceModel programmeServiceModel);

    boolean isProgrammeActive(Programme programme);

//    void replaceInactiveProgrammesWithActiveOnes()

    // void deleteProgrammesOlderThanOneYear()
}
