package com.wit.contacts.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wit.contacts.bean.Group;
import com.wit.contacts.data.ContactDatabaseHelper;
import com.wit.contacts.model.IUserModel;
import com.wit.contacts.model.UserModelImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/9/12.
 */
public class GroupDaoImp implements GroupDao{

    private SQLiteDatabase mDatabase;

    public GroupDaoImp(){
        mDatabase = ContactDatabaseHelper.getInstance();
    }

    @Override
    public void insertGroup(Group group) {
        mDatabase.beginTransaction();
        String sql = "insert into contact_group values(?,?)";
        Object object[] = new Object[]{null, group.getName()};
        try{
            mDatabase.execSQL(sql,object);
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void deleteGroup(int id) {
        mDatabase.beginTransaction();
        try{
            mDatabase.delete("contact_group","id=?",new String[]{id+""});
            mDatabase.setTransactionSuccessful();   //set successful and insert to db

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public void updateGroup(Group group) {
        mDatabase.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("name", group.getName());
            mDatabase.update("contact_group",values,"name=?",new String[]{group.getName()});
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }

    @Override
    public Group selectGroupById(int id) {
        Group group = new Group();
            mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_group",null,"id=?",new String[]{id+""}, null, null,null);
            if(cursor.moveToFirst()){
                do{
                    group.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    group.setName(cursor.getString(cursor.getColumnIndex("name")));
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            mDatabase.endTransaction();
        }
        return group;
    }

    @Override
    public Group selectGroupByName(String name) {
        Group group = new Group();
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_group", null, "name=?", new String[]{name}, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    group.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    group.setName(cursor.getString(cursor.getColumnIndex("name")));
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return group;
    }

    @Override
    public List<Group> selectAllGroup() {
        List<Group> groupList = new ArrayList<>();
        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_group",null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    Group group = new Group();
                    group.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    group.setName(cursor.getString(cursor.getColumnIndex("name")));
                    groupList.add(group);
                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return groupList;
    }

    @Override
    public String selectGroupNameById(int id) {
        String groupName ="";

        mDatabase.beginTransaction();
        try{
            Cursor cursor = mDatabase.query("contact_group",null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    Group group = new Group();
                    group.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    group.setName(cursor.getString(cursor.getColumnIndex("name")));

                }while (cursor.moveToNext());
            }
            cursor.close();
            mDatabase.setTransactionSuccessful();   //set successful and insert to db
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return groupName;
    }
}
