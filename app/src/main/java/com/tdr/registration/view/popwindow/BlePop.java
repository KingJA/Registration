package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.tdr.registration.R;

/**
 * 蓝牙弹出框
 * Created by Linus_Xie on 2016/10/13.
 */
public class BlePop extends PopupWindowBaseDown implements View.OnClickListener {
    private LinearLayout linearUpdateBle;
    private LinearLayout linearSingleBlack;
    private LinearLayout linear_AerialSignal;

    public BlePop(View parentView, Activity activity) {
        super(parentView, activity);
    }

    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_ble, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        linearUpdateBle = (LinearLayout) popupView.findViewById(R.id.linear_updateBle);
        linearSingleBlack = (LinearLayout) popupView.findViewById(R.id.linear_singleBlack);
        linear_AerialSignal = (LinearLayout) popupView.findViewById(R.id.linear_AerialSignal);
        linearUpdateBle.setOnClickListener(this);
        linearSingleBlack.setOnClickListener(this);
        linear_AerialSignal.setOnClickListener(this);

    }

    @Override
    public void OnChildClick(View v) {
        if (onBlePopClickListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.linear_updateBle:
                onBlePopClickListener.onBlePop(0);
                break;

            case R.id.linear_singleBlack:
                onBlePopClickListener.onBlePop(1);
                break;
            case R.id.linear_AerialSignal:
                onBlePopClickListener.onBlePop(2);
                break;
        }
    }

    public interface OnBlePopClickListener {
        void onBlePop(int position);
    }

    private OnBlePopClickListener onBlePopClickListener;

    public void setOnBlePopClickListener(OnBlePopClickListener onBlePopClickListener) {
        this.onBlePopClickListener = onBlePopClickListener;
    }
}