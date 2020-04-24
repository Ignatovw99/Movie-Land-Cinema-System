package movieland.web.controllers;

import movieland.services.interfaces.CinemasService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cinemas")
public class CinemasController extends BaseController {

    private final CinemasService cinemasService;

    private final ModelMapper modelMapper;

    public CinemasController(CinemasService cinemasService, ModelMapper modelMapper) {
        this.cinemasService = cinemasService;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/add")
//    public ModelAndView addCinema(CinemaAddBindingModel cinemaAddBindingModel) {
//        return view(cinemaAddBindingModel);
//    }
//
//    @PostMapping("/add")
//    public ModelAndView addCinemaConfirm(@ModelAttribute CinemaAddBindingModel cinemaAddBindingModel, BindingResult bindingResult, ModelAndView modelAndView) {
//        if (bindingResult.hasErrors()) {
//            return view("cinemas-add");
//        }
//
//        CinemaServiceModel cinemaServiceModel = this.modelMapper.map(cinemaAddBindingModel, CinemaServiceModel.class);
//        //TODO: WHEN ADDING EXCEPTION HANDLING, CHECK IF THE APPROPRIATE ERROR PAGE IS RETURNED
//        this.cinemasService.createCinema(cinemaServiceModel);
//
//        return redirect("/");
//    }
//
//    @GetMapping("/id={cinemaId}")
//    public ModelAndView detailsCinemaById(@PathVariable String cinemaId) {
//        CinemaServiceModel cinemaServiceModel = cinemasService.getCinemaById(cinemaId);
//        CinemaViewModel cinemaViewModel = modelMapper.map(cinemaServiceModel, CinemaViewModel.class);
//        return view(cinemaViewModel);
//    }
}
