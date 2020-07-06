package movieland.services.interfaces;

import movieland.domain.models.service.ProjectionServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.time.LocalDateTime;

public interface ProjectionsService extends CrudService<ProjectionServiceModel, String> {

    boolean isStaringTimeInRangeOfWorkingHours(String cinemaId, LocalDateTime projectionDateAndStartingTime);

    boolean isHallFree(String hallId, String movieId, LocalDateTime projectionStartingTime);

    boolean isMovieAlreadyProjectedInCinemaAtTheGivenTime(String movieId, String cinemaId, LocalDateTime projectionStartingTime);
}
