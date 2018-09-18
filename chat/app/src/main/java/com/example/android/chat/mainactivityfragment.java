package com.example.android.uni;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class mainactivityfragment extends AppCompatActivity {
    ViewPager viewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new myadapter(fragmentManager));
    }

    class myadapter extends FragmentStatePagerAdapter {

        public myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (position == 0) {

                fragment = new chatactivityfragment();
            }
            if (position == 1) {
                fragment = new photosfragment();

            }
            if(position==2){
                fragment=new discussion();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}