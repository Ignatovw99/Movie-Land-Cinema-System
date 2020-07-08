package movieland.domain.models.view.programme;

import movieland.domain.models.view.movie.MovieViewModel;
import movieland.domain.models.view.projection.ProjectionViewModel;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class MovieProjectionViewModel {

    private MovieViewModel movie;

    private Set<ProjectionViewModel> projections = new TreeSet<>(Comparator.comparing(ProjectionViewModel::getStartingTime));

    public MovieProjectionViewModel() {
    }

    public MovieViewModel getMovie() {
        return movie;
    }

    public void setMovie(MovieViewModel movie) {
        this.movie = movie;
    }

    public Set<ProjectionViewModel> getProjections() {
        return projections;
    }

    public void addProjection(ProjectionViewModel projection) {
        projections.add(projection);
    }

    public void setProjection(Set<ProjectionViewModel> projections) {
        this.projections = projections;
    }
}
