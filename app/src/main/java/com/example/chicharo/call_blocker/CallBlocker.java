package com.example.chicharo.call_blocker;

import com.android.internal.telephony.ITelephony;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * Created by chicharo on 30/12/14.
 */
public class CallBlocker extends BroadcastReceiver {

    NotificationManager mNM;
    TelephonyManager telephonyManager;
    ITelephony telephonyService;
    String number;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("Caller (from intent)","numero: "+intent.getStringExtra("incoming_number"));
        Bundle bb = intent.getExtras();
        blockCall(context,bb);

    }
        public void blockCall(Context c, Bundle b){
            TelephonyManager telephony = (TelephonyManager)
                    c.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                Class cls = Class.forName(telephony.getClass().getName());
                Method m = cls.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                telephonyService = (ITelephony) m.invoke(telephony);
                //telephonyService.silenceRinger();
                telephonyService.endCall();
                if (null != b.getString("incoming_number")){
                    number = b.getString("incoming_number");
                    showNotification(c);
                }

                Log.d("Caller (from bb)","numero: "+number);
                //Log.d("Colgar :",b.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(Context context) {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        //CharSequence text = getText(R.string.remote_service_started);
        mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification.Builder(context)
                .setContentTitle("We just canceled a call")
                .setContentText(number)
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
