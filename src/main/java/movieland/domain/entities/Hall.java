package movieland.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import movieland.domain.entities.base.BaseEntity;
import movieland.domain.entities.interfaces.Nameable;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static movieland.constants.entities.HallConstants.*;

@Entity
@Table(name = "halls")
public class Hall extends BaseEntity implements Nameable {

    private String name;

    private Integer rows;

    private Integer columns;

    private String filmTechnology;

    private String soundSystem;

    @JsonManagedReference
    private Cinema cinema;

    public Hall() {
    }

    @Column(name = "name", nullable = false)
    @NotNull(message = NAME_NOT_EMPTY)
    @Length(min = NAME_LENGTH_MIN_VALUE, message = NAME_CHARACTERS_LENGTH)
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "number_of_rows", nullable = false)
    @NotNull(message = ROWS_NOT_NULL)
    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Column(name = "number_of_columns", nullable = false)
    @NotNull(message = COLUMNS_NOT_NULL)
    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    @Column(name = "film_technology", nullable = false)
    @NotNull(message = FILM_TECHNOLOGY_NOT_NULL)
    public String getFilmTechnology() {
        return filmTechnology;
    }

    public void setFilmTechnology(String filmTechnology) {
        this.filmTechnology = filmTechnology;
    }

    @Column(name = "sound_system", nullable = false)
    @NotNull(message = SOUND_SYSTEM_NOT_NULL)
    public String getSoundSystem() {
        return soundSystem;
    }

    public void setSoundSystem(String soundSystem) {
        this.soundSystem = soundSystem;
    }

    @ManyToOne(targetEntity = Cinema.class)
    @JoinColumn(name = "cinema_id", referencedColumnName = "id", nullable = false, updatable = false)
    @NotNull(message = CINEMA_NOT_NULL)
    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
