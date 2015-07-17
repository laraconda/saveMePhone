package com.example.chicharo.call_blocker.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
    private android.support.v4.app.FragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_number);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        mPager = (ViewPager)findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
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
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "First Tab";
                case 1:
                default:
                    return "Second Tab";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
