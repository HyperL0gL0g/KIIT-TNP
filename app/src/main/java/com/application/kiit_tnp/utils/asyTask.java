package com.application.kiit_tnp.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.application.kiit_tnp.HTTPManager;
import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.helpers;
import com.application.kiit_tnp.views.mainView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Response;

public class asyTask {

    public static class login extends AsyncTask<String,Void, JSONObject> {
        HTTPManager httpManager = new HTTPManager();
        Context context;

        public login(Context context) {
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                Response response= httpManager.send_postRequest("https://apiv3.kiittnp.in/api/1.1/connect/login",strings[0]);
                String returnData = response.body().string();
                JSONObject reader = new JSONObject(returnData);
                return reader;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject reader) {
            super.onPostExecute(reader);
            SQLiteDatabase dbread = new dbHelper(context).getReadableDatabase();
            SQLiteDatabase dbwrite = new dbHelper(context).getReadableDatabase();
            try {
                if(reader.getString("status").equals("Authenticated")){
                    Toast.makeText(context, "Logged in succesfully", Toast.LENGTH_SHORT).show();
                    Cursor c = dbread.rawQuery("select * from sessionID",null);
                    if(c.getCount()==1){
                        new helpers().uppdateSessionID(reader.get("sessionId1").toString(),reader.get("sessionId2").toString(),reader.get("sessionId3").toString(),
                                    reader.get("sessionId4").toString(),reader.get("rollNo").toString(),dbwrite);

                    }else{
                            new helpers().insertSessionID(reader.get("sessionId1").toString(),reader.get("sessionId2").toString(),reader.get("sessionId3").toString(),
                                    reader.get("sessionId4").toString(),reader.get("rollNo").toString(),dbwrite);
                    }
                    c.close();
                    Cursor sessCursor = dbread.rawQuery("select * from sessionID",null);
                    if (sessCursor.moveToFirst()) {
                        new get_notice(context).execute(sessCursor.getString(5),sessCursor.getString(1),sessCursor.getString(2),sessCursor.getString(3),sessCursor.getString(4),"JUNK");
                    }
                    sessCursor.close();
                }else{
                    Toast.makeText(context, "Invalid username/password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class get_notice extends AsyncTask<String,Void, JSONArray>{

        HTTPManager httpManager = new HTTPManager();
        Context context;

        public get_notice(Context context) {
            this.context = context;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if(jsonArray == null){
                Cursor credsCursor = new dbHelper(context).getReadableDatabase().rawQuery("select * from users",null);
                if(credsCursor.getCount()==1){
                    if (credsCursor.moveToFirst()) {
                        new asyTask.login(context).execute(credsCursor.getString(1));
                    }
                }
            }else{
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject reader = null;
                    try {
                        reader = jsonArray.getJSONObject(i);
                        Log.d("testing",reader.getString("bd"));
                        SQLiteDatabase dbwrite = new dbHelper(context).getReadableDatabase();
                        new helpers().insertNoticeID(reader.getString("st_dt"),reader.getString("en_dt"),reader.getString("heading"),
                                reader.getString("encrypted_notice_id"),dbwrite);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                context.startActivity(new Intent(context,mainView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        }

        @Override
        protected JSONArray doInBackground(String... strings) {
            try {
                Response response = httpManager.send_getRequest("https://apiv3.kiittnp.in/api/1.2/connect/notice",strings);
                String returnData = response.body().string();
                Log.d("noticeData",returnData);
                if(returnData.contains("Access Denied")){
                    return  null;
                }else{
                    return new JSONArray(returnData);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public static class getNoticeDown extends AsyncTask<String,Void,Response>{

        HTTPManager httpManager = new HTTPManager();

        @Override
        protected Response doInBackground(String... strings) {
            try {
                Response response = httpManager.getNoticeDown("https://apiv3.kiittnp.in/api/1.2/connect/notice/downlaod",strings);
                return  response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            InputStream input = response.body().byteStream();
            String fileName = response.header("content-disposition").split("=")[1];
            Log.d("Adad", fileName);
            try {
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                try (OutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4 * 1024];
                    int read;
                    while ((read = input.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                    output.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
