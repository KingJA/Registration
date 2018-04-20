package com.tdr.registration.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public HomePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
