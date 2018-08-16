package com.example.demo.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;


@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Resource
	private UserMapper userDao;

	@Override
	public User findUserByUserName(String userName) {
		User user = null;
		try {
			user=userDao.findByUserName(userName);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" findUserByUserName Exception : "+e.getMessage());
		}
		return user;
	}

}
