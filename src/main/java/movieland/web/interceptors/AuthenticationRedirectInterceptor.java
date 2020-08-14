package movieland.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static movieland.constants.GlobalConstants.LOGIN_REDIRECT_URL;

@Component
public class AuthenticationRedirectInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            Method requestMethod = ((HandlerMethod) handler).getMethod();
            Class<?> controllerClass = requestMethod.getDeclaringClass();

            //RESTful request should not be stored in session
            if (requestMethod.isAnnotationPresent(GetMapping.class) && controllerClass.isAnnotationPresent(Controller.class) && !request.getRequestURI().equals("/login") && !request.getRequestURI().equals("/register")) {
                String authRedirectUrl = request.getRequestURI() + (request.getQueryString() == null ? "" : request.getQueryString());
                request.getSession().setAttribute(LOGIN_REDIRECT_URL, authRedirectUrl);
            }
        }
    }
}
