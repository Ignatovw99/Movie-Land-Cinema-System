package movieland.web.controllers;

import movieland.domain.models.binding.cinema.CinemaCreateBindingModel;
import movieland.domain.models.binding.cinema.CinemaUpdateBindingModel;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.view.cinema.CinemaDeleteViewModel;
import movieland.domain.models.view.cinema.CinemaViewModel;
import movieland.services.interfaces.CinemasService;
import movieland.validation.cinema.CinemasCreateValidator;
import movieland.validation.cinema.CinemasUpdateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cinemas")
public class CinemasController extends BaseController {

    private final CinemasService cinemasService;

    private final CinemasCreateValidator cinemasCreateValidator;

    private final CinemasUpdateValidator cinemasUpdateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public CinemasController(CinemasService cinemasService, CinemasCreateValidator cinemasCreateValidator, CinemasUpdateValidator cinemasUpdateValidator, ModelMapper modelMapper) {
        this.cinemasService = cinemasService;
        this.cinemasCreateValidator = cinemasCreateValidator;
        this.cinemasUpdateValidator = cinemasUpdateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createCinema(CinemaCreateBindingModel cinemaCreateBindingModel) {
        return view("cinema/cinema-create", cinemaCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createCinemaConfirm(@ModelAttribute(name = "model") CinemaCreateBindingModel cinemaCreateBindingModel, BindingResult bindingResult) {
        cinemasCreateValidator.validate(cinemaCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("cinema/cinema-create");
        }

        CinemaServiceModel cinemaServiceModel = modelMapper.map(cinemaCreateBindingModel, CinemaServiceModel.class);
        cinemasService.create(cinemaServiceModel);

        return redirect("/");
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateCinema(@PathVariable String id) {
        CinemaServiceModel cinemaServiceModel = cinemasService.findById(id);
        CinemaUpdateBindingModel cinemaToUpdate = modelMapper.map(cinemaServiceModel, CinemaUpdateBindingModel.class);
        return view("cinema/cinema-update", cinemaToUpdate);
    }

    @PostMapping("/update")
    public ModelAndView updateCinemaConfirm(@ModelAttribute(name = "model") CinemaUpdateBindingModel cinemaUpdateBindingModel, BindingResult bindingResult) {
        cinemasUpdateValidator.validate(cinemaUpdateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("cinema/cinema-update");
        }

        CinemaServiceModel cinemaToUpdateServiceModel = modelMapper.map(cinemaUpdateBindingModel, CinemaServiceModel.class);
        cinemasService.update(cinemaToUpdateServiceModel.getId(), cinemaToUpdateServiceModel);
        return redirect("/cinemas/all");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteCinema(@PathVariable String id) {
        CinemaServiceModel cinemaServiceModel = cinemasService.findById(id);
        CinemaDeleteViewModel cinemaToDelete = modelMapper.map(cinemaServiceModel, CinemaDeleteViewModel.class);
        return view("cinema/cinema-delete", cinemaToDelete);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCinemaConfirm(@RequestParam String id) {
        cinemasService.delete(id);
        return redirect("/cinemas/all");
    }

    @GetMapping("/all")
    public ModelAndView getAllCinemas() {
        List<CinemaViewModel> allCinemas = cinemasService.findAll()
                .stream()
                .map(cinemaServiceModel -> modelMapper.map(cinemaServiceModel, CinemaViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return view("cinema/cinemas-all", allCinemas);
    }
}
