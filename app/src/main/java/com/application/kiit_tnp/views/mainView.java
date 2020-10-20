package com.application.kiit_tnp.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.application.kiit_tnp.R;
import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.utils.asyTask;
import com.application.kiit_tnp.utils.dataObj;
import com.application.kiit_tnp.utils.rvAdapter;

import java.util.ArrayList;
import java.util.List;

public class mainView extends AppCompatActivity {

    List<dataObj> mdata;
    RecyclerView recyclerView;
    TextView null_data;

    @Override
    protected void onResume() {
        super.onResume();
        //startActivity(new Intent(getApplicationContext(),mainView.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        null_data = findViewById(R.id.null_data);

        mdata = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SQLiteDatabase db = new dbHelper(getApplicationContext()).getReadableDatabase();
        Cursor c = db.rawQuery("select * from noticeID order by st_dt desc",null);

            if (c.moveToFirst()) {
                do{
                    mdata.add(new dataObj("Posted on "+c.getString(1),"Expires on "+c.getString(2),c.getString(3),c.getString(0)));
                }while (c.moveToNext());
            }
            c.close();

        if(mdata.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            null_data.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            null_data.setVisibility(View.GONE);
        }

        rvAdapter adapter = new rvAdapter(this, mdata,this);
        recyclerView.setAdapter(adapter);


    }
}