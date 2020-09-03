package com.zw.test;

import java.util.List;

import com.zw.entity.User;
import com.zw.mapper.UserMapper;
import com.zw.session.Sqlsession;
import com.zw.session.SqlsessionFactory;

public class TestMyBatis {
	public static void main(String[] args) {
		SqlsessionFactory factory = new SqlsessionFactory();
		Sqlsession sqlsession = factory.openSession();
		UserMapper userMapper = sqlsession.getMapper(UserMapper.class);
		User user = userMapper.selectUserByUserId(1);
		System.out.println(user);
		
		List<User> users = userMapper.selectAllUsers();
		for (User u : users) {
			System.out.println(u);
		}
	}
}
