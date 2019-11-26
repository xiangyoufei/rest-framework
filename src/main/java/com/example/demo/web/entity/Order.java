package com.example.demo.web.entity;

import java.io.Serializable;

import com.example.demo.core.annotation.SetValue;
import com.example.demo.web.dao.UserMapper;

/**
 * 该类的customerName属性可能需要根据customerId查询,
* 类描述：   
* 创建人：yang   
* 创建时间：2018-05-24 07:20:31   
* @version
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6461439256832865451L;
	
	private String id;
	
	private String customerId;
	
	@SetValue(beanClass=UserMapper.class,method="find",paramField="customerId",targetField="userName")
	private String customerName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Order [id=" + id + ", customerId=" + customerId + ", customerName=" + customerName + "]";
	}
	
	
}
