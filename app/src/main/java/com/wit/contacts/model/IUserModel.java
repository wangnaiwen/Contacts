package com.wit.contacts.model;

import com.wit.contacts.bean.Group;

import java.util.List;

/**
 * Created by wnw on 2016/9/7.
 */

public interface IUserModel {

   /**
    * 加载数据
    * */
    void loadUser(UserLoadingListener loadingListener);

    /**
     * 加载数据完成的回调
     * */
    interface UserLoadingListener{
        void complete(List<Group> groups);
    }

    /**
     * insert the group
     * */
    void insertGroup(String groupName);

    /**
     * update the group name
     * */
    void updateGroupName(String name, int groupId);
}
