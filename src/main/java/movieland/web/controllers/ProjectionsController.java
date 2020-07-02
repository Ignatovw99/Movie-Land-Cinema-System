package movieland.web.controllers;

import movieland.domain.models.binding.projection.ProjectionCreateBindingModel;
import movieland.domain.models.service.ProjectionServiceModel;
import movieland.services.interfaces.ProjectionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/projections")
public class ProjectionsController extends BaseController {

    private final ProjectionsService projectionsService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectionsController(ProjectionsService projectionsService, ModelMapper modelMapper) {
        this.projectionsService = projectionsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView addProjection(ProjectionCreateBindingModel projectionCreateBindingModel) {
        return view(projectionCreateBindingModel);
    }

    @PostMapping("/add")
    public ModelAndView addProjectionConfirm(@Valid @ModelAttribute ProjectionCreateBindingModel projectionCreateBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return view("movies-add");
        }

        ProjectionServiceModel projectionServiceModel = modelMapper.map(projectionCreateBindingModel, ProjectionServiceModel.class);

        projectionsService.create(projectionServiceModel);

        return redirect("/");
    }
}
