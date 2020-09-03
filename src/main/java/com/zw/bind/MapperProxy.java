package com.zw.bind;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;

import com.zw.session.Sqlsession;

public class MapperProxy implements InvocationHandler {

	private Sqlsession sqlsession;

	public MapperProxy(Sqlsession sqlsession) {
		super();
		this.sqlsession = sqlsession;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Class<?> clazz = method.getReturnType();
		if (!Collection.class.isAssignableFrom(clazz)) {
			return sqlsession.selectOne(method.getDeclaringClass().getName()+"."+method.getName(), args == null ? null : args[0]);
		} else {
			return sqlsession.selectList(method.getDeclaringClass().getName()+"."+method.getName(), args == null ? null : args[0]);
		}
	}

}
