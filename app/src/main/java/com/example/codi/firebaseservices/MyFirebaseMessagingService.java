package com.example.codi.firebaseservices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.codi.MainActivity;
import com.example.codi.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String LOG_TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.v(LOG_TAG, remoteMessage.getData().toString());

        if (remoteMessage.getData().size()>0){
            sendNotification(remoteMessage.getData().toString());
        }

        if (remoteMessage.getNotification()!=null){

            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    public void sendNotification(String messageBody){
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this)
                .setContentTitle("FCM Message")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1,notificationBuilder.build());
    }
}