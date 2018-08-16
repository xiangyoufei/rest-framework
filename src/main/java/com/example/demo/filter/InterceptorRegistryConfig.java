package com.example.demo.filter;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.security.JWTFilter;


/** 
 * 
 * 注册拦截器 
 * 
 */  
//@Configuration
public class InterceptorRegistryConfig implements WebMvcConfigurer {  
//  
//    @Override  
//    public void addInterceptors(InterceptorRegistry registry) {  
//        //注册自定义拦截器，添加拦截路径和排除拦截路径  
//        registry.addInterceptor(new JWTFilter()).addPathPatterns("/*")/*.excludePathPatterns("/login*")*/;  
//    }  
}