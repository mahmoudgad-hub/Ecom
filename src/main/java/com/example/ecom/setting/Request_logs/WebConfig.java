package com.example.ecom.setting.Request_logs;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestLoggingInterceptor requestLoggingInterceptor;

    public WebConfig(RequestLoggingInterceptor requestLoggingInterceptor) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor)
                .addPathPatterns("/**"); // على كل الـ endpoints
//                .addPathPatterns("/auth/**") // على كل الـ endpoints
//                .addPathPatterns("/admin/**") // على كل الـ endpoints
//                .excludePathPatterns("/data/**");
    }
}
