package movieland.web.controllers;

import movieland.domain.models.binding.HallAddBindingModel;
import movieland.domain.models.service.HallServiceModel;
import movieland.services.interfaces.HallsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/halls")
public class HallsController extends BaseController {

    private final HallsService hallsService;

    private final ModelMapper modelMapper;

    public HallsController(HallsService hallsService, ModelMapper modelMapper) {
        this.hallsService = hallsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView addHall(HallAddBindingModel hallAddBindingModel) {
        hallAddBindingModel.setCinemaId("fc57c655-175d-46ee-8d28-9ef9a512affc");
        return view(hallAddBindingModel);
    }

    @PostMapping("/add")
    public ModelAndView addHallConfirm(@ModelAttribute HallAddBindingModel hallAddBindingModel, ModelAndView modelAndView) {
        this.hallsService.createHall(
                this.modelMapper.map(hallAddBindingModel, HallServiceModel.class)
        );
        return redirect("/");
    }
}
