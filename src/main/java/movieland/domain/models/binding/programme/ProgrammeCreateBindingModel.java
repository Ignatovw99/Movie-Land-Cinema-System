package movieland.domain.models.binding.programme;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static movieland.constants.entities.ProgrammeConstants.DATE_PATTERN;

public class ProgrammeCreateBindingModel {

    private String cinemaId;

    private LocalDate startDate;

    private LocalDate endDate;

    public ProgrammeCreateBindingModel() {
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    @DateTimeFormat(pattern = DATE_PATTERN)
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @DateTimeFormat(pattern = DATE_PATTERN)
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
