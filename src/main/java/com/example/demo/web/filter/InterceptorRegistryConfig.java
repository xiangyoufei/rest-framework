package com.example.demo.web.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


/** 
 * 
 * 注册拦截器 
 * 
 */  
@Configuration
public class InterceptorRegistryConfig implements WebMvcConfigurer {  
  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        //注册自定义拦截器，添加拦截路径和排除拦截路径  
//        registry.addInterceptor(new JWTFilter())
//    	.addPathPatterns("/*")
//    	.excludePathPatterns("/login*");  
    	//注册国际化标注拦截器
    	registry.addInterceptor(new MessageResourceInterceptor())
    	.addPathPatterns("/**");
//    	.excludePathPatterns("/login*");
    	registry.addInterceptor(new LocaleChangeInterceptor()).
    	addPathPatterns("/**");
    }  
}