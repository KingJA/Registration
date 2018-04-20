package com.tdr.registration.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Linus_Xie on 2016/9/17.
 */
public class ActivityUtil {
    public static void goActivity(Activity activity, Class clazz) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public static void goActivityAndFinish(Activity activity, Class clazz) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void goActivityWithBundle(Activity activity, Class clazz, Bundle bundle) {
        Intent intent = new Intent(activity,
                clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
    }

    public static void goActivityForResult(Activity activity, Class clazz, int requestID) {
        Intent intent = new Intent(activity,
                clazz);
        activity.startActivityForResult(intent, requestID);
    }

    public static void goActivityForResultWithBundle(Activity activity, Class clazz, Bundle bundle, int requestID) {
        Intent intent = new Intent(activity,
                clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestID);
    }

}
