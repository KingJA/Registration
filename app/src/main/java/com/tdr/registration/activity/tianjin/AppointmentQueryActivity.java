package com.tdr.registration.activity.tianjin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.CityPickerActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.adapter.PeriodAdapter;
import com.tdr.registration.adapter.PointAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.PointModel;
import com.tdr.registration.model.RcPreRateModel;
import com.tdr.registration.model.RegistrationPointModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PermissionUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约查询
 */

public class AppointmentQueryActivity extends BaseActivity {

    private final static int REGISTRATIONNAME = 1991;
    private final static int PERIODTIME = 0316;

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_appointmentDate)
    TextView textAppointmentDate;
    @BindView(R.id.text_registrationName)
    TextView textRegistrationName;
    @BindView(R.id.text_appointment)
    TextView textAppointment;
    @BindView(R.id.btn_search)
    Button btnSearch;

    private TimePickerView timePickerView;
    private Context mContext;

    private ZProgressHUD mProgressHUD;

    private String registersiteId = "";
    private String configId = "";

    private List<RcPreRateModel> rcPreRateModels = new ArrayList<>();
    private Gson mGson;
    private Intent intent;
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_query);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        intent = new Intent();
        initView();
    }

    private void initView() {
        textTitle.setText("预约查询");
        textAppointmentDate.setText(Utils.getNowDate());

        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setTime(new Date());
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(true);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textAppointmentDate.setText(Utils.setDate(date));
            }
        });

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    @OnClick({R.id.image_back, R.id.text_appointmentDate, R.id.text_registrationName, R.id.text_appointment, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_appointmentDate:
                timePickerView.show();
                break;
            case R.id.text_registrationName:
                //ActivityUtil.goActivityForResult(AppointmentQueryActivity.this, RegistrationPointActivity.class, REGISTRATIONNAME);
                intent.setClass(AppointmentQueryActivity.this, RegistrationPointActivity.class);
                startActivityForResult(intent, REGISTRATIONNAME);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.text_appointment:
                if(textRegistrationName.getText().toString().equals("")){
                    Utils.myToast(mContext, "请先选择备案登记点");
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString("appointmentDate", textAppointmentDate.getText().toString());
                bundle.putString("registersiteId", registersiteId);
                intent.putExtras(bundle);
                intent.setClass(AppointmentQueryActivity.this, PeriodTimeActivity.class);
                startActivityForResult(intent, PERIODTIME);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.btn_search:
                String appointmentDate = textAppointmentDate.getText().toString();
                if (appointmentDate.equals("")) {
                    Utils.myToast(mContext, "请选择预约时间");
                    break;
                }
                String registrationName = textRegistrationName.getText().toString();
                if (registrationName.equals("")) {
                    Utils.myToast(mContext, "请选择备案登记点");
                    break;
                }
                String appointment = textAppointment.getText().toString();
                if (appointment.equals("")) {
                    Utils.myToast(mContext, "请选择预约时段");
                    break;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("RegistersiteId", registersiteId);
                    jsonObject.put("ConfigId", configId);
                    jsonObject.put("ReservateTime", appointmentDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressHUD.show();
                HashMap<String, String> map = new HashMap<>();
                map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
                map.put("infoJsonStr", jsonObject.toString());
                WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETRCPRERATE, map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    mProgressHUD.dismiss();
                                    rcPreRateModels = mGson.fromJson(data, new TypeToken<List<RcPreRateModel>>() {
                                    }.getType());
                                    if (rcPreRateModels.size() > 0) {
                                        Bundle bundle = new Bundle();
                                        ArrayList list = new ArrayList();
                                        list.add(rcPreRateModels);
                                        bundle.putParcelableArrayList("rcPreRateModels", list);
                                        ActivityUtil.goActivityWithBundle(AppointmentQueryActivity.this, AppointmentListActivity.class, bundle);
                                    } else {
                                        Utils.myToast(mContext, "暂时没有预约列表");
                                    }
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.myToast(mContext, data);
                                } else {
                                    Utils.myToast(mContext, data);
                                    mProgressHUD.dismiss();
                                }
                            } catch (JSONException e) {
                                mProgressHUD.dismiss();
                                e.printStackTrace();
                                Utils.myToast(mContext, "JSON解析出错");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(AppointmentQueryActivity.this, HomeActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATIONNAME) {
            if (resultCode == RESULT_OK) {
                RegistrationPointModel model = (RegistrationPointModel) data.getSerializableExtra("registrationPoint");
                textRegistrationName.setText(Utils.initNullStr(model.getRegistersiteName()));
                registersiteId = model.getRegistersiteId();
            }
        } else if (requestCode == PERIODTIME) {
            if (resultCode == RESULT_OK) {
                PointModel model = (PointModel) data.getSerializableExtra("periodTime");
                textAppointment.setText(model.getOnTime() + "~" + model.getOffTime());
                configId = model.getConfigId();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
