package com.example.demo.security.shiro;

import java.util.LinkedHashMap;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfiguration {
	/**应该是使用shiro的过滤器*/
	//anon  无参，开放权限，可以理解为匿名用户或游客
	//authc  无参，需要认证
	//perms[user]  参数可写多个，表示需要某个或某些权限才能通过，多个参数时写 perms["user, admin"]，当有多个参数时必须每个参数都通过才算通过
	//roles[admin] 参数可写多个，表示是某个或某些角色才能通过，多个参数时写 roles["admin，user"]，当有多个参数时必须每个参数都通过才算通过
	//rest[user] 根据请求的方法，相当于 perms[user:method]，其中 method 为 post，get，delete 等
	@Bean(name="shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
		ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
		bean.setSecurityManager(manager);
		// 配置登录的url和登录成功的url 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
//		bean.setLoginUrl("/login");
//		bean.setSuccessUrl("/home");
		// 设置无权限时跳转的 url;
		bean.setUnauthorizedUrl("/401");
		//配置访问权限
		LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
		//用户，需要角色权限 “user”
		filterChainDefinitionMap.put("/user/**", "roles[user]");
		//管理员，需要角色权限 “admin”
		filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//		filterChainDefinitionMap.put("/jsp/login.jsp*", "anon"); //表示可以匿名访问
		filterChainDefinitionMap.put("/loginUser", "anon"); 
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/logout*","anon");
		filterChainDefinitionMap.put("/require_permission","perms[view, edit,test]");
		filterChainDefinitionMap.put("/require_role","roles[admin]");
//		filterChainDefinitionMap.put("/jsp/error.jsp*","anon");
//		filterChainDefinitionMap.put("/jsp/index.jsp*","authc");
		//以下表示除了上面所有的，都需要权限，只能放在最后。
		filterChainDefinitionMap.put("/*", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
		filterChainDefinitionMap.put("/*.*", "authc");
		bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return bean;
	}
	//配置核心安全事务管理器
	@Bean(name="securityManager")
	public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm) {
		DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
		manager.setRealm(authRealm);
		return manager;
	}
	//配置自定义的权限登录器
	@Bean(name="authRealm")
	public AuthRealm authRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
		AuthRealm authRealm=new AuthRealm();
		authRealm.setCredentialsMatcher(matcher);
		return authRealm;
	}
	//配置自定义的密码比较器
	@Bean(name="credentialsMatcher")
	public CredentialsMatcher credentialsMatcher() {
		return new CredentialsMatcher();
	}
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
		AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(manager);
		return advisor;
	}
}
