package com.ericabraham.leapfrog;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

/* Use this to get SHA1 Key using CMD in Windows
* keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
*/

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        CompoundButton.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        ResultCallback<Status> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private Switch pushBtn;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
//    protected ArrayList<Geofence> mGeofenceList;
    private int PLACE_PICKER_REQUEST = 1;
    private FloatingActionButton fabPickPlace;
    SharedPreferences sharedPrefs;
    boolean switchState;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, MainActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs = getSharedPreferences("SwitchButton", MODE_PRIVATE);
        switchState = sharedPrefs.getBoolean("SwitchButton", false);
     //   mGeofenceList = new ArrayList<Geofence>();

        displayData();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();

        fabPickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initViews() {
        fabPickPlace = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        googleApiClient.disconnect();
        super.onStop();
    }


    // Generating Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        pushBtn = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.pushBtn);
        if(switchState){
            pushBtn.setChecked(true);
        } else {
            pushBtn.setChecked(false);
        }
        pushBtn.setOnCheckedChangeListener(this);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.addtask: {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
            }
            case R.id.map_view: {
                Intent intent = new Intent(this, MyMap.class);
                this.startActivity(intent);
                return true;
            }

            case R.id.about: {
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(fabPickPlace, connectionResult.getErrorMessage() + "", Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        locationDatabase db = new locationDatabase(this);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                taskSetting(latitude,longitude,placename,address);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

//calling add new remnder activity with Lat, Long, name and address
    private void taskSetting(String lat, String longi, String pname, String address) {
        Intent intent = new Intent(this, task_setting.class);
        intent.putExtra("lat", lat);
        intent.putExtra("longi", longi);
        intent.putExtra("pname", pname);
        intent.putExtra("address", address);
        startActivity(intent);
        this.finish();
    }

    //open new activity to UPDATE or DELETE existing task
    private void manageTask(int i) {
        Intent intent = new Intent(this, ManageTask.class);
        String idName = String.valueOf(i);
        intent.putExtra("idName", idName);
        startActivity(intent);
        this.finish();
    }

//generating the custom list view
    private void displayData(){
        locationDatabase db = new locationDatabase(this);
        db.getCount();
        String[] task;
        String[] pname;
        String[] date;
        String[] address;
        final int[] id;
        task = db.displayTask();
        pname = db.displayName();
        date = db.displayDate();
        address = db.displayAddress();
        id = db.displayId();
        CustomList customList = new CustomList(this, task,pname,date,address,id);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                manageTask(id[i]);
            }
        });
        initViews();
    }

//Master Geo-Fencing Switch
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "Switch is : "+(isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        if (isChecked){
            SharedPreferences.Editor editor = getSharedPreferences("SwitchButton", MODE_PRIVATE).edit();
            editor.putBoolean("SwitchButton", true).apply();;
            editor.commit();
            startGeofence();
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences("SwitchButton", MODE_PRIVATE).edit();
            editor.putBoolean("SwitchButton", false).apply();;
            editor.commit();
            clearGeofence();
        }
    };



//Everything needed for the background service (GEOFENCING) is beyond this point
    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
        lastLocation = location;
        writeActualLocation(location);
    }

    // GoogleApiClient.ConnectionCallbacks connected
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        //If switchState is ON then Enable Geofencing
        if(switchState) {
            startGeofence();
        }else {
            clearGeofence();
        }
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }


    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }

    private void writeActualLocation(Location location) {
        new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    // Start Geofence creation process
    private void startGeofence() {
        locationDatabase db = new locationDatabase(this);
        db.getCount();
        String[] mlat;
        String[] mlong;
        String[] mtask;
        int[] taskid;
        int [] rad;
        mlat = db.displayLat();
        mlong = db.displayLong();
        mtask = db.displayTask();
        rad = db.displayAllRadius();
        taskid = db.displayId();
        int numberOfItems = mlat.length;
        for (int i=0; i<numberOfItems; i++) {
            double lat = Double.parseDouble(mlat[i]);
            double lon = Double.parseDouble(mlong[i]);
            int radius = rad[i];
            String tid = toTitleCase(mtask[i]);
            LatLng latLng = new LatLng(lat, lon);
            Geofence geofence = createGeofence(latLng, radius, tid);
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        }
    }


    // Create a Geofence (called by startGeofence function - in a loop)
    private Geofence createGeofence( LatLng latLng, float radius, String tid ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(tid)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius*100)
                //// TODO: 21-Sep-17 Calculate Duratation
                .setExpirationDuration( 1000*60*60 )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }


    // Create a Geofence Request  (called by startGeofence function - in a loop)
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }


    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            Log.d(TAG, "addGeofence");
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

// Call for the service
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;
        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }



    public void onResult(@NonNull Status status) {
        Log.d(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            Log.d(TAG, "Sucess");
        } else {
            // inform about fail
            Log.d(TAG, "Fail");
        }
    }


    // Clear Geofence
    private void clearGeofence() {
        Log.d(TAG, "clearGeofence()");
        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                createGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if ( status.isSuccess() ) {
//                    Toast.makeText(MainActivity.this, "All Geofencing service is stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Convert Task Title to Camel Case
    public static String toTitleCase(String input) {
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
}