package com.ericabraham.leapfrog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ManageTask extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private int idno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_task);

        // initiate the date picker and a button
        date = findViewById(R.id.date);
        date.setKeyListener(null);
        CheckBox chk = (CheckBox) findViewById(R.id.ne);


        //Check for never expires box
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    date.setText("January 19, 2038");
                    date.setEnabled(false);   // Do your coding
                }
                else{
                    date.setEnabled(true);  // Do your coding
                }
            }
        });


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
                datePickerDialog = new DatePickerDialog(ManageTask.this,
                        new DatePickerDialog.OnDateSetListener() {


                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                String mon = MONTHS[monthOfYear];
                                date.setText(mon + " " + dayOfMonth + ", " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });



        String i = getIntent().getStringExtra("idName");
        int id = Integer.parseInt(i);
        //   Toast.makeText(getApplicationContext(),"Ahhh..I am Manage Task, i got: " +i ,Toast.LENGTH_SHORT).show();


        final locationDatabase db = new locationDatabase(this);

        final TextView title = findViewById(R.id.title_edittext);
        final SeekBar radius = findViewById(R.id.seekBar);
        final TextView date = findViewById(R.id.date);
        final TextView todo = findViewById(R.id.todo_edittext);
        TextView tid = findViewById(R.id.taskid);





        String[] taskData = db.displayTask(id);
        int raduisData = db.displayRadius(id);

        title.setText(taskData[0]);
        radius.setProgress(raduisData);
        date.setText(taskData[1]);
        todo.setText(taskData[2]);
//        tid.setText(taskData[3]);
        idno = Integer.valueOf(taskData[3]);


        //checkbox is checked and date disabled if date is end of calender
        if (taskData[1].equals("January 19, 2038")) {
            //disable the datebox and make checkbox checked
            //Toast.makeText(getApplicationContext(),"Infinate Date" ,Toast.LENGTH_SHORT).show();
            chk.setChecked(true);
            date.setEnabled(false);

        }

        Button del_button = findViewById(R.id.del_button);

        del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.delTask(idno);
                Toast.makeText(getApplicationContext(), "Your Task is Deleted" + idno, Toast.LENGTH_SHORT).show();
                returnToMain();
            }
        });


        Button update_button = findViewById(R.id.update_button);

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String utask = title.getText().toString();
                String utodo = todo.getText().toString();
                int uradius = radius.getProgress();
                //      Integer.parseInt(date.getText().toString());
                String udate = date.getText().toString();

// Validation
                if (utask.equals("")) {
                    Toast.makeText(getApplicationContext(), "Awww... Pleas give me a TITLE.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (udate.equals("")) {
                    Toast.makeText(getApplicationContext(), "Awww... Do i have a DATE?.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (utodo.equals("")) {
                    Toast.makeText(getApplicationContext(), "Awww... I can not remind NOTHING.", Toast.LENGTH_SHORT).show();
                    return;
                }


                db.updateTask(idno, utask, utodo, uradius, udate);
                returnToMain();
            }
        });
    }

    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }


}
