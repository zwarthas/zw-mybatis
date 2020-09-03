package com.zw.util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReflectionUtil {
	
	// 为特定属性赋值
	public static void setPropToBean(Object bean, String prop, Object value) {
		try {
			Field f = bean.getClass().getDeclaredField(prop);
			f.setAccessible(true);
			f.set(bean, value);
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 为所有属性赋值
	public static void setPropToBeanFromResultSet(Object bean, ResultSet resultSet) throws SQLException {
		Field[] declaredFields = bean.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getType().getSimpleName().equals("String")) {
				setPropToBean(bean, field.getName(), resultSet.getString(field.getName()));
			} else if (field.getType().getSimpleName().equals("Integer")) {
				setPropToBean(bean, field.getName(), resultSet.getInt(field.getName()));
			} else if (field.getType().getSimpleName().equals("Long")) {
				setPropToBean(bean, field.getName(), resultSet.getLong(field.getName()));
			} 
		}
	}
}
