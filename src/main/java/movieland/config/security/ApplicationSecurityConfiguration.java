package movieland.config.security;

import movieland.config.security.handlers.CustomAuthenticationFailureHandler;
import movieland.config.security.handlers.CustomAuthenticationSuccessHandler;
import movieland.services.interfaces.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import static movieland.config.security.permissions.ApplicationUserPermission.SEAT_BOOKING;
import static movieland.config.security.permissions.ApplicationUserRole.ADMIN;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UsersService usersService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(UsersService usersService, PasswordEncoder passwordEncoder) {
        this.usersService = usersService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .disable()
                .csrf()
//                    .csrfTokenRepository(csrfTokenRepository())
                .disable()
//                .and()
                //TODO secure endpoints
                .authorizeRequests()
                    .antMatchers("/css/*", "/js/*", "/images/*").permitAll()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login", "/register").anonymous()

                    .antMatchers("/api/cinemas/all").permitAll()

                    .antMatchers("/programmes/cinema/{id}", "/api/programmes/cinema/{id}/date/{date}").permitAll()

                    .antMatchers("/projections/{id}", "/api/projections/{id}/seats").permitAll()

                    .antMatchers("/users/bookings").authenticated()

                    .antMatchers("/api/projections/seats/booking").hasAuthority(SEAT_BOOKING.getPermission())

                    .antMatchers("/**").hasRole(ADMIN.name())
                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureHandler(customAuthenticationFailureHandler())
                    .usernameParameter("email")
                .and()
                .rememberMe()
                    .key("REMEMBERMEKEY")
                    .userDetailsService(usersService)
                    .rememberMeCookieName("REMEMBER-ME-COOKIE")
                    .rememberMeParameter("rememberMe")
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7))
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "REMEMBER-ME-COOKIE")
                    .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/error/unauthorized") // TODO create an custom page for unauthorized requests
                    .authenticationEntryPoint(delegatingAuthenticationEntryPoint());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(usersService);
        return authProvider;
    }

    @Bean
    public DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint() {
        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
        entryPoints.put(new AntPathRequestMatcher("/api/**"), new Http403ForbiddenEntryPoint());
        DelegatingAuthenticationEntryPoint defaultEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        defaultEntryPoint.setDefaultEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        return defaultEntryPoint;
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
