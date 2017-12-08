package com.example.leapfrog.jobdispatcher;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {


    BackgroundTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        Log.d("sync", "sync process running");

        //since the job is offloaded to a new thread, jobFinished() to be called explicitly and onStartJob() must
        //return true
        backgroundTask = new BackgroundTask() {
            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), "Message from background task " + s, Toast.LENGTH_SHORT).show();
                jobFinished(job, false);//returning true is for rescheduling same job other wise false.
            }
        };

        backgroundTask.execute();
        return true;
        //returning true means you have to call jobFinished() method, otherwise it will keep running
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        //if your job is interupted before completion, system will call this method

        return false; // true means reschedule the job if failed
    }


    public static class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return "Hello from background job";
        }
    }


}