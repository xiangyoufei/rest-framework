package com.example.demo.web.dao;

import com.example.demo.web.entity.User;

public interface UserMapper {
	User findByUserName(String customerId);
}
