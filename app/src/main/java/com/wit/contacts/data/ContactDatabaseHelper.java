package com.wit.contacts.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        sqLiteDatabase.execSQL(Sql.CREATE_GROUP);
        sqLiteDatabase.execSQL(Sql.CREATE_USER);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

    }
}
