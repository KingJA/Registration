package com.tdr.kingja.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingja.supershapeview.view.SuperShapeTextView;
import com.orhanobut.logger.Logger;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.utils.CheckUtil;
import com.tdr.kingja.utils.DialogUtil;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.kingja.utils.ImageUtil;
import com.tdr.kingja.view.dialog.PowerRecycleDialog;
import com.tdr.registration.R;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:电瓶回收-回收
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryUnregisteredRecycleActivity extends BaseTitleActivity {

    @BindView(R.id.tv_battery_recycle_recordId)
    TextView tvBatteryRecycleRecordId;
    @BindView(R.id.et_battery_recycle_name)
    EditText etBatteryRecycleName;
    @BindView(R.id.tv_battery_recycle_phone)
    EditText tvBatteryRecyclePhone;
    @BindView(R.id.tv_battery_recycle_address)
    EditText tvBatteryRecycleAddress;
    @BindView(R.id.iv_battery_recycle_photo)
    ImageView ivBatteryRecyclePhoto;
    @BindView(R.id.stv_battery_recycle)
    SuperShapeTextView stvBatteryRecycle;
    private String recordId;

    private static final int REQUEST_CAMERA = 0x02;
    private String photoBase64;

    @OnClick({R.id.iv_battery_recycle_photo, R.id.stv_battery_recycle})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_battery_recycle_photo:
                takePhoto();
                break;
            case R.id.stv_battery_recycle:
                recycleBattery();
                break;
            default:
                break;
        }
    }

    private void recycleBattery() {
        final String name = etBatteryRecycleName.getText().toString().trim();
        final String phone = tvBatteryRecyclePhone.getText().toString().trim();
        final String address = tvBatteryRecycleAddress.getText().toString().trim();

        if (!CheckUtil.checkEmpty(name, "请输入姓名")
                || !CheckUtil.checkPhoneFormat(phone)
                || !CheckUtil.checkEmpty(address, "请输地址信息")
                || !CheckUtil.checkEmpty(photoBase64, "请上传照片")) {
            return;
        }
        showUnregisteredDialog(name, phone, address);
    }

    private void showUnregisteredDialog(final String name, final String phone, final String address) {
        PowerRecycleDialog unRegisteredRecycleDialog = new PowerRecycleDialog(this, "请核对是否本人回收", "收购有风险，举报有义务，违法必追究",
                "取消", "确定");
        unRegisteredRecycleDialog.setOnDoubleClickListener(new PowerRecycleDialog.OnDoubleClickListener() {
            @Override
            public void onCancle() {
            }
            @Override
            public void onConfirm() {
                doRecycle(name, phone, address);
            }
        });
        unRegisteredRecycleDialog.show();
    }


    private void doRecycle(String name, String phone, String address) {
        showProgress(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("RECOVERYID","");
        map.put("BATTERYID", "");
        map.put("BATTERY_RECORDID", recordId);
        map.put("BATTERY_THEFTNO", "");
        map.put("VEHICLEBRAND", "");
        map.put("VEHICLEMODELS","");
        map.put("COLORID", "");
        map.put("BATTERY_QUANTITY", "");
        map.put("BUYDATE", "");
        map.put("PRICE", "");
        map.put("OWNERNAME", name);
        map.put("CARDID", "");
        map.put("CARDTYPE", "");
        map.put("PHONE", phone);
        map.put("ADDRESS", address);
        map.put("ISREGISTER", "0");//是否备案登记（0否，1是）
        map.put("PHOTOLIST", photoBase64);
        map.put("REASON", "");
        map.put("RECOVER_UNITID", "");
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_RecoveryBattery);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        Log.e(TAG, "JB.toString(): " + JB.toString());
        HttpUtils.postK(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                showProgress(false);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");
                    if (errorCode == 0) {
                        DialogUtil.showSuccess(BatteryUnregisteredRecycleActivity.this,"电瓶回收成功");
                    } else {
                        ToastUtil.showToast(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                showProgress(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivBatteryRecyclePhoto.setImageBitmap(bitmap);
                    photoBase64 = ImageUtil.bitmapToBase64(bitmap);
                    break;
                default:
                    break;
            }
        }
    }

    private void takePhoto() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    @Override
    public void initVariable() {
    }

    @Override
    protected String getContentTitle() {
        return "回收电瓶";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_recycle;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNet() {
        showProgress(true);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetRecordNo);
        HttpUtils.get(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                showProgress(false);
                Logger.d(result);
                try {
                    JSONObject resultObject = new JSONObject(result);
                    int errorCode = resultObject.getInt("ErrorCode");
                    String data = resultObject.getString("Data");
                    if (errorCode == 0) {
                        recordId = data;
                        tvBatteryRecycleRecordId.setText(data);
                    } else {
                        ToastUtil.showToast(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                showProgress(false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
