package com.tdr.registration.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.tdr.registration.util.mLog;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * 项目名称：物联网城市防控(警用版)
 * 类描述：TODO
 * 创建人：KingJA
 * 创建时间：2016/4/1 14:56
 * 修改备注：
 */
public class Util {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    private static PackageInfo getAppInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static int getVersionCode(Context context) {
        return getAppInfo(context).versionCode;
    }

    public static String getVersionName(Context context) {
        return getAppInfo(context).versionName;
    }

    public static boolean ifNeedUpdate(int versionCode, Context context) {
        mLog.e("versionCode="+versionCode+"   getVersionCode="+getVersionCode(context));
        return versionCode > getVersionCode(context);
    }

    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            Log.w("StorageUtils", "Can't define system cache directory! The app should be re-installed.");
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        //File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        File appCacheDir = new File(dataDir, context.getPackageName());
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

}
