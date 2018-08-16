package com.example.demo.dao;

import com.example.demo.entity.User;

public interface UserMapper {
	User findByUserName(String customerId);
}
