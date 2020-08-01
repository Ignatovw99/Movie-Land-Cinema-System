package movieland.web.controllers;

import movieland.domain.models.service.UserServiceModel;
import movieland.domain.models.view.user.UserViewModel;
import movieland.services.interfaces.UsersService;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private final UsersService usersService;

    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UsersService usersService, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users/all")
    @Page(title = "All Users", name = "user/users-all")
    public ModelAndView getAllUsers() {
        List<UserServiceModel> userServiceModels = usersService.findAll();

        List<UserViewModel> userViewModels = userServiceModels.stream()
                .map(userServiceModel -> modelMapper.map(userServiceModel, UserViewModel.class))
                .collect(Collectors.toUnmodifiableList());

        return view(userViewModels);
    }
}
