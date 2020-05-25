package movieland.web.controllers;

import movieland.domain.models.binding.HallCreateBindingModel;
import movieland.domain.models.service.HallServiceModel;
import movieland.services.interfaces.HallsService;
import movieland.validation.hall.HallsCreateValidator;
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
@RequestMapping("/halls")
public class HallsController extends BaseController {

    private final HallsService hallsService;

    private final HallsCreateValidator hallsCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public HallsController(HallsService hallsService, HallsCreateValidator hallsCreateValidator, ModelMapper modelMapper) {
        this.hallsService = hallsService;
        this.hallsCreateValidator = hallsCreateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    public ModelAndView createHall(HallCreateBindingModel hallCreateBindingModel) {
        return view("hall/hall-create", hallCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createHallConfirm(@ModelAttribute(name = "model") HallCreateBindingModel hallCreateBindingModel, BindingResult bindingResult) {
        hallsCreateValidator.validate(hallCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("hall/hall-create");
        }

        HallServiceModel hallServiceModel = modelMapper.map(hallCreateBindingModel, HallServiceModel.class);
        hallsService.create(hallServiceModel);

        return redirect("/");
    }
}
