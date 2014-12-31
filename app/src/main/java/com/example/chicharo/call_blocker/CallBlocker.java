package com.example.chicharo.call_blocker;

import com.android.internal.telephony.ITelephony;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    Boolean acceptCallFromHiddenNumbers=false; //false by default

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("Caller (from intent)","numero: "+intent.getStringExtra("incoming_number"));
        //Log.d("estado; ",intent.getStringExtra("state"));
        if(intent.getStringExtra("state").equals("RINGING")) {
        Bundle bb = intent.getExtras();
            if(acceptCallFromHiddenNumbers){
                if(/*is in own blackList*/false){
                    blockCall(context, bb);
                } else {

                    if(/*is in blackList*/ true){
                        if(!isAContact(context,intent)) { //is a contact?
                            blockCall(context, bb);
                        }

                    }
                }

            }
            else {
                if(intent.getStringExtra("incoming_number")==null){
                    blockCall(context, bb);
                } else {
                    if(/*is in own blackList*/false){
                        blockCall(context, bb);
                    } else {

                        if(/*is in blackList*/ true){
                            if(!isAContact(context,intent)) { //is a contact?
                                blockCall(context, bb);
                            }

                        }
                    }
                }

            }

        }

    }

    private boolean isAContact(Context context, Intent intent){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(intent.getStringExtra("incoming_number")));
        Cursor phone = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        return phone.getCount()!=0;
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
                    number = b.getString("incoming_number");
                    showNotification(c);

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
                .setContentTitle("Bloqueamos una llamada peligrosa")
                .setContentText("numero : "+number)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(0)
                .getNotification(); //Require API 16
                //getNotification()

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
