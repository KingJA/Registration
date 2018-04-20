package com.tdr.registration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.VisitStatisticsModel;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.NoScrollGridView;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 回访统计.
 */
@ContentView(R.layout.activity_visit_statistics)
public class VisitStatistics extends Activity implements View.OnClickListener {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.IV_Refresh)
    private ImageView IV_Refresh;

    @ViewInject(R.id.RL_startTime)
    private RelativeLayout RL_startTime;
    @ViewInject(R.id.TV_starttime)
    private TextView TV_starttime;


    @ViewInject(R.id.RL_EndTime)
    private RelativeLayout RL_EndTime;
    @ViewInject(R.id.TV_endtime)
    private TextView TV_endtime;

    @ViewInject(R.id.GV_VisitType)
    private NoScrollGridView GV_VisitType;

    @ViewInject(R.id.TV_VisitSum)
    private TextView TV_VisitSum;

    @ViewInject(R.id.TV_NotVisitSum)
    private TextView TV_NotVisitSum;
    private Activity mActivity;
    private Gson mGson;
    private MyGVAdapter GVAdapter;
    private List<Visit> VisitType = new ArrayList<>();
    private TimePickerView startTime;
    private TimePickerView endTime;
    private List<VisitTypeModel> VTM;
    private VisitStatisticsModel VSM;
    private ZProgressHUD mProgressHUD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
    }

    private void initview() {
        mActivity = this;
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        IV_Back.setOnClickListener(this);
        IV_Refresh.setOnClickListener(this);
        RL_startTime.setOnClickListener(this);
        RL_EndTime.setOnClickListener(this);
        TV_starttime.setText(Utils.getNowDate());
        TV_endtime.setText(Utils.getNowDate());
        startTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        startTime.setTime(new Date());
        startTime.setCyclic(false);
        startTime.setCancelable(true);
        startTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_starttime.setText(Utils.setDate(date));
            }
        });
        endTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        endTime.setTime(new Date());
        endTime.setCyclic(false);
        endTime.setCancelable(true);
        endTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                TV_endtime.setText(Utils.setDate(date));
            }
        });
        VTM = (List<VisitTypeModel>) TransferUtil.retrieve("VisitType");
        if (VTM == null || VTM.size() == 0) {
            getVistiType();
        }

        GVAdapter = new MyGVAdapter();
        GV_VisitType.setAdapter(GVAdapter);
        getVisitStatistics();
    }

    /**
     * 获取回访统计
     */
    private void getVisitStatistics() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("startDate", TV_starttime.getText().toString().trim());
        map.put("endDate", TV_endtime.getText().toString().trim());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBACKVISITSTATISTICS, map,
                new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(String result) {
                        mLog.e("回访统计：" + result);
                        mProgressHUD.dismiss();
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    VSM = mGson.fromJson(data, new TypeToken<VisitStatisticsModel>() {
                                    }.getType());
                                    setData();
                                } else {
                                    Utils.showToast(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast("JSON解析出错");
                            }
                        } else {
                            Utils.showToast("获取数据超时，请检查网络连接");
                        }
                    }
                });
    }
    private void setData(){
        TV_VisitSum.setText(VSM.getNeed_Visit_Total());
        TV_NotVisitSum.setText(getNotVisit());
        mLog.e("Records="+ VSM.getRecords().size());
        mLog.e("VTM="+ VTM.size());
        VisitType.clear();
        for (VisitStatisticsModel.Records records : VSM.getRecords()) {
            for (VisitTypeModel visitTypeModel : VTM) {
                if(visitTypeModel.getCASE_STATUS().equals(records.getStatus())){
                    mLog.e(records.getParentStatus()+"====="+ records.getStatusTxt());
                    if(!records.getParentStatus().equals("0")){
                        VisitType.add(new Visit(records.getStatusTxt(),records.getResultCnt()));
                        mLog.e(records.getResultCnt()+"-----"+ records.getStatusTxt());
                    }
                }
            }
        }
        GVAdapter.notifyDataSetChanged();
    }
    private String getNotVisit() {
        int A=0;
        int B=0;
        try {
            A = Integer.parseInt(VSM.getNeed_Visit_Total());
            B = Integer.parseInt(VSM.getHas_Visit_Total());
        } catch (NumberFormatException e) {
            return "";
        }
        int C = A - B;
        return C + "";
    }

    /**
     * 获取回访类型
     */
    private void getVistiType() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBACKVISITRESULTDIC, map,
                new WebServiceUtils.WebServiceCallBack() {

                    @Override
                    public void callBack(String result) {
                        mProgressHUD.dismiss();
                        mLog.e("回访类型列表：" + result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    VTM = mGson.fromJson(data, new TypeToken<List<VisitTypeModel>>() {
                                    }.getType());
                                    GVAdapter.notifyDataSetChanged();
                                } else {
                                    Utils.showToast(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast("JSON解析出错");
                            }
                        } else {
                            Utils.showToast("获取数据超时，请检查网络连接");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
                break;
            case R.id.IV_Refresh:
                getVisitStatistics();
                break;
            case R.id.RL_startTime:
                startTime.show();
                break;
            case R.id.RL_EndTime:
                endTime.show();
                break;

        }
    }

    class MyGVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return VisitType.size();
        }

        @Override
        public Object getItem(int position) {
            return VisitType.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(mActivity).inflate(R.layout.item_visit_statistics, null);
            v.setMinimumHeight(AppUtil.dp2px(60));
            ((TextView) v.findViewById(R.id.TV_num)).setText(VisitType.get(position).getVisitNum());
            ((TextView) v.findViewById(R.id.TV_type)).setText(VisitType.get(position).getVisitName());
            return v;
        }
    }

    public class Visit {
        String VisitName;
        String VisitNum;

        public Visit(String name, String num) {
            VisitName = name;
            VisitNum = num;
        }

        public String getVisitName() {
            return VisitName;
        }

        public void setVisitName(String visitName) {
            VisitName = visitName;
        }

        public String getVisitNum() {
            return VisitNum;
        }

        public void setVisitNum(String visitNum) {
            VisitNum = visitNum;
        }
    }
}
