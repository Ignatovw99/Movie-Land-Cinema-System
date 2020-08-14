package movieland.config;

import movieland.web.interceptors.AuthenticationRedirectInterceptor;
import movieland.web.interceptors.FaviconInterceptor;
import movieland.web.interceptors.PageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final PageInterceptor pageInterceptor;

    private final FaviconInterceptor faviconInterceptor;

    private final AuthenticationRedirectInterceptor authenticationRedirectInterceptor;

    @Autowired
    public WebMvcConfiguration(PageInterceptor pageInterceptor, FaviconInterceptor faviconInterceptor, AuthenticationRedirectInterceptor authenticationRedirectInterceptor) {
        this.pageInterceptor = pageInterceptor;
        this.faviconInterceptor = faviconInterceptor;
        this.authenticationRedirectInterceptor = authenticationRedirectInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageInterceptor);
        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(authenticationRedirectInterceptor);
    }
}
