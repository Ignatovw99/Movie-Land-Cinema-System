package movieland.domain.entities.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProjectionBookingDetails {

    private final String movieTitle;

    private final String cinemaName;

    private final String hallName;

    private final LocalDateTime startingTime;

    private final Long bookedSeats;

    private final BigDecimal totalPrice;

    public ProjectionBookingDetails(String movieTitle, String cinemaName, String hallName, LocalDateTime startingTime, Long bookedSeats, BigDecimal totalPrice) {
        this.movieTitle = movieTitle;
        this.cinemaName = cinemaName;
        this.hallName = hallName;
        this.startingTime = startingTime;
        this.bookedSeats = bookedSeats;
        this.totalPrice = totalPrice;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public String getHallName() {
        return hallName;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public Long getBookedSeats() {
        return bookedSeats;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
