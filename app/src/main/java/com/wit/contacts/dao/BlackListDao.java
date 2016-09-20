package com.wit.contacts.dao;

import com.wit.contacts.bean.BlackList;

/**
 * Created by wnw on 2016/9/20.
 */
public interface BlackListDao {
    void insertBlackList(BlackList blackList);
    void deleteBlackList(int id);
}
