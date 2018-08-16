package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解添加在需要补充 对象属性的方法上面，使用aop增强。
* 类描述：   
* 创建人：yang   
* 创建时间：2018-05-17 10:52:02   
* @version
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedSetValue {

}
