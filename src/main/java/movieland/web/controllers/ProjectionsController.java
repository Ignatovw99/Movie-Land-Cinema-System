package movieland.web.controllers;

import movieland.domain.models.binding.projection.ProjectionCreateBindingModel;
import movieland.domain.models.service.CinemaServiceModel;
import movieland.domain.models.service.ProgrammeServiceModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.domain.models.view.projection.ProjectionViewModel;
import movieland.services.interfaces.ProjectionsService;
import movieland.validation.projection.ProjectionsCreateValidator;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        CinemaServiceModel projectionCinemaServiceModel = new CinemaServiceModel();
        projectionCinemaServiceModel.setId(projectionCreateBindingModel.getCinemaId());
        projectionServiceModel.setProgramme(new ProgrammeServiceModel());
        projectionServiceModel.getProgramme().setCinema(projectionCinemaServiceModel);

        projectionsService.create(projectionServiceModel);

        return redirect("/");
    }

    @GetMapping("/{id}")
    @Page(title = "View Projection", name = "projection/projection-view")
    public ModelAndView viewProjection(@PathVariable String id) {
        ProjectionServiceModel projectionServiceModel = projectionsService.findById(id);
        ProjectionViewModel projectionViewModel = modelMapper.map(projectionServiceModel, ProjectionViewModel.class);
        return view(projectionViewModel);
    }
}
