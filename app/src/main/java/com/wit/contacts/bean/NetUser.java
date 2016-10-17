package com.wit.contacts.bean;

/**
 * Created by wnw on 2016/10/17.
 */

public class NetUser {
    private Integer id;
    private String name;
    private String phone;
    private String phone2;
    private String email;
    private String password;

    // Constructors

    /** default constructor */
    public NetUser() {
    }

    /** minimal constructor */
    public NetUser(String name, String phone, String password) {
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

    /** full constructor */
    public NetUser(String name, String phone, String phone2, String email,
                   String password) {
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.password = password;
    }

    // Property accessors

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return this.phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
