package com.example.demo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)  
@Retention(RetentionPolicy.RUNTIME)
public @interface SetValue {
	//要执行的方法所属类名
	Class<?> beanClass();
	//需要执行的方法
	String method();
	//输入的值
	String paramField();
	//输出的值
	String targetField();
}
