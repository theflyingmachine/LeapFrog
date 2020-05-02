package com.ericabraham.leapfrog.Ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ericabraham.leapfrog.R;
import com.ericabraham.leapfrog.Utils.Utils;
import com.ericabraham.leapfrog.Database.locationDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TaskSetting extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private static final String TAG = "TaskSetting";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        mAuth = FirebaseAuth.getInstance();
//       Initialize and set Location Title and Address
        String placename = getIntent().getStringExtra("pname");
        String address = getIntent().getStringExtra("address");
        final TextView addressTitle = findViewById(R.id.addressTitle);
        final TextView displayaddress = findViewById(R.id.address);
        addressTitle.setText(placename);
        displayaddress.setText(address);
        // initiate the date picker and a button
        date = findViewById(R.id.date);
        date.setKeyListener(null);
        // perform click event on edit text

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDatePickerDialog();
                }
            }
        });


        //        //Check for never expires box
        CheckBox chk = (CheckBox) findViewById(R.id.ne);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked) {
                    date.setText("Never Expires");
                    date.setEnabled(false);   // Do your coding
                } else {
                    date.setText("");
                    date.setEnabled(true);  // Do your coding
                }
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDatePickerDialog();
            }
        });

        final locationDatabase db = new locationDatabase(this);

        Button cancel_button = findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   finish();
                saveNoStatus(view);
            }
        });


        Button save_button = findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String latitude = getIntent().getStringExtra("lat");
                String longitude = getIntent().getStringExtra("longi");
                String pname = getIntent().getStringExtra("pname");
                String address = getIntent().getStringExtra("address");


                EditText txtname = findViewById(R.id.title_edittext);
                String task = txtname.getText().toString();

                EditText todoTxt = findViewById(R.id.todo_edittext);
                String todo = todoTxt.getText().toString();

                SeekBar rad = findViewById(R.id.seekBar);
                int radius = rad.getProgress();

                EditText datetxt = findViewById(R.id.date);
                String date = datetxt.getText().toString();

                // Validation
                if (task.equals("")) {
                    Snackbar.make(view, "Please Enter Title", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (date.equals("")) {
                    Snackbar.make(view, "Please Enter Date", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (todo.equals("")) {
                    Snackbar.make(view, "Please Enter Description", Snackbar.LENGTH_LONG).show();
                    return;
                }

                long id = db.insertLocation(latitude, longitude, task, todo, radius, date, pname, address);


                // Access a Cloud Firestore instance from your Activity
// Create a new user with a first, middle, and last name
                Map<String, Object> taskdata = new HashMap<>();
                taskdata.put("latitude", latitude);
                taskdata.put("longitude", longitude);
                taskdata.put("task", task);
                taskdata.put("todo", todo);
                taskdata.put("radius", radius);
                taskdata.put("date", date);
                taskdata.put("pname", pname);
                taskdata.put("address", address);
                taskdata.put("localId", id);

// Add a new document with a generated ID
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    DocumentReference documentReference = db.collection(user.getEmail()).document(String.valueOf(id));
                    documentReference.set(taskdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(TaskSetting.this, "Saved Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
//                    Toast.makeText(TaskSetting.this, "Not Logged in", Toast.LENGTH_SHORT).show();
                }


                saveOkStatus(view);


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void onDatePickerDialog() {
        Utils.hideKeyboard(this);
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        datePickerDialog = new DatePickerDialog(TaskSetting.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text

                        final String[] MONTHS = {
                                "January", "February", "March", "April", "May", "June", "July", "August",
                                "September", "October", "November", "December"
                        };
                        String mon = MONTHS[monthOfYear];
                        date.setText(mon + " " + dayOfMonth + ", " + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void saveOkStatus(View view) {
        Snackbar.make(view, "Task Saved Successfully", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveNoStatus(View view) {
        Snackbar.make(view, "Oops, Something Went Wrong", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


}
