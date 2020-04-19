package movieland.web.controllers;

import movieland.domain.models.binding.movie.MovieCreateBindingModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.services.interfaces.MoviesService;
import movieland.validation.movie.MoviesCreateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/movies")
public class MoviesController extends BaseController {

    private final MoviesService moviesService;

    private final MoviesCreateValidator moviesCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public MoviesController(MoviesService moviesService, MoviesCreateValidator moviesCreateValidator, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.moviesCreateValidator = moviesCreateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createMovie(MovieCreateBindingModel movieCreateBindingModel) {
        return view("movie/movie-create", movieCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView addMovieConfirm(@ModelAttribute(name = "model") MovieCreateBindingModel movieCreateBindingModel, BindingResult bindingResult) {
        moviesCreateValidator.validate(movieCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("movie/movie-create");
        }

        MovieServiceModel movieToCreateServiceModel = modelMapper.map(movieCreateBindingModel, MovieServiceModel.class);
        moviesService.create(movieToCreateServiceModel);

        return redirect("/");
    }
}
