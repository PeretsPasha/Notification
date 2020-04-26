package com.example.notification;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import org.apache.http.params.HttpConnectionParams;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
                showNotifWithImage();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public void showNotifWithImage(){

        new AsyncTask<String, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(String... strings) {
                InputStream inputStream;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);

                } catch (Exception e){

                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                showNotification(bitmap);
            }
        }.execute("https://apparel-sourcing-paris.fr.messefrankfurt.com/content/dam/messefrankfurt-france/apparel-sourcing-paris/top-home-slider/Pic%20Paris%205.jpg");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification(Bitmap bitmap){
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
        builder.setContentText("Lorem ipsum — класичний варіант умовного беззмістовного тексту, що вставляється в макет сторінки (у сленгу дизайнерів такий текст називається «рибою»). Lorem ipsum — це перекручений уривок з філософського трактату Цицерона «Про межі добра і зла», написаного в 45 році до нашої ери латиною. З точки зору зручності сприйняття друкованих текстів Lorem ipsum показує, що латинська графіка найслушніше пристосовується для написання саме творів латинською мовою (тут враховується частота вживання символів з виносними елементами, таких як g, l, h, p).");

        //builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Lorem ipsum — класичний варіант умовного беззмістовного тексту, що вставляється в макет сторінки (у сленгу дизайнерів такий текст називається «рибою»). Lorem ipsum — це перекручений уривок з філософського трактату Цицерона «Про межі добра і зла», написаного в 45 році до нашої ери латиною. З точки зору зручності сприйняття друкованих текстів Lorem ipsum показує, що латинська графіка найслушніше пристосовується для написання саме творів латинською мовою (тут враховується частота вживання символів з виносними елементами, таких як g, l, h, p)."));

        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));

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
