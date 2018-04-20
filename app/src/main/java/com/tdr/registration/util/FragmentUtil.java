package com.tdr.registration.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.tdr.registration.R;
import com.tdr.registration.fragment.BusinessFragment;
import com.tdr.registration.fragment.InspectFragment;
import com.tdr.registration.fragment.SettingFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment工具类
 * Created by Linus_Xie on 2016/9/14.
 */
public class FragmentUtil {
    private static Map<Integer, Fragment> fragmentMap = new HashMap<>();

//    public static Fragment switchFragment(FragmentActivity activity, Fragment currentFragment, Fragment newFragment) {
//        FragmentTransaction mTransaction = activity.getSupportFragmentManager().beginTransaction();
//        if (!newFragment.isAdded()) {
//            mTransaction.hide(currentFragment).add(R.id.frame_main, newFragment).commit();
//        } else {
//            mTransaction.hide(currentFragment).show(newFragment).commit();
//        }
//        return newFragment;
//    }

    public static Fragment getFragment(int position) {
        Fragment fragment = fragmentMap.get(position);
        if (fragment != null) {
            return fragment;
        } else {
            switch (position) {
                case 0:
                    fragment = new BusinessFragment();
                    break;
                case 1:
                    fragment = new InspectFragment();
                    break;
                case 2:
                    fragment = new SettingFragment();
                    break;
            }
            fragmentMap.put(position, fragment);
            return fragment;
        }
    }
}
