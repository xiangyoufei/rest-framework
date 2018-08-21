package com.example.demo.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.aspect.IgnoreSecurity;
import com.example.demo.dto.Response;
import com.example.demo.entity.User;

//@RestController
public class LoginController {
	//	//密匙 12345678
	//	JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("12345678");
	private static final Logger logger=LoggerFactory.getLogger(LoginController.class);
	
	@Autowired  
	HttpServletRequest request;
	@Autowired
	HttpSession session;


	@PostMapping(value="/login" )
	@IgnoreSecurity
	public Response login(String username, String password,String vcode,Boolean rememberMe){
		System.out.println(username);
		UsernamePasswordToken token = new UsernamePasswordToken(username, password,rememberMe);
		SecurityUtils.getSubject().login(token);

		return new Response().success("loginSuccess");
	}


	@PostMapping("/loginUser")
	@IgnoreSecurity
	public Response loginUser(String username,String password) {
		UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password);
		//        HttpServletRequest request=HttpCont
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(usernamePasswordToken);   //完成登录
			User user=(User) subject.getPrincipal();
			session.setAttribute("user", user);
			return new Response().success("loginSuccess");
		} catch(Exception e) {
			return new Response().success("loginFaild");//返回登录页面
		}

	}
	@PostMapping("/logOut")
	public Response logOut(HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		session.removeAttribute("user");
		return new Response().success("logOutSuccess");
	}


}
