package com.wit.contacts.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wit.contacts.bean.Group;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;

/**
 * Created by wnw on 2016/9/12.
 */
public class ContactDatabaseHelper extends SQLiteOpenHelper {

    public static ContactDatabaseHelper instance;

    public static SQLiteDatabase getInstance(){
        return instance.getWritableDatabase();
    }

    public static SQLiteDatabase getInstance(Context context){
        if(instance == null){
            instance = new ContactDatabaseHelper(context, "Contact.db", null, 1);
            /**
             *安装的时候，默认添加一个分组：好友
             * */
            /*GroupDao groupDao = new GroupDaoImp();
            Group group = new Group("好友",null);
            groupDao.insertGroup(group);*/
        }
        return instance.getWritableDatabase();
    }

    public ContactDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ContactDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(Sql.USER);
        sqLiteDatabase.execSQL(Sql.CREATE_GROUP);
        sqLiteDatabase.execSQL(Sql.CREATE_USER);
        sqLiteDatabase.execSQL(Sql.CREATE_BLACKLIST);
        sqLiteDatabase.execSQL(Sql.CREATE_SYSTEM_CONTACTS);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

    }
}
