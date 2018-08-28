package com.example.demo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseBean;
import com.example.demo.entity.User;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.UserService;

/**
 * 权限、角色的管理可以通过以下的注解方式实现，也可以在shiro配置文件中尽享配置。
 * 	如下写法更加灵活，可以设置登陆/非登陆的双页面。  统一配置 其实也可以实现，只要配置为游客模式即可。
 * @author lysss
 *
 */
@RestController("/require")
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    private UserService userService;

    @Autowired
    public void setService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/article")
    public ResponseBean article() {
    	logger.info("===================  article =====================");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ResponseBean(200, "You are already logged in", null);
        } else {
            return new ResponseBean(200, "You are guest", null);
        }
    }

    @GetMapping("/auth")
//    @RequiresAuthentication
    public ResponseBean requireAuth() {
    	logger.info("===================  require_auth =====================");
        return new ResponseBean(200, "You are authenticated", null);
    }

    @GetMapping("/role")
//    @RequiresRoles("admin")
    public ResponseBean requireRole() {
    	logger.info("===================  require_role =====================");
        return new ResponseBean(200, "You are visiting require_role", null);
    }

    @GetMapping("/permission")
//    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseBean requirePermission() {
        return new ResponseBean(200, "You are visiting permission require edit,view", null);
    }

}