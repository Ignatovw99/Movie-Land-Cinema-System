package movieland.domain.models.view.programme;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import movieland.config.JacksonCustomMapSerializer;
import movieland.domain.models.view.movie.MovieViewModel;
import movieland.domain.models.view.projection.ProjectionViewModel;

import java.time.LocalDate;
import java.util.*;

public class CinemaProgrammeDateViewModel {

    private String cinemaName;

    private LocalDate date;

    @JsonSerialize(using = JacksonCustomMapSerializer.class)
    private Map<MovieViewModel, Set<ProjectionViewModel>> movieProjections = new TreeMap<>(Comparator.comparing(MovieViewModel::getTitle));

    public CinemaProgrammeDateViewModel() {
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<MovieViewModel, Set<ProjectionViewModel>> getMovieProjections() {
        return movieProjections;
    }

    public void setMovieProjections(Map<MovieViewModel, Set<ProjectionViewModel>> movieProjections) {
        this.movieProjections = movieProjections;
    }

    public void addProjection(ProjectionViewModel projection, MovieViewModel movie) {
        date = projection.getStartingTime().toLocalDate();

        movieProjections.putIfAbsent(movie, new TreeSet<>(Comparator.comparing(ProjectionViewModel::getStartingTime)));
        movieProjections.get(movie).add(projection);
    }
}
