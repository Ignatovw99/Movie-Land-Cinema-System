package movieland.domain.models.binding.movie;

import movieland.config.mappings.CustomMappable;
import movieland.constants.entities.MovieConstants;
import movieland.domain.models.service.MovieServiceModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieCreateBindingModel implements CustomMappable {

    private String title;

    private String description;

    private String director;

    private String cast;

    private Integer runningTime;

    private String country;

    private Integer yearOfProduction;

    private LocalDate releaseDate;

    private BigDecimal budget;

    private Integer ageRestriction;

    private String genreId;

    public MovieCreateBindingModel() {
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

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
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

    @DateTimeFormat(pattern = MovieConstants.RELEASE_DATE_FORMAT, iso = DateTimeFormat.ISO.DATE)
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    @Override
    public void configureMappings(ModelMapper modelMapper) {
        Converter<String, Set<String>> castConverter = mappingContext ->
            mappingContext.getSource() == null
                    ? null
                    : Arrays.stream(mappingContext.getSource().split(", ")).collect(Collectors.toSet());

        modelMapper.createTypeMap(MovieCreateBindingModel.class, MovieServiceModel.class)
                .addMappings(map -> map.using(castConverter).map(
                        MovieCreateBindingModel::getCast,
                        MovieServiceModel::setCast
                ));
    }
}
