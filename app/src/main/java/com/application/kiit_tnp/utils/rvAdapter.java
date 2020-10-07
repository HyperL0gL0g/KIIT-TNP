package com.application.kiit_tnp.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.kiit_tnp.HTTPManager;
import com.application.kiit_tnp.R;
import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.helpers;

import java.util.List;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.viewHolder>{

    Context context;
    Activity activity;
    List<dataObj> mdata;

    public rvAdapter(Context context,List<dataObj> mdata,Activity activity) {
        this.context = context;
        this.mdata = mdata;
        this.activity = activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.noticelist,null);
        rvAdapter.viewHolder holder = new rvAdapter.viewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull rvAdapter.viewHolder holder, int position) {
        dataObj data = mdata.get(position);
        holder.banner.setText(data.getBanner());
        holder.edate.setText(data.getEdata());
        holder.sdate.setText(data.getSdata());
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Starting download", Toast.LENGTH_SHORT).show();
                asyTask a = new asyTask();
                new helpers().verifyStoragePermissions(activity);
                SQLiteDatabase dbread = new dbHelper(context).getReadableDatabase();
                Cursor c = dbread.rawQuery("select * from sessionID",null);
                if (c.moveToFirst()) {
                        //mdata.add(new dataObj("Posted on "+c.getString(1),"Expires on "+c.getString(2),c.getString(3),c.getString(0)));
                    new asyTask.getNoticeDown().execute(new String[]{c.getString(5), c.getString(1), c.getString(2), c.getString(3), c.getString(4), mdata.get(position).getNoticeID()});
                }
                //new asyTask.getNoticeDown().execute({mdata.get(position)});

            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView banner,sdate,edate;
        ImageButton button;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            sdate = itemView.findViewById(R.id.sdate);
            edate = itemView.findViewById(R.id.edate);
            button = itemView.findViewById(R.id.imageButton);
        }
    }
}
