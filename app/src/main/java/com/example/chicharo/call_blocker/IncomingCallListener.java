package com.example.chicharo.call_blocker;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by chicharo on 29/12/14.
 */
public class IncomingCallListener extends IntentService {
    TelephonyManager tm;
    NotificationManager mNM;

    public IncomingCallListener() {
        super("IncomingCallListener");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service","remote_service_started(Command)");

        PhoneStateListener phon = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        // called when someone is ringing to this phone

                        Toast.makeText(getApplicationContext(),
                                "Incoming: "+incomingNumber,
                                Toast.LENGTH_LONG).show();
                        Log.d("Incoming",String.valueOf(incomingNumber));
                        break;
                }
            }
        };

        tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen( phon, PhoneStateListener.LISTEN_CALL_STATE);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Log.d("Service","remote_service_started");

        // Display a notification about us starting.
        showNotification();
    }

    @Override
    public void onDestroy() {
        Log.d("Service","remote_service_stopped");
        // Cancel the persistent notification.
        mNM.cancel(R.string.remote_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "MyCustomService Handling Intent", Toast.LENGTH_LONG).show();
        // INSERT THE WORK TO BE DONE HERE
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.remote_service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification.Builder(getApplicationContext())
            .setContentTitle(getText(R.string.remote_service_label))
            .setSmallIcon(R.drawable.ic_launcher)
            .setWhen(0)
            .build(); //Require API 16

        // The PendingIntent to launch our activity if the user selects this notification
        /*PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);*/

        // Set the info for the views that show in the notification panel.
        /*notification.setLatestEventInfo(this, getText(R.string.remote_service_label),
                text, contentIntent);*/

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
         mNM.notify(R.string.remote_service_started, notification);
        //startForeground(1234,notification);
    }


}
