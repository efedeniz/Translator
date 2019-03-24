package com.example.efede.translator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

public class TranslateNotification {
    private static final int NOTIFICATION_ID = 100;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static NotificationManager notificationManager;
    private Context context;

    public TranslateNotification(Context context){
        this.context = context;
    }

    public void sendNotification(){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

         /* NotificationChannel notificationChannel = new NotificationChannel("notOne","notName",NotificationManager.IMPORTANCE_DEFAULT);
       notificationChannel.enableVibration(false);  //Android 8 ve yukarısı için gerekli
       notificationManager.createNotificationChannel(notificationChannel);*/

        String replyLabel = context.getString(R.string.reply);

        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent resultIntent = new Intent(context,NotificationHandler.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.not_icon,
                "Translate",
                resultPendingIntent).
                addRemoteInput(remoteInput)
                .build();

        Notification newMessageNotification = new NotificationCompat.Builder(context)
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setSmallIcon(R.drawable.not_icon)
                .setContentTitle(context.getString(R.string.translator))
                .setContentText(context.getString(R.string.translate))
                .setAutoCancel(false)
                .addAction(replyAction).build();

        notificationManager.notify(NOTIFICATION_ID,newMessageNotification);


    }

    public static void CancelNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
