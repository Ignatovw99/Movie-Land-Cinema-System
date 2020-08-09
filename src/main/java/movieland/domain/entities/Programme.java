package movieland.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import movieland.domain.entities.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

import static movieland.constants.entities.ProgrammeConstants.*;

@Entity
@Table(name = "programmes")
public class Programme extends BaseEntity {

    private LocalDate startDate;

    private LocalDate endDate;

    @JsonManagedReference
    private Cinema cinema;

    @JsonBackReference
    private Set<Projection> projections;

    public Programme() {
    }

    @Column(name = "start_date", nullable = false)
    @NotNull(message = START_DATE_NOT_NULL)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date", nullable = false)
    @NotNull(message = END_DATE_NOT_NULL)
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @ManyToOne
    @JoinColumn(name = "cinema_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = CINEMA_NOT_NULL)
    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    @OneToMany(targetEntity = Projection.class, mappedBy = "programme", cascade = CascadeType.ALL)
    public Set<Projection> getProjections() {
        return projections;
    }

    public void setProjections(Set<Projection> projections) {
        this.projections = projections;
    }
}
