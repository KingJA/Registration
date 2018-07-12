package com.tdr.kingja.view.dialog;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;


public class PowerRegisterDialog extends DialogBaseAlert {
    private final String plateNumber;
    private final String ownerName;
    private final String idcard;
    private final String phone;
    private OnDoubleClickListener onDoubleClickListener;

    public PowerRegisterDialog(Context context, @Nullable String plateNumber, @Nullable String ownerName, @Nullable
            String idcard, @Nullable String phone) {
        super(context);
        this.plateNumber = plateNumber;
        this.ownerName = ownerName;
        this.idcard = idcard;
        this.phone = phone;
    }

    private TextView mTvPlateNumber;
    private TextView mTvOwnerName;
    private TextView mTvIdcard;
    private TextView mTvPhone;
    private RelativeLayout mRlDoubledialogLeft;
    private RelativeLayout mRlDoubledialogRight;


    @Override
    public void initView() {
        setContentView(R.layout.dialog_power_register);
        mTvPlateNumber = (TextView) findViewById(R.id.tv_plateNumber);
        mTvOwnerName = (TextView) findViewById(R.id.tv_ownerName);
        mTvIdcard = (TextView) findViewById(R.id.tv_idcard);
        mTvPhone = (TextView) findViewById(R.id.tv_phone);
        mRlDoubledialogLeft = (RelativeLayout) findViewById(R.id.rl_doubledialog_left);
        mRlDoubledialogRight = (RelativeLayout) findViewById(R.id.rl_doubledialog_right);


    }

    @Override
    public void initNet() {

    }

    @Override
    public void initEvent() {
        mRlDoubledialogLeft.setOnClickListener(this);
        mRlDoubledialogRight.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mTvPlateNumber.setText(plateNumber);
        mTvOwnerName.setText(ownerName);
        mTvIdcard.setText(idcard);
        mTvPhone.setText(phone);

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
                onDoubleClickListener.onRegister();
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

        void onRegister();
    }
}
