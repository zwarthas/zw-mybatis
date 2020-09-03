package com.zw.executor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zw.config.Configuration;
import com.zw.config.MappedStatement;
import com.zw.util.ReflectionUtil;

public class DefaultExecutor implements Executor {
	
	private final Configuration conf;
	
	public DefaultExecutor(Configuration conf) {
		this.conf = conf;
	}
	
	
	@Override
	public <T> List<T> query(MappedStatement mappedStatement, Object param) {
		List<T> list = new ArrayList<>();
		try {
			Class.forName(conf.getJdbcDriver());//加载jdbc驱动
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			// 获取数据库连接
			connection = DriverManager.getConnection(conf.getJdbcUrl(), conf.getJdbcUsername(), conf.getJdbcPassword());
			// 获取sql语句
			preparedStatement = connection.prepareStatement(mappedStatement.getSql());
			
			// 处理sql语句中占位符
			parameterize(preparedStatement, param);
			System.out.println("sql:" + preparedStatement.toString());
			// 查询结果
			resultSet = preparedStatement.executeQuery();
			// 通过反射把结果转为对象
			handleResultSet(resultSet, list, mappedStatement.getResultType());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				connection.close();
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	// 处理结果，转为对象添加到list中
	private <E> void handleResultSet(ResultSet resultSet, List<E> list, String className) {
		Class<E> clazz = null;
		try {
			clazz = (Class<E>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while (resultSet.next()) {
				Object o = clazz.newInstance();
				ReflectionUtil.setPropToBeanFromResultSet(o, resultSet);
				list.add((E)o);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void parameterize(PreparedStatement prepareStatement, Object parameter) throws SQLException {
		if(parameter instanceof Integer){
			prepareStatement.setInt(1,(int)parameter);
		}else if(parameter instanceof Long){
			prepareStatement.setLong(1, (long)parameter);
		}else if(parameter instanceof String){
			prepareStatement.setString(1, (String)parameter);
		}
	}

}
