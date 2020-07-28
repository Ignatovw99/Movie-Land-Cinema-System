package movieland.services.implementations;

import movieland.config.security.permissions.ApplicationUserRole;
import movieland.domain.entities.User;
import movieland.domain.entities.UserAuthority;
import movieland.domain.models.service.UserServiceModel;
import movieland.errors.exceptions.UserNotValidException;
import movieland.repositories.UserAuthoritiesRepository;
import movieland.repositories.UsersRepository;
import movieland.services.interfaces.UsersService;
import movieland.services.validation.UsersValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static movieland.config.security.permissions.ApplicationUserRole.USER;
import static movieland.constants.entities.UserConstants.*;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    private final UserAuthoritiesRepository userAuthoritiesRepository;

    private final UsersValidationService usersValidationService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserAuthoritiesRepository userAuthoritiesRepository, UsersValidationService usersValidationService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usersRepository = usersRepository;
        this.userAuthoritiesRepository = userAuthoritiesRepository;
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

        UserAuthority userRole = userAuthoritiesRepository.findByAuthority(USER.getRole());

        User userEntity = modelMapper.map(userServiceModel, User.class);
        userEntity.setAuthorities(new HashSet<>(List.of(userRole)));
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
}
