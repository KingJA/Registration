package com.tdr.registration.update.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.tdr.registration.update.Util;
import com.tdr.registration.update.dialog.DialogDouble;
import com.tdr.registration.update.strategy.LoadStrategy;


/**
 * Description：TODO
 * Create Time：2016/9/26 13:30
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class VersionTask extends AsyncTask<String, Integer, Integer> {
    private Activity context;
    private boolean cancleable;
    private boolean showDownloadDialog;
    private LoadStrategy loadStrategy;
    private String updateContent;

    public VersionTask(Activity context, boolean updateCancleable, boolean showDownloadDialog, LoadStrategy loadStrategy, String updateContent) {
        this.context = context;
        this.cancleable = updateCancleable;
        this.showDownloadDialog = showDownloadDialog;
        this.loadStrategy = loadStrategy;
        this.updateContent = updateContent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... params) {
        return loadStrategy.getVersionCode(params[0]);
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        if (Util.ifNeedUpdate(i, context)) {
            Log.e("onPostExecute", "需要更新: ");
            showAskDialog();
        }
    }

    private void showAskDialog() {

        DialogDouble dialogDouble = new DialogDouble(context, updateContent, "退出", "马上升级");
        dialogDouble.setOnDoubleClickListener(new DialogDouble.OnDoubleClickListener() {
            @Override
            public void onLeft() {
                System.exit(0);
            }

            @Override
            public void onRight() {
                new DownloadTask(context,showDownloadDialog).execute();
            }
        });
        dialogDouble.setCanceledOnTouchOutside(cancleable);
        dialogDouble.setCancelable(cancleable);
        dialogDouble.show();

    }

}
