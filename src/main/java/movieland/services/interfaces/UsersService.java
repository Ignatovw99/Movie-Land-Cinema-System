package movieland.services.interfaces;

import movieland.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    UserServiceModel register(UserServiceModel userServiceModel, String confirmPassword);

    void seedDatabaseWithAuthorities();

    List<UserServiceModel> findAll();
}
