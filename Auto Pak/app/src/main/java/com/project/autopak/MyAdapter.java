package com.project.autopak;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.autopak.fragments.ShopkeeperFragment;
import com.project.autopak.fragments.WholesellerFragment;

public class MyAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ShopkeeperFragment shopkeeperFragment = new ShopkeeperFragment();
                return shopkeeperFragment;
            case 1:
                WholesellerFragment wholesellerFragment = new WholesellerFragment();
                return wholesellerFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}