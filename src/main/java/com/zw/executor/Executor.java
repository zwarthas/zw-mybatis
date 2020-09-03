package com.zw.executor;

import java.util.List;

import com.zw.config.MappedStatement;

public interface Executor {
	<T> List<T> query(MappedStatement mappedStatement, Object param);
}
