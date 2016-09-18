package com.wit.contacts.dao;

import com.wit.contacts.bean.User;

import java.util.List;

/**
 * Created by wnw on 2016/9/12.
 */
public interface UserDao {
    void insertUser(User user);
    void deleteUser(int id);
    void updateUser(User user);
    User selectUserById(int id);
    User selectUserByName(String name);
    List<User> selectUserByGroupId(int groupId);
    List<User> selectAllUser();
}
