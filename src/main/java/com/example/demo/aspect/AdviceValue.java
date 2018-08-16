package com.example.demo.aspect;

import java.util.Collection;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.demo.springContext.SpringContextUtil;
/**
 * 为有@NeedSetValue的方法中实体  类上有@SetValue注解的字段通过调用传入的目标对象及method方法查询并赋值。
* 类描述：   
* 创建人：yang   
* 创建时间：2018-05-22 07:35:31   
* @version
 */
@Aspect
@Component
public class AdviceValue {
	
	
	@Around("@annotation(com.example.demo.annotation.NeedSetValue)")
	public Object setValue( ProceedingJoinPoint pjp) throws Throwable{
		String methodName=pjp.getSignature().getName();
		
		//执行目标方法
		Object ret=pjp.proceed();
		
		//当传入的是一个集合，即集合中的元素都需要这种查询赋值
		if(ret instanceof Collection){
			SpringContextUtil.setFieldValueForCollection((Collection)ret);
		}else if(ret instanceof Map){//当传入的是一个map，即map中的元素都需要这种查询赋值
			setFieldValueForMap((Map)ret);
		}
		return pjp;
	}

	private void setFieldValueForMap(Map ret) {
		// TODO Auto-generated method stub
		
	}

	
}
