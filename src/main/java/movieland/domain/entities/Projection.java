package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "projections")
public class Projection extends BaseEntity {

    private Movie movie;

    private Hall hall;

    private Programme programme;

    private LocalDateTime startingTime;

    private LocalDateTime endingTime;

    private boolean isStateOfEmergency;

    private Set<Seat> seats;

    public Projection() {
    }

    @ManyToOne(targetEntity = Movie.class)
    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @ManyToOne(targetEntity = Hall.class)
    @JoinColumn(name = "hall_id", referencedColumnName = "id", nullable = false)
    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    @ManyToOne(targetEntity = Programme.class)
    @JoinColumn(name = "programme_id", referencedColumnName = "id", nullable = false)
    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    @Column(name = "starting_time", nullable = false)
    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    @Column(name = "ending_time", nullable = false)
    public LocalDateTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    @Column(name = "is_state_of_emergency")
    public boolean isStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setStateOfEmergency(boolean stateOfEmergency) {
        isStateOfEmergency = stateOfEmergency;
    }

    @OneToMany(targetEntity = Seat.class, mappedBy = "projection", cascade = CascadeType.ALL)
    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
}
