package movieland.web.controllers.api;

import movieland.domain.models.rest.MovieIdAndTitleResponseModel;
import movieland.services.interfaces.MoviesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
public class MoviesApiController {

    private final MoviesService moviesService;

    private final ModelMapper modelMapper;

    @Autowired
    public MoviesApiController(MoviesService moviesService, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/titles")
    public Set<MovieIdAndTitleResponseModel> allMoviesTitles() {
        return moviesService.findAll()
                .stream()
                .map(genreServiceModel -> modelMapper.map(genreServiceModel, MovieIdAndTitleResponseModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }
}
