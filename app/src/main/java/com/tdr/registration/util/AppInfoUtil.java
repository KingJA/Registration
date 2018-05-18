package com.tdr.registration.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tdr.registration.base.App;

/**
 * Created by Linus_Xie on 2016/9/19.
 */
public class AppInfoUtil {
    private static Context context = App.getContext();

    private static PackageInfo getAppInfo() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static String getPackageName() {
        return context.getPackageName();
    }

    public static String getVersionName() {
        return getAppInfo().versionName;
    }

    public static int getVersionCode() {
        return getAppInfo().versionCode;
    }

}

