package com.doanhuuquang.thoikhoabieuvnua.service;

import com.doanhuuquang.thoikhoabieuvnua.model.User;

public interface UserService {
	public User login(String studentCode, String password);
	public User register(String studentCode, String password);
}
