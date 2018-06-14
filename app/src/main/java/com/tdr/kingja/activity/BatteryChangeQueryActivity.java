package com.tdr.kingja.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.entity.BatteryInfo;
import com.tdr.kingja.utils.CheckUtil;
import com.tdr.kingja.view.dialog.PowerChangeDialog;
import com.tdr.registration.R;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶更换-查询
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryChangeQueryActivity extends BaseTitleActivity {
    private static final String TAG = "BatteryRegisterQueryActivity";
    @BindView(R.id.et_plateNumber)
    EditText etPlateNumber;

    @Override
    public void initVariable() {

    }

    @OnClick({R.id.stv_power_query})
    public void click(View view) {
        showProgress(true);
        String plateNumber = etPlateNumber.getText().toString().trim();
        if (!CheckUtil.checkEmpty(plateNumber, "请输入查询关键字")) {
            return;
        }
        showProgress(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PLATENUMBER", plateNumber);
        map.put("OWNER_PHONE", "");
        map.put("BATTERY_RECORDID", "");
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetBatteryModel);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
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
                        BatteryInfo batteryInfo = new Gson().fromJson(data, BatteryInfo.class);
                        showBatteryChangeDialog(batteryInfo);

                    } else if (errorCode == 1) {
                        ToastUtil.showToast(data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(BatteryChangeQueryActivity.this, LoginActivity
                                .class);
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

    private void showBatteryChangeDialog(final BatteryInfo info) {
        final PowerChangeDialog powerChangeDialog = new PowerChangeDialog(this, info);
        powerChangeDialog.setOnDoubleClickListener(new PowerChangeDialog.OnDoubleClickListener() {
            @Override
            public void onCancle() {
            }

            @Override
            public void onChange() {
                BatteryChangeActivity.goActivity(BatteryChangeQueryActivity.this,info);

            }
        });
        powerChangeDialog.show();
    }


    @Override
    protected String getContentTitle() {
        return "电瓶更换";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_query;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNet() {

    }

}
