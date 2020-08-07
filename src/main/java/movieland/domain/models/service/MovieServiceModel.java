package movieland.domain.models.service;

import java.math.BigDecimal;
import java.util.Set;

public class MovieServiceModel extends BaseServiceModel {

    private String title;

    private String description;

    private String director;

    private Set<String> cast;

    private Integer runningTime;

    private String country;

    private Integer yearOfProduction;

    private BigDecimal budget;

    private Integer ageRestriction;

    private String imageUrl;

    private GenreServiceModel genre;

    public MovieServiceModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Set<String> getCast() {
        return cast;
    }

    public void setCast(Set<String> cast) {
        this.cast = cast;
    }

    public Integer getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(Integer runningTime) {
        this.runningTime = runningTime;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(Integer yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Integer getAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(Integer ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public GenreServiceModel getGenre() {
        return genre;
    }

    public void setGenre(GenreServiceModel genre) {
        this.genre = genre;
    }
}
