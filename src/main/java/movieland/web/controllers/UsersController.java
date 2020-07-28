package movieland.web.controllers;

import movieland.domain.models.binding.user.UserRegisterBindingModel;
import movieland.domain.models.service.UserServiceModel;
import movieland.services.interfaces.UsersService;
import movieland.validation.user.UsersRegistrationValidator;
import movieland.web.annotations.Page;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static movieland.constants.GlobalConstants.MODEL_NAME;

@Controller
public class UsersController extends BaseController {

    private final UsersService usersService;

    private final UsersRegistrationValidator usersRegistrationValidator;

    private final ModelMapper modelMapper;

    public UsersController(UsersService usersService, UsersRegistrationValidator usersRegistrationValidator, ModelMapper modelMapper) {
        this.usersService = usersService;
        this.usersRegistrationValidator = usersRegistrationValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @Page(title = "Register", name = "user/user-register")
    public ModelAndView registerUser(UserRegisterBindingModel userRegisterBindingModel) {
        return view(userRegisterBindingModel);
    }

    @PostMapping("/register")
    public ModelAndView registerUserConfirm(@ModelAttribute(MODEL_NAME) UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        usersRegistrationValidator.validate(userRegisterBindingModel, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("user/user-register");
        }

        UserServiceModel userToRegister = modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        usersService.register(userToRegister, userRegisterBindingModel.getConfirmPassword());

        return redirect("/login");
    }
}
