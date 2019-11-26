package com.example.demo.web.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置拦截器
 * 
 * @author 
 *这样也可以配置过滤器，但是现在有更加简单的方法，使用注解@WebFilter
 */
@Configuration
public class FilterConfig {
	/**
	 * 过滤器注册
	 * 
	 * @return
	 */
//	@Bean
	public FilterRegistrationBean<CorsFilter> CorsFilter() {
		FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<CorsFilter>();
		registration.setFilter(new CorsFilter());
		registration.addUrlPatterns("/*");// 拦截路径
		registration.setName("CorsFilter");// 拦截器名称
		registration.setOrder(1);// 顺序
//		registration.addInitParameter(name, value);
		return registration;
	}
	
	@Bean
	public FilterRegistrationBean<WebContextFilter> WebContextFilter() {
		FilterRegistrationBean<WebContextFilter> registration = new FilterRegistrationBean<WebContextFilter>();
		registration.setFilter(new  WebContextFilter());
		registration.addUrlPatterns("/*");// 拦截路径
		registration.setName("WebContextFilter");// 拦截器名称
		registration.setOrder(1);// 顺序
		return registration;
	}

	

}	