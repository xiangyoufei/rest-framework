package com.example.demo.security.jwt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class JWTCredentialsMatcher extends SimpleCredentialsMatcher{

	
	/**
	 * token  存储的是JWT token
	 */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
//        String inPassword = (String)token.getCredentials();
    	JWTToken jtoken=new JWTToken((String)token.getCredentials());
    	String inPassword =(String)jtoken.getCredentials();
        //获得数据库中的密码
        String dbPassword=(String) info.getCredentials();
        //进行密码的比对
        return this.equals(inPassword, dbPassword);
    }
    
}