package com.example.demo.security.jwt;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.web.entity.Module;
import com.example.demo.web.entity.Role;
import com.example.demo.web.entity.User;
import com.example.demo.web.service.UserService;

@Component(value="jwtRealm")
public class JWTRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;

	private static final Logger logger=LoggerFactory.getLogger(JWTRealm.class);



	/**
	 * 大坑！，必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}



	//认证.登录
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
		logger.info("==========shiro Authentication  认证 ");
		//        UsernamePasswordToken utoken=(UsernamePasswordToken) token;//获取用户输入的token
		//        String username = utoken.getUsername();
		String token = (String) auth.getCredentials();
		String username = JwtUtils.getUsername(token);
		if (username == null) {
			throw new AuthenticationException("token invalid");
		}

		User user = userService.findUserByUserName(username);
		if(user==null) {
			throw new AccountException("用户名不正确");
		}
		if (! JwtUtils.verify(token, username, user.getPassword())) {
			throw new AuthenticationException("Username or password error");
		}

		//TODO 此处没有重写SimpleAuthenticationInfo ，因为jwt中不能存储密码等关键信息。所以密码的比较器传入的都是token。
		return new SimpleAuthenticationInfo(token, token,this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
	}

	//授权 doGetAuthorizationInfo 方法只有在需要权限认证时才会进去
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		logger.info("==========AuthorizationInfo 授权");
		//        User user=(User) principal.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户

		String username = JwtUtils.getUsername(principal.toString());
		User user = userService.findUserByUserName(username);

		//TODO  此处用list还是set呢？
//		List<String> permissions=new ArrayList<>();
		Set<String> permissions=new HashSet<>();
		Set<Role> roles = user.getRoles();
		Set<String> rolesName = new HashSet<>();
		if(roles.size()>0) {
			for(Role role : roles) {
				Set<Module> modules = role.getModules();
				if(modules.size()>0) {
					for(Module module : modules) {
						permissions.add(module.getMname());
					}
				}
				rolesName.add(role.getRname());
			}
		}
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		info.addStringPermissions(permissions);//将权限放入shiro中.
		info.addRoles(rolesName);
		return info;
	}

}