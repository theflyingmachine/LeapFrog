package com.ericabraham.leapfrog;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.Date;

public class Splash extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {


        super.onCreate(icicle);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_splash);

        ImageView im = (ImageView) findViewById(R.id.imageView);
        im.setImageResource(R.drawable.splashimg);

        /* New Handler to start the Menu-Activity
         * and close this plash-Screen after some seconds.*/
        /*
      Duration of wait
     */
        int SPLASH_DISPLAY_LENGTH = 500;
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                cleanUp();
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void cleanUp() {
        locationDatabase db = new locationDatabase(this);
        final int count = db.getCount();
        final int[] id = db.displayId();
        String[] dateStr = db.displayDate();
        String monthToNumStr = "";
        int monthToNum;
        for (int i = 0; i < count; i++) {
            String[] splitDate = dateStr[i].split("\\s+");
            String month = splitDate[0];
            String date = splitDate[1].substring(0, splitDate[1].length() - 1);
            if (Integer.parseInt(date) < 10) date = "0" + date;
            String year = splitDate[2];
            final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            for (int j = 0; j < 12; j++) {
                if (month.equals(MONTHS[j])) {
                    monthToNum = j + 1;
                    if (monthToNum < 10) {
                        monthToNumStr = Integer.toString(monthToNum);
                        monthToNumStr = "0" + monthToNumStr;
                    } else {
                        monthToNumStr = Integer.toString(monthToNum);
                    }
                    break;
                }
            }
            String conStrDate = monthToNumStr + "/" + date + "/" + year + " 23:59:59";
            //create date object
            Date current = new Date();
            Date prev = new Date(conStrDate);
            //compare both dates
            if (prev.before(current)) db.delTask(id[i]);
        }
    }


}