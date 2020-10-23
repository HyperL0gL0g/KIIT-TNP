package com.application.kiit_tnp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.utils.asyTask;
import com.application.kiit_tnp.views.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final EditText user,pass;
        Button login;

        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(user.getText());
                String password = String.valueOf(pass.getText());
                String encoded = username+":"+password;
                    try {
                        String creds = Base64.encodeToString(encoded.getBytes("UTF-8"),Base64.NO_WRAP);
                        Log.d("testing",creds);
                        SQLiteDatabase dbread = new dbHelper(getApplicationContext()).getReadableDatabase();
                        SQLiteDatabase dbwrite = new dbHelper(getApplicationContext()).getWritableDatabase();
                        Cursor credsCursor = dbread.rawQuery("select * from users",null);
                        if(credsCursor.getCount()==1) {
                            new helpers().updateAuth(creds,dbwrite);
                            new asyTask.login(getApplicationContext(),activity).execute(creds);

                        }else{
                            new helpers().insertAuth(creds,dbwrite);
                            new asyTask.login(getApplicationContext(),activity).execute(creds);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

        });

    }
}