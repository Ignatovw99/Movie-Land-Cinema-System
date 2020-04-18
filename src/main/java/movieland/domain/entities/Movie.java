package movieland.domain.entities;

import movieland.domain.entities.base.BaseEntity;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static movieland.constants.entities.MovieConstants.*;

@Entity
@Table(name = "movies")
public class Movie extends BaseEntity {

    private String title;

    private String description;

    private String director;

    private Set<String> cast;

    private Integer runningTime;

    private String country;

    private Integer yearOfProduction;

    private LocalDate releaseDate;

    private BigDecimal budget;

    private Integer ageRestriction;

    private Genre genre;

//    private Set<Projection> projections;

    public Movie() {
    }

    @Column(name = "title", nullable = false, unique = true, length = TITLE_MAX_LENGTH)
    @NotNull(message = TITLE_NOT_EMPTY)
    @Length(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_CHARACTERS_LENGTH)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    @NotNull(message = DESCRIPTION_NOT_EMPTY)
    @Length(min = DESCRIPTION_MIN_LENGTH, max = DESCRIPTION_MAX_LENGTH, message = DESCRIPTION_CHARACTERS_LENGTH)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "director", nullable = false)
    @NotNull(message = DIRECTOR_NOT_NULL)
    @Length(min = DIRECTOR_MIN_LENGTH, max = DIRECTOR_MAX_LENGTH, message = DIRECTOR_CHARACTERS_LENGTH)
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @ElementCollection
    @NotNull(message = CAST_NOT_NULL)
    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    @Column(name = "running_time", nullable = false)
    @NotNull(message = RUNNING_TIME_NOT_NULL)
    @Min(value = RUNNING_TIME_MIN, message = RUNNING_TIME_MIN_VALUE)
    @Max(value = RUNNING_TIME_MAX, message = RUNNING_TIME_MAX_VALUE)
    public Integer getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Integer runningTime) {
        this.runningTime = runningTime;
    }

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "year_of_production", nullable = false)
    @NotNull(message = YEAR_OF_PRODUCTION_NOT_NULL)
    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    @Column(name = "release_date", nullable = false)
    @NotNull(message = RELEASE_DAY_NOT_NULL)
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column(name = "budget")
    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    @Column(name = "age_restriction")
    @Min(value = AGE_RESTRICTION_MIN, message = AGE_RESTRICTION_MIN_VALUE)
    @Max(value = AGE_RESTRICTION_MAX, message = AGE_RESTRICTION_MAX_VALUE)
    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id", nullable = false)
    @NotNull(message = GENRE_NOT_NULL)
    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

//    @OneToMany(targetEntity = Projection.class, mappedBy = "movie", cascade = CascadeType.ALL)
//    public Set<Projection> getProjections() {
//        return projections;
//    }
//
//    public void setProjections(Set<Projection> projections) {
//        this.projections = projections;
//    }

}
