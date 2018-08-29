package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages="com.example.demo")
@ServletComponentScan
@MapperScan("com.example.demo.dao")//类文件的位置
@EnableTransactionManagement//开启mybatis事务
public class RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}
}
