package com.example.demo.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.example.demo.util.PropsUtil;

/**
 * 数据库连接上下文
 *
 * @since 1.0.0
 */
//@Configuration
public class ConnectionContext {

    private static Logger logger = LoggerFactory.getLogger(ConnectionContext.class);


    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
    
    @Resource
    private static  DataSource dataSource;
    public static Connection connect() {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                logger.error("get connection failure", e);
                throw new RuntimeException(e);
            }
            if (connection != null) {
                connectionHolder.set(connection);
            }
        }
        return connection;
    }

    public static void release() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("release connection failure", e);
                throw new RuntimeException(e);
            }
            connectionHolder.remove();
        }
    }
}
