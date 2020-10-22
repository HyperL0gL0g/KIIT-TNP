package com.application.kiit_tnp;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class notification_scheduler extends JobService {
    String TAG = "jobScehuler_notification";
    boolean jobCancelled;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        sendNotification(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return false;
    }


    public void work(JobParameters params){
        notification_scheduler activity = this;
        new helpers().worker(getApplicationContext(),null);
        jobFinished(params, true);
    }

    private void sendNotification(final JobParameters params) {

        if (jobCancelled) { return; }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Job finished");
                work(params);
            }
        }).start();

    }
}
