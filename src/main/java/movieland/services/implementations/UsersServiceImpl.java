package movieland.services.implementations;

import movieland.services.interfaces.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static movieland.config.security.permissions.ApplicationUserRole.ADMIN;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return User.builder()
                .username("movie-land@gmail.com")
                .password(passwordEncoder.encode("encrypted_pass"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();
    }
}
