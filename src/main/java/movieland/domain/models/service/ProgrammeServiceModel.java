package movieland.domain.models.service;

import java.time.LocalDate;

public class ProgrammeServiceModel extends BaseServiceModel {

    private LocalDate startDate;

    private LocalDate endDate;

    private CinemaServiceModel cinema;

    public ProgrammeServiceModel() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public CinemaServiceModel getCinema() {
        return cinema;
    }

    public void setCinema(CinemaServiceModel cinema) {
        this.cinema = cinema;
    }
}
