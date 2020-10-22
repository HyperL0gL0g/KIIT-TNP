package com.application.kiit_tnp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.application.kiit_tnp.HTTPManager;
import com.application.kiit_tnp.MainActivity;
import com.application.kiit_tnp.R;
import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.helpers;
import com.application.kiit_tnp.utils.asyTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class SplashScreen extends AppCompatActivity {

    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        hideSystemUI();
        new helpers().jobScheduler(getApplicationContext());
        SQLiteDatabase dbread = new dbHelper(getApplicationContext()).getReadableDatabase();
        Cursor c = dbread.rawQuery("select * from sessionID",null);
        if(c.getCount()==1){
            if (c.moveToFirst()) {
                new asyTask.get_notice(getApplicationContext(),activity,false).execute(c.getString(5), c.getString(1), c.getString(2), c.getString(3), c.getString(4), "JUNK");

            }
        }else{
            Cursor credsCursor = dbread.rawQuery("select * from users",null);
            if(credsCursor.getCount()==1){
                if (credsCursor.moveToFirst()) {
                    new asyTask.login(getApplicationContext(),activity).execute(credsCursor.getString(1));

                }
            }else{
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NO_HISTORY));

            }
        }

        //finish();

    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}