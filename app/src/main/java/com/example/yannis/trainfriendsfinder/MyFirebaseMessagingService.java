package com.example.yannis.trainfriendsfinder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by yannis on 6/08/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String notificationTitle = null, notificationBody = null;

        //check if msg contains notification payload
        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendNotification(notificationTitle, notificationBody);
    }

    private void sendNotification(String notificationTitle, String notificationBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
}
