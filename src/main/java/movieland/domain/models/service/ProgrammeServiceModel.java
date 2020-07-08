package movieland.domain.models.service;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDate;
import java.util.Set;

public class ProgrammeServiceModel extends BaseServiceModel {

    private LocalDate startDate;

    private LocalDate endDate;

    private CinemaServiceModel cinema;

    @JsonBackReference
    private Set<ProjectionServiceModel> projections;

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

    public Set<ProjectionServiceModel> getProjections() {
        return projections;
    }

    public void setProjections(Set<ProjectionServiceModel> projections) {
        this.projections = projections;
    }
}
