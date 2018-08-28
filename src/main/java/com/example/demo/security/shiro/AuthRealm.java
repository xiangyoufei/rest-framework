package com.example.demo.security.shiro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.entity.Module;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.security.jwt.JWTToken;
import com.example.demo.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	
    @Autowired
    private UserService userService;
    
    private static final Logger logger=LoggerFactory.getLogger(AuthRealm.class);
    
    
    
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
    	
//        return token instanceof JWTToken;
    	return super.supports(token);
    }


    
    //认证.登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	logger.info("==========shiro Authentication  认证 ");
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;//获取用户输入的token
        String username = utoken.getUsername();
        User user = userService.findUserByUserName(username);
        if(user==null) {
        	 throw new AccountException("用户名不正确");
        }else if(!user.getPassword().equals(new String((char[]) token.getCredentials()))) {
        	throw new AccountException("用户名或密码不正确");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(),this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
    }
    //授权 doGetAuthorizationInfo 方法只有在需要权限认证时才会进去
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
    	logger.info("==========AuthorizationInfo 授权");
        User user=(User) principal.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
        List<String> permissions=new ArrayList<>();
        Set<Role> roles = user.getRoles();
        Set<String> rolesName=new HashSet<>();
        if(roles.size()>0) {
            for(Role role : roles) {
            	rolesName.add(role.getRname());
                Set<Module> modules = role.getModules();
                if(modules.size()>0) {
                    for(Module module : modules) {
                        permissions.add(module.getMname());
                    }
                }
            }
        }
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);//将权限放入shiro中.
        info.addRoles(rolesName);
        return info;
    }

}