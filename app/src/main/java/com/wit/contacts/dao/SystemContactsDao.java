package com.wit.contacts.dao;

import com.wit.contacts.bean.SystemContacts;

import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public interface SystemContactsDao {
    void insertSystemContacts(SystemContacts contacts);
    List<SystemContacts> selectAllSystemContacts();
    void deleteAllSystemContacts();
}
