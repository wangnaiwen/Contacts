package com.wit.contacts.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wit.contacts.bean.User;
import com.wit.contacts.data.ContactDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by wnw on 2016/9/12.
 */

public class UserDaoImp implements UserDao {

    private SQLiteDatabase mDatabase;

    public UserDaoImp(){
        mDatabase = ContactDatabaseHelper.getInstance();
    }

    @Override
    public void insertUser(User user) {
        mDatabase.beginTransaction();
        String sql = "insert into user values(?,?,?,?,?)";
        Object object[] = new Object[]{null, user.getName(), user.getPhone(), user.getPosition(), user.getGroupId()};
        try{
            mDatabase.execSQL(sql, object);
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void deleteUser(int id) {
        mDatabase.beginTransaction();
        try{
            mDatabase.delete("contact_user","id=?",new String[]{id+""});
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void updateUser(User user) {
        mDatabase.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("id", user.getId());
            values.put("name", user.getName());
            values.put("phone", user.getPhone());
            values.put("position", user.getPosition());
            values.put("groupid", user.getGroupId());
            mDatabase.update("contact_user",values,"id=?",new String[]{user.getId()+""});
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public User selectUserById(int id) {
        User user = new User();
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_user",null, "id=?", new String[]{id+""},null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setName(cursor.getString(cursor.getColumnIndex("name")));
                    user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    user.setPosition(cursor.getString(cursor.getColumnIndex("position")));
                    user.setGroupId(cursor.getInt(cursor.getColumnIndex("groupid")));
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return user;
    }

    @Override
    public User selectUserByName(String name) {
        User user = new User();
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_user",null, "name=?", new String[]{name},null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setName(cursor.getString(cursor.getColumnIndex("name")));
                    user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    user.setPosition(cursor.getString(cursor.getColumnIndex("position")));
                    user.setGroupId(cursor.getInt(cursor.getColumnIndex("groupid")));
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return user;
    }

    @Override
    public List<User> selectUserByGroupId(int groupId) {
        List<User> userList = new ArrayList<>();

        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_user",null, "groupid=?", new String[]{groupId+""},null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setName(cursor.getString(cursor.getColumnIndex("name")));
                    user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    user.setPosition(cursor.getString(cursor.getColumnIndex("position")));
                    user.setGroupId(cursor.getInt(cursor.getColumnIndex("groupid")));
                    userList.add(user);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return userList;
    }

    @Override
    public List<User> selectAllUser() {
        List<User> userList = new ArrayList<>();
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_user",null, null, null, null, null, null, null);
            if(cursor.moveToFirst()){
                do{
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setName(cursor.getString(cursor.getColumnIndex("name")));
                    user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    user.setPosition(cursor.getString(cursor.getColumnIndex("position")));
                    user.setGroupId(cursor.getInt(cursor.getColumnIndex("groupid")));
                    userList.add(user);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return userList;
    }
}
