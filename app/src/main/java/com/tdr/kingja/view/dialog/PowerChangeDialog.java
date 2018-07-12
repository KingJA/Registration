package com.tdr.kingja.view.dialog;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.kingja.entity.BatteryInfo;
import com.tdr.registration.R;


public class PowerChangeDialog extends DialogBaseAlert {
    private OnDoubleClickListener onDoubleClickListener;

    private TextView mTvTitle;
    private TextView mTvRecordId;
    private TextView mTvOwnName;
    private TextView mTvPhone;
    private TextView mTvBatteryCount;
    private TextView mTvBrand;
    private TextView mTvType;
    private RelativeLayout mRlLeft;
    private RelativeLayout mRlRight;
    private BatteryInfo batteryInfo;

    public PowerChangeDialog(Context context, BatteryInfo batteryInfo) {
        super(context);
        this.batteryInfo = batteryInfo;
    }


    @Override
    public void initView() {
        setContentView(R.layout.dialog_power_change);
        mTvTitle = findViewById(R.id.tv_title);
        mTvRecordId = findViewById(R.id.tv_recordId);
        mTvOwnName = findViewById(R.id.tv_ownName);
        mTvPhone = findViewById(R.id.tv_phone);
        mTvBatteryCount = findViewById(R.id.tv_batteryCount);
        mTvBrand = findViewById(R.id.tv_brand);
        mTvType = findViewById(R.id.tv_type);
        mRlLeft = findViewById(R.id.rl_left);
        mRlRight = findViewById(R.id.rl_right);


    }

    @Override
    public void initNet() {

    }

    @Override
    public void initEvent() {
        mRlLeft.setOnClickListener(this);
        mRlRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvTitle.setText(batteryInfo.getPLATENUMBER());
        mTvRecordId.setText(batteryInfo.getBATTERY_RECORDID());
        mTvOwnName.setText(batteryInfo.getOWNER_NAME());
        mTvPhone.setText(batteryInfo.getOWNER_PHONE());
        mTvBatteryCount.setText(batteryInfo.getBATTERY_QUANTITY()+"");
        mTvBrand.setText(batteryInfo.getVEHICLEBRAND());
        mTvType.setText(batteryInfo.getVEHICLEMODELS());
    }


    @Override
    public void childClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                dismiss();
                onDoubleClickListener.onCancle();
                break;
            case R.id.rl_right:
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
