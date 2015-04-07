package com.example.chicharo.call_blocker.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.chicharo.call_blocker.R;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String settingSharedPreferencesName = "Settings";
    private static final String allowHiddenNumbers = "allowHiddenNumbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAllowHiddenNumbersSharedPreferences();
        Button btn_myBlackList = (Button)findViewById(R.id.btn_myBlackList);
        ToggleButton tggbtn_allowHiddenNumbers = (ToggleButton)findViewById(R.id.tggbtn_HiddenNumbers);
        tggbtn_allowHiddenNumbers.setChecked(acceptCallFromHiddenNumbers());
        btn_myBlackList.setOnClickListener(this);
        tggbtn_allowHiddenNumbers.setOnCheckedChangeListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_myBlackList){
            Intent i = new Intent(getApplicationContext(),myBlackList.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("isChecked", String.valueOf(isChecked));
        setAllowHiddenNumbersSharedPreferences(isChecked);
    }

    public void setAllowHiddenNumbersSharedPreferences(boolean isChecked){
        SharedPreferences pref = getApplicationContext().getSharedPreferences(settingSharedPreferencesName, Context.MODE_PRIVATE); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(allowHiddenNumbers, isChecked);
        editor.commit();
    }

    public void checkAllowHiddenNumbersSharedPreferences(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences(settingSharedPreferencesName, Context.MODE_PRIVATE);
        try{
            settings.getBoolean(allowHiddenNumbers, true);
        } catch(Exception e) {
            setAllowHiddenNumbersSharedPreferences(false);
        }
    }

    private boolean acceptCallFromHiddenNumbers() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(settingSharedPreferencesName, Context.MODE_PRIVATE);
        boolean allowCallFromHiddenNumbers = false;
        try{
            allowCallFromHiddenNumbers = settings.getBoolean(allowHiddenNumbers, true);
        } catch(Exception e) {
        }
        return allowCallFromHiddenNumbers;
    }
}
