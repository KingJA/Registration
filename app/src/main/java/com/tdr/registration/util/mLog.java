package com.tdr.registration.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/4/14.
 */

public class mLog {
    private static String TAG="Pan";
    private static String TAG2="BleLog";

    private static boolean isShowLog=true;
    public static void i(String msg){
        if(isShowLog){
            Log.i(TAG,msg);
        }
    }
    public static void i(String tag,String msg){
        if(isShowLog){
            Log.i(tag,msg);
        }
    }
    public static void e(String msg){
        if(isShowLog){
            Log.e(TAG,msg);
        }
    }
    public static void e(String tag,String msg){
        if(isShowLog){
            Log.e(tag,msg);
        }
    }
    public static void d(String msg){
        if(isShowLog){
            Log.d(TAG,msg);
        }
    }
    public static void d(String tag,String msg){
        if(isShowLog){
            Log.d(tag,msg);
        }
    }
    public static void v(String msg){
        if(isShowLog){
            Log.v(TAG,msg);
        }
    }
    public static void v(String tag,String msg){
        if(isShowLog){
            Log.v(tag,msg);
        }
    }
    public static void w(String msg){
        if(isShowLog){
            Log.w(TAG,msg);
        }
    }
    public static void w(String tag,String msg){
        if(isShowLog){
            Log.w(tag,msg);
        }
    }
    public static void w(String tag,String msg,Throwable throwable){
        if(isShowLog){
            Log.w(tag,msg,throwable);
        }
    }
    public static void BLE(String msg){
        if(isShowLog){
            Log.e(TAG2,msg);
        }
    }
}
