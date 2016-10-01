package com.wit.contacts.dao;

import com.wit.contacts.bean.BlackList;

import java.util.List;

/**
 * Created by wnw on 2016/9/20.
 */
public interface BlackListDao {
    void insertBlackList(BlackList blackList);
    List<BlackList> selectAllBlackList();
    void deleteBlackList(int id);
}
