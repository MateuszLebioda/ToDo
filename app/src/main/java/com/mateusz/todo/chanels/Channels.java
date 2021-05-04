package com.mateusz.todo.chanels;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Channels extends Application {
    public static final String CHANEL_ID = "chanel1";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANEL_ID,
                    "Default chanel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Chanel from term");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
