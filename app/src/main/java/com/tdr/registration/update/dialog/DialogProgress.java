package com.tdr.registration.update.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tdr.registration.R;


/**
 * Description：TODO
 * Create Time：2016/9/27 15:49
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class DialogProgress extends AlertDialog {

    private ProgressBar pb_update;
    private TextView tv_progress;

    public DialogProgress(Context context) {
        super(context, R.style.CustomAlertDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_kprogress);
        pb_update = (ProgressBar) findViewById(R.id.pb_update);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
    }

    public void setProgress(int progress) {
        pb_update.setProgress(progress);
        tv_progress.setText("拼命下载中("+progress+"/100)");
    }
}
