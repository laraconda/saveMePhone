package com.example.chicharo.call_blocker.Listener;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.chicharo.call_blocker.DataBases.PhonesDataSource;
import com.example.chicharo.call_blocker.R;

import java.lang.reflect.Method;

//TODO: change variables names. They need to be better.
public class CallBlocker extends BroadcastReceiver {

    private NotificationManager notificationManager;
    private TelephonyManager telephonyManager;
    private ITelephony telephonyService;
    private static final String settingSharedPreferencesName = "Settings";
    private static final String allowHiddenNumbers = "allowHiddenNumbers";
    private static final String intentStringExtra = "state";

    @Override
    public void onReceive(Context context, Intent intent) {

        Boolean acceptCallFromHiddenNumbers=acceptCallFromHiddenNumbers(context);
        if(intent.getStringExtra("state").equals("RINGING")) {
            String incomingNumber = intent.getStringExtra("incoming_number");
            Log.d("incoming number: ", incomingNumber);
            Bundle bb = intent.getExtras();
            if(acceptCallFromHiddenNumbers){
                if(isInOwnBlackList(context,incomingNumber)){
                    Log.d("isInOwnBlackList", "true");
                    blockCall(context, bb);
                } else {
                    if(/*is in online blackList*/ true){
                        if(!isAContact(context,incomingNumber)) {
                            blockCall(context, bb);
                        }
                    }
                }
            } else {
                if(incomingNumber==null){
                    blockCall(context, bb);
                } else {
                    if(isInOwnBlackList(context,incomingNumber)){
                        blockCall(context, bb);
                    } else {
                        if(/*is in online blackList*/ true){
                            if(!isAContact(context,incomingNumber)) { //is a contact?
                                blockCall(context, bb);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean acceptCallFromHiddenNumbers(Context context){
        SharedPreferences settings = context.getSharedPreferences(settingSharedPreferencesName, Context.MODE_PRIVATE);
        boolean allowCallFromHiddenNumbers = settings.getBoolean(allowHiddenNumbers, true);
        return allowCallFromHiddenNumbers;
    }

    private boolean isInOwnBlackList(Context context,String incomingNumber){
        PhonesDataSource phonesDataSource = new PhonesDataSource(context);
        phonesDataSource.open();
        Boolean boolIsInOwnBlackList = phonesDataSource.isInOwnBlackList(incomingNumber);
        phonesDataSource.close();
        return boolIsInOwnBlackList;
    }

    private boolean isAContact(Context context, String incomingNumber){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
        Cursor phone = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        return phone.getCount()!=0;
    }

    //TODO: change bundle to phone number only.
    public void blockCall(Context c, Bundle b){
        TelephonyManager telephony = (TelephonyManager)
                c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class cls = Class.forName(telephony.getClass().getName());
            Method m = cls.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
            telephonyService.silenceRinger(); //Cosa rara
            String Inumber = b.getString("incoming_number");
            showNotification(c, Inumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Add date and hour of the call
    private void showNotification(Context context, String number) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Set the icon, scrolling text and timestamp
        Notification callBlockedNotification = new Notification.Builder(context)
                .setContentTitle("Bloqueamos una llamada peligrosa")
                .setContentText("numero : "+number)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(0)
                .getNotification();

        notificationManager.notify(R.string.remote_service_started, callBlockedNotification);
    }

}
