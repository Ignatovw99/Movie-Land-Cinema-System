package movieland.services.interfaces;

import movieland.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

    UserServiceModel register(UserServiceModel userServiceModel, String confirmPassword);

    void seedDatabaseWithAuthorities();
}
