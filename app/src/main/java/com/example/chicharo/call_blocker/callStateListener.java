package com.example.chicharo.call_blocker;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Listener to detect incoming calls.
 */
public class callStateListener extends PhoneStateListener {
    Context ctx;
    public callStateListener(Context context){
        this.ctx = context;
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                // called when someone is ringing to this phone

                Toast.makeText(ctx,
                        "Incoming: "+incomingNumber,
                        Toast.LENGTH_LONG).show();
                break;
        }
    }
}