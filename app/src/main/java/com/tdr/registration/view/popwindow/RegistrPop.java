package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tdr.registration.R;
import com.tdr.registration.util.SharedPreferencesUtils;

/**
 * 备案登记 下拉菜单
 * Created by Linus_Xie on 2016/11/1.
 */

public class RegistrPop extends PopupWindowBaseDown implements View.OnClickListener {

    private LinearLayout linearScanPre;
    private LinearLayout linearQueryIdentity;
    private LinearLayout linearQueryPlateNumber;
    private ImageView imageQueryPlateNumber;
    private ImageView imageQueryIdentity;

    private LinearLayout linear_PlateNumber;
    private ImageView image_PlateNumber;
    private LinearLayout linear_qrcode;
    private ImageView image_queryIdentity2;

    public RegistrPop(View parentView, Activity activity) {
        super(parentView, activity);
    }


    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_registr, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        linearScanPre = (LinearLayout) popupView.findViewById(R.id.linear_scanPre);
        linearScanPre.setOnClickListener(this);
        linearQueryIdentity = (LinearLayout) popupView.findViewById(R.id.linear_queryIdentity);
        linearQueryIdentity.setOnClickListener(this);



        linearQueryPlateNumber = (LinearLayout) popupView.findViewById(R.id.linear_queryPlateNumber);
        linearQueryPlateNumber.setOnClickListener(this);
        imageQueryIdentity = (ImageView) popupView.findViewById(R.id.image_queryIdentity);
        imageQueryPlateNumber = (ImageView) popupView.findViewById(R.id.image_queryPlateNumber);

        linear_PlateNumber= (LinearLayout) popupView.findViewById(R.id.linear_PlateNumber);
        linear_PlateNumber.setOnClickListener(this);
        image_PlateNumber = (ImageView) popupView.findViewById(R.id.image_PlateNumber);


        String locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("昆明")){
            linearQueryIdentity.setVisibility(View.VISIBLE);
            imageQueryIdentity.setVisibility(View.VISIBLE);
            linearQueryPlateNumber.setVisibility(View.VISIBLE);
            imageQueryPlateNumber.setVisibility(View.VISIBLE);
        }else if(locCityName.contains("天津")){
            linear_PlateNumber.setVisibility(View.VISIBLE);
            image_PlateNumber.setVisibility(View.VISIBLE);
        }else if(locCityName.contains("温州")){
            linear_PlateNumber.setVisibility(View.VISIBLE);
            image_PlateNumber.setVisibility(View.VISIBLE);
            linearQueryIdentity.setVisibility(View.VISIBLE);
            imageQueryIdentity.setVisibility(View.VISIBLE);
//            linearQueryPlateNumber.setVisibility(View.VISIBLE);
//            imageQueryPlateNumber.setVisibility(View.VISIBLE);
        }else if(locCityName.contains("以卡管车")){
            linear_PlateNumber.setVisibility(View.VISIBLE);
            image_PlateNumber.setVisibility(View.VISIBLE);
            linearQueryIdentity.setVisibility(View.VISIBLE);
            imageQueryIdentity.setVisibility(View.VISIBLE);
        }
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
            case R.id.linear_queryIdentity:
                onRegistrPopClickListener.onRegistrPop(1);
                break;
            case R.id.linear_queryPlateNumber:
                onRegistrPopClickListener.onRegistrPop(2);
                break;
            case R.id.linear_PlateNumber:
                onRegistrPopClickListener.onRegistrPop(3);
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
