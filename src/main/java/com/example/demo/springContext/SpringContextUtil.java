package com.example.demo.springContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.example.demo.annotation.SetValue;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;

/**
 * 获取Spring上下文
* 类描述：   
* 创建人：yang   
* 创建时间：2018-05-17 11:08:48   
* @version
 */
@Component
public class SpringContextUtil implements ApplicationContextAware  {

	 private static ApplicationContext applicationContext; // Spring应用上下文环境
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		 SpringContextUtil.applicationContext = applicationContext;
	}
	
	
	
	public static void setFieldValueForCollection(Collection col) {
		
		//该方法执行功能实例：
		/*for(Order order: col){
			User user=userMapper.find(order.getCustomerId());
			if(user!=null){
				order.setCustomerName(user.getUserName());
			}
		}*/
		// TODO Auto-generated method stub
		//1 判断目标是否是collection
		if(col.isEmpty()) return;
		//2. 获取到要执行方法的类   (获取要赋值对象的类)
		Class<?> beanClass=col.iterator().next().getClass();//获取Order类
		// 获取有注解的属性
		Field[] fields = beanClass.getDeclaredFields();
		
		for(Field field:fields){
			SetValue sv = field.getAnnotation(SetValue.class);
			if(sv==null){
				continue;
			}
			//获取annotation上传入的信息（有注解需要查询赋值的属性）
			//1. 要调用的bean  //通过注解上面传入的类，获取查询需要的bean
			Object setValueBean=applicationContext.getBean(sv.beanClass());
			try {
				//要调用的方法  （找到find方法）
				Method method=sv.beanClass().getMethod(sv.method(),
						beanClass.getDeclaredField(sv.paramField()).getType());//通过传入的类名，以及paramField的类型，获取需要的值调用的方法
				//获取给入参数的值的获取方法 (找到getCustomerId()方法)
				Method paramValueGetMethod = beanClass.getMethod("get"+StringUtils.capitalize(sv.paramField()));//capitalize（）转换成首字母大写
				//设置set的方法
				Method targetFieldSetMethod = beanClass.getMethod("set"+ StringUtils.capitalize(sv.targetField()),field.getType());
				//获取目标属性值的方法
				Method getTargetValeMethod=null;
				boolean needInnerField=StringUtils.isEmpty(sv.targetField());
				
				String keyPrefix=sv.beanClass().getName()+"-"+sv.method();
				
				//遍历对象
				for(Object obj:col){
					//获取到参数值 
					Object paramValue=paramValueGetMethod.invoke(obj);//获取customId的值
					if(paramValue==null){
						continue;
					}
					Object value=method.invoke(setValueBean, paramValue);//查询出User
					if(needInnerField){
						if(value!=null){
							if(getTargetValeMethod==null){
								getTargetValeMethod=value.getClass().getMethod("get"+StringUtils.capitalize(sv.targetField()));//获取getUserName()
							}
							value=getTargetValeMethod.invoke(value);
						}
					}
					//设置值
					targetFieldSetMethod.invoke(obj, value);
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
		}
		
		
		
	}

}
