package com.tdr.registration.update;

import com.tdr.registration.util.SharedPreferencesUtils;

/**
 * Description：TODO
 * Create Time：2016/9/26 13:30
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Constants {
    /*========================================Download Variable========================================*/
    public static final String DOWNLOAD_URL = "http://dmi.tdr-cn.com/newestapk/";// APK下载地址
    public static final String APK_NAME = SharedPreferencesUtils.get("apkName", "") + ".apk";
    public static final String FILE_NAME = "FileName";
    public static final String ANDROID_VERSIONCODE = "android:versionCode";
}
