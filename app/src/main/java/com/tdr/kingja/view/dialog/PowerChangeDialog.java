package com.tdr.kingja.view.dialog;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;


public class PowerChangeDialog extends DialogBaseAlert {
    private TextView tv_doubledialog_title;
    private RelativeLayout rl_doubledialog_left;
    private RelativeLayout rl_doubledialog_right;
    private TextView tv_doubledialog_left;
    private TextView tv_doubledialog_right;
    private OnDoubleClickListener onDoubleClickListener;

    public PowerChangeDialog(Context context) {
        super(context);
    }


    @Override
    public void initView() {
        setContentView(R.layout.dialog_power_change);
        tv_doubledialog_title = findViewById(R.id.tv_title);
        tv_doubledialog_left = findViewById(R.id.tv_doubledialog_left);
        tv_doubledialog_right = findViewById(R.id.tv_doubledialog_right);
        rl_doubledialog_left = findViewById(R.id.rl_doubledialog_left);
        rl_doubledialog_right = findViewById(R.id.rl_doubledialog_right);


    }

    @Override
    public void initNet() {

    }

    @Override
    public void initEvent() {
        rl_doubledialog_left.setOnClickListener(this);
        rl_doubledialog_right.setOnClickListener(this);

    }

    @Override
    public void initData() {
//        tv_doubledialog_title.setText(title);
//        tv_doubledialog_left.setText(leftString);
//        tv_doubledialog_right.setText(rightString);

    }


    @Override
    public void childClick(View v) {
        switch (v.getId()) {
            case R.id.rl_doubledialog_left:
                dismiss();
                onDoubleClickListener.onCancle();
                break;
            case R.id.rl_doubledialog_right:
                dismiss();
                onDoubleClickListener.onChange();
                break;
            default:
                break;
        }
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public interface OnDoubleClickListener {
        void onCancle();

        void onChange();
    }

}
