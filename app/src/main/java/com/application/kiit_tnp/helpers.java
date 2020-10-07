package com.application.kiit_tnp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.app.ActivityCompat;

public class helpers {

    public void insertAuth(String auth,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("auth",auth);
        database.insert("users",null,values);
    }
    public void updateAuth(String auth,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("auth",auth);
        database.update("users",values,"_id=1",null);
    }

    public void insertSessionID(String s1,String s2,String s3, String s4,String roll,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("sessionId1",s1);
        values.put("sessionId2",s2);
        values.put("sessionId3",s3);
        values.put("sessionId4",s4);
        values.put("rollNo",roll);
        database.insert("sessionID",null,values);

    }

    public void uppdateSessionID(String s1,String s2,String s3, String s4,String roll,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("sessionId1",s1);
        values.put("sessionId2",s2);
        values.put("sessionId3",s3);
        values.put("sessionId4",s4);
        values.put("rollNo",roll);
        database.update("sessionID",values,"_id=1",null);
    }

    public void insertNoticeID(String sDate,String eDate,String banner,String noticeid,SQLiteDatabase database){
        ContentValues values = new ContentValues();
        values.put("st_dt",sDate);
        values.put("en_dt",eDate);
        values.put("heading",banner);
        values.put("encrypted_notice_id",noticeid);
        int id = (int) database.insertWithOnConflict("noticeID", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update("noticeID", values, "encrypted_notice_id=?", new String[] {noticeid});
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1
            );
        }
    }





}
