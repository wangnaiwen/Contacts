package com.wit.contacts.dao;

import android.database.sqlite.SQLiteDatabase;

import com.wit.contacts.bean.BlackList;
import com.wit.contacts.data.ContactDatabaseHelper;

/**
 * Created by wnw on 2016/9/20.
 */
public class BlackListDaoImp implements BlackListDao{

    private SQLiteDatabase mDatabase;

    public BlackListDaoImp(){
        mDatabase = ContactDatabaseHelper.getInstance();
    }

    @Override
    public void insertBlackList(BlackList blackList) {
        mDatabase.beginTransaction();
        String sql = "insert into contact_blacklist values(?,?,?,?)";
        Object object[] = new Object[]{null, blackList.getName(), blackList.getPhone(), blackList.getPosition()};
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
    public void deleteBlackList(int id) {
        mDatabase.beginTransaction();
        try{
            mDatabase.delete("contact_blacklist","id=?",new String[]{id+""});
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }
}
