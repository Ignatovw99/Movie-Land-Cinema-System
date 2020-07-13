package movieland.domain.models.service;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import movieland.domain.entities.Seat;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectionServiceModel extends BaseServiceModel {

    private MovieServiceModel movie;

    private HallServiceModel hall;

    @JsonManagedReference
    private ProgrammeServiceModel programme;

    private LocalDateTime startingTime;

    private LocalDateTime endingTime;

    private boolean isStateOfEmergency;

    public ProjectionServiceModel() {
    }

    public MovieServiceModel getMovie() {
        return movie;
    }

    public void setMovie(MovieServiceModel movie) {
        this.movie = movie;
    }

    public HallServiceModel getHall() {
        return hall;
    }

    public void setHall(HallServiceModel hall) {
        this.hall = hall;
    }

    public ProgrammeServiceModel getProgramme() {
        return programme;
    }

    public void setProgramme(ProgrammeServiceModel programme) {
        this.programme = programme;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    public boolean getIsStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setIsStateOfEmergency(boolean isStateOfEmergency) {
        this.isStateOfEmergency = isStateOfEmergency;
    }
}
