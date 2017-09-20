package com.ericabraham.leapfrog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

/* Use this to get SHA1 Key using CMD in Windows
* keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
*/

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CompoundButton.OnCheckedChangeListener {


    private ListView listView;
    private Switch pushBtn;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private FloatingActionButton fabPickPlace;
    SharedPreferences sharedPrefs;
    boolean switchState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefs = getSharedPreferences("SwitchButton", MODE_PRIVATE);
        switchState = sharedPrefs.getBoolean("SwitchButton", false);

        displayData();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
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
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
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


    private void taskSetting(String lat, String longi, String pname, String address) {
        Intent intent = new Intent(this, task_setting.class);
        intent.putExtra("lat", lat);
        intent.putExtra("longi", longi);
        intent.putExtra("pname", pname);
        intent.putExtra("address", address);
        startActivity(intent);
        this.finish();
    }

    private void manageTask(int i) {
        Intent intent = new Intent(this, ManageTask.class);
        String idName = String.valueOf(i);
        intent.putExtra("idName", idName);
        startActivity(intent);
        this.finish();
    }




    private void displayData(){
        locationDatabase db = new locationDatabase(this);
        db.getCount();
        String[] task;
        String[] pname;
        String[] date;
        String[] address;
        final int[] id;
        final String[] todo;
        task = db.displayTask();
        pname = db.displayName();
        date = db.displayDate();
        address = db.displayAddress();
        todo = db.displayTodo();
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



    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, MainActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "Switch is : "+(isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        if (isChecked){
            SharedPreferences.Editor editor = getSharedPreferences("SwitchButton", MODE_PRIVATE).edit();
            editor.putBoolean("SwitchButton", true).apply();;
            editor.commit();
        }
        else {
            SharedPreferences.Editor editor = getSharedPreferences("SwitchButton", MODE_PRIVATE).edit();
            editor.putBoolean("SwitchButton", false).apply();;
            editor.commit();
        }
    }
}