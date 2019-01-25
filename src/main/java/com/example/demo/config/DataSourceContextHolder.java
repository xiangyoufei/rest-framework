package com.example.demo.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {
    public static final Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);



    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    // 设置数据源名
    public static void setDB(String dbType) {
        log.debug("切换到{}数据源", dbType);
        contextHolder.set(dbType);
    }

    // 获取数据源名
    public static String getDB() {
        return (contextHolder.get());
    }

    // 清除数据源名
    public static void clearDB() {
        contextHolder.remove();
    }
    
    
   public static Connection connect() {
    	
    	String dbName = DataSourceContextHolder.getDB();
    	DataSource dataSource = DynamicDataSource.targetDataSource.get(dbName);
    	Connection connection = null ;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
       
        return connection;
    }

    public static void release() {
    	String dbName = DataSourceContextHolder.getDB();
    	DataSource dataSource = DynamicDataSource.targetDataSource.get(dbName);
    	Connection connection;
		try {
			connection = dataSource.getConnection();
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("release connection failure", e);
					throw new RuntimeException(e);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
			DynamicDataSource.targetDataSource.remove(dbName);
			DataSourceContextHolder.clearDB();
		}
    }
    
    
}
