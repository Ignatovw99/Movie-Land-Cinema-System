package movieland.services.validation.implementations;

import movieland.domain.models.service.HallServiceModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.services.validation.ProjectionsValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectionsValidationServiceImpl implements ProjectionsValidationService {

    private boolean isStartingTimeValid(LocalDateTime startingTime) {
        return startingTime != null;
    }

    private boolean isHallValid(HallServiceModel hall) {
        return hall != null && hall.getId() != null;
    }

    private boolean isMovieValid(MovieServiceModel movie) {
        return movie != null && movie.getId() != null;
    }

    @Override
    public boolean isValid(ProjectionServiceModel projectionServiceModel) {
        return isMovieValid(projectionServiceModel.getMovie()) && isHallValid(projectionServiceModel.getHall()) && isStartingTimeValid(projectionServiceModel.getStartingTime());
    }
}
