package com.wit.contacts.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wit.contacts.bean.CurrentUser;
import com.wit.contacts.bean.User;
import com.wit.contacts.data.ContactDatabaseHelper;

/**
 * Created by wnw on 2017/1/1.
 */

public class CurrentUserImpl implements CurrentUserDAO {
    private SQLiteDatabase mDatabase;

    public CurrentUserImpl(){
        mDatabase = ContactDatabaseHelper.getInstance();
    }

    @Override
    public void insertUser(CurrentUser user) {
        mDatabase.beginTransaction();
        String sql = "insert into user values(?,?,?,?,?,?)";
        Object object[] = new Object[]{user.getId(), user.getName(), user.getPhone(),user.getPhone2(),
                user.getEmail(),  user.getPassword()};
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
    public void deleteUser() {
        mDatabase.beginTransaction();
        int id = 0;
        try{
            Cursor cursor = mDatabase.query("user",null, null,null, null, null, null);
            if(cursor.moveToFirst()){
                id = cursor.getInt(cursor.getColumnIndex("id"));
            }
            cursor.close();
            mDatabase.delete("user","id=?",new String[]{id+""});
            mDatabase.delete("contact_group",null,null);
            mDatabase.delete("contact_user",null,null);
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public CurrentUser selectCurrentUser() {
        CurrentUser user = null;
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("user",null, null, null,null, null, null);
            if(cursor.moveToFirst()){
                user = new CurrentUser();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setName(cursor.getString(cursor.getColumnIndex("name")));
                user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                user.setPhone2(cursor.getString(cursor.getColumnIndex("phone2")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
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
}
