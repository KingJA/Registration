package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tdr.registration.R;

/**
 * 备案登记 下拉菜单
 * Created by Linus_Xie on 2016/11/1.
 */

public class ShangPaiRegistrPop extends PopupWindowBaseDown implements View.OnClickListener {

    private LinearLayout linearScanPre;
    private LinearLayout linearQueryIdentity;
    private LinearLayout linearQueryPlateNumber;
    private ImageView imageQueryPlateNumber;
    private ImageView imageQueryIdentity;

    private LinearLayout linear_PlateNumber;
    private ImageView image_PlateNumber;
    private LinearLayout linear_qrcode;
    private ImageView image_queryIdentity2;

    public ShangPaiRegistrPop(View parentView, Activity activity) {
        super(parentView, activity);
    }


    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_shangpai_registr, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        linearScanPre = (LinearLayout) popupView.findViewById(R.id.linear_scanPre);
        linearScanPre.setOnClickListener(this);
    }

    @Override
    public void OnChildClick(View v) {
        if (onRegistrPopClickListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.linear_scanPre:
                onRegistrPopClickListener.onRegistrPop(0);
                break;
            default:
                break;

        }
    }

    public interface OnRegistrPopClickListener {
        void onRegistrPop(int position);
    }

    private OnRegistrPopClickListener onRegistrPopClickListener;

    public void setOnRegistrPopClickListener(OnRegistrPopClickListener onRegistrPopClickListener) {
        this.onRegistrPopClickListener = onRegistrPopClickListener;
    }
}
