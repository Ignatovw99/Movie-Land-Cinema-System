package movieland.domain.models.binding.projection;

import movieland.config.mappings.CustomMappable;
import movieland.domain.models.service.ProjectionServiceModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ProjectionCreateBindingModel implements CustomMappable {

    private String movieId;

    private String cinemaId;

    private String hallId;

    private LocalDateTime startingTime;

    private boolean isStateOfEmergency;

    public ProjectionCreateBindingModel() {
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public boolean getIsStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setIsStateOfEmergency(boolean isStateOfEmergency) {
        this.isStateOfEmergency = isStateOfEmergency;
    }

    @Override
    public void configureMappings(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<ProjectionCreateBindingModel, ProjectionServiceModel>() {
            protected void configure() {
                skip().setId("");
            }
        });
    }
}
