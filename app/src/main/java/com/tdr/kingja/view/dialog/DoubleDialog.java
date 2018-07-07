package com.tdr.kingja.view.dialog;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;


public class DoubleDialog extends DialogBaseAlert {
    private Context context;
    private String message;
    private TextView tv_doubledialog_message;
    private TextView tv_doubledialog_title;
    private RelativeLayout rl_doubledialog_left;
    private RelativeLayout rl_doubledialog_right;
    private TextView tv_doubledialog_left;
    private TextView tv_doubledialog_right;
    private String title;
    private String leftString;
    private String rightString;
    private OnDoubleClickListener onDoubleClickListener;

    public DoubleDialog(Context context, String title, String message, String leftString, String rightString) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.leftString = leftString;
        this.rightString = rightString;
    }

    public DoubleDialog(Context context, String title, String message) {
        this(context, title, message, "取消", "确定");
    }

    @Override
    public void initView() {
        setContentView(R.layout.dialog_double);
        tv_doubledialog_message = findViewById(R.id.tv_message);
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
        tv_doubledialog_message.setText(message);
        tv_doubledialog_title.setText(title);
        tv_doubledialog_left.setText(leftString);
        tv_doubledialog_right.setText(rightString);

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
                onDoubleClickListener.onConfirm();
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

        void onConfirm();
    }

}
