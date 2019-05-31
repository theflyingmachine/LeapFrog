package com.ericabraham.leapfrog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Calendar;


public class TaskSetting extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private static final String TAG = "TaskSetting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        // initiate the date picker and a button
        date = findViewById(R.id.date);
        date.setKeyListener(null);
        // perform click event on edit text

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onDatePickerDialog();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onDatePickerDialog();
            }
        });

        final locationDatabase db = new locationDatabase(this);

        Button cancel_button = findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   finish();
                saveNoStatus();
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
                    Toast.makeText(getApplicationContext(), "Awww... Pleas give me a TITLE.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.equals("")) {
                    Toast.makeText(getApplicationContext(), "Awww... do i have a DATE?.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (todo.equals("")) {
                    Toast.makeText(getApplicationContext(), "Awww... I can not remind NOTHING.", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.insertLocation(latitude, longitude, task, todo, radius, date, pname, address);
                saveOkStatus();


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

    private void onDatePickerDialog(){
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

    private void saveOkStatus() {
        Toast.makeText(getApplicationContext(), "Ahhh... Saved!! ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveNoStatus() {
        Toast.makeText(getApplicationContext(), "Ahhh... NOT SAVED!! ", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


}
