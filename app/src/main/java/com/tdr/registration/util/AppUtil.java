package com.tdr.registration.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.DisplayMetrics;

import com.tdr.registration.base.MyApplication;

import java.util.List;

public class AppUtil {
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /**
     * dp转换为px
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * px转换为dp
     *
     * @param px
     * @return
     */
    public static int px2dp(int px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     *判断当前应用程序处于前台还是后台
     */
    public static boolean isAppForeground() {
        android.app.ActivityManager am = (android.app.ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(MyApplication.getContext().getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
