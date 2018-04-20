package com.tdr.registration.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.adapter.VisitList1Adapter;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.VisitCenterModel;
import com.tdr.registration.model.VisitListModel;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 回访中心
 */
@ContentView(R.layout.activity_1_visit)
public class Visit_1_Activity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;
    @ViewInject(R.id.IV_GoVisitList)
    private ImageView IV_GoVisitList;
    @ViewInject(R.id.TV_DayVisit)
    private TextView TV_DayVisit;
    @ViewInject(R.id.TV_WeekVisit)
    private TextView TV_WeekVisit;
    @ViewInject(R.id.TV_MonthVisit)
    private TextView TV_MonthVisit;
    @ViewInject(R.id.IV_Time)
    private ImageView IV_Time;
    @ViewInject(R.id.XRL_Visit)
    private XRecyclerView XRL_Visit;
    @ViewInject(R.id.TV_Month)
    private TextView TV_Month;
    @ViewInject(R.id.TV_null)
    private TextView TV_null;


    private ZProgressHUD mProgressHUD;
    private VisitList1Adapter VLA;
    private Activity mActivity;
    private Gson mGson;
    private List<VisitTypeModel> VTM = new ArrayList<VisitTypeModel>();
    private boolean isgetdata = false;
    private List<String> TimeList;
    private VisitCenterModel VCM = new VisitCenterModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        SetVisitList();
        getVistiType();
        TimeList = Utils.getMonth();

    }

    private void initview() {
        mActivity = this;
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        TransferUtil.remove("VisitType");


        IV_Back.setOnClickListener(this);
        IV_GoVisitList.setOnClickListener(this);
        IV_Time.setOnClickListener(this);
    }


    /**
     * 回访列表
     */
    private void SetVisitList() {
        VLA = new VisitList1Adapter(mActivity, VCM.getDetail());
        VLA.setOnItemVisitClickLitener(new VisitList1Adapter.OnItemVisitClickLitener() {
            @Override
            public void onItemVisitClick(int position) {
                mLog.e(VCM.getDetail().size()+"  position="+position);
                if (isgetdata) {
                    Utils.showToast("正在下载回访类型,请稍候...");
                } else {
                    Bundle bundle=new Bundle();
                    bundle.putString("date",VCM.getDetail().get(position).getDate());
                    ActivityUtil.goActivityWithBundle(Visit_1_Activity.this, Visit_2_Activity.class,bundle);
                }
            }
        });

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        XRL_Visit.setLayoutManager(linearLayoutManager);
        XRL_Visit.addItemDecoration(new RVItemDecoration());
        XRL_Visit.setAdapter(VLA);
        XRL_Visit.setLoadingMoreEnabled(false);
        XRL_Visit.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getVistiList(null);
            }

            @Override
            public void onLoadMore() {

            }
        });

        XRL_Visit.refresh();

    }

    /**
     * 获取回访中心
     */
    private void getVistiList(String date) {
        if (date == null) {
            date = Utils.getdate();
        }
        TV_null.setText("正在获取数据");
        TV_Month.setText(date.substring(date.length() - 2) + "月");
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("date", date);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBACKVISITCENTER, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("回访中心列表：" + result);
                        XRL_Visit.refreshComplete();
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    VCM = mGson.fromJson(data, new TypeToken<VisitCenterModel>() {
                                    }.getType());
                                    if(VCM.getDetail()!=null&&VCM.getDetail().size()>0){
                                        TV_null.setVisibility(View.GONE);
                                    }else{
                                        TV_null.setVisibility(View.VISIBLE);
                                        TV_null.setText("暂无回访数据！");
                                    }
                                    TV_DayVisit.setText(VCM.getToday()+"个");
                                    TV_WeekVisit.setText(VCM.getWeek()+"个");
                                    TV_MonthVisit.setText(VCM.getMonth()+"个");
                                    VLA.update(VCM.getDetail());
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

    /**
     * 获取回访类型
     */
    private void getVistiType() {
        isgetdata = true;
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
                                    TransferUtil.save("VisitType", VTM);
                                    isgetdata = false;
                                    getVistiList(null);
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

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow() {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View color_view = mInflater.inflate(R.layout.layout_color, null);
        ListView list_colors1 = (ListView) color_view.findViewById(R.id.list_colors1);
        MyLVAdapter CA = new MyLVAdapter();
        list_colors1.setAdapter(CA);
        list_colors1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getVistiList(TimeList.get(position));
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.isCancelable(false);
        dialogBuilder.setCustomView(color_view, mActivity);
        dialogBuilder.withTitle("选择查询月份")
                .withTitleColor("#333333")
                .withMessage(null)
                .withEffect(effectstype)
                .show();


    }



    class MyLVAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return TimeList.size();
        }

        @Override
        public Object getItem(int position) {
            return TimeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView TV = new TextView(mActivity);
            TV.setText(TimeList.get(position));
            TV.setTextColor(Color.parseColor("#333333"));
            TV.setTextSize(AppUtil.dp2px(10));
            TV.setGravity(Gravity.CENTER);
            return TV;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
                break;
            case R.id.IV_Time:
                dialogShow();
                break;
            case R.id.IV_GoVisitList:
                ActivityUtil.goActivity(Visit_1_Activity.this, VisitStatistics.class);
                break;

        }
    }


    class RVItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //设定底部边距为1px
            outRect.set(0, 0, 0, 20);
        }

    }

}
