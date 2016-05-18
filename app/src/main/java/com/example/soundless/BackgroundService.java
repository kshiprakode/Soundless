package com.example.soundless;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackgroundService extends Service {

    private com.google.api.services.calendar.Calendar mService = null;
    private MyBinder myBinder = new MyBinder();
    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnableCode, 15 * 1000);
        }

    };

    public BackgroundService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();

        handler.post(runnableCode);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    public class MyBinder extends Binder {
        BackgroundService getService()
        {
            return BackgroundService.this;
        }
    }



}
