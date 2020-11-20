package com.luissabias.stoply.test;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class MainJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("JobServiceSample", "MainJobService start" );
        MainJobService.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("JobServiceSample", "MainJobService stop" );
        return true;
    }


    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, MainJobService.class);
        JobInfo jobInbo = new JobInfo.Builder(0, serviceComponent)
                .setMinimumLatency(5000)      // Temps d'attente minimal avant déclenchement
                .setOverrideDeadline(6000)    // Temps d'attente maximal avant déclenchement
                .build();

        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
        }
        jobScheduler.schedule(jobInbo);
    }

}