package movieland.services.interfaces;

import movieland.domain.models.service.MovieServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.util.Optional;
import java.util.Set;

public interface MoviesService extends CrudService<MovieServiceModel, String> {

    Set<MovieServiceModel> getAll();

    Optional<MovieServiceModel> getById(String movieId);
}
