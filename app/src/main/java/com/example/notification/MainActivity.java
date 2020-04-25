package com.example.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(){
        int notificationID = new Random().nextInt(100);
        String chanellID = "notification_chanel_1";

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanellID);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle("Привіт Світ! Як справи?");
        builder.setContentText("Це простий приклад повідомлення");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (manager != null && manager.getNotificationChannel(chanellID) == null){
            NotificationChannel channel = new NotificationChannel(chanellID, "Notif 1", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is a chanel");
            channel.enableVibration(true);
            channel.enableLights(true);
            manager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        if (manager != null){
            manager.notify(notificationID, notification);
        }
    }
}
