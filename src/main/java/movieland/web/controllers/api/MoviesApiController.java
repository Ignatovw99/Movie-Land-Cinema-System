package movieland.web.controllers.api;

import movieland.domain.models.rest.MovieIdAndTitleResponseModel;
import movieland.services.interfaces.MoviesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    public ResponseEntity<List<MovieIdAndTitleResponseModel>> allMoviesTitles() {
        List<MovieIdAndTitleResponseModel> moviesResult = moviesService.findAll()
                .stream()
                .map(genreServiceModel -> modelMapper.map(genreServiceModel, MovieIdAndTitleResponseModel.class))
                .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok(moviesResult);
    }
}
