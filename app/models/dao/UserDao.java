package models.dao;

import models.User;

public interface UserDao {
	public boolean addUser(String userName, String profile);
	public User getUser(String userName);
}
