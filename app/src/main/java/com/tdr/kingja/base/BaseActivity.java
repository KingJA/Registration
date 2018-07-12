package com.tdr.kingja.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingja.volleysir.VolleySir;

/**
 * Description:TODO
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildCommonComponents();
        initVariable();
        setContentView(getContentView());
        initView();
        initData();
        initNet();
    }

    private void buildCommonComponents() {
        progressBar = new ProgressDialog(this);
    }

    public void showProgress(boolean ifShow) {
        if (ifShow) {
            progressBar.show();
        }else{
            progressBar.dismiss();
        }
    }

    protected abstract void initVariable();

    protected abstract View getContentView();

    protected abstract void initData();

    protected abstract void initNet();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySir.getDefault().cancle(this);
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }
}
