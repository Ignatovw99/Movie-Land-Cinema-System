package movieland.services.interfaces;

import movieland.domain.models.service.ProjectionServiceModel;
import movieland.domain.models.service.SeatServiceModel;
import movieland.services.interfaces.base.CrudService;

import java.time.LocalDateTime;
import java.util.Set;

public interface ProjectionsService extends CrudService<ProjectionServiceModel, String> {

    boolean isStaringTimeInRangeOfWorkingHours(String cinemaId, LocalDateTime projectionDateAndStartingTime);

    boolean isHallFree(String hallId, String movieId, LocalDateTime projectionStartingTime);

    boolean isMovieAlreadyProjectedInCinemaAtTheGivenTime(String movieId, String cinemaId, LocalDateTime projectionStartingTime);

    Set<SeatServiceModel> findAllSeatsByProjectionId(String projectionId);
}
