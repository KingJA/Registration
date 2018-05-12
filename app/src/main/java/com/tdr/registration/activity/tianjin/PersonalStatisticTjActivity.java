package com.tdr.registration.activity.tianjin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.adapter.InsuranceShowAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ResultInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.LinearLayoutForShowInsurance;
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
 * 个人备案统计
 */
public class PersonalStatisticTjActivity extends BaseActivity {
    private static final String TAG = "StatisticActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.text_startTime)
    TextView textStartTime;
    @BindView(R.id.relative_startTime)
    RelativeLayout relativeStartTime;
    @BindView(R.id.text_endTime)
    TextView textEndTime;
    @BindView(R.id.relative_endTime)
    RelativeLayout relativeEndTime;
    @BindView(R.id.text_registrationName)
    TextView textRegistrationName;
    @BindView(R.id.relative_registrationName)
    RelativeLayout relativeRegistrationName;
    @BindView(R.id.text_registrationNumber)
    TextView textRegistrationNumber;
    @BindView(R.id.text_registrationArea)
    TextView textRegistrationArea;
    @BindView(R.id.relative_registrationArea)
    RelativeLayout relativeRegistrationArea;
    @BindView(R.id.text_totalNum)
    TextView textTotalNum;
    @BindView(R.id.text_totalAntitheft)
    TextView textTotalAntitheft;
    @BindView(R.id.text_totalWithoutAntitheft)
    TextView textTotalWithoutAntitheft;
    @BindView(R.id.text_reissuePlate)
    TextView textReissuePlate;
    @BindView(R.id.text_reissueAntitheft)
    TextView textReissueAntitheft;
    @BindView(R.id.text_registrationTotalNum)
    TextView textRegistrationTotalNum;
    @BindView(R.id.list_insurance)
    LinearLayoutForShowInsurance listInsurance;
    @BindView(R.id.text_packageTotal)
    TextView textPackageTotal;
    @BindView(R.id.text_packageMoney)
    TextView textPackageMoney;
    @BindView(R.id.image_registrationArea)
    ImageView imageRegistrationArea;
    @BindView(R.id.text_newCar)
    TextView textNewCar;
    @BindView(R.id.text_oldCar)
    TextView textOldCar;
    @BindView(R.id.text_palteNumberChange)
    TextView textPalteNumberChange;
    @BindView(R.id.text_havaInsurance)
    TextView textHavaInsurance;
    @BindView(R.id.image_totalWithoutAntitheft)
    ImageView imageTotalWithoutAntitheft;
    @BindView(R.id.relative_totalWithoutAntitheft)
    RelativeLayout relativeTotalWithoutAntitheft;

    private ZProgressHUD mProgressHUD;
    private Context mContext;
    private Gson mGson;
    private InsuranceShowAdapter mAdapter;

    private List<ResultInsuranceModel.ItemsBean> itemsBeanList = new ArrayList<>();
    private List<List<ResultInsuranceModel.ItemsBean.SubItemsBean>> subItemsBeanList = new ArrayList<>();
    private List<ResultInsuranceModel.ItemsBean.SubItemsBean> itemsBeans = new ArrayList<>();
    private List<ResultInsuranceModel.ItemsBean.SubItemsBean> newBeans = new ArrayList<>();

    private TimePickerView startTime;
    private TimePickerView endTime;

    private String locCityName = "";
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_tj);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText(getIntent().getStringExtra("title"));
        imageScan.setBackgroundResource(R.mipmap.ic_refresh);
        imageScan.setVisibility(View.VISIBLE);
        startTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        startTime.setTime(new Date());
        startTime.setCyclic(false);
        startTime.setCancelable(true);
        startTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textStartTime.setText(Utils.setDate(date));
            }
        });
        endTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        endTime.setTime(new Date());
        endTime.setCyclic(false);
        endTime.setCancelable(true);
        endTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                textEndTime.setText(Utils.setDate(date));
            }
        });
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");

        textStartTime.setText(Utils.getNowDate());
        textEndTime.setText(Utils.getNowDate());
        textRegistrationName.setText((String) SharedPreferencesUtils.get("regionName", ""));
        textRegistrationNumber.setText((String) SharedPreferencesUtils.get("regionNo", ""));

        imageRegistrationArea.setVisibility(View.GONE);
        textRegistrationArea.setText((String) SharedPreferencesUtils.get("regionName", ""));

        if (locCityName.contains("昆明")){
            imageTotalWithoutAntitheft.setVisibility(View.GONE);
            relativeTotalWithoutAntitheft.setVisibility(View.GONE);
        }
    }

    private void initData() {
        mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("startDate", textStartTime.getText().toString().trim());
        map.put("endDate", textEndTime.getText().toString().trim());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETREGISTRATIONSTATISTICSBYUSER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Log.e(TAG, result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            int registrationTotalNum = 0;
                            newBeans.clear();
                            itemsBeans.clear();
                            subItemsBeanList.clear();
                            JSONObject object = new JSONObject(data);
                            textTotalNum.setText(object.getString("zs"));
                            textTotalAntitheft.setText(object.getString("ybq"));
                            textTotalWithoutAntitheft.setText(object.getString("wbq"));
                            textNewCar.setText(object.getString("xxs"));
                            textOldCar.setText(object.getString("ysl"));
                            textPalteNumberChange.setText(object.getString("cpbg"));
                            textHavaInsurance.setText(object.getString("ysbx"));
                            String items = object.getString("Items");
                            itemsBeanList = mGson.fromJson(items, new TypeToken<List<ResultInsuranceModel.ItemsBean>>() {
                            }.getType());
                            for (ResultInsuranceModel.ItemsBean model : itemsBeanList) {
                                subItemsBeanList.add(model.getSubItems());
                            }
                            for (int i = 0; i < subItemsBeanList.size(); i++) {
                                itemsBeans = subItemsBeanList.get(i);
                                for (int j = 0; j < itemsBeans.size(); j++) {
                                    newBeans.add(itemsBeans.get(j));
                                }
                                ResultInsuranceModel.ItemsBean.SubItemsBean mSubItemsBean = new ResultInsuranceModel.ItemsBean.SubItemsBean();
                                mSubItemsBean.setTitle(itemsBeanList.get(i).getTitle());
                                mSubItemsBean.setDeadline("0");
                                mSubItemsBean.setCount(itemsBeanList.get(i).getSumCount());
                                mSubItemsBean.setMoney(itemsBeanList.get(i).getSumMoney());
                                registrationTotalNum += Integer.valueOf(itemsBeanList.get(i).getSumMoney());
                                newBeans.add(mSubItemsBean);
                            }
                            textRegistrationTotalNum.setText(String.valueOf(registrationTotalNum));
                            mAdapter = new InsuranceShowAdapter(mContext, newBeans);
                            listInsurance.setAdapter(mAdapter);
                            mProgressHUD.dismiss();
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PersonalStatisticTjActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.myToast(mContext, "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.relative_startTime, R.id.relative_endTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_scan:
                initData();
                break;
            case R.id.relative_startTime:
                startTime.show();
                break;
            case R.id.relative_endTime:
                endTime.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public static void goActivity(Context context, String title) {
        Intent intent = new Intent(context, PersonalStatisticTjActivity.class);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }
}
