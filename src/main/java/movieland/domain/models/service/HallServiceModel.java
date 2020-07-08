package movieland.domain.models.service;

import com.fasterxml.jackson.annotation.JsonManagedReference;

public class HallServiceModel extends BaseServiceModel {

    private String name;

    private Integer rows;

    private Integer columns;

    private String filmTechnology;

    private String soundSystem;

    @JsonManagedReference
    private CinemaServiceModel cinema;

    public HallServiceModel() {
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

    public CinemaServiceModel getCinema() {
        return cinema;
    }

    public void setCinema(CinemaServiceModel cinema) {
        this.cinema = cinema;
    }
}
