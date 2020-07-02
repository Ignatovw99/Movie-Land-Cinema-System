package movieland.domain.models.service;

import movieland.domain.entities.Seat;

import java.time.LocalDateTime;
import java.util.Set;

public class ProjectionServiceModel extends BaseServiceModel {

    private MovieServiceModel movie;

    private HallServiceModel hall;

    private ProgrammeServiceModel programme;

    private LocalDateTime startingTime;

    private boolean isStateOfEmergency;

//    private Set<Seat> seats;

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

    public boolean isStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setStateOfEmergency(boolean stateOfEmergency) {
        isStateOfEmergency = stateOfEmergency;
    }

    //    public Set<Seat> getSeats() {
//        return seats;
//    }
//
//    public void setSeats(Set<Seat> seats) {
//        this.seats = seats;
//    }
}
