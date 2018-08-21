package com.example.demo.security.jwt;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.security.TokenException;


/**
 * 用于检查 token 的拦截器
 *
 * @author huangyong
 * @since 1.0.0
 */
/**jwt  检测*/
// 代码执行顺序 : preHandle->isAccessAllowed->isLoginAttempt->executeLogin
public class JWTFilter extends BasicHttpAuthenticationFilter   {

	private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);


	@Resource
	private JwtUtils jwtUtils;

	/**
	 * 判断用户是否想要登入。
	 * 检测header里面是否包含Authorization字段即可
	 */
	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("token");
		return token != null;
	}


	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

		//		String token = getRequestToken((HttpServletRequest) request);
		//判断请求的请求头是否带上 "Token"
		try {
			if (isLoginAttempt(request, response)) {
				executeLogin(request, response);
			}
		} catch ( Exception e) {

			logger.error(" jwt token check faild "+e.getMessage());
//			response401(request, response);
			throw new TokenException(" jwt token check faild "+e.getMessage());
		} 
		//如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true

		return true;


	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader("token");

		JWTToken jwtToken = new JWTToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;

	}

	/**
	 * 将非法请求跳转到 /401
	 */
	private void response401(ServletRequest req, ServletResponse resp) {
		try {
			HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
			httpServletResponse.sendRedirect("/401");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}


