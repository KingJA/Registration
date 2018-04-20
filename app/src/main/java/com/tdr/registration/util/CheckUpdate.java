package com.tdr.registration.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tdr.registration.BuildConfig;
import com.tdr.registration.R;
import com.tdr.registration.update.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Linus_Xie on 2016/3/12.
 */
public class CheckUpdate {
    public static final String ApkName = "RegistrationCurrency.apk";
    public static final String DOWNLOAD_URL = "http://dmi.tdr-cn.com/newestapk/";// APK下载地址
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    private Activity mActivity;
    /* 更新进度条 */
    private int CheckTimes=0;
    private ProgressBar PB_DL;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    mLog.e("CheckTimes="+CheckTimes);
                    // 设置进度条位置
                    PB_DL.setProgress(CheckTimes);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 获取版本信息并升级
     */
    public void UpdateAPK() {
        if(CheckTimes>10){
            return;
        }
        CheckTimes++;
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("version", AppInfoUtil.getVersionCode()+"_ANDROID");
        mLog.e("检查版本更新"+AppInfoUtil.getVersionCode()+"");
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKVERSION, update, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(final String result) {
                if (result != null) {
                    mLog.e(  "GetVersionCode= " + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        mLog.e( "callBack: " + data);
                        if (errorCode == 0) {//已是最新版本
                            mLog.e("已是最新版本");
                        } else if (errorCode == 2) {//需要升级
                            showNoticeDialog();
                        } else if (errorCode == 7) {//版本号没上传
                            UpdateAPK();
                        } else {
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mLog.e( "升级接口访问超时");
                }
            }
        });

    }

    public CheckUpdate(Activity mActivity) {
        this.mActivity = mActivity;
    }
    /**
     * 显示软件更新对话框
     */
    TextView txt_content, txt_cancel;

    LinearLayout ll_ok;
    Dialog D;
    public void showNoticeDialog() {
        View contentView = LayoutInflater.from(mActivity).inflate(
                R.layout.update_app, null);

        D = new Dialog(mActivity);
        D.setCanceledOnTouchOutside(false);

        txt_content = (TextView) contentView.findViewById(R.id.txt_content);
        TextView update_cancel = (TextView) contentView.findViewById(R.id.update_cancel);
        TextView update_ok = (TextView) contentView.findViewById(R.id.update_ok);
        PB_DL = (ProgressBar) contentView.findViewById(R.id.PB_DL);
        txt_cancel = (TextView) contentView.findViewById(R.id.txt_cancel);
        ll_ok = (LinearLayout) contentView.findViewById(R.id.ll_ok);


        //软件更新描述
//        txt_content.setText("");


        update_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile();
            }
        });

        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
                System.exit(0);
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                D.dismiss();
                System.exit(0);
            }
        });
        D.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_UP) {
                    Utils.showToast("系统正在更新...");
                }
                return true;
            }
        });
        D.setContentView(contentView);
        D.show();
    }

    private void downloadFile() {

        String path = Util.getCacheDirectory(mActivity).getAbsolutePath() + "/" + ApkName;
        String url=DOWNLOAD_URL+ApkName;
        mLog.e("path=" + path);
        mLog.e("url=" + url);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setAutoRename(true);//断点下载
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                PB_DL.setVisibility(View.VISIBLE);
                ll_ok.setVisibility(View.GONE);
                PB_DL.setMax((int) total);
                CheckTimes=(int) current;
                mHandler.sendEmptyMessage(DOWNLOAD);


                String CURRENT = String.format("%.2f", (Double.valueOf(current) / 1024 / 1024)) + "MB";
                String TOTAL = String.format("%.2f", (Double.valueOf(total) / 1024 / 1024)) + "MB";
                txt_content.setText("正在下载，进度：" + CURRENT + "/" + TOTAL);
            }

            @Override
            public void onSuccess(File result) {
                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                D.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                mLog.e(ex.toString());
                Utils.showToast("下载失败，请检查网络和SD卡" + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(Util.getCacheDirectory(mActivity), ApkName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID , apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }
        mActivity.startActivity(intent);
    }
}
