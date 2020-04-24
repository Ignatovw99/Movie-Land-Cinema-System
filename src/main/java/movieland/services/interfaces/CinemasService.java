package movieland.services.interfaces;

import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.util.Set;

public interface CinemasService extends CrudService<CinemaServiceModel, String> {

    boolean createCinema(CinemaServiceModel cinemaServiceModel);

    Set<CinemaServiceModel> getAll();

    CinemaServiceModel getCinemaById(String id);
}
