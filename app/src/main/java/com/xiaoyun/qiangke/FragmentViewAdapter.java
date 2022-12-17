package com.xiaoyun.qiangke;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentViewAdapter extends FragmentPagerAdapter {

    public ArrayList<Home_Sub_View> views;

    public FragmentViewAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Home_Sub_View> views) {
        super(fm, behavior);
        this.views=views;
    }

    @Override
    public int getCount() {
        return this.views.size();
    }


    @Override
    public Fragment getItem(int position) {
        return this.views.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return views.get(position).getTitle();
    }
}
