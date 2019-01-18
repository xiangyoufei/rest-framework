package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class DynamicDataSource extends AbstractRoutingDataSource {
	
    /**
     * 默认数据源
     */
    public static final String DEFAULTDATASOURCE = "mainDataSource";
	
	// 默认数据源，也就是主库
    protected DataSource masterDataSource;
    // 保存动态创建的数据源
    public static final Map<String,DataSource> targetDataSource = new HashMap<>();
	
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected String determineCurrentLookupKey() {
        log.debug("数据源为{}", DataSourceContextHolder.getDB());

        return DataSourceContextHolder.getDB();
    }
    
    /**
     * 加载该方法决定使用的数据源
     */
    @Override
    protected DataSource determineTargetDataSource() {
        // 根据数据库选择方案，拿到要访问的数据库
        String dataSourceName = determineCurrentLookupKey();
        if (DEFAULTDATASOURCE.equals(dataSourceName)) {
            // 访问默认主库
            return masterDataSource;
        }
        // 根据数据库名字，从已创建的数据库中获取要访问的数据库
        DataSource dataSource =  targetDataSource.get(dataSourceName);
        if (null == dataSource) {
            System.out.println("dataSource==null");
            // 从已创建的数据库中获取要访问的数据库，如果没有则创建一个
            dataSource = this.selectDataSource(dataSourceName);
        }
        System.out.println("determineTargetDataSource---" + dataSourceName);
        return dataSource;
    }
    
    
    /**
     * 该方法为同步方法，防止并发创建两个相同的数据库 使用双检锁的方式，防止并发
     * 
     * @param dbType
     * @return
     */
    public synchronized DataSource selectDataSource(String dbType) {
        // 再次从数据库中获取，双检锁
        DataSource obj = targetDataSource.get(dbType);
        if (null != obj) {
            return obj;
        }
        // 为空则创建数据源
        DataSource dataSource = this.getDataSource(dbType);
        if (null != dataSource) {
            // 将新创建的数据库保存到map中
            this.setDataSource(dbType, dataSource);
            return dataSource;
        }
        return dataSource;
    }
    
    
    /**
	 * 查询对应数据库的信息
	 * 
	 * 这里是生成多数据源资源
	 * 
	 * @param dbtype
	 * @return
	 */
	public DataSource getDataSource(String dbtype) {
		return  null;
	}
 
    
    public void setDataSource(String type, DataSource dataSource) {
    	targetDataSource.put(type, dataSource);
        DataSourceContextHolder.setDB(type);
    }
  
 
 
    /*
     * @Override public void setTargetDataSources(Map targetDataSources) { //
     * TODO Auto-generated method stub
     * super.setTargetDataSources(targetDataSources); //
     * 重点：通知container容器数据源发生了变化 afterPropertiesSet(); }
     */
 
    /**
     * 该方法重写为空，因为AbstractRoutingDataSource类中会通过此方法将，
     * targetDataSources变量中保存的数据源交给resolvedDefaultDataSource变量
     * 在本方案中动态创建的数据源保存在了本类的targetDataSource变量中。如果不重写该方法为空，
     * 会因为targetDataSources变量为空报错
     * 如果仍然想要使用AbstractRoutingDataSource类中的变量保存数据源，则需要在每次数据源变更时，
     * 调用此方法来为resolvedDefaultDataSource变量更新
     */
    @Override
    public void afterPropertiesSet() {
    }
    
    public DataSource getMasterDataSource() {
        return masterDataSource;
    }
 
    public void setMasterDataSource(DataSource masterDataSource) {
        this.masterDataSource = masterDataSource;
    }
    

}