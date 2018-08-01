package com.ubereat.world.Activity.Services;

/**
 * Created by hamza on 24-Jun-18.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ubereat.world.Activity.LoginActivity;
import com.ubereat.world.R;

import java.util.Map;

public class InstanceIdGenerator extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
       // UploadToServer(s);
    }

    void UploadToServer(String key)
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference dR = firebaseDatabase.getReference("FCM_InstanceId").child(firebaseUser.getUid());
            dR.setValue(key);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map payload=remoteMessage.getData();
        if(payload!=null)
        {
            createNotification(payload);

        }
    }

    private void createNotification(Map payload) {


        Intent i=new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
               i , 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.noti1))
                        .setContentTitle((String)payload.get("title"))
                        .setContentText((String)payload.get("message"));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
