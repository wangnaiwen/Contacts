package com.wit.contacts.bean;

/**
 * Created by wnw on 2016/9/20.
 */
public class BlackList {
    private int id;
    private String name;
    private String phone;
    private String position;

    public BlackList(){

    }

    public BlackList(int id, String name, String phone, String position){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.position = position;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
