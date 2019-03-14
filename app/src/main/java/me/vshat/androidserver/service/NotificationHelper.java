package me.vshat.androidserver.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper  {

    //Notification2
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    //Notification1
    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_PREFIX = NotificationHelper.class.getCanonicalName();
    private static final String CHANNEL_ID = CHANNEL_PREFIX + "default_id";
    private static final String CHANNEL_NAME = CHANNEL_PREFIX + "default_name";

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    //Создание уведомления
    //Как сделать обновление уведомления, удаление уведомления и создание нового уведомления с новым ID
    public Notification createNotification(String text) {

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setChannelId(CHANNEL_ID)
                .setContentText(text)
                .setContentTitle("UMDOM1")
                .setSound(null)
                .setOngoing(true)
                .build();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        //Notification1
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Запуск сервера",
                NotificationManager.IMPORTANCE_DEFAULT);

        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setSound(null, null);
        notificationManager.createNotificationChannel(notificationChannel);



        //Notification2

            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,
                    "Оповещение газа",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,
                    "Оповещение движения",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is Channel 2");

            //NotificationManager manager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }



}
