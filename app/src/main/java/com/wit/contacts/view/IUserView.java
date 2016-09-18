package com.wit.contacts.view;

import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;

import java.util.List;

/**
 * Created by wnw on 2016/9/7.
 */
public interface IUserView {
    /**
     * 显示进度条
     * */
    void showLoading();

    /**
     * 显示数据
     * */
    void showUsers(List<Group> groups);
}
