package com.tdr.kingja.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.registration.R;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.RegisterInsuranceActivity;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶回收-查询
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class PowerRecycleQueryActivity extends BaseTitleActivity {
    @BindView(R.id.et_query_recordId)
    EditText etQueryRecordId;
    @BindView(R.id.et_query_phone)
    EditText etQueryPhone;

    @OnClick({R.id.stv_recycle_query, R.id.tv_recycle_unrecord_query})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.stv_recycle_query:
                GoUtil.goActivity(this, PowerRecycleActivity.class);
                break;
            case R.id.tv_recycle_unrecord_query:
                ToastUtil.showToast("未备案登记");
                break;
            default:
                break;
        }
    }

    private void getBatteryInfo() {
       showProgress(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PLATENUMBER", "");
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
                    if (errorCode == 0) {

                    } else if (errorCode == 1) {
                        String data = jsonObject.getString("Data");
                        ToastUtil.showToast(data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(PowerRecycleQueryActivity.this, LoginActivity
                                .class);
                    } else {
                        String errorMsg = jsonObject.getString("Data");
                        Utils.showToast(errorMsg);
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
    public void initVariable() {

    }

    @Override
    protected String getContentTitle() {
        return "电瓶回收";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_recycle_query;
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
