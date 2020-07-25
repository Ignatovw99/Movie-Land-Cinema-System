package movieland.services.implementations;

import movieland.config.security.permissions.ApplicationUserRole;
import movieland.domain.entities.UserAuthority;
import movieland.repositories.UserAuthoritiesRepository;
import movieland.services.interfaces.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

import static movieland.config.security.permissions.ApplicationUserRole.ADMIN;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

    private final UserAuthoritiesRepository userAuthoritiesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersServiceImpl(UserAuthoritiesRepository userAuthoritiesRepository) {
        this.userAuthoritiesRepository = userAuthoritiesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return User.builder()
                .username("movie-land@gmail.com")
                .password(passwordEncoder.encode("encrypted_pass"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
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
