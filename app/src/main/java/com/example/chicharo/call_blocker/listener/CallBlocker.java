package com.example.chicharo.call_blocker.listener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.chicharo.call_blocker.activities.MainActivity;
import com.example.chicharo.call_blocker.dataBases.PhonesDataSource;
import com.example.chicharo.call_blocker.R;

import java.lang.reflect.Method;
import java.util.Calendar;

public class CallBlocker extends BroadcastReceiver {

    NotificationManager mNM;
    TelephonyManager telephonyManager;
    ITelephony telephonyService;

    private static final String SETTINGS_SHARED_PREFERENCES_NAME = "Settings";
    private static final String ALLOW_HIDDEN_NUMBERS = "allowHiddenNumbers";

    public final String INCOMING_NUMBER = "incoming_number";
    public final String EXTRA_STRING_STATE = "state";
    public final String PHONE_STATE_RINGING = "RINGING";

    @Override
    public void onReceive(Context context, Intent intent) {
        String incomingNumber = intent.getStringExtra(INCOMING_NUMBER);
        boolean isNumberAContact = isAContact(context,incomingNumber);
        boolean blockHiddenNumbers = blockCallFromHiddenNumbers(context);
        boolean isInOwnBlackList = isInOwnBlackList(context,incomingNumber);

        if(intent.getStringExtra(EXTRA_STRING_STATE).equals(PHONE_STATE_RINGING)) {
        Bundle bb = intent.getExtras();
            if(!blockHiddenNumbers){
                if(isInOwnBlackList){
                    blockCall(context, bb);
                } else {
                    if(/*is in blackList*/ false){
                        if(!isNumberAContact) { //is a contact?
                            blockCall(context, bb);
                        }
                    }
                }
            }
            else {
                if(incomingNumber==null){
                    blockCall(context, bb);
                } else {
                    if(isInOwnBlackList){
                        blockCall(context, bb);
                    } else {
                        if(/*is in blackList*/ false){
                            if(!isNumberAContact) { //is a contact?
                                blockCall(context, bb);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean blockCallFromHiddenNumbers(Context context) {
        SharedPreferences settings = context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        boolean blockCallFromHiddenNumbers = false;
        try{
            blockCallFromHiddenNumbers = settings.getBoolean(ALLOW_HIDDEN_NUMBERS, true);
        } catch(Exception e) {
            System.out.print("error" + e);
        }
        return blockCallFromHiddenNumbers;
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

    private void showNotification(Context context, String number, Long curent_time) {
        String content_title = context.getResources().getString(R.string.notification_block_call_content_title);
        String content_text = context.getResources().getString(R.string.notification_block_call_content_text);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if(number == null){
            content_title = context.getResources().getString(R.string.notification_block_call_private_content_title);
            content_text = context.getResources().getString(R.string.notification_block_call_private_content_text);
            number = "";
        }

        Notification notification = new Notification.Builder(context)
                .setContentTitle(content_title)
                .setContentText(content_text + number)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setWhen(curent_time)
                .getNotification();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }

}
