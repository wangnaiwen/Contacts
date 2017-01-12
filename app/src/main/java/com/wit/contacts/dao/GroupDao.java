package com.wit.contacts.dao;

import com.wit.contacts.bean.Group;

import java.util.List;

/**
 * Created by wnw on 2016/9/12.
 */
public interface GroupDao {
    void insertGroup(Group group);
    void deleteGroup(int id);
    void updateGroup(Group group);
    Group selectGroupById(int id);
    Group selectGroupByName(String name);
    List<Group> selectAllGroup();
    String selectGroupNameById(int id);
    void updateGroupName(String name, int id);
    int SelectFinalGroupId();
}
