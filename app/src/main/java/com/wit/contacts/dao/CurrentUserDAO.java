package com.wit.contacts.dao;


import com.wit.contacts.bean.CurrentUser;

/**
 * Created by wnw on 2017/1/1.
 */

public interface CurrentUserDAO {
    void insertUser(CurrentUser user);
    void deleteUser();
    CurrentUser selectCurrentUser();
}
