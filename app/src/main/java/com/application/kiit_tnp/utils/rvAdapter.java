package com.application.kiit_tnp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.application.kiit_tnp.BuildConfig;
import com.application.kiit_tnp.HTTPManager;
import com.application.kiit_tnp.R;
import com.application.kiit_tnp.db.dbHelper;
import com.application.kiit_tnp.helpers;

import java.io.File;
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

                if (new helpers().verifyStoragePermissions(activity)){
                    SQLiteDatabase dbread = new dbHelper(context).getReadableDatabase();
                    File mFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "KIIT-TNP");
                    File directory = new File(mFolder.getAbsolutePath());
                    File[] files = directory.listFiles();
                    Cursor cursor = dbread.rawQuery("select * from noticeDown where encrypted_notice_id = ?",new String[] {mdata.get(position).getNoticeID()});
                    boolean checker = false;
                    int index = 0;
                    if(cursor.getCount()>0){
                        cursor.moveToFirst();
                        for(int i=0;i<files.length;i++){
                            if(files[i].getName().equals(cursor.getString(2))){
                                checker=true;
                                index = i;
                            }
                        }
                    }
                    if(checker){
                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(files[index].getAbsolutePath())),"application/pdf");
                        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        activity.startActivity(target);
                    }else{
                        Cursor c = dbread.rawQuery("select * from sessionID",null);
                        if (c.moveToFirst()) {
                            Toast.makeText(context,"Starting download", Toast.LENGTH_SHORT).show();
                            new asyTask.getNoticeDown(activity).execute(new String[]{c.getString(5), c.getString(1), c.getString(2), c.getString(3), c.getString(4), mdata.get(position).getNoticeID()});
                        }
                    }

                }
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
