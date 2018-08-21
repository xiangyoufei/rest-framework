package com.example.demo;

import org.junit.Test;

import com.example.demo.entity.User;
import com.example.demo.security.jwt.JwtUtils;

public class JWTTest {
	
	@Test
	public void jwtTest() {
		String username="lysss";
		String password="0203";
		String token = JwtUtils.sign(username, password);
		User user=new User();
		user.setPassword(password);
		user.setUsername(username);
		boolean verify = JwtUtils.verify(token, username, password);
		System.out.println(verify);
	}

}
