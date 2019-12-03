package com.example.periodicworkerdemo.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.periodicworkerdemo.MainActivity;
import com.example.periodicworkerdemo.R;

public class WorkerClass extends Worker {

    public WorkerClass(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        //init all members
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e("doWork","Worker");
        //do your work here
        createNotificationChannel();

        return Result.success();
    }

    private void createNotificationChannel() {
        Log.e("doWork","createNotificationChannel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "demo_channel";
            String description = "demo_channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("PERSONAL_NOTIFICATION",
                    name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getApplicationContext()
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            displayNotification();
        } else {
            displayNotification();
        }
    }

    private void displayNotification() {

        Log.e("doWork","displayNotification");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, 0);

        Uri uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //------------------------------------------------------------------------------------------
        //Notification Group Builder

        NotificationCompat.Builder mGroupBuilder = new NotificationCompat.Builder(getApplicationContext(),
                "PERSONAL_NOTIFICATION");
        mGroupBuilder.setSmallIcon(R.drawable.images);
        mGroupBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mGroupBuilder.setGroupSummary(true);
        mGroupBuilder.setGroup("GROUP_KEY_DEMO_NOTIFICATION");
        mGroupBuilder.setContentIntent(pendingIntent);
        //mGroupBuilder.setAutoCancel(true);

        //------------------------------------------------------------------------------------------
        //Single child notification
        NotificationCompat.Builder mNotification = new NotificationCompat.Builder(getApplicationContext(),
                "PERSONAL_NOTIFICATION");
        mNotification.setSmallIcon(R.drawable.images);
        mNotification.setContentTitle("This is demo title for notification");//title of notification
        mNotification.setContentText("this is notification body for demo project");//body of notification
        mNotification.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mNotification.setContentIntent(pendingIntent);
        mNotification.setSound(uriRingtone);
        mNotification.setGroup("GROUP_KEY_DEMO_NOTIFICATION");
        //mNotification.setAutoCancel(true);

        int id = (int) System.currentTimeMillis();
        int group_id = 1;
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(getApplicationContext());
        notificationManagerCompat.notify(group_id, mGroupBuilder.build());
        notificationManagerCompat.notify(id, mNotification.build());
        //notificationManagerCompat.cancelAll();
    }
}
