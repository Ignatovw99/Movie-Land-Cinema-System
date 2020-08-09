package movieland.web.controllers;

import movieland.domain.models.binding.programme.ProgrammeCreateBindingModel;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.domain.models.view.programme.ProgrammeViewModel;
import movieland.services.interfaces.ProgrammesService;
import movieland.validation.programme.ProgrammesCreateValidator;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

import static movieland.constants.GlobalConstants.MODEL_NAME;

@Controller
@RequestMapping("/programmes")
public class ProgrammesController extends BaseController {

    private final ProgrammesService programmesService;

    private final ProgrammesCreateValidator programmesCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public ProgrammesController(ProgrammesService programmesService, ProgrammesCreateValidator programmesCreateValidator, ModelMapper modelMapper) {
        this.programmesService = programmesService;
        this.programmesCreateValidator = programmesCreateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @Page(title = "Create Programme", name = "programme/programme-create")
    public ModelAndView createProgramme(ProgrammeCreateBindingModel programmeCreateBindingModel) {
        return view(programmeCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createProgrammeConfirm(@ModelAttribute(MODEL_NAME) ProgrammeCreateBindingModel programmeCreateBindingModel, BindingResult bindingResult) {
        programmesCreateValidator.validate(programmeCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("programme/programme-create");
        }

        ProgrammeServiceModel programmeServiceModel = modelMapper.map(programmeCreateBindingModel, ProgrammeServiceModel.class);
        ProgrammeServiceModel createdProgrammeServiceModel = programmesService.createNext(programmeServiceModel);

        return redirect("/programmes/cinema/" + createdProgrammeServiceModel.getCinema().getId());
    }

    @GetMapping("/cinema/{id}")
    @Page(title = "Programme", name = "programme/programme-view")
    public ModelAndView showProgramme(@PathVariable("id") String cinemaId) {
        ProgrammeServiceModel programmeServiceModel = programmesService.getProgrammeByCinemaIdAndDate(cinemaId, LocalDate.now());
        ProgrammeViewModel programmeViewModel = modelMapper.map(programmeServiceModel, ProgrammeViewModel.class);
        return view(programmeViewModel);
    }
}
