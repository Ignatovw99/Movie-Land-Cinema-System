package movieland.domain.models.view.programme;

import movieland.config.mappings.CustomMappable;
import movieland.domain.entities.Projection;
import movieland.domain.models.binding.movie.MovieCreateBindingModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.domain.models.view.movie.MovieViewModel;
import movieland.domain.models.view.projection.ProjectionViewModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CinemaProgrammeDateViewModel {

    private String cinemaName;

    private LocalDate date;

    private Set<MovieProjectionViewModel> movieProjections = new TreeSet<>(Comparator.comparing(movieProjections -> movieProjections.getMovie().getTitle()));

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

    public Set<MovieProjectionViewModel> getMovieProjections() {
        return movieProjections;
    }

    public void setMovieProjections(Set<MovieProjectionViewModel> movieProjections) {
        this.movieProjections = movieProjections;
    }

    public void addProjection(ProjectionViewModel projection, MovieViewModel movie) {
        date = projection.getStartingTime().toLocalDate();

        MovieProjectionViewModel movieProjectionViewModel = new MovieProjectionViewModel();
        movieProjectionViewModel.setMovie(movie);
        movieProjectionViewModel.addProjection(projection);

        movieProjections.add(movieProjectionViewModel);
    }
}
