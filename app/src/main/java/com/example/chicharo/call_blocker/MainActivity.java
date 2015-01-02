package com.example.chicharo.call_blocker;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_myBlackList = (Button)findViewById(R.id.btn_myBlackList);
        btn_myBlackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),myBlackList.class);
                startActivity(i);
                finish();
            }
        });
    }

}
