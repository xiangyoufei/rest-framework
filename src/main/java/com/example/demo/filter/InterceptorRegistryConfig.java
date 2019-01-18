package com.example.demo.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


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
//        registry.addInterceptor(new JWTFilter()).addPathPatterns("/*")/*.excludePathPatterns("/login*")*/;  
    	registry.addInterceptor(new HandlerInterceptor() {
    		
    		@Override
    		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    				throws Exception {
    			// TODO Auto-generated method stub
    			return HandlerInterceptor.super.preHandle(request, response, handler);
    		}
    		
    		@Override
    		public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
    				ModelAndView modelAndView) throws Exception {
    			// TODO Auto-generated method stub
    			HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    		}
    		
    		@Override
    		public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
    				Exception ex) throws Exception {
    			// TODO Auto-generated method stub
    			HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    		}
    		
		});
    }  
}