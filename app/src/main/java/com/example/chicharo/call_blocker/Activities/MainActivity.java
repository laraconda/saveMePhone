package com.example.chicharo.call_blocker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.chicharo.call_blocker.R;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_myBlackList = (Button)findViewById(R.id.btn_myBlackList);
        btn_myBlackList.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_myBlackList){
            Intent i = new Intent(getApplicationContext(),myBlackList.class);
            startActivity(i);
            finish();
        }
    }
}
