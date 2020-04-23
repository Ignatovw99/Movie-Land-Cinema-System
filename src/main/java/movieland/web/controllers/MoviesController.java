package movieland.web.controllers;

import movieland.domain.models.binding.movie.MovieCreateBindingModel;
import movieland.domain.models.binding.movie.MovieUpdateBindingModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.domain.models.view.MovieViewModel;
import movieland.services.interfaces.MoviesService;
import movieland.validation.movie.MoviesCreateValidator;
import movieland.validation.movie.MoviesUpdateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/movies")
public class MoviesController extends BaseController {

    private final MoviesService moviesService;

    private final MoviesCreateValidator moviesCreateValidator;

    private final MoviesUpdateValidator moviesUpdateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public MoviesController(MoviesService moviesService, MoviesCreateValidator moviesCreateValidator, MoviesUpdateValidator moviesUpdateValidator, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.moviesCreateValidator = moviesCreateValidator;
        this.moviesUpdateValidator = moviesUpdateValidator;
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

    @GetMapping("/update/{id}")
    public ModelAndView updateGenre(@PathVariable String id) {
        MovieServiceModel movieServiceModel = moviesService.findById(id);
        MovieUpdateBindingModel movieToUpdate = modelMapper.map(movieServiceModel, MovieUpdateBindingModel.class);
        return view("movie/movie-update", movieToUpdate);
    }

    @PostMapping("/update")
    public ModelAndView updateGenreConfirm(@ModelAttribute("model") MovieUpdateBindingModel movieToUpdate, BindingResult bindingResult) {
        moviesUpdateValidator.validate(movieToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("movie/movie-update");
        }
        MovieServiceModel movieToUpdateServiceModel = modelMapper.map(movieToUpdate, MovieServiceModel.class);
        moviesService.update(movieToUpdate.getId(), movieToUpdateServiceModel);
        return redirect("/movies/all");
    }

//    @GetMapping("/delete/{id}")
//    public ModelAndView deleteGenre(@PathVariable String id) {
//        //TODO: get movies count in GenreDeleteViewModel
//        GenreServiceModel genreServiceModel = genresService.findById(id);
//        GenreDeleteViewModel genreToDelete = modelMapper.map(genreServiceModel, GenreDeleteViewModel.class);
//        return view("genre/genre-delete", genreToDelete);
//    }
//
//    //TODO: add additional check logic if there are some movies assigned to the genre
//    @PostMapping("/delete")
//    public ModelAndView deleteGenreConfirm(@RequestParam String id) {
//        GenreServiceModel deletedGenreServiceModel = genresService.delete(id);
//        return redirect("/");
//    }

    @GetMapping("/all")
    public ModelAndView getAllMovies(ModelAndView modelAndView) {
        List<MovieViewModel> allMovies = moviesService.findAll()
                .stream()
                .map(movieServiceModel -> modelMapper.map(movieServiceModel, MovieViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return view("movie/movies-all", allMovies);
    }
}
