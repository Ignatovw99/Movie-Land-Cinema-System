package movieland.web.controllers;

import movieland.domain.models.binding.projection.ProjectionCreateBindingModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.services.interfaces.ProjectionsService;
import movieland.validation.projection.ProjectionsCreateValidator;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static movieland.constants.GlobalConstants.MODEL_NAME;

@Controller
@RequestMapping("/projections")
public class ProjectionsController extends BaseController {

    private final ProjectionsService projectionsService;

    private final ProjectionsCreateValidator projectionsCreateValidator;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectionsController(ProjectionsService projectionsService, ProjectionsCreateValidator projectionsCreateValidator, ModelMapper modelMapper) {
        this.projectionsService = projectionsService;
        this.projectionsCreateValidator = projectionsCreateValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @Page(title = "Create Projection", name = "projection/projection-create")
    public ModelAndView createProjection(ProjectionCreateBindingModel projectionCreateBindingModel) {
        return view(projectionCreateBindingModel);
    }

    @PostMapping("/create")
    public ModelAndView createProjectionConfirm(@ModelAttribute(MODEL_NAME) ProjectionCreateBindingModel projectionCreateBindingModel, BindingResult bindingResult) {
        projectionsCreateValidator.validate(projectionCreateBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("projection/projection-create");
        }

        ProjectionServiceModel projectionServiceModel = modelMapper.map(projectionCreateBindingModel, ProjectionServiceModel.class);
        projectionsService.create(projectionServiceModel);

        return redirect("/");
    }
}
