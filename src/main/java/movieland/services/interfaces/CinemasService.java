package movieland.services.interfaces;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.util.List;

public interface CinemasService extends CrudService<CinemaServiceModel, String> {

    List<CinemaServiceModel> findAllCinemasWithoutGivenHallName(String hallName);

    CinemaServiceModel findCinemaByHallId(String hallId);
}
