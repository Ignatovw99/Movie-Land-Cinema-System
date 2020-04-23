package movieland.domain.models.view.movie;

import movieland.config.mappings.CustomMappable;
import movieland.domain.models.service.MovieServiceModel;
import movieland.domain.models.view.BaseViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class MovieViewModel extends BaseViewModel implements CustomMappable {

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

    private String genreName;

    public MovieViewModel() {
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

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public void configureMappings(ModelMapper modelMapper) {
        Converter<Set<String>, String> castConverter = mappingContext ->
                mappingContext.getSource() == null
                        ? null
                        : String.join(", ", mappingContext.getSource());

        modelMapper.createTypeMap(MovieServiceModel.class, MovieViewModel.class)
                .addMappings(map -> map.using(castConverter).map(
                        MovieServiceModel::getCast,
                        MovieViewModel::setCast
                ));
    }
}
