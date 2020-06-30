package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static movieland.constants.entities.ProgrammeConstants.*;

@Entity
@Table(name = "programmes")
public class Programme extends BaseEntity {

    private LocalDate startDate;

    private LocalDate endDate;

    private Cinema cinema;

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
}
