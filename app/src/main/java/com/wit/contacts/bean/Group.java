package com.wit.contacts.bean;

import java.util.List;

/**
 * Created by wnw on 2016/9/7.
 */
public class Group {

    private int id;
    private String name;
    private List<User> userList;

    public Group(){

    }

    public Group(String name, List<User> users){
        this.name = name;
        this.userList = users;
    }

    public Group(int id, String name, List<User> users){
        this.id = id;
        this.name = name;
        this.userList = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
