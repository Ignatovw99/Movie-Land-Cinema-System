package movieland.web.controllers;

import movieland.domain.models.binding.hall.HallCreateBindingModel;
import movieland.domain.models.binding.hall.HallUpdateBindingModel;
import movieland.domain.models.service.HallServiceModel;
import movieland.domain.models.view.hall.HallViewModel;
import movieland.services.interfaces.HallsService;
import movieland.validation.hall.HallsCreateValidator;
import movieland.validation.hall.HallsUpdateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/halls")
public class HallsController extends BaseController {

    private final HallsService hallsService;

    private final HallsCreateValidator hallsCreateValidator;

    private final HallsUpdateValidator hallsUpdateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public HallsController(HallsService hallsService, HallsCreateValidator hallsCreateValidator, HallsUpdateValidator hallsUpdateValidator, ModelMapper modelMapper) {
        this.hallsService = hallsService;
        this.hallsCreateValidator = hallsCreateValidator;
        this.hallsUpdateValidator = hallsUpdateValidator;
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

    @GetMapping("/update/{id}")
    public ModelAndView updateHall(@PathVariable String id) {
        HallServiceModel hallServiceModel = hallsService.findById(id);
        HallUpdateBindingModel hallToUpdate = modelMapper.map(hallServiceModel, HallUpdateBindingModel.class);
        return view("hall/hall-update", hallToUpdate);
    }

    @PostMapping("/update")
    public ModelAndView updateHallConfirm(@ModelAttribute(name = "model") HallUpdateBindingModel hallUpdateBindingModel, BindingResult bindingResult) {
        hallsUpdateValidator.validate(hallUpdateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("hall/hall-update");
        }

        HallServiceModel hallToUpdateServiceModel = modelMapper.map(hallUpdateBindingModel, HallServiceModel.class);
        hallsService.update(hallToUpdateServiceModel.getId(), hallToUpdateServiceModel);
        return redirect("/");
    }

    //TODO: add delete endpoint

    @GetMapping("/all")
    public ModelAndView getAllHalls() {
        List<HallViewModel> allHalls = hallsService.findAll()
                .stream()
                .map(hallServiceModel -> modelMapper.map(hallServiceModel, HallViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return view("hall/halls-all", allHalls);
    }
}
