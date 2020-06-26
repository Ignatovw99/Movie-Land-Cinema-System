package movieland.web.interceptors;

import movieland.web.annotations.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static movieland.constants.GlobalConstants.*;

@Component
public class PageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            Method requestMethod = ((HandlerMethod) handler).getMethod();
            if (requestMethod.isAnnotationPresent(Page.class) && requestMethod.isAnnotationPresent(GetMapping.class)) {
                Page requestMethodPageMetadata = requestMethod.getAnnotation(Page.class);
                String pageTitle = requestMethodPageMetadata.title();
                String pageName = requestMethodPageMetadata.name();

                if (pageTitle.isEmpty()) {
                    modelAndView.addObject(PAGE_TITLE_KEY, APPLICATION_DEFAULT_TITLE_PAGE);
                } else {
                    modelAndView.addObject(PAGE_TITLE_KEY, APPLICATION_DEFAULT_TITLE_PAGE + " - " + pageTitle);
                }

                modelAndView.setViewName(BASE_VIEW_TEMPLATE);
                modelAndView.addObject(VIEW_TEMPLATE_KEY, pageName);
            }
        }
    }
}
