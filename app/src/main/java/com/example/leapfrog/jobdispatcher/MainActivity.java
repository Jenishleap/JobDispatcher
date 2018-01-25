package com.example.leapfrog.jobdispatcher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity {

    public static final String UNIQUE_TAG = "my-unique-tag";

    FirebaseJobDispatcher dispatcher;
    Button btnstartJob, btnStopJob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnstartJob = (Button) findViewById(R.id.btnStartJob);
        btnStopJob = (Button) findViewById(R.id.btnStopJob);

        btnstartJob.setOnClickListener(clickListener);
        btnStopJob.setOnClickListener(clickListener);


        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));


    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnStartJob:

                    Job myJob = dispatcher.newJobBuilder()
                            // the JobService that will be called
                            .setService(MyJobService.class)
                            // uniquely identifies the job
                            .setTag(UNIQUE_TAG)
                            // one-off job
                            .setRecurring(true)
                            // don't persist past a device reboot
                            .setLifetime(Lifetime.FOREVER)
                            // start between 0 and 60 seconds from now
                            .setTrigger(Trigger.executionWindow(5, 5))
                            // don't overwrite an existing job with the same tag
                            .setReplaceCurrent(false)
                            // retry with exponential backoff
                            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                            // constraints that need to be satisfied for the job to run
                            .build();


                    dispatcher.mustSchedule(myJob);
                    Toast.makeText(MainActivity.this, "Job scheduled", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.btnStopJob:
                    dispatcher.cancel(UNIQUE_TAG);
                    Toast.makeText(MainActivity.this, "Job cancelled", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "app destroyed", Toast.LENGTH_SHORT).show();
        dispatcher.cancel(UNIQUE_TAG);
        super.onDestroy();


    }
}
//i've made changes