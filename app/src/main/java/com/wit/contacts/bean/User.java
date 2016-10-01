package com.wit.contacts.bean;

/**
 * Created by wnw on 2016/9/7.
 */
public class User {
    private int id;
    private String name;
    private String phone;
    private String phoneMore;
    private String email;
    private String position;
    private int groupId;

    public User(){

    }

    public User(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public User(int id, String name, String phone, String phoneMore, String email, String position, int groupId){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phoneMore = phoneMore;
        this.email = email;
        this.position = position;
        this.groupId = groupId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneMore() {
        return phoneMore;
    }

    public void setPhoneMore(String phoneMore) {
        this.phoneMore = phoneMore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
