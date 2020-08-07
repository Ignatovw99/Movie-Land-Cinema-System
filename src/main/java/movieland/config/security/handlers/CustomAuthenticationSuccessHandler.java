package movieland.config.security.handlers;

import movieland.constants.GlobalConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object redirectUrl = request.getSession().getAttribute(GlobalConstants.LOGIN_REDIRECT_URL);
        if (redirectUrl == null) {
            response.sendRedirect("/");
            return;
        }
        response.sendRedirect(redirectUrl.toString());
        request.getSession().removeAttribute(GlobalConstants.LOGIN_REDIRECT_URL);
    }
}
