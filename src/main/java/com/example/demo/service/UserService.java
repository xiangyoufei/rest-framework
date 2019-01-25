package com.example.demo.service;

import com.example.demo.core.entity.User;

public interface UserService {
	User findUserByUserName(String userName);
}
