package com.tdr.kingja.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.entity.BatteryInfo;
import com.tdr.kingja.utils.CheckUtil;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.kingja.view.dialog.DoubleDialog;
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
 * Description:电瓶回收-查询
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryRecycleQueryActivity extends BaseTitleActivity {
    @BindView(R.id.et_query_recordId)
    EditText etQueryRecordId;
    @BindView(R.id.et_query_phone)
    EditText etQueryPhone;

    @OnClick({R.id.stv_recycle_query, R.id.tv_recycle_unrecord_query})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.stv_recycle_query:
                getBatteryInfo();
                break;
            case R.id.tv_recycle_unrecord_query:
                showUnregisteredDialog();
                break;
            default:
                break;
        }
    }

    private void showUnregisteredDialog() {
        DoubleDialog unRegisteredRecycleDialog = new DoubleDialog(this, "电瓶未登记", "收购有风险，举报有义务，违法必追究",
                "取消", "确定");
        unRegisteredRecycleDialog.setOnDoubleClickListener(new DoubleDialog.OnDoubleClickListener() {
            @Override
            public void onCancle() {

            }

            @Override
            public void onConfirm() {
                GoUtil.goActivityAndFinish(BatteryRecycleQueryActivity.this, BatteryUnregisteredRecycleActivity.class);
            }
        });
        unRegisteredRecycleDialog.show();
    }

    private void getBatteryInfo() {
        String recordId = etQueryRecordId.getText().toString().trim();
        String phone = etQueryPhone.getText().toString().trim();
        if (TextUtils.isEmpty(recordId) && TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("请输入备案登记号或手机号");
            return;
        }
        if (!TextUtils.isEmpty(phone) && !CheckUtil.checkPhoneFormat(phone)) {
            return;
        }
        showProgress(true);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PLATENUMBER", "");
        map.put("OWNER_PHONE", phone);
        map.put("BATTERY_RECORDID", recordId);
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
                    String data = jsonObject.getString("Data");
                    int errorCode = jsonObject.getInt("ErrorCode");
                    if (errorCode == 0) {
                        BatteryInfo batteryInfo = new Gson().fromJson(data, BatteryInfo.class);
                        BatteryRecycleActivity.goActivity(BatteryRecycleQueryActivity.this, batteryInfo);
                    } else if (errorCode == 1) {
                        ToastUtil.showToast(data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(BatteryRecycleQueryActivity.this, LoginActivity
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
