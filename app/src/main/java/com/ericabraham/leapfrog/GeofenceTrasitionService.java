package com.ericabraham.leapfrog;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTrasitionService extends IntentService {

    private static final String TAG = GeofenceTrasitionService.class.getSimpleName();
    private String status = null;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private
    String title = "";

    public GeofenceTrasitionService() {
        super(TAG);
    }

    // Convert Task Title to Camel Case
    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // Handling errors
        if (geofencingEvent.hasError()) {
            String errorMsg = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMsg);
            return;
        }
        Log.d(TAG, "Transision Service called");
        int geoFenceTransition = geofencingEvent.getGeofenceTransition();
        // Check if the transition type is of interest
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Get the geofence that were triggered
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);

            // Send notification details as a String
            sendNotification(geofenceTransitionDetails);
        }
    }

    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }


        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "entering";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL)
            status = "dwelling";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "exiting";
        return TextUtils.join(", ", triggeringGeofencesList);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String msg) {
        if (msg.contains(",")) {
            String[] parts = msg.split(",");
            String firstTask = parts[0];
            locationDatabase db = new locationDatabase(this);
            title = db.displayTaskName(Integer.parseInt(firstTask));
            msg = firstTask;
            db.close();
        } else {
            String firstTask = msg;
            locationDatabase db = new locationDatabase(this);
            title = db.displayTaskName(Integer.parseInt(firstTask));
            msg = firstTask;
            db.close();
        }


        Log.i(TAG, "sendNotification: " + msg);

        // Intent to start the MainActivity - Open Map
        Intent notificationIntent = new Intent(this, MainActivity.NotificationMgr.class);
        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent to start the MainActivity - Skip Task
        Intent notificationSkip = new Intent(this, MainActivity.SkipTask.class);
        notificationSkip.putExtra("id", msg);
        PendingIntent markSkip = PendingIntent.getBroadcast(this, 0, notificationSkip, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent to start the MainActivity - Done Task
        Intent notificationDone = new Intent(this, MainActivity.MarkDone.class);
        notificationDone.putExtra("id", msg);
        PendingIntent markDone = PendingIntent.getBroadcast(this, 0, notificationDone, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification noti = new Notification.Builder(this)
                .setSmallIcon(R.drawable.frog1)
                .setColor(Color.RED)
                .setContentTitle(toTitleCase(title))
                .setContentText("LeapFrog - You are " + status + " nearby!")
                .addAction(R.drawable.ic_near, "Map", notificationPendingIntent) // #0
                .addAction(R.drawable.ic_skip, "Skip", markSkip) // #1
                .addAction(R.drawable.ic_done, "Done", markDone) // #2
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .build();

// Get the notification manager system service
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        assert mNotificationManager != null;
        mNotificationManager.notify(0, noti);
    }
}