package com.wit.contacts.model;

import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/7.
 */
public class UserModelImp implements IUserModel {

    private List<Group> groupList ;
    private GroupDao groupDao ;
    private UserDao userDao ;

    public UserModelImp(){
        groupList = new ArrayList<>();
        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();
    }

    @Override
    public void loadUser(UserLoadingListener loadingListener) {

        //在这里从数据库获取数据
        List<Group> groups = groupDao.selectAllGroup();
        for(int i = 0; i < groups.size();i++){
            Group group = new Group();
            List<User> users = userDao.selectUserByGroupId(groups.get(i).getId());
            group.setName(groups.get(i).getName());
            group.setUserList(users);
            groupList.add(group);
        }

        if(loadingListener != null){
            loadingListener.complete(groupList);
        }
    }
}