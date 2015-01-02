package com.example.chicharo.call_blocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by chicharo on 30/12/14.
 * No es necesario 'iniciar' el receiver callBlocker. Lo hace solo.
 */
public class BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //CallBlocker callBlocker = new CallBlocker();
        //IntentFilter intentFilter = new IntentFilter(intent.ACTION_ANSWER);
        //context.registerReceiver(callBlocker,intentFilter);
        Log.d("Boot", "launching ACTION_ANSWER");
    }
}
