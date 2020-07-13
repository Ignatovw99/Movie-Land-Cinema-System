package movieland.domain.models.view.projection;

import java.time.LocalDateTime;

public class ProjectionViewModel {

    private String id;

    private String movieTitle;

    private String hallCinemaName;

    private String hallName;

    private Integer hallRows;

    private Integer hallColumns;

    private LocalDateTime startingTime;

    private LocalDateTime endingTime;

    private boolean isStateOfEmergency;

    public ProjectionViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getHallCinemaName() {
        return hallCinemaName;
    }

    public void setHallCinemaName(String hallCinemaName) {
        this.hallCinemaName = hallCinemaName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public Integer getHallRows() {
        return hallRows;
    }

    public void setHallRows(Integer hallRows) {
        this.hallRows = hallRows;
    }

    public Integer getHallColumns() {
        return hallColumns;
    }

    public void setHallColumns(Integer hallColumns) {
        this.hallColumns = hallColumns;
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

    public boolean isStateOfEmergency() {
        return isStateOfEmergency;
    }

    public void setStateOfEmergency(boolean stateOfEmergency) {
        isStateOfEmergency = stateOfEmergency;
    }
}
