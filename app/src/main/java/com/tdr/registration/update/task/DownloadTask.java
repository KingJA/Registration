package com.tdr.registration.update.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.tdr.registration.BuildConfig;
import com.tdr.registration.update.Constants;
import com.tdr.registration.update.Util;
import com.tdr.registration.update.dialog.DialogProgress;
import com.tdr.registration.util.SharedPreferencesUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Description：TODO
 * Create Time：2016/9/26 13:30
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class DownloadTask extends AsyncTask<Void, Integer, Void> {//启动任务执行的输入参数、后台任务执行的进度、后台计算结果的类型
    private Activity context;
    private boolean showDownloadDialog;
    private DialogProgress mDownloadDialog;
    private static final int BUFFER_SIZE = 10 * 1024;
    private String TAG = getClass().getSimpleName();
    private File apkFile;

    public DownloadTask(Activity context, boolean showDownloadDialog) {
        this.context = context;
        this.showDownloadDialog = showDownloadDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (showDownloadDialog) {
            showDownLoadDialog();
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        String APK_NAME = (String) SharedPreferencesUtils.get("apkName", "") + ".apk";
        String urlStr = Constants.DOWNLOAD_URL + APK_NAME;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            urlConnection.connect();
            long bytetotal = urlConnection.getContentLength();
            long bytesum = 0;
            int byteread = 0;
            in = urlConnection.getInputStream();
            File dir = Util.getCacheDirectory(context);
            Log.e(TAG, "保存路径: " + dir.getAbsolutePath());
            String apkName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
            apkFile = new File(dir, apkName);
            out = new FileOutputStream(apkFile);
            byte[] buffer = new byte[BUFFER_SIZE];

            int oldProgress = 0;

            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread);

                int progress = (int) (bytesum * 100L / bytetotal);
                // 如果进度与之前进度相等，则不更新，如果更新太频繁，否则会造成界面卡顿
                if (progress != oldProgress) {
                    publishProgress(progress);
                    Log.e(TAG, "progress: " + progress);
                }
                oldProgress = progress;
            }
        } catch (Exception e) {
            Log.e(TAG, "download apk file error" + e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {

                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mDownloadDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        closeDownloadDialog();
        installAPk(apkFile);

    }

    private void closeDownloadDialog() {
        if (mDownloadDialog != null && mDownloadDialog.isShowing()) {
            mDownloadDialog.dismiss();
        }
    }

    private void showDownLoadDialog() {
        mDownloadDialog = new DialogProgress(context);
        mDownloadDialog.setCanceledOnTouchOutside(true);
        mDownloadDialog.setCancelable(true);
        mDownloadDialog.show();
        mDownloadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "进入后台下载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void installAPk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //如果没有设置SDCard写权限，或者没有sdcard,apk文件保存在内存中，需要授予权限才能安装
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        }
        context.startActivity(intent);
    }
}
