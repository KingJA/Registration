package com.tdr.registration.update.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;


public class DialogDouble extends AlertDialog implements View.OnClickListener {
    private Context context;
    private String message;
    private TextView tv_doubledialog_title;
    private TextView tv_doubledialog_message;
    private RelativeLayout rl_doubledialog_left;
    private RelativeLayout rl_doubledialog_right;
    private TextView tv_doubledialog_left;
    private TextView tv_doubledialog_right;
    private String leftString;
    private String rightString;
    private OnDoubleClickListener onDoubleClickListener;

    public DialogDouble(Context context, String message, String leftString, String rightString) {
        super(context, R.style.CustomAlertDialog);
        this.context = context;
        this.message = message;
        this.leftString = leftString;
        this.rightString = rightString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    public void initView() {
        setContentView(R.layout.dialog_kdouble);
        tv_doubledialog_title = (TextView) findViewById(R.id.tv_doubledialog_title);
        tv_doubledialog_message = (TextView) findViewById(R.id.tv_message);
        tv_doubledialog_left = (TextView) findViewById(R.id.tv_doubledialog_left);
        tv_doubledialog_right = (TextView) findViewById(R.id.tv_doubledialog_right);
        rl_doubledialog_left = (RelativeLayout) findViewById(R.id.rl_doubledialog_left);
        rl_doubledialog_right = (RelativeLayout) findViewById(R.id.rl_doubledialog_right);
    }

    public void initEvent() {
        rl_doubledialog_left.setOnClickListener(this);
        rl_doubledialog_right.setOnClickListener(this);
    }

    public void initData() {
        tv_doubledialog_message.setText(message);
        tv_doubledialog_left.setText(leftString);
        tv_doubledialog_right.setText(rightString);
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_doubledialog_left) {
            dismiss();
            onDoubleClickListener.onLeft();
        }
        if (v.getId() == R.id.rl_doubledialog_right) {
            dismiss();
            onDoubleClickListener.onRight();
        }
    }

    public interface OnDoubleClickListener {
        void onLeft();

        void onRight();
    }

}
