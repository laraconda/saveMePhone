package com.example.chicharo.call_blocker.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.chicharo.call_blocker.R;
import com.example.chicharo.call_blocker.fragments.contactsToBlockFragment;
import com.example.chicharo.call_blocker.fragments.recentsCallsToBlockFragment;

public class chooseContactsToBlock extends FragmentActivity {

    private ViewPager mPager;
    private android.support.v4.view.PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_number);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new contactsToBlockFragment();
            } else if (position == 1){
                return new recentsCallsToBlockFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
