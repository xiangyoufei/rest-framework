package com.example.demo.core.aspect;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.util.JwtTokenProvider;

import io.jsonwebtoken.Claims;


/**
 * 登陆验证,在需要进行登陆验证的方法上添加@AuthChecker注解
 * @author lysss
 *
 */
@Component
@Aspect
public class HttpAopAdviseDefine {
	
	
	@Resource
	private JwtTokenProvider jwtProvider;

    // 定义一个 Pointcut, 使用 切点表达式函数 来描述对哪些 Join point 使用 advise.
    @Pointcut("@annotation(com.example.demo.core.annotation.AuthChecker)")
    public void pointcut() {
    }

    // 定义 advise
    @Around("pointcut()")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        // 检查用户所传递的 token 是否合法
        String token = getUserToken(request);
        if (!token.equalsIgnoreCase("123456")) {
            return "错误, 权限不合法!";
        }

        return joinPoint.proceed();
    }

    private String getUserToken(HttpServletRequest request) {
    	
    	String token=request.getHeader("AuthChecker");
    	Claims claims=jwtProvider.parseToken(token);
    	
    	
    	/*
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("user_token")) {
                return cookie.getValue();
            }
        }*/
        return "";
    }
}