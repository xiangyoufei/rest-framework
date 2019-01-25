package com.example.demo.dao;

import com.example.demo.core.entity.User;

public interface UserMapper {
	User findByUserName(String customerId);
}
