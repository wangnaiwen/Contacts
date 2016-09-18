package com.wit.contacts.data;

/**
 * Created by wnw on 2016/9/12.
 */
public class Sql {

    public static final String CREATE_GROUP = "create table contact_group(" +
                    "id integer primary key autoincrement," +
                    "name text not null)";

    public static final String CREATE_USER = "create table contact_user(" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
                    "phone text not null," +
                    "position text," +
                    "groupid integer constraint group_id_fk references contact_group(id) on delete cascade)";
}
