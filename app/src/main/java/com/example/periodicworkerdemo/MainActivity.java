package com.example.periodicworkerdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.periodicworkerdemo.worker.WorkerClass;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*for every worker you have to give constraints
         * Like NetworkType connected tells that execute the worker only if internet is available
         */

        Button button = findViewById(R.id.btnWorker);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Btn","Clicked");

                /* These are the constraints to set
                 * It specifies the behaviour of the worker
                 * like when to start(Only when connected)*/
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build();

                /* You can pass data to worker by Data.Builder
                 * you can put String, int, float, double values*/
                Data.Builder builder = new Data.Builder();
                builder.putInt("INTEGER",101);
                builder.putString("STRING","UserName");

                /*This is how to start a one time worker
                 * First set your input data that you wish to pass to worker
                 * then set constraints
                 * and finally build*/
                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(WorkerClass.class)
                        .setInputData(builder.build())
                        .setConstraints(constraints)
                        .build();

                WorkManager workManager = WorkManager.getInstance(getApplicationContext());
                workManager.enqueue(oneTimeWorkRequest);

                /*This is how to start periodic worker
                 * First parameter is the class which extends Worker
                 * Second is Time Interval which will execute worker after every 15 minutes
                 * Third is type of Time unit like minutes, day
                 * the minimum time interval is 15 minutes, and you cannot give below than that or it will not work properly
                 */
                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(WorkerClass.class,
                        15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build();


                // TODO: 03-12-2019 Uncomment Code
                /* getInstance() method with no parameter is deprecated and
                    now it need context to be passed */
                //workManager = WorkManager.getInstance(getApplicationContext());

                /*Example of KEEP
                 * Here when you start application and the previous worker is already running
                 * then KEEP will keep the previous worker rather than starting a new one
                 */
                workManager.enqueueUniquePeriodicWork("demoWorker",
                        ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);

                /*Example of REPLACE
                 * Here when an application is started then every time it will replace the previous worker and
                 * every time it will execute the worker.
                 */
                workManager.enqueueUniquePeriodicWork("demoWorker",
                        ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
            }
        });

    }
}
