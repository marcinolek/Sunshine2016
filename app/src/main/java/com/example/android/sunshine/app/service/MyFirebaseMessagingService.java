package com.example.android.sunshine.app.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sunshine.app.MainActivity;
import com.example.android.sunshine.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by marcinolek on 17.01.2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getSimpleName();

    private static final String EXTRA_DATA = "data";
    private static final String EXTRA_WEATHER = "weather";
    private static final String EXTRA_LOCATION = "location";

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String senderId = getString(R.string.gcm_defaultSenderId);
        if (senderId.length() == 0) {
            showToast("SenderID string needs to be set");
        }
        if (senderId.equals(remoteMessage.getFrom())) {

            String weather = data.get(EXTRA_WEATHER);
            String location = data.get(EXTRA_LOCATION);
            String alert =
                    String.format(getString(R.string.gcm_weather_alert), weather, location);
            sendNotification(alert);

        }
        Log.i(TAG, "Received: " + data.toString());

    }

    private void showToast(final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *  Put the message into a notification and post it.
     *  This is just one simple example of what you might choose to do with a GCM message.
     *
     * @param message The alert message to be posted.
     */
    private void sendNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        // Notifications using both a large and a small icon (which yours should!) need the large
        // icon as a bitmap. So we need to create that here from the resource ID, and pass the
        // object along in our notification builder. Generally, you want to use the app icon as the
        // small icon, so that users understand what app is triggering this notification.
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.art_clear)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Weather Alert!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
