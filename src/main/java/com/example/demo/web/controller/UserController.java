package com.example.demo.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.dto.Response;

@RestController
@RequestMapping("/user")
public class UserController{
   

    @PostMapping("/getMessage")
    public Response getMessage() {
        return new Response().success("您拥有用户权限，可以获得该接口的信息！");
    }
}