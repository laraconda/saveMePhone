package com.example.chicharo.call_blocker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.example.chicharo.call_blocker.DataBase.PhonesDataSource;

import java.lang.reflect.Method;
import java.util.Calendar;

public class CallBlocker extends BroadcastReceiver {

    NotificationManager mNM;
    TelephonyManager telephonyManager;
    ITelephony telephonyService;
    Boolean acceptCallFromHiddenNumbers=false; //false by default
    public final String INCOMING_NUMBER = "incoming_number";
    public final String EXTRA_STRING_STATE = "state";
    public final String PHONE_STATE_RINGING = "RINGING";
    public final String CONTENT_TITLE = "Bloqueamos pelgrosa llamada";
    public final String CONTENT_TEXT = ""; // Dont forget the last blank space
    @Override
    public void onReceive(Context context, Intent intent) {
        String incomingNumber = intent.getStringExtra(INCOMING_NUMBER);
        if(intent.getStringExtra(EXTRA_STRING_STATE).equals(PHONE_STATE_RINGING)) {
        Bundle bb = intent.getExtras();
            if(acceptCallFromHiddenNumbers){
                if(isInOwnBlackList(context,incomingNumber)){
                    blockCall(context, bb);
                } else {
                    if(/*is in blackList*/ false){
                        if(!isAContact(context,incomingNumber)) { //is a contact?
                            blockCall(context, bb);
                        }
                    }
                }
            }
            else {
                if(intent.getStringExtra(INCOMING_NUMBER)==null){
                    blockCall(context, bb);
                } else {
                    if(isInOwnBlackList(context,incomingNumber)){
                        blockCall(context, bb);
                    } else {
                        if(/*is in blackList*/ false){
                            if(!isAContact(context,incomingNumber)) { //is a contact?
                                blockCall(context, bb);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isInOwnBlackList(Context context,String incomingNumber){
        PhonesDataSource pDS = new PhonesDataSource(context);
        pDS.open();
        Boolean bool = pDS.isInOwnBlackList(incomingNumber);
        pDS.close();

        return bool;
    }

    private boolean isAContact(Context context, String incomingNumber){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
        Cursor phone = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        return phone.getCount()!=0;
    }

        public void blockCall(Context ctx, Bundle b){
            TelephonyManager telephony = (TelephonyManager)
                    ctx.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                Class cls = Class.forName(telephony.getClass().getName());
                Method m = cls.getDeclaredMethod("getITelephony");
                m.setAccessible(true);
                telephonyService = (ITelephony) m.invoke(telephony);
                //telephonyService.silenceRinger();
                telephonyService.endCall();
                String incoming_number = b.getString(INCOMING_NUMBER);
                Long current_time = getCurrentTime();
                showNotification(ctx, incoming_number, current_time);

            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    public Long getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        Long current_time = calendar.getTime().getTime();
        return current_time;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification(Context context, String number, Long curent_time) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(CONTENT_TITLE)
                .setContentText(CONTENT_TEXT + number)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setWhen(curent_time)
                .getNotification();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }

}
