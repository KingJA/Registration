package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.ShangPaiStatistics;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.HashMap;

/**
 * 电信预登记统计
 */
@ContentView(R.layout.activity_dx_pre_registration_statistics_tj)
public class DX_PreRegistration_Statistics_Activity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.image_back)
    ImageView imageBack;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.image_scan)
    ImageView imageScan;

    @ViewInject(R.id.RL_startTime)
    RelativeLayout RL_startTime;

    @ViewInject(R.id.RL_endTime)
    RelativeLayout RL_endTime;

    @ViewInject(R.id.TV_startTime)
    TextView TV_startTime;
    @ViewInject(R.id.TV_endTime)
    TextView TV_endTime;

    @ViewInject(R.id.TV_registrationName)
    TextView TV_registrationName;

    @ViewInject(R.id.TV_registrationNumber)
    TextView TV_registrationNumber;

    @ViewInject(R.id.TV_registrationArea)
    TextView TV_registrationArea;

    @ViewInject(R.id.TV_PR_UnApproval)
    TextView TV_PR_UnApproval;

    @ViewInject(R.id.TV_PR_Approval)
    TextView TV_PR_Approval;

    @ViewInject(R.id.TV_PR_sum)
    TextView TV_PR_sum;


    private Activity mActivity;
    private TimePickerView startTime;
    private TimePickerView endTime;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private StatisticsData SD;
    private Callback.Cancelable cancelable;
    private ShangPaiStatistics shangPaiStatistics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initData();
    }

    private void initview() {
        mActivity = this;
        mGson = new Gson();
        textTitle.setText("上牌统计");
        imageScan.setBackgroundResource(R.mipmap.ic_refresh);
        imageScan.setVisibility(View.VISIBLE);
        imageScan.setOnClickListener(this);
        imageBack.setOnClickListener(this);
        RL_startTime.setOnClickListener(this);
        RL_endTime.setOnClickListener(this);

        startTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        startTime.setTime(new Date());
        startTime.setCyclic(false);
        startTime.setCancelable(true);
        startTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_startTime.setText(Utils.setDate(date));
            }
        });
        endTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        endTime.setTime(new Date());
        endTime.setCyclic(false);
        endTime.setCancelable(true);
        endTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_endTime.setText(Utils.setDate(date));
            }
        });
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");

        TV_startTime.setText(Utils.getNowDate());
        TV_endTime.setText(Utils.getNowDate());
    }

    private void initData() {
        mProgressHUD.show();
        RequestParams rp = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_VehicleBoardStatisticsAPP);
        rp.addBodyParameter("startDate", TV_startTime.getText().toString().trim()+" 00:00:00");
        rp.addBodyParameter("endDate", TV_startTime.getText().toString().trim()+" 23:59:59");
        cancelable = HttpUtils.get(rp, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                mProgressHUD.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");
                    if (errorCode == 0) {
                        shangPaiStatistics = mGson.fromJson(data, new TypeToken<ShangPaiStatistics>() {
                        }.getType());
                        setdata();
                    } else if (errorCode == 1) {
                        Utils.showToast(data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(DX_PreRegistration_Statistics_Activity.this,
                                LoginActivity.class);
                    } else {
                        //错误
                        String errorMsg = jsonObject.getString("Data");
                        Utils.showToast(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                mProgressHUD.dismiss();
                Utils.showToast("网络异常");
            }
        });
    }

    private void setdata() {
        TV_registrationName.setText((String) SharedPreferencesUtils.get("regionName", ""));//登记点名称
        TV_registrationNumber.setText((String) SharedPreferencesUtils.get("regionNo", ""));//登记点编号
        TV_registrationArea.setText((String) SharedPreferencesUtils.get("regionName", ""));//所属辖区
        TV_PR_UnApproval.setText(shangPaiStatistics.getNo_theftno());
        TV_PR_Approval.setText(shangPaiStatistics.getHas_theftno());
        TV_PR_sum.setText(shangPaiStatistics.getHas_platenumber());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.image_scan:
                initData();
                break;
            case R.id.RL_startTime:
                startTime.show();
                break;
            case R.id.RL_endTime:
                endTime.show();
                break;
            default:
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

    class StatisticsData {
        String id;
        String login;
        String type;
        String username;
        String unitid;
        String manageunitid;
        String unitname;
        String unitno;
        String unittype;
        String total;
        String no_platenumber;
        String has_platenumber;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUnitid() {
            return unitid;
        }

        public void setUnitid(String unitid) {
            this.unitid = unitid;
        }

        public String getManageunitid() {
            return manageunitid;
        }

        public void setManageunitid(String manageunitid) {
            this.manageunitid = manageunitid;
        }

        public String getUnitname() {
            return unitname;
        }

        public void setUnitname(String unitname) {
            this.unitname = unitname;
        }

        public String getUnitno() {
            return unitno;
        }

        public void setUnitno(String unitno) {
            this.unitno = unitno;
        }

        public String getUnittype() {
            return unittype;
        }

        public void setUnittype(String unittype) {
            this.unittype = unittype;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getNo_platenumber() {
            return no_platenumber;
        }

        public void setNo_platenumber(String no_platenumber) {
            this.no_platenumber = no_platenumber;
        }

        public String getHas_platenumber() {
            return has_platenumber;
        }

        public void setHas_platenumber(String has_platenumber) {
            this.has_platenumber = has_platenumber;
        }
    }
}
