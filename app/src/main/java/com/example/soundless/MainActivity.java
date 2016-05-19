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

//Application Imports
import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    //Local variables for enabling google services
    GoogleAccountCredential mCredential;
    private com.google.api.services.calendar.Calendar mService = null;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    //Local variables for managing audio services
    AudioManager audiomanager;
    private boolean previousbusy;
    private boolean busy = false;

    //Local Variables for entering data from events to the listview
    ListView listView;
    private DateTime now;
    private List<String> eventNames;
    private List<String> eventTimeStart;
    private List<String> eventTimeEnd;
    private List<String> eventLocations;
    private List<Event> items;

    private static final int notificationID = 1000001;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mCallApiButton = (Button) findViewById(R.id.buttonApi);
        mCallApiButton.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listView);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        //On creating of the Application Activity, the user is not busy, thus, busy is off
        busy = false;

        //Set the custom type face for the Sync button
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/IrmaTextRoundStd-SemiBold copy.ttf");
        mCallApiButton.setTypeface(customFont);
        audiomanager = (AudioManager)getSystemService(AUDIO_SERVICE);


        Toast.makeText(MainActivity.this,"Soundless is fetching your events...",Toast.LENGTH_LONG).show();

        getResultsFromApi();
        actionSync();

        //Call the handler to check if there is an active event
        handler.post(runnableCode);

    }

    //Handler that checks the google calendar API for current events and if any new events are created
    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {

            now = new DateTime(System.currentTimeMillis());
            Events events = null;

            //Pulls next 10 events in the google calender
            if(mService!=null){
                try {

                    events = mService.events().list("primary")
                            .setMaxResults(10)
                            .setTimeMin(now)
                            .setOrderBy("startTime")
                            .setSingleEvents(true)
                            .execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                }
                assert events != null;
                items = events.getItems();

                List<String> eventStrings = new ArrayList<>();
                eventLocations = new ArrayList<>();
                eventTimeStart = new ArrayList<>();
                eventTimeEnd = new ArrayList<>();
                eventNames = new ArrayList<>();

                busy=false;

                //Parses through each event adding the contents to list view and also checking if there are events running currently
                for (Event event : items) {

                    eventNames.add(event.getSummary());
                    eventTimeStart.add(event.getStart().getDateTime().toString().substring(0, 10) + " : " + event.getStart().getDateTime().toString().substring(11, 19));
                    eventTimeEnd.add(event.getEnd().getDateTime().toString().substring(0, 10) + " : " + event.getEnd().getDateTime().toString().substring(11, 19));
                    eventLocations.add(event.getLocation());
                    DateTime start = event.getStart().getDateTime();
                    DateTime end = event.getEnd().getDateTime();
                    event.getStatus();
                    if (start == null) {
                        // All-day events don't have start times, so just use
                        // the start date.
                        start = event.getStart().getDate();
                    }
                    if (end == null) {
                        // All-day events don't have start times, so just use
                        // the start date.
                        end = event.getEnd().getDate();
                    }

                    if (start == null || end == null) {
                        Log.d("Error", "Error!");
                        continue;
                    }
                    //Current event found, busy is set to true
                    if (now.getValue() > start.getValue() + start.getTimeZoneShift() && now.getValue() < end.getValue() + end.getTimeZoneShift()) {
                        busy = true;
                        break;
                    }
                    else{
                        busy=false;
                    }

                    eventStrings.add(
                            String.format("%s (%s)\n", event.getSummary(), start));
                }
            }
            //Call the audio updater and notification manager
            actionSync();
            handler.postDelayed(runnableCode, 10000);
        }

    };


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Log.d("Error", "Network Error");
            Toast.makeText(MainActivity.this,"Network Error",Toast.LENGTH_LONG).show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(this,"Please Grant Permission for Contacts",REQUEST_PERMISSION_GET_ACCOUNTS,Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    String error ="This app requires Google Play Services. Please install. Google Play Services on your device and relaunch this app.";
                    Log.d("Error", error);
                } else {
                    getResultsFromApi();
                }
                break;

            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                Dialog dialog = apiAvailability.getErrorDialog(MainActivity.this,connectionStatusCode,REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Soundless")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        public List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            }
            catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */

        public List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            now = new DateTime(System.currentTimeMillis());
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            items = events.getItems();
            List<String> eventStrings = new ArrayList<>();
            eventLocations = new ArrayList<>();
            eventTimeStart = new ArrayList<>();
            eventTimeEnd = new ArrayList<>();
            eventNames = new ArrayList<>();

            for (Event event : items) {
                eventNames.add(event.getSummary());
                eventTimeStart.add(event.getStart().getDateTime().toString().substring(0,10) + " : " + event.getStart().getDateTime().toString().substring(11,19));
                eventTimeEnd.add(event.getEnd().getDateTime().toString().substring(0,10) + " : " + event.getEnd().getDateTime().toString().substring(11,19));
                eventLocations.add(event.getLocation());
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                event.getStatus();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                if (end == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    end = event.getEnd().getDate();
                }

                if (start == null || end == null) {
                    Log.d("Error", "Error!");
                    continue;
                }

                if (now.getValue() > start.getValue() + start.getTimeZoneShift() && now.getValue() < end.getValue() + end.getTimeZoneShift()) {
                    busy = true;
                    break;
                }
                else{
                    busy=false;
                }
                eventStrings.add(
                        String.format("%s (%s)\n", event.getSummary(), start));
            }
            return eventStrings;

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(List<String> output) {

            if (output == null || output.size() == 0) {
                String message = "No results returned.";
            }
            else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                listView.setAdapter(new CustomAdapter(MainActivity.this, eventNames, eventTimeStart,eventTimeEnd, eventLocations));
            }
        }

        //When clicked on cancel to select an Google Account
        @Override
        protected void onCancelled() {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    String error = "The following error occurred:\n" + mLastError.getMessage();
                    Log.d("Error",error);
                }
            } else {
                String error = "Request cancelled.";
                Log.d("Error", error);
            }
        }
    }

    /**
     * Fetches data from the calendar and sets the audio profile
     */
    void actionSync(){

        new MakeRequestTask(mCredential).execute();
        Log.d("Busy Mode", String.valueOf(busy));

        //The busy boolean is set true if there is current on-going activity
        //On current on-going activity, set the audio manager ringer mode to vibrate
        if(busy){
            if(!previousbusy) {
                notificationThrow("Audio Profile Changed to Vibrate");
                previousbusy=true;
            }
            audiomanager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
        //On current on-going activity, set the audio manager ringer mode to vibrate
        else if(!busy){
            if(previousbusy) {
                notificationThrow("Audio Profile Changed to Loud");
                previousbusy = false;
            }
            audiomanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    /**
     * Action to be performed on clicking the Sync Button
     */
    public void onClick(View v) {
        actionSync();
        Toast.makeText(MainActivity.this,"Sync done!",Toast.LENGTH_LONG).show();
    }


    /**
     * Notification builder function
     * @param message The string message to be passed to the notification that is generated
     */

    void notificationThrow(String message){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.soundless_96);
        mBuilder.setContentTitle("Soundless Alert");
        mBuilder.setContentText(message);
        mBuilder.setColor(getResources().getColor(R.color.purple));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());

    }

}