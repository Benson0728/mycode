package com.grade.config;

import com.grade.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自定义的鉴权拦截器，并指定拦截的路径
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/students/**","/teachers/**");
    }
}
