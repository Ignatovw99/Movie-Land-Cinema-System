package movieland.domain.models.view.hall;

import movieland.domain.models.view.BaseViewModel;

public class HallViewModel extends BaseViewModel {

    private String name;

    private Integer rows;

    private Integer columns;

    private String filmTechnology;

    private String soundSystem;

    private String cinemaName;

    public HallViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public String getFilmTechnology() {
        return filmTechnology;
    }

    public void setFilmTechnology(String filmTechnology) {
        this.filmTechnology = filmTechnology;
    }

    public String getSoundSystem() {
        return soundSystem;
    }

    public void setSoundSystem(String soundSystem) {
        this.soundSystem = soundSystem;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }
}
