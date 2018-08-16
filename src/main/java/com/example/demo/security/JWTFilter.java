package com.example.demo.security;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Jwts;

/**
 * 用于检查 token 的拦截器
 *
 * @author huangyong
 * @since 1.0.0
 */
/**jwt  检测*/
public class JWTFilter extends FormAuthenticationFilter   {

	private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

	
	@Resource
    private JwtUtils jwtUtils;

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		  String token = getRequestToken((HttpServletRequest) request);

		//判断请求的请求头是否带上 "Token"
        try {
			if (token != null) {
				/**单纯使用jwt的方式进行检验*/
//				Jwts.parser().setSigningKey(jwtUtils.getSecret()).parseClaimsJws(token).getBody();
				/**结合shiro的方式进行检验*/ 
				//如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
				 executeLogin(request, response);

			}
		} catch ( Exception e) {
			
			logger.error(" jwt token check faild "+e.getMessage());
			
			throw new TokenException(" jwt token check faild "+e.getMessage());
		} 
      //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true

		return false;


	}
	
	 @Override
	    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
	        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	        String token = httpServletRequest.getHeader("Token");
//	        JWTToken jwtToken = new JWTToken(token);
//	        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
//	        getSubject(request, response).login(jwtToken);
	        // 如果没有抛出异常则代表登入成功，返回true
	        return true;
	    }
	
	 /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader(jwtUtils.getHeader());
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            return httpRequest.getParameter(jwtUtils.getHeader());
        }
        /*if (StringUtils.isBlank(token)) {
            // 从 cookie 获取 token
            Cookie[] cookies = httpRequest.getCookies();
            if (null == cookies || cookies.length == 0) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtUtils.getHeader())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }*/
        return token;
    }

}
