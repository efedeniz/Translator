package com.example.efede.translator;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0 :
                fragment = new TranslateFragment();
                break;
            case 1:
                fragment = new WordListFragment();
                break;
             default:
                 fragment = null;

        }

        return fragment;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        CharSequence title = "";

        switch (position){
            case 0:
                title = "Translate";
                break;
            case 1:
                title = "Word List";
                break;
        }

        return title;
    }
}
