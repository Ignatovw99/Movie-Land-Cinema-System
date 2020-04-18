package movieland.web.controllers;

import movieland.domain.models.binding.MovieAddBindingModel;
import movieland.domain.models.service.MovieServiceModel;
import movieland.services.interfaces.MoviesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/movies")
public class MoviesController extends BaseController {

    private final MoviesService moviesService;

    private final ModelMapper modelMapper;

    @Autowired
    public MoviesController(MoviesService moviesService, ModelMapper modelMapper) {
        this.moviesService = moviesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView addMovie(MovieAddBindingModel movieAddBindingModel) {
        return view(movieAddBindingModel);
    }

    @PostMapping("/add")
    public ModelAndView addMovieConfirm(@Valid @ModelAttribute MovieAddBindingModel movieAddBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return view("movies-add");
        }

        MovieServiceModel movieServiceModel = modelMapper.map(movieAddBindingModel, MovieServiceModel.class);

//        moviesService.addMovie(movieServiceModel);

        return redirect("/");
    }
}
