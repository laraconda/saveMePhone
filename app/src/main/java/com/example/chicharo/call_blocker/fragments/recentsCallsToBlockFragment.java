package com.example.chicharo.call_blocker.fragments;


import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chicharo.call_blocker.R;
import com.example.chicharo.call_blocker.adapters.ContactAdapter;
import com.example.chicharo.call_blocker.adapters.RecentCallAdapter;
import com.example.chicharo.call_blocker.models.RecentCallModel;

import java.util.ArrayList;
import java.util.List;


public class recentsCallsToBlockFragment extends Fragment implements ContactAdapter.onItemClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_choose_recent_calls, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_blocked_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecentCallAdapter biValueAdapter = new RecentCallAdapter(getRecentCalls(20));
        recyclerView.setAdapter(biValueAdapter);
        return rootView;
    }

    private List<RecentCallModel> getRecentCalls(int limit){
        List<RecentCallModel> recentCallModelList = new ArrayList<>(limit);
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
        if (cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
            recentCallModel.setHeader(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
        } else {
            recentCallModel.setHeader(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
        }
        recentCallModel.setDate(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
        return recentCallModel;
    }

    @Override
    public void onItemClick(View v, int position) {
        //storeContact(contactModel, numbers);
    }


}
