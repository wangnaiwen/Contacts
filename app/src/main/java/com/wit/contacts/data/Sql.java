package com.wit.contacts.data;

/**
 * Created by wnw on 2016/9/12.
 */
public class Sql {

    public static final String USER = "create table user(" +
            "id integer," +
            "name text not null," +
            "phone text not null," +
            "phone2 text," +
            "email text," +
            "password text not null)";

    public static final String CREATE_GROUP = "create table contact_group(" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
            "userid integer constraint user_id_fk references user(id) on delete cascade)";

    public static final String CREATE_USER = "create table contact_user(" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
                    "phone text not null," +
                    "phonemore text," +
                    "email text," +
                    "position text," +
                    "groupid integer constraint group_id_fk references contact_group(id) on delete cascade)";

    public static final String CREATE_BLACKLIST = "create table contact_blacklist(" +
            "id integer primary key autoincrement," +
            "name text not null," +
            "phone text not null," +
            "phonemore text," +
            "email text," +
            "position text)";

    public static final String CREATE_SYSTEM_CONTACTS = "create table system_contacts(" +
            "id integer primary key autoincrement, "+
            "name text not null,"+
            "phone text not null,"+
            "phonemore text,"+
            "email text)";
}
