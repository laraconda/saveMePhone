package com.example.chicharo.call_blocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.provider.ContactsContract;


public class MainActivity extends ActionBarActivity {

    BroadcastReceiver CallBlocker;
    String dummy;

    public static final String ACTION_ANSWER = "answer";
    public static final String ACTION_IGNORE = "ignore";

    Runnable runnable =  new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(getApplicationContext(), IncomingCallListener.class);
            getApplicationContext().startService(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("You","aint gonna die");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cursor contacts = getContentResolver().query(lookupUri, new String[]{ContactsContract.Contacts.DISPLAY_NAME});
        /*Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode("5554968900"));*/
        //ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        //phones.get
        //int Ccount = phones.getColumnCount();

        /*for (Integer i=0;Ccount>i;i++){
            Log.d("column at:"+i,phones.getColumnName(i));
        }*/
        //Cursor c = getContentResolver().query(ContactsContract., projection, null, null, People.NAME + " ASC");
        //Log.d("lookupUri getQuery",lookupUri.getQuery());
            /*Cursor pCur = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{id}, null);
            while (pCur.moveToNext()) {
                // Do something with phones
            }
            pCur.close();
        }*/
        //performOnBackgroundThread(runnable);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }


}
