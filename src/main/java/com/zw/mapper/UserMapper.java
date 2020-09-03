package com.zw.mapper;

import java.util.List;

import com.zw.entity.User;

public interface UserMapper {
	User selectUserByUserId(int userId);
	List<User> selectAllUsers();
}
