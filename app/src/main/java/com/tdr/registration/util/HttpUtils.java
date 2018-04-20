package com.tdr.registration.util;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtils {
    public static final String Success = "onSuccess";
    public static final String Error = "onError";
    public static final String Cancelled = "onCancelled";
    public static final String Finished = "onFinished";

    public static void get(RequestParams RP, final HttpGetCallBack httpcallback) {
        RP.addHeader("accessToken", (String) SharedPreferencesUtils.get("token", ""));
//        //设置联网超时时间
        RP.setConnectTimeout(5000);
//        //设置重试次数
        RP.setMaxRetryCount(3);
        x.http().get(RP, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                httpcallback.getcallback(Success, result);
                mLog.e("onSuccess:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLog.e("onError:" + ex.toString());
                httpcallback.getcallback(Error, "");
                if(ex.getMessage().contains("Network is unreachable")){
                    Utils.showToast("网络连接中断！请检查您的网络。");
                }
                if(ex.getMessage().contains("timeout")){
                    Utils.showToast("访问网络超时。请检查网络状况并联系相关工作人员。");
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {
                httpcallback.getcallback(Cancelled, "");
                mLog.e("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
                httpcallback.getcallback(Finished, "");
                mLog.e("onFinished");
            }
        });
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
                mLog.e("onSuccess:" + result);
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

    public interface HttpGetCallBack {
        void getcallback(String Finish, String paramString);
    }
    public interface HttpPostUpLoadFileCallBack {
        void UpLoadFilePostCallBack(String Finish, String paramString);
    }

}
