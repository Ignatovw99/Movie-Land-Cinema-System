package movieland.services.implementations;

import movieland.config.security.permissions.ApplicationUserRole;
import movieland.domain.entities.User;
import movieland.domain.entities.UserAuthority;
import movieland.domain.entities.projections.ProjectionBookingDetails;
import movieland.domain.models.service.UserServiceModel;
import movieland.errors.exceptions.UserNotValidException;
import movieland.errors.notfound.UserNotFoundException;
import movieland.repositories.ProjectionsRepository;
import movieland.repositories.UserAuthoritiesRepository;
import movieland.repositories.UsersRepository;
import movieland.services.interfaces.UsersService;
import movieland.services.validation.UsersValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static movieland.config.security.permissions.ApplicationUserRole.ADMIN;
import static movieland.config.security.permissions.ApplicationUserRole.USER;
import static movieland.constants.entities.UserConstants.*;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final UserAuthoritiesRepository userAuthoritiesRepository;

    private final ProjectionsRepository projectionsRepository;

    private final UsersValidationService usersValidationService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserAuthoritiesRepository userAuthoritiesRepository, ProjectionsRepository projectionsRepository, UsersValidationService usersValidationService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.userAuthoritiesRepository = userAuthoritiesRepository;
        this.projectionsRepository = projectionsRepository;
        this.usersValidationService = usersValidationService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userCandidate = usersRepository.findByUsername(username);

        if (userCandidate.isEmpty()) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return userCandidate.get();
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel, String confirmPassword) {
        if (!usersValidationService.isValid(userServiceModel)) {
            throw new UserNotValidException(USER_NOT_VALID);
        }
        if (usersRepository.existsByUsername(userServiceModel.getEmail())) {
            throw new UserNotValidException(EMAIL_EXISTS);
        }
        if (!userServiceModel.getPassword().equals(confirmPassword)) {
            throw new UserNotValidException(CONFIRM_PASS_NOT_VALID);
        }

        userServiceModel.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        User userEntity = modelMapper.map(userServiceModel, User.class);
        assignRoleAndAuthoritiesToUser(userEntity, USER);
        userEntity = usersRepository.save(userEntity);

        return modelMapper.map(userEntity, UserServiceModel.class);
    }

    @Async
    @Override
    public void seedDatabaseWithAuthorities() {

        Arrays.stream(ApplicationUserRole.values())
                .forEach(applicationRole -> applicationRole.getGrantedAuthorities()
                        .forEach(applicationAuthority -> {
                            String authority = applicationAuthority.getAuthority();
                            if (userAuthoritiesRepository.existsByAuthority(authority)) {
                                return;
                            }
                            boolean isRole = authority.startsWith("ROLE_");
                            UserAuthority userAuthority = new UserAuthority(authority, isRole);
                            userAuthoritiesRepository.saveAndFlush(userAuthority);
                        })
                );
    }

    @Override
    public List<UserServiceModel> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void manageUserRole(String userId, boolean isDemotion) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        //TODO: exact to security class somewhere !!!!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getName().equals(user.getUsername())) {
            throw new UserNotValidException(SELF_MODIFYING_NOT_ALLOWED);
        }

        UserAuthority userCurrentRole = userAuthoritiesRepository.findByIsRoleIsTrueAndUsersEquals(user);
        String roleAsString = userCurrentRole.getAuthority().substring(userCurrentRole.getAuthority().indexOf('_') + 1);

        if (userCurrentRole.getAuthority().equals(ApplicationUserRole.ROOT_ADMIN.getRole())) {
            throw new UserNotValidException(ROOT_ADMIN_NOT_UPDATABLE);
        }

        ApplicationUserRole[] availableRoles = ApplicationUserRole.values();
        List<ApplicationUserRole> userRoles = Arrays.asList(availableRoles);
        int roleIndex = ApplicationUserRole.valueOf(roleAsString).ordinal();

        if (isDemotion) {
            Collections.reverse(userRoles);
            roleIndex = userRoles.size() - roleIndex;
        }

        ApplicationUserRole newRole = null;

        for (int i = roleIndex + 1; i < userRoles.size(); i++) {
            ApplicationUserRole role = userRoles.get(i);
            if (!role.equals(USER) && !role.equals(ADMIN)) {
                continue;
            }
            newRole = role;
        }

        if (newRole == null) {
            return;
        }

        user.setAuthorities(new HashSet<>());
        usersRepository.saveAndFlush(user);

        assignRoleAndAuthoritiesToUser(user, newRole);

        usersRepository.saveAndFlush(user);
    }

    @Override
    public List<ProjectionBookingDetails> findAllProjectionBookingsByUserEmail(String userEmail) {
        return projectionsRepository.findAllProjectionBookingsByUser(userEmail)
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    private void assignRoleAndAuthoritiesToUser(User user, ApplicationUserRole role) {
        role.getGrantedAuthorities()
                .forEach(grantedAuthority -> {
                    UserAuthority authority = userAuthoritiesRepository.findByAuthority(grantedAuthority.getAuthority());
                    if (authority == null) {
                        return;
                    }
                    user.addAuthority(authority);
                });
    }
}
