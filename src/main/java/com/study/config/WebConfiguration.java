package com.study.config;

import com.study.interceptor.IdempotencyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public IdempotencyInterceptor myInterceptor() {
        return new IdempotencyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor())
                // 拦截的请求
                .addPathPatterns("/**");
        // 不用拦截的请求
        //.excludePathPatterns("/login");
    }
}
