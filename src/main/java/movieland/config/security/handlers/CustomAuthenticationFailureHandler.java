package movieland.config.security.handlers;

import movieland.constants.GlobalConstants;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        request.getSession().setAttribute(GlobalConstants.IS_AUTHENTICATION_FAILED, true);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/login");
    }
}