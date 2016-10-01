package com.wit.contacts.bean;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContacts {
    private int id;
    private String name;
    private String phone;
    private String phoneMore;
    private String email;

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
}
