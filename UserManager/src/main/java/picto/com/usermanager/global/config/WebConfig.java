package picto.com.usermanager.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import picto.com.usermanager.global.JwtAuthInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String[] INTERCEPTOR_LOCATIONS = {
            "/signup/**",
            "/signin/**",
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JwtAuthInterceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/signup", "/signin");
    }
}
