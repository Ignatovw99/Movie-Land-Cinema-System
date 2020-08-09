package movieland.web.controllers;

import movieland.domain.models.binding.genre.GenreCreateBindingModel;
import movieland.domain.models.binding.genre.GenreUpdateBindingModel;
import movieland.domain.models.service.GenreServiceModel;
import movieland.domain.models.view.genre.GenreDeleteViewModel;
import movieland.domain.models.view.genre.GenreViewModel;
import movieland.services.interfaces.GenresService;
import movieland.validation.genre.GenresCreateValidator;
import movieland.validation.genre.GenresUpdateValidator;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static movieland.constants.GlobalConstants.MODEL_NAME;

@Controller
@RequestMapping("/genres")
public class GenresController extends BaseController {

    private final GenresService genresService;

    private final GenresCreateValidator genresCreateValidator;

    private final GenresUpdateValidator genresUpdateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public GenresController(GenresService genresService, GenresCreateValidator genresCreateValidator, GenresUpdateValidator genresUpdateValidator, ModelMapper modelMapper) {
        this.genresService = genresService;
        this.genresCreateValidator = genresCreateValidator;
        this.genresUpdateValidator = genresUpdateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @Page(title = "Create Genre", name = "genre/genre-create")
    public ModelAndView createGenre(GenreCreateBindingModel genreCreateBindingModel) {
        return view(genreCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createGenreConfirm(@ModelAttribute(name = MODEL_NAME) GenreCreateBindingModel genreCreateBindingModel, BindingResult bindingResult) {
        genresCreateValidator.validate(genreCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("genre/genre-create");
        }

        GenreServiceModel genreToCreateServiceModel = modelMapper.map(genreCreateBindingModel, GenreServiceModel.class);
        genresService.create(genreToCreateServiceModel);

        return redirect("/genres/all");
    }

    @GetMapping("/update/{id}")
    @Page(title = "Update Genre", name = "genre/genre-update")
    public ModelAndView updateGenre(@PathVariable String id) {
        GenreServiceModel genreServiceModel = genresService.findById(id);
        GenreUpdateBindingModel genreToUpdate = modelMapper.map(genreServiceModel, GenreUpdateBindingModel.class);
        return view(genreToUpdate);
    }

    @PostMapping("/update")
    public ModelAndView updateGenreConfirm(@ModelAttribute(MODEL_NAME) GenreUpdateBindingModel genreToUpdate, BindingResult bindingResult) {
        genresUpdateValidator.validate(genreToUpdate, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("genre/genre-update");
        }
        GenreServiceModel genreToUpdateServiceModel = modelMapper.map(genreToUpdate, GenreServiceModel.class);
        genresService.update(genreToUpdate.getId(), genreToUpdateServiceModel);
        return redirect("/genres/all");
    }

    @GetMapping("/delete/{id}")
    @Page(title = "Delete Genre", name = "genre/genre-delete")
    public ModelAndView deleteGenre(@PathVariable String id) {
        //TODO: get movies count in GenreDeleteViewModel
        GenreServiceModel genreServiceModel = genresService.findById(id);
        GenreDeleteViewModel genreToDelete = modelMapper.map(genreServiceModel, GenreDeleteViewModel.class);
        return view(genreToDelete);
    }

    //TODO: add additional check logic if there are some movies assigned to the genre
    @PostMapping("/delete")
    public ModelAndView deleteGenreConfirm(@RequestParam String id) {
        GenreServiceModel deletedGenreServiceModel = genresService.delete(id);
        return redirect("/genres/all");
    }

    @GetMapping("/all")
    @Page(title = "All Genres", name = "genre/genres-all")
    public ModelAndView getAllGenres() {
        List<GenreViewModel> allGenres = genresService.findAll()
                .stream()
                .map(genreServiceModel -> modelMapper.map(genreServiceModel, GenreViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return view(allGenres);
    }
}
