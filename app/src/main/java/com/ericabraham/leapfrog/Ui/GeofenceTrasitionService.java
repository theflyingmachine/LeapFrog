package com.ericabraham.leapfrog.Ui;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.ericabraham.leapfrog.Database.locationDatabase;
import com.ericabraham.leapfrog.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class GeofenceTrasitionService extends IntentService {

  private static final String TAG = GeofenceTrasitionService.class.getSimpleName();
  private String status = null;

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

  public GeofenceTrasitionService() {
    super(TAG);
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

      ArrayList<JSONObject> geofenceTransitionDetails =
          getGeofenceTrasitionDetails(geoFenceTransition, triggeringGeofences);

      // Send notification details as a String
      sendNotification(geofenceTransitionDetails);
    }
  }

  private ArrayList<JSONObject> getGeofenceTrasitionDetails(int geoFenceTransition,
      List<Geofence> triggeringGeofences) {
    try {
      // get the ID of each geofence triggered
      ArrayList<JSONObject> triggeringGeofencesList = new ArrayList<>();
      for (Geofence geofence : triggeringGeofences) {
        String notificationStatus = "";
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
          notificationStatus = "entering";
        } else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
          notificationStatus = "dwelling";
        } else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
          notificationStatus = "exiting";
        }
        JSONObject person = new JSONObject();
        person.put("id", geofence.getRequestId());

        person.put("status", notificationStatus);

        triggeringGeofencesList.add(person);
      }
      return triggeringGeofencesList;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  private void sendNotification(ArrayList<JSONObject> msg) {
    try {
      locationDatabase db = new locationDatabase(this);
      for (JSONObject info : msg) {
        int id = info.getInt("id");
        String title = db.displayTaskName(id);

        Log.i(TAG, "sendNotification: " + id);

        // Intent to start the MainActivity - Open Map
        Intent notificationIntent = new Intent(this, MainActivity.NotificationMgr.class);
        notificationIntent.putExtra("id", id);
        PendingIntent notificationPendingIntent =
            PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent to start the MainActivity - Skip Task
        Intent notificationSkip = new Intent(this, MainActivity.SkipTask.class);
        notificationSkip.putExtra("id", id);
        PendingIntent markSkip =
            PendingIntent.getBroadcast(this, 0, notificationSkip, PendingIntent.FLAG_UPDATE_CURRENT);

        // Intent to start the MainActivity - Done Task
        Intent notificationDone = new Intent(this, MainActivity.MarkDone.class);
        notificationDone.putExtra("id", id);
        PendingIntent markDone =
            PendingIntent.getBroadcast(this, 0, notificationDone, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification noti = new Notification.Builder(this)
            .setSmallIcon(R.drawable.frog1)
            .setColor(Color.RED)
            .setContentTitle(toTitleCase(title))
            .setContentText("LeapFrog - You are " + info.get("status") + " nearby!")
            .addAction(R.drawable.ic_near, "Map", notificationPendingIntent) // #0
            .addAction(R.drawable.ic_skip, "Skip", markSkip) // #1
            .addAction(R.drawable.ic_done, "Done", markDone) // #2
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setDefaults(
                Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
            .setPriority(Notification.PRIORITY_MAX)
            .build();

        // Get the notification manager system service
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        assert mNotificationManager != null;
        mNotificationManager.notify(id, noti);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}