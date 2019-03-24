package com.example.efede.translator;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class NotificationHandler extends Activity {
    private static final int NOTIFICATION_ID = 100;
    private static final String KEY_TEXT_REPLY = "key_text_reply";
    private static NotificationManager notificationManager;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        Intent ıntent = getIntent();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(ıntent);


        if(auth.getCurrentUser() != null){

            if (remoteInput != null) {
                String sourceText = remoteInput.getCharSequence(KEY_TEXT_REPLY).toString();

                String targetText = Translate.Translate(sourceText, "en-tr", NotificationHandler.this);

                FireBaseConnector.FireBaseAddWord(sourceText, targetText, auth.getCurrentUser().getUid());

                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

               /*NotificationChannel notificationChannel = new NotificationChannel("notOne","notName",NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);*/

                Notification notification = new Notification.Builder(this).
                        setSmallIcon(R.drawable.not_icon).
                        setContentText(targetText).setColor(ContextCompat.
                        getColor(this, R.color.colorAccent)).build();

                notificationManager.notify(NOTIFICATION_ID, notification);


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TranslateNotification translateNotification = new TranslateNotification(NotificationHandler.this);
                        translateNotification.sendNotification();
                    }
                }, 15000);

                finish();


            }
        }else {
            TranslateNotification translateNotification = new TranslateNotification(NotificationHandler.this);
            translateNotification.CancelNotification();
        }


    }
}