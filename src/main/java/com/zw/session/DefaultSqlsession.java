package com.zw.session;

import java.lang.reflect.Proxy;
import java.util.List;

import com.zw.bind.MapperProxy;
import com.zw.config.Configuration;
import com.zw.config.MappedStatement;
import com.zw.executor.DefaultExecutor;
import com.zw.executor.Executor;

public class DefaultSqlsession implements Sqlsession {

	private Configuration conf;
	private Executor executor;
	
	public DefaultSqlsession(Configuration conf) {
		this.conf = conf;
		executor = new DefaultExecutor(conf);
	}
	
	@Override
	public <T> T selectOne(String statement, Object param) {
		List<T> result = this.selectList(statement, param);
		if (result == null || result.size() == 0) return null;
		
		if (result.size() > 1) {
			throw new RuntimeException("Too Many Results!");
		}

		return result.get(0);
	}

	@Override
	public <T> List<T> selectList(String statement, Object param) {
		MappedStatement ms = conf.getMappedStatements().get(statement);
		return executor.query(ms, param);
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		T result = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] {type}, new MapperProxy(this));
		return result;
	}

}
