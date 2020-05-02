package movieland.web.controllers;

import movieland.domain.models.binding.cinema.CinemaCreateBindingModel;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.services.interfaces.CinemasService;
import movieland.validation.cinema.CinemasCreateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cinemas")
public class CinemasController extends BaseController {

    private final CinemasService cinemasService;

    private final CinemasCreateValidator cinemasCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public CinemasController(CinemasService cinemasService, CinemasCreateValidator cinemasCreateValidator, ModelMapper modelMapper) {
        this.cinemasService = cinemasService;
        this.cinemasCreateValidator = cinemasCreateValidator;
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
//
//    @GetMapping("/id={cinemaId}")
//    public ModelAndView detailsCinemaById(@PathVariable String cinemaId) {
//        CinemaServiceModel cinemaServiceModel = cinemasService.getCinemaById(cinemaId);
//        CinemaViewModel cinemaViewModel = modelMapper.map(cinemaServiceModel, CinemaViewModel.class);
//        return view(cinemaViewModel);
//    }
}
