package com.wit.contacts.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wit.contacts.bean.SystemContacts;
import com.wit.contacts.data.ContactDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/1.
 */

public class SystemContactsDapImp implements SystemContactsDao{

    private SQLiteDatabase mDatabase;

    public SystemContactsDapImp(){
        mDatabase = ContactDatabaseHelper.getInstance();
    }

    @Override
    public void insertSystemContacts(SystemContacts contacts) {
        mDatabase.beginTransaction();
        String sql = "insert into system_contacts values(?,?,?,?,?)";
        Object object[] = new Object[]{null, contacts.getName(), contacts.getPhone(),
                contacts.getPhoneMore(), contacts.getEmail()};
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
    public List<SystemContacts> selectAllSystemContacts() {
        mDatabase.beginTransaction();
        List<SystemContacts> systemContactsArrayList = new ArrayList<>();
        try{
            Cursor cursor = mDatabase.query("system_contacts",null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    SystemContacts contacts = new SystemContacts();
                    contacts.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
                    contacts.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    contacts.setPhoneMore(cursor.getString(cursor.getColumnIndex("phonemore")));
                    contacts.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                    systemContactsArrayList.add(contacts);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
        return systemContactsArrayList;
    }

    @Override
    public void deleteAllSystemContacts() {
        mDatabase.beginTransaction();
        try{
            String sql = "delete from system_contacts";
            /*List<SystemContacts> contactses = selectAllSystemContacts();
            if(contactses != null){
                int length = contactses.size();
                for (int i = 0; i  < length; i++){
                    int id = contactses.get(i).getId();
                    mDatabase.delete("system_contacts","id=?",new String[]{id+""});
                }
            }*/
            mDatabase.execSQL(sql);
            mDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mDatabase.endTransaction();
        }
    }
}
