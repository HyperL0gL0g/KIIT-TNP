package com.application.kiit_tnp;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.utils.asyTask;

import java.io.File;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class helpers {

    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };



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
        database.insert("noticeID",null,values);
//        int id = (int) database.insertWithOnConflict("noticeID", null, values, SQLiteDatabase.CONFLICT_IGNORE);
//        if (id == -1) {
//            database.update("noticeID", values, "encrypted_notice_id=?", new String[] {noticeid});
//        }
    }


    public  boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((permissionWrite != PackageManager.PERMISSION_GRANTED) ||
                (permissionRead != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
            return false;
        }else{
            return true;
        }
    }


    public void checkDownload(Activity activity,String[] data,String filename){
        String url = "https://apiv3.kiittnp.in/api/1.2/connect/notice/downlaod";
        Uri downloaduri = Uri.parse(url);
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

        try {
            if(manager!=null){
                DownloadManager.Request request = new DownloadManager.Request(downloaduri);
                request.addRequestHeader("rollno",data[0])
                        .addRequestHeader("sessionid1",data[1])
                        .addRequestHeader("sessionid2",data[2])
                        .addRequestHeader("sessionid3",data[3])
                        .addRequestHeader("sessionid4",data[4])
                        .addRequestHeader("notice_id", data[5])
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setTitle(filename)
                        .setDescription("downloading")
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
                        .setMimeType("application/pdf");

                manager.enqueue(request);
                Toast.makeText(activity, "uccessfull", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.d("adad",e.getMessage());
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteNoticeTable(SQLiteDatabase database){
        database.execSQL("delete from noticeID");
        //database.update("sessionID",values,"_id=1",null);
    }

    public void jobScheduler(Context context){
        ComponentName componentName = new ComponentName(context,notification_scheduler.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setPeriodic(900000)
                .build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("jobSchedulerTest", "Job scheduled");
        } else {
            Log.d("jobSchedulerTest", "Job scheduling failed");
        }
    }


    public void notification(Context context,String title,String body){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,
                "default" )
                .setSmallIcon(R.drawable. ic_launcher_foreground )
                .setContentTitle(title)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentText(body);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                    .setUsage(AudioAttributes. USAGE_ALARM )
                    .build() ;
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new
                    NotificationChannel( "10001" , "notification_channel" , importance) ;
            notificationChannel.enableLights( true ) ;
            notificationChannel.setLightColor(Color. RED ) ;
            notificationChannel.enableVibration( true ) ;
            notificationChannel.setVibrationPattern( new long []{ 100 , 200 , 300 , 400 , 500 , 400 , 300 , 200 , 400 }) ;
            notificationChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI , audioAttributes) ;
            mBuilder.setChannelId("10001") ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis (), mBuilder.build()) ;
    }

    public void worker(Context context,Activity activity) {
        SQLiteDatabase dbread = new dbHelper(context).getReadableDatabase();
        Cursor c = dbread.rawQuery("select * from sessionID", null);
        if (c.getCount() == 1) {
            if (c.moveToFirst()) {
                new asyTask.get_notice(context,activity,true).execute(c.getString(5), c.getString(1), c.getString(2), c.getString(3), c.getString(4), "JUNK");

            }
        } else {
            Cursor credsCursor = dbread.rawQuery("select * from users", null);
            if (credsCursor.getCount() == 1) {
                if (credsCursor.moveToFirst()) {
                    new asyTask.login(context,activity).execute(credsCursor.getString(1));

                }
            }
        }
    }




}
