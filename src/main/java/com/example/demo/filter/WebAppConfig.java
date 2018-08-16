package com.example.demo.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.security.SecurityInterceptor;


/** 
 * 
 * 注册拦截器 
 * 
 */  
@Configuration
public class WebAppConfig implements WebMvcConfigurer {  
  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        //注册自定义拦截器，添加拦截路径和排除拦截路径  
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/*")/*.excludePathPatterns("/login*")*/;  
    }  
}