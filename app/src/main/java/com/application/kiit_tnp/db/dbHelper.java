package com.application.kiit_tnp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private  static String dbName = "kiit-tnp";

    public dbHelper(Context context){
        super(context,dbName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "create table users (_id INTEGER PRIMARY KEY AUTOINCREMENT,auth TEXT)";
        String query2 = "create table sessionID (_id INTEGER PRIMARY KEY AUTOINCREMENT,sessionid1 TEXT,sessionid2 TEXT,sessionid3 TEXT,sessionid4 TEXT,rollNo TEXT)";
        String query3 = "create table noticeID (encrypted_notice_id TEXT PRIMARY KEY,st_dt TEXT,en_dt TEXT,heading TEXT)";
        String query4 = "create table noticeDown (_id INTEGER PRIMARY KEY AUTOINCREMENT,encrypted_notice_id TEXT,header TEXT)";
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
        sqLiteDatabase.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists users");
        sqLiteDatabase.execSQL("drop table if exists sessionID");
        sqLiteDatabase.execSQL("drop table if exists noticeID");
        sqLiteDatabase.execSQL("drop table if exists noticeDown");
        onCreate(sqLiteDatabase);
    }


}
