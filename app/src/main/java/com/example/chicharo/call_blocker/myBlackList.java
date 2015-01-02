package com.example.chicharo.call_blocker;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import com.example.chicharo.call_blocker.DataBase.PhonesDataSource;
import com.example.chicharo.call_blocker.Models.phone_model;

public class myBlackList extends ListActivity {
    PhonesDataSource phonesDataSource;
    phone_model pm;
    final String regexStr = "^[0-9]{8,12}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myblacklist_layout);

        phonesDataSource = new PhonesDataSource(this);
        phonesDataSource.open();
        List<phone_model> values = phonesDataSource.getAllComments();
        final ArrayAdapter<phone_model> adapter = new ArrayAdapter<phone_model>(this,android.R.layout.simple_list_item_1,values);

        //ListView ownBlackList = (ListView) findViewById(R.id.list_myBlackList);
        setListAdapter(adapter);

        final EditText editNewPhone = (EditText)findViewById(R.id.edit_newPhone);
        Button btnNewPhone = (Button)findViewById(R.id.btn_newPhone);

        btnNewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editNewPhone.getText().toString().matches(regexStr)) {
                    pm = phonesDataSource.createPhone(editNewPhone.getText().toString());
                    adapter.add(pm);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter " + "\n" + " valid phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnDel = (Button)findViewById(R.id.btn_DeleteAll);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonesDataSource.deleteAll();
                adapter.clear();
            }
        });
    }
    @Override
    protected void onResume() {
        phonesDataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        phonesDataSource.close();
        super.onPause();
    }
}
