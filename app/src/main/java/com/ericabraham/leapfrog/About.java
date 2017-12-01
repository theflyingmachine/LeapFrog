package com.ericabraham.leapfrog;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

public class About extends AppCompatActivity implements OnTouchListener {
    private ImageView wheel;
    private double mCurrAngle = 0;
    private boolean touched = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        wheel = findViewById(R.id.mylogo);
        wheel.setOnTouchListener(this);


        Toast.makeText(this, "Do NOT touch LeapFrog logo", Toast.LENGTH_SHORT).show();


    }

    // Generating Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_view: {
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                return true;
            }

            case R.id.map_view: {
                Intent intent = new Intent(this, MyMap.class);
                this.startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        final float xc = wheel.getWidth() / 2;
        final float yc = wheel.getHeight() / 2;

        final float x = event.getX();
        final float y = event.getY();

        if (touched) {
            Toast.makeText(this, "Aaawwww... You did it..", Toast.LENGTH_SHORT).show();
            touched = false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                wheel.clearAnimation();
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE:
                double mPrevAngle;
            {
                mPrevAngle = mCurrAngle;
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                animate(mPrevAngle, mCurrAngle);
                System.out.println(mCurrAngle);
                //audio
//                mPlayer.start();
                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
        }
        return true;
    }

    private void animate(double fromDegrees, double toDegrees) {
        MediaPlayer mPlayer = MediaPlayer.create(this, R.raw.about_wouble); // in 2nd param u have to pass your desire ringtone
        mPlayer.start();
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration((long) 500);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        wheel.startAnimation(rotate);
        System.out.println(mCurrAngle);
    }
}