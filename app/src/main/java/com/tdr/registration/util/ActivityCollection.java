package com.tdr.registration.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linus_Xie on 2016/9/7.
 */
public class ActivityCollection {
    private final static List<Activity> activityList = new ArrayList<>();

    public static List<Activity> getList() {
        return activityList;
    }

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityList.clear();
    }

    public static int getLeftSize() {
        return activityList.size();
    }

    public static void refreshAllActivity() {
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }
}
