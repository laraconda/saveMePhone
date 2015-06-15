package com.example.chicharo.call_blocker.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by chicharo on 30/12/14.
 * No es necesario 'iniciar' el receiver callBlocker. Lo hace solo.
 */
public class BootCompleted extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*CallBlocker callBlocker = new CallBlocker();
        IntentFilter intentFilter = new IntentFilter(intent.ACTION_ANSWER);
        context.registerReceiver(callBlocker, intentFilter);*/
    }
}
