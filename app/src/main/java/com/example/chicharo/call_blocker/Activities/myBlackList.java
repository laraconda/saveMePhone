package com.example.chicharo.call_blocker.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.chicharo.call_blocker.DataBases.PhonesDataSource;
import com.example.chicharo.call_blocker.Models.PhoneModel;
import com.example.chicharo.call_blocker.R;
import java.util.List;

public class myBlackList extends ListActivity implements View.OnClickListener{
    PhonesDataSource phonesDataSource;
    PhoneModel phoneModel;
    private static final String regexIsAValidPhoneNumber = "^[0-9]{8,12}$";
    private EditText editTextAddNewPhone;
    private ArrayAdapter<PhoneModel> BlockedPhonesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myblacklist_layout);
        phonesDataSource = new PhonesDataSource(this);
        phonesDataSource.open();
        List<PhoneModel> values = phonesDataSource.getAllComments();
        BlockedPhonesAdapter = new ArrayAdapter<PhoneModel>(this,android.R.layout.simple_list_item_1,values);
        setListAdapter(BlockedPhonesAdapter);
        editTextAddNewPhone = (EditText)findViewById(R.id.edit_newPhone);
        Button btnAddNewPhone = (Button)findViewById(R.id.btn_newPhone);
        btnAddNewPhone.setOnClickListener(this);
        Button btnDeleletePhone = (Button)findViewById(R.id.btn_DeleteAll);
        btnDeleletePhone.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_newPhone) {
            if(editTextAddNewPhone.getText().toString().matches(regexIsAValidPhoneNumber)) {
                phoneModel = phonesDataSource.createPhone(editTextAddNewPhone.getText().toString());
                BlockedPhonesAdapter.add(phoneModel);
            } else {
                Toast.makeText(getApplicationContext(), "This is not a valid phone number", Toast.LENGTH_SHORT).show();
            }
        } else if(v.getId() == R.id.btn_DeleteAll){
            phonesDataSource.deleteAll();
            BlockedPhonesAdapter.clear();
        }
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
