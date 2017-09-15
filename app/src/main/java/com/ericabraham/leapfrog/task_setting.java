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


public class task_setting extends AppCompatActivity {

    private Button cancel_button;
    private Button save_button;
    EditText date;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);

        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.date);

        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day


                // date picker dialog
                datePickerDialog = new DatePickerDialog(task_setting.this,
                        new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                String mon=MONTHS[monthOfYear];
                                date.setText(mon +" " +  dayOfMonth + ", " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


       final locationDatabase db = new locationDatabase(this);

        cancel_button = (Button) findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   finish();
                saveNoStatus();
            }
        });




        save_button = (Button) findViewById(R.id.save_button);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String latitude = getIntent().getStringExtra("lat");
                String longitude = getIntent().getStringExtra("longi");

                EditText txtname = (EditText)findViewById(R.id.title_edittext);
                String task      =  txtname.getText().toString();

                EditText todoTxt = (EditText)findViewById(R.id.todo_edittext);
                String todo      =  todoTxt.getText().toString();

                SeekBar  rad = (SeekBar)findViewById(R.id.seekBar);
                int radius = rad.getProgress();

                EditText datetxt = (EditText)findViewById(R.id.date);
                String date      =  datetxt.getText().toString();

                db.insertLocation(latitude,longitude,task,todo,radius,date);
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

    public boolean saveOkStatus(){
        Toast.makeText(getApplicationContext(),"Ahhh... Saved!! ",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
      return true;
    }

    public boolean saveNoStatus(){
        Toast.makeText(getApplicationContext(),"Ahhh... NOT SAVED!! ",Toast.LENGTH_SHORT).show();
        finish();
        return false;
    }




}
