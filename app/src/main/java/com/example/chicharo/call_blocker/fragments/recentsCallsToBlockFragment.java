package com.example.chicharo.call_blocker.fragments;


import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chicharo.call_blocker.R;
import com.example.chicharo.call_blocker.activities.myBlackList;
import com.example.chicharo.call_blocker.adapters.ContactAdapter;
import com.example.chicharo.call_blocker.adapters.RecentCallAdapter;
import com.example.chicharo.call_blocker.dataBases.ContactsDataSource;
import com.example.chicharo.call_blocker.dataBases.PhonesDataSource;
import com.example.chicharo.call_blocker.models.ContactModel;
import com.example.chicharo.call_blocker.models.PhoneModel;
import com.example.chicharo.call_blocker.models.RecentCallModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class recentsCallsToBlockFragment extends Fragment implements RecentCallAdapter.onItemClickListener{

    List<RecentCallModel> recentCallModelList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_choose_recent_calls, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_blocked_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecentCallAdapter biValueAdapter = new RecentCallAdapter(getRecentCalls(20));
        biValueAdapter.SetOnItemClickListener(this);
        recyclerView.setAdapter(biValueAdapter);
        return rootView;
    }

    private List<RecentCallModel> getRecentCalls(int limit){
        recentCallModelList = new ArrayList<>(limit);
        Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;
        String[] fields = new String[]{
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE,
        };
        String sortOrder = String.format("%s limit " + limit + " ", CallLog.Calls.DATE + " DESC");
        Cursor cursor = getActivity().getContentResolver().query(queryUri, fields, null, null, sortOrder);
        while(cursor.moveToNext()){
            recentCallModelList.add(cursorToRecentCall(cursor));
        }
        return recentCallModelList;
    }

    private RecentCallModel cursorToRecentCall(Cursor cursor){
        RecentCallModel recentCallModel = new RecentCallModel();
        recentCallModel.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
        if (cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
            recentCallModel.setHeader(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
        } else {
            recentCallModel.setHeader(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
        }
        Date callDayTime = new Date(Long.valueOf(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))));
        recentCallModel.setDate(callDayTime.toString());
        return recentCallModel;
    }

    public void addCallToBlockedNumbers(RecentCallModel call){
        getContactByPhone(call.getNumber());
        PhonesDataSource phonesDataSource = new PhonesDataSource(getActivity());
        phonesDataSource.open();
        phonesDataSource.blockNumber(call.getNumber());
        phonesDataSource.close();
    }

    public ContactModel getContactByPhone(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor contact_cursor = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (contact_cursor.moveToFirst()){
            ContactModel contactModel = new ContactModel();
            String id = contact_cursor.getString(contact_cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor pCur = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
            while (pCur.moveToNext())
            {
                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactModel.addPhoneNumber(contactNumber);
            }
            pCur.close();
            contactModel.setContactName(contact_cursor.getString(contact_cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
            contact_cursor.close();
            return contactModel;
        }
        contact_cursor.close();
        return null;
    }

    @Override
    public void onItemClick(View v, int position) {
        ContactModel contactModel = getContactByPhone(recentCallModelList.get(position).getNumber());
        if (contactModel == null) {
            addCallToBlockedNumbers(recentCallModelList.get(position));
        } else {
            addContactToBlockedContacts(contactModel);
        }
        Intent startMyBlackList = new Intent(getActivity(), myBlackList.class);
        startActivity(startMyBlackList);
        getActivity().finish();
    }

    public void addContactToBlockedContacts(ContactModel contact) {
        ContactsDataSource contactsDataSource = new ContactsDataSource(getActivity());
        contactsDataSource.open();
        contactsDataSource.addBlockedContact(contact);
        contactsDataSource.close();
    }

}
