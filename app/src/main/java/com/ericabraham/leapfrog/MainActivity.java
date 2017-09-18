package com.ericabraham.leapfrog;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private ListView listView;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private FloatingActionButton fabPickPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayData();
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
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        fabPickPlace = (FloatingActionButton) findViewById(R.id.fab);
       // tvPlaceDetails = (TextView) findViewById(R.id.placeDetails);
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
//                stBuilder.append("Name: ");
//                stBuilder.append(placename);
//                stBuilder.append("\n");
//                stBuilder.append("Latitude: ");
  //              stBuilder.append(latitude);
//                stBuilder.append("\n");

//                stBuilder.append("\n");
//                stBuilder.append("Address: ");
//                stBuilder.append(address);
//               tvPlaceDetails.setText(stBuilder.toString());

                taskSetting(latitude,longitude,placename,address);


//                Toast.makeText(getApplicationContext(),""+placename,Toast.LENGTH_SHORT).show();
  //              Toast.makeText(getApplicationContext(),""+address,Toast.LENGTH_SHORT).show();
//                db.insertLocation(latitude,longitude);
                String[] reminderdata;

                reminderdata = db.displayLocation();


//                stBuilder.append("Logitude: ");
//                stBuilder.append(reminderdata[0]);
//                stBuilder.append("\n");
//
//                stBuilder.append("Logitude: ");
//                stBuilder.append(reminderdata[1]);
//                stBuilder.append("\n");
//
//                stBuilder.append("Task: ");
//                stBuilder.append(reminderdata[2]);
//                stBuilder.append("\n");

//                tvPlaceDetails.setText(stBuilder.toString());


            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    private void taskSetting(String lat, String longi, String pname, String address) {
        //Toast.makeText(getApplicationContext(),"Ahhh... I'm CALLED!! ",Toast.LENGTH_SHORT).show();
        // EditText txtname = (EditText)findViewById(R.id.editText);
        //  String dataToPass      =  txtname.getText().toString();
        Intent intent = new Intent(this, task_setting.class);
        intent.putExtra("lat", lat);
        intent.putExtra("longi", longi);
        intent.putExtra("pname", pname);
        intent.putExtra("address", address);
        startActivity(intent);
        this.finish();
    }

    private void manageTask(int i) {
        //Toast.makeText(getApplicationContext(),"Ahhh... I'm CALLED!! ",Toast.LENGTH_SHORT).show();
        // EditText txtname = (EditText)findViewById(R.id.editText);
        //  String dataToPass      =  txtname.getText().toString();
        Intent intent = new Intent(this, ManageTask.class);
       // Toast.makeText(getApplicationContext(),"Ahhh... dont touch me now!! " +i , Toast.LENGTH_SHORT).show();
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

               // Toast.makeText(getApplicationContext(),"Ahhh... dont touch me now!! " +id[i] , Toast.LENGTH_SHORT).show();
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
}