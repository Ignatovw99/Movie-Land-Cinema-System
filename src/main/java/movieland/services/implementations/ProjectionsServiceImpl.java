package movieland.services.implementations;

import movieland.domain.entities.Movie;
import movieland.domain.entities.Projection;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.repositories.ProjectionsRepository;
import movieland.repositories.SeatsRepository;
import movieland.services.interfaces.HallsService;
import movieland.services.interfaces.MoviesService;
import movieland.services.interfaces.ProjectionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectionsServiceImpl implements ProjectionsService {

    private final ProjectionsRepository projectionsRepository;

    private final SeatsRepository seatsRepository;

    private final MoviesService moviesService;

    private final HallsService hallsService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectionsServiceImpl(ProjectionsRepository projectionsRepository, SeatsRepository seatsRepository, MoviesService moviesService, HallsService hallsService, ModelMapper modelMapper) {
        this.projectionsRepository = projectionsRepository;
        this.seatsRepository = seatsRepository;
        this.moviesService = moviesService;
        this.hallsService = hallsService;
        this.modelMapper = modelMapper;
    }
//
//    private void initializeSeatsForCurrentProjection(Projection projection) {
//        projection.setSeats(new HashSet<>());
//        int rows = projection
//                .getHall()
//                .getRows();
//
//        int columns = projection
//                .getHall()
//                .getColumns();
//
//        for (int row = 1; row <= rows; row++) {
//            for (int col = 1; col <= columns; col++) {
//                Seat seat = new Seat()
//                        .row(row)
//                        .column(col)
//                        .projection(projection);
//
//                projection.getSeats()
//                        .add(seat);
//            }
//        }
//    }


    @Override
    public ProjectionServiceModel create(ProjectionServiceModel projectionServiceModel) {
        Projection projection = new Projection();

        moviesService.getById(projectionServiceModel.getMovie().getId())
                .ifPresent(movieServiceModel -> {
                    Movie movie = modelMapper.map(movieServiceModel, Movie.class);
                    projection.setMovie(movie);
//                    movie
//                            .getProjections()
//                            .add(projection);
                });

//        HallServiceModel hallServiceModel = hallsService.findById(projectionAddServiceModel.getHallId());
//        Hall hall = modelMapper.map(hallServiceModel, Hall.class);
//        projection.setHall(hall);
//
//        initializeSeatsForCurrentProjection(projection);
//
//        projection.setStartingTime(projectionAddServiceModel.getStartingTime());

        return null;
    }

    @Override
    public ProjectionServiceModel update(String id, ProjectionServiceModel serviceModel) {
        return null;
    }

    @Override
    public ProjectionServiceModel delete(String id) {
        return null;
    }

    @Override
    public ProjectionServiceModel findById(String id) {
        return null;
    }

    @Override
    public List<ProjectionServiceModel> findAll() {
        return null;
    }
}
