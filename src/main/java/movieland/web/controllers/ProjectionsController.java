package movieland.web.controllers;

import movieland.domain.models.binding.ProjectionAddBindingModel;
import movieland.domain.models.service.ProjectionAddServiceModel;
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
    public ModelAndView addProjection(ProjectionAddBindingModel projectionAddBindingModel) {
        return view(projectionAddBindingModel);
    }

    @PostMapping("/add")
    public ModelAndView addProjectionConfirm(@Valid @ModelAttribute ProjectionAddBindingModel projectionAddBindingModel, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return view("movies-add");
        }

        ProjectionAddServiceModel projectionAddServiceModel = modelMapper.map(projectionAddBindingModel, ProjectionAddServiceModel.class);

        projectionsService.addProjection(projectionAddServiceModel);

        return redirect("/");
    }
}
