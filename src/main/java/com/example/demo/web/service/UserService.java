package com.example.demo.web.service;

import com.example.demo.web.entity.User;

public interface UserService {
	User findUserByUserName(String userName);
}
