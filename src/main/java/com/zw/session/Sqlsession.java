package com.zw.session;

import java.util.List;

public interface Sqlsession {
	<T> T selectOne(String statement, Object param);
	<T> List<T> selectList(String statement, Object param);
	<T> T getMapper(Class<T> type);
}
