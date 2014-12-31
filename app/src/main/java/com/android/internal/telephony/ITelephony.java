package com.android.internal.telephony;

/**
 * Created by chicharo on 30/12/14.
 */
public interface ITelephony {

    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
