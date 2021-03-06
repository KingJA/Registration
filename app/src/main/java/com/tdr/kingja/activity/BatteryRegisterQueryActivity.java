package com.tdr.kingja.activity;

import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.entity.BatteryInfo;
import com.tdr.kingja.entity.CarRegisterInfo;
import com.tdr.kingja.utils.CheckUtil;
import com.tdr.kingja.view.dialog.PowerRegisterDialog;
import com.tdr.registration.R;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶登记-查询
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class BatteryRegisterQueryActivity extends BaseTitleActivity {
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
        HashMap<String, String> map = new HashMap<>();
        map.put("token", (String) SharedPreferencesUtils.get("token", ""));
        map.put("platenumber", plateNumber);
        WebServiceUtils.callWebService(this, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                .WEBSERVER_GETCARBYPLATENUMBER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                showProgress(false);
                Logger.json(result);
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            CarRegisterInfo carRegisterInfo = new Gson().fromJson(data, CarRegisterInfo.class);
                            showPowerRegisterDialog(carRegisterInfo);

                        } else if (errorCode == 1) {
                            ToastUtil.showToast(data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(BatteryRegisterQueryActivity.this, LoginActivity.class);
                        } else {
                            ToastUtil.showToast(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showToast("JSON解析出错");
                    }
                } else {
                    ToastUtil.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private void showPowerRegisterDialog(final CarRegisterInfo info) {
        final PowerRegisterDialog powerRegisterDialog = new PowerRegisterDialog(this, info.getPlateNumber(),
                info.getOwnerName(), info.getCardId(), info.getPhone1());
        powerRegisterDialog.setOnDoubleClickListener(new PowerRegisterDialog.OnDoubleClickListener() {
            @Override
            public void onCancle() {

            }
            @Override
            public void onRegister() {
                BatteryInfo batteryInfo = new BatteryInfo();
                batteryInfo.setECID(info.getEcId());
                batteryInfo.setPLATENUMBER(info.getPlateNumber());
                batteryInfo.setOWNER_NAME(info.getOwnerName());
                batteryInfo.setOWNER_CARDID(info.getCardId());
                batteryInfo.setOWNER_CARDTYPE(info.getCARDTYPE());
                batteryInfo.setOWNER_PHONE(info.getPhone1());
                batteryInfo.setUNITID(info.getUnitID());
                batteryInfo.setVehicleType(info.getVehicleType());
                BatteryRegisterActivity.goActivity(BatteryRegisterQueryActivity.this, batteryInfo);

            }
        });
        powerRegisterDialog.show();
    }


    @Override
    protected String getContentTitle() {

        return "电瓶备案登记";
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
