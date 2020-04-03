package movieland.web.controllers;

import movieland.domain.models.binding.GenreCreateBindingModel;
import movieland.domain.models.service.GenreServiceModel;
import movieland.services.interfaces.GenresService;
import movieland.validation.genre.GenresCreateValidator;
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
@RequestMapping("/genres")
public class GenresController extends BaseController {

    private final GenresService genresService;

    private final GenresCreateValidator genresCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public GenresController(GenresService genresService, GenresCreateValidator genresCreateValidator, ModelMapper modelMapper) {
        this.genresService = genresService;
        this.genresCreateValidator = genresCreateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createGenre(GenreCreateBindingModel genreCreateBindingModel) {
        return view(genreCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createGenreConfirm(@Valid @ModelAttribute(name = "model") GenreCreateBindingModel genreCreateBindingModel, BindingResult bindingResult) {
        genresCreateValidator.validate(genreCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("genres-create");
        }

        GenreServiceModel cinemaServiceModel = modelMapper.map(genreCreateBindingModel, GenreServiceModel.class);
        genresService.create(cinemaServiceModel);

        return redirect("/");
    }
}
