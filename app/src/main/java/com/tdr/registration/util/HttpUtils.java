package com.tdr.registration.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtils {
    public static final String Success = "onSuccess";
    public static final String Error = "onError";
    public static final String Cancelled = "onCancelled";
    public static final String Finished = "onFinished";
    private static final String TAG = "HttpUtils";

    public static Callback.Cancelable get(RequestParams RP, final HttpCallBack httpcallback) {
        RP.addHeader("accessToken", (String) SharedPreferencesUtils.get("token", ""));
//        //设置联网超时时间
        RP.setConnectTimeout(5000);
//        //设置重试次数
        RP.setMaxRetryCount(3);
        Callback.Cancelable cancelable = x.http().get(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Logger.json(result);
                Log.e(TAG, "onSuccess: "+result);
                httpcallback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.onError(ex);
                if (ex.getMessage().contains("Network is unreachable")) {
                    Utils.showToast("网络连接中断！请检查您的网络。");
                }
                if (ex.getMessage().contains("timeout")) {
                    Utils.showToast("访问网络超时。请检查网络状况并联系相关工作人员。");
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
        return cancelable;
    }

    public static void post(RequestParams RP, final HttpPostCallBack httpcallback) {
        mLog.e("Token:"+(String) SharedPreferencesUtils.get("token", ""));
        RP.addHeader("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        //设置联网超时时间
        RP.setConnectTimeout(5000);
        //设置重试次数
        RP.setMaxRetryCount(3);
        x.http().post(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                httpcallback.postcallback(Success, result);
                Log.d(TAG,"onSuccess:" + result);
                Logger.json(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.postcallback(Error, "");
                if(ex.getMessage().contains("Network is unreachable")){
                    Utils.showToast("网络连接中断！请检查您的网络。");
                }
                if(ex.getMessage().contains("timeout")){
                    Utils.showToast("访问网络超时。请检查网络状况并联系相关工作人员。");
                }
                mLog.e("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                httpcallback.postcallback(Cancelled, "");
                mLog.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                httpcallback.postcallback(Finished, "");
                mLog.e("onFinished");
            }
        });
    }

    public static void postK(RequestParams RP, final HttpCallBack httpcallback) {
        mLog.e("Token:"+(String) SharedPreferencesUtils.get("token", ""));
        RP.addHeader("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        //设置联网超时时间
        RP.setConnectTimeout(5000);
        //设置重试次数
        RP.setMaxRetryCount(3);
        x.http().post(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                mLog.e("onSuccess:" + result);
                Logger.json(result);
                httpcallback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.onError(ex);
                if(ex.getMessage().contains("Network is unreachable")){
                    Utils.showToast("网络连接中断！请检查您的网络。");
                }
                if(ex.getMessage().contains("timeout")){
                    Utils.showToast("访问网络超时。请检查网络状况并联系相关工作人员。");
                }
              Logger.d(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mLog.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                mLog.e("onFinished");
            }
        });
    }


    public static void UploadFile(RequestParams RP, final HttpPostUpLoadFileCallBack httpcallback) {
        mLog.e("Token:"+(String) SharedPreferencesUtils.get("token", ""));
        RP.addHeader("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        //设置联网超时时间
        RP.setConnectTimeout(5000);
        //设置重试次数
        RP.setMaxRetryCount(3);

        x.http().post(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                httpcallback.UpLoadFilePostCallBack(Success, result);
                mLog.e("onSuccess:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                httpcallback.UpLoadFilePostCallBack(Error, "");
                if(ex.getMessage().contains("Network is unreachable")){
                    Utils.showToast("网络连接中断！请检查您的网络。");
                }
                if(ex.getMessage().contains("timeout")){
                    Utils.showToast("访问网络超时。请检查网络状况并联系相关工作人员。");
                }
                mLog.e("onError:" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                httpcallback.UpLoadFilePostCallBack(Cancelled, "");
                mLog.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                httpcallback.UpLoadFilePostCallBack(Finished, "");
                mLog.e("onFinished");
            }
        });
    }


    public interface HttpPostCallBack {
        void postcallback(String Finish, String paramString);
    }
    public interface HttpCallBack {
        void onSuccess(String result);
        void onError(Throwable ex);
    }
    public interface HttpPostUpLoadFileCallBack {
        void UpLoadFilePostCallBack(String Finish, String paramString);
    }

}
