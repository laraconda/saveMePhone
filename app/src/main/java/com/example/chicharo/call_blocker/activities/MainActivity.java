package com.example.chicharo.call_blocker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.chicharo.call_blocker.R;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String SETTINGS_SHARED_PREFERENCES_NAME = "Settings";
    private static final String ALLOW_HIDDEN_NUMBERS = "allowHiddenNumbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBlockHiddenNumbersSharedPreferences();
        Button btn_myBlackList = (Button)findViewById(R.id.btn_myBlackList);
        Switch switch_hidden_numbers = (Switch)findViewById(R.id.switch_hidden_numbers);
        switch_hidden_numbers.setChecked(acceptCallFromHiddenNumbers());
        btn_myBlackList.setOnClickListener(this);
        switch_hidden_numbers.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_myBlackList){
            Intent i = new Intent(getApplicationContext(),myBlackList.class);
            startActivity(i);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("isChecked", String.valueOf(isChecked));
        setBlockHiddenNumbersSharedPreferences(isChecked);
    }

    public void setBlockHiddenNumbersSharedPreferences(boolean isChecked){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(ALLOW_HIDDEN_NUMBERS, isChecked);
        editor.apply();
    }

    public void checkBlockHiddenNumbersSharedPreferences(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        try{
            settings.getBoolean(ALLOW_HIDDEN_NUMBERS, true);
        } catch(Exception e) {
            setBlockHiddenNumbersSharedPreferences(true);
        }
    }

    private boolean acceptCallFromHiddenNumbers() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        boolean allowCallFromHiddenNumbers = false;
        try{
            allowCallFromHiddenNumbers = settings.getBoolean(ALLOW_HIDDEN_NUMBERS, true);
        } catch(Exception e) {
            System.out.print("error" + e);
        }
        return allowCallFromHiddenNumbers;
    }
}
