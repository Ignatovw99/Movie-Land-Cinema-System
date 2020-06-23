package movieland.domain.models.binding.hall;

public class HallCreateBindingModel {

    private String name;

    private Integer rows;

    private Integer columns;

    private String filmTechnology;

    private String soundSystem;

    private String cinemaId;

    public HallCreateBindingModel() {
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

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }
}
