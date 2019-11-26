package com.example.demo.core.springContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring上下文
 * 类描述：
 * 创建人：yang
 * 创建时间：2018-05-17 11:08:48
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext; // Spring应用上下文环境

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        SpringContextUtil.applicationContext = applicationContext;
    }


}
