package com.tdr.registration.base;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tdr.registration.R;

/**
 * 对话框Dialog基类
 * Created by Linus_Xie on 2016/9/18.
 */
public abstract class BaseDialogAlert extends AlertDialog implements View.OnClickListener {
    private Context context;

    public BaseDialogAlert(Context context) {
        super(context, R.style.custom_alertDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initNet();
        initEvent();
        initData();

    }

    public abstract void initView();

    public abstract void initNet();

    public abstract void initEvent();

    public abstract void initData();

    public abstract void childClick(View v);

    @Override
    public void onClick(View v) {
        childClick(v);

    }

}
