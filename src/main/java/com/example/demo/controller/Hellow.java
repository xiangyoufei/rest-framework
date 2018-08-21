package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Response;

@RestController
public class Hellow {

	
	@PostMapping("/hellow")
	public Response  sayHellow() {
		
		return new Response().success("成功向老大问候！");
	}
	
}
