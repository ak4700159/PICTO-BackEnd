package picto.com.usermanager.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import picto.com.usermanager.global.JwtAuthInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtAuthInterceptor jwtAuthInterceptor;

    private String[] INTERCEPTOR_LOCATIONS = {
            "/user-manager/signup/**",
            "/user-manager/signin/**",
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 인터셉터 생성
        registry.addInterceptor(jwtAuthInterceptor)
                .order(1) // 첫번째로 실행될 인터셉터
                .addPathPatterns() // 인터셉터할 경로 지정
                .excludePathPatterns("/user-manager/signup", "/user-manager/signin"); // 그 중에서 제외하는 경로
    }
}
