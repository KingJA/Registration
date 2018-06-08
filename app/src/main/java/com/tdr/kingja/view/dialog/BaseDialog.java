package com.tdr.kingja.view.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tdr.registration.R;


public abstract class BaseDialog extends AlertDialog implements View.OnClickListener {
    protected Context context;

    protected BaseDialog(Context context) {
        super(context, R.style.CustomAlertDialog);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initView();
        initData();
        settData();

    }

    protected abstract int getContentView();
    public abstract void initView();
    public abstract void initData();
    public abstract void settData();
    public abstract void childClick(View v);

    @Override
    public void onClick(View v) {
        childClick(v);
    }

}
