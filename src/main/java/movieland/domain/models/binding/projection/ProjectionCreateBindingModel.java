package movieland.domain.models.binding.projection;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ProjectionCreateBindingModel {

    private String movieId;

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

    public boolean isStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setStateOfEmergency(boolean stateOfEmergency) {
        isStateOfEmergency = stateOfEmergency;
    }
}
