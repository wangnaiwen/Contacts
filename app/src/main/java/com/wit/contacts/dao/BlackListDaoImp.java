package com.wit.contacts.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wit.contacts.bean.BlackList;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.data.ContactDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
        String sql = "insert into contact_blacklist values(?,?,?,?,?,?)";
        Object object[] = new Object[]{null, blackList.getName(), blackList.getPhone(),
                blackList.getPhoneMore(), blackList.getEmail(), blackList.getPosition()};
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
    public List<BlackList> selectAllBlackList() {
        mDatabase.beginTransaction();
        List<BlackList> blackLists = new ArrayList<>();
        try{
            Cursor cursor = mDatabase.query("contact_blacklist",null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    BlackList blackList = new BlackList();
                    blackList.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    blackList.setName(cursor.getString(cursor.getColumnIndex("name")));
                    blackList.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    blackList.setPhoneMore(cursor.getString(cursor.getColumnIndex("phonemore")));
                    blackList.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    blackList.setPosition(cursor.getString(cursor.getColumnIndex("position")));
                    blackLists.add(blackList);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return blackLists;
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
