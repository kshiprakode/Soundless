/**
 * Soundless - Application that automatically changes the profile of the phone based on the Google Calendar events.

 * Team Adroit -
 *  Computer Science:
 *    Joseph Daniels 
 *    Kshipra Kode 
 *    Oladipupo Eke
 *    Mark Pileggi
 *  Designer:
 *    Monica Williams

 * Date : 19th May 2016

 **/

package com.example.soundless;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    //Defines SplashScreen Timeout
    int SPLASH_SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Handler that calls the MainActivity after the splash screen is done
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, com.example.soundless.MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }

}
