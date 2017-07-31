package com.ynu.healthyfriendv05.service;

import android.content.Context;

import com.ynu.healthyfriendv05.dao.UserDAO;
import com.ynu.healthyfriendv05.po.User;

public class UserService {
	private UserDAO userDAO;

	public UserService(Context context) {
		this.userDAO = new UserDAO(context);
	}

	public boolean isExist() {
		return userDAO.checkUser();
	}

	public void register(User user) {
		userDAO.add(user);
	}

	public void updateInform(User user) {
		userDAO.modify(user);
	}

	public User findUser() {
		return userDAO.selectAll();
	}

	public void closeDB() {
		userDAO.closeDBHelper();
	}
}
