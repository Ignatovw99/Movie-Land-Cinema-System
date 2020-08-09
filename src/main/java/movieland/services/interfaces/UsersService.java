package movieland.services.interfaces;

import movieland.domain.entities.projections.ProjectionBookingDetails;
import movieland.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    UserServiceModel register(UserServiceModel userServiceModel, String confirmPassword);

    void seedDatabaseWithAuthorities();

    List<UserServiceModel> findAll();

    void manageUserRole(String userId, boolean isDemotion);

    List<ProjectionBookingDetails> findAllProjectionBookingsByUserEmail(String userEmail);

    void createRootAdmin();
}
