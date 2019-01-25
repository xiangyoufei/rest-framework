package com.example.demo.jdbc;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.example.demo.config.DataSourceContextHolder;
import com.example.demo.config.DynamicDataSource;
import com.example.demo.util.PropsUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据库连接上下文
 *
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class ConnectionContext {



    
    
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
