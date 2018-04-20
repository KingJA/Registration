package com.tdr.registration.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tdr.registration.R;
import com.tdr.registration.adapter.OrderAdapter;
import com.tdr.registration.model.OrderModel;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.PopVisitType;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 订单列表
 */
@ContentView(R.layout.activity_order_details)
public class OrderListActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;
    @ViewInject(R.id.LL_Title)
    private LinearLayout LL_Title;
    @ViewInject(R.id.TV_title)
    private TextView TV_title;
    @ViewInject(R.id.IV_Triangle)
    private ImageView IV_Triangle;
    @ViewInject(R.id.TV_ShowTime)
    private TextView TV_ShowTime;
    @ViewInject(R.id.IV_Time)
    private ImageView IV_Time;
    @ViewInject(R.id.IV_X)
    private ImageView IV_X;

    @ViewInject(R.id.XRL_Visit)
    private XRecyclerView RL_Visit;
    @ViewInject(R.id.TV_null)
    private TextView TV_null;


    private ZProgressHUD mProgressHUD;
    private OrderAdapter VLA;
    private Activity mActivity;
    private PopVisitType PVT;//筛选回访列表
    private Gson mGson;
    private List<VisitTypeModel> VTMS = new ArrayList<VisitTypeModel>();//所选筛选条件
    private List<OrderModel> VLMSelect = new ArrayList<OrderModel>();//赛选后的数据
    private boolean isgetdata = false;
    private int pagenum = 1;
    private List<String> TimeList;
    private List<OrderModel> OMList;

    private TimePickerView StartTimePickerView;
    private TimePickerView EndTimePickerView;
    private String StartTime = "";
    private String EndTime = "";
    private  String ServerTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
        getBillreCordList(true);
        TimeList = Utils.getMonth();
        getServerTime();



    }

    /**
     * 加载视图
     */
    private void initview() {
        mActivity = this;
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        IV_Back.setOnClickListener(this);
        LL_Title.setOnClickListener(this);
        IV_Time.setOnClickListener(this);
        IV_X.setOnClickListener(this);
        //回访筛选弹窗
        PVT = new PopVisitType(mActivity, new PopVisitType.OnPopCallBack() {
            @Override
            public void onBack(List<VisitTypeModel> CDL) {
                Select(CDL);
            }
        });
        StartTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        StartTimePickerView.setTime(new Date());
        StartTimePickerView.setCyclic(false);
        StartTimePickerView.setCancelable(true);
        StartTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                StartTime = Utils.setDate(date);
                dialogShow();
            }
        });
        EndTimePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        EndTimePickerView.setTime(new Date());
        EndTimePickerView.setCyclic(false);
        EndTimePickerView.setCancelable(true);
        EndTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                EndTime = Utils.setDate(date);
                dialogShow();
            }
        });
    }

    /**
     * 加载数据
     */
    private void initdata() {
        SetVisitList();
        setVisitType();

    }

    /**
     * 获取回访类型并加载
     */
    private void setVisitType() {
        PVT.setTitle("订单状态");
        List<VisitTypeModel> list = new ArrayList<VisitTypeModel>();
        VisitTypeModel vtm1 = new VisitTypeModel();
        VisitTypeModel vtm2 = new VisitTypeModel();
        VisitTypeModel vtm3 = new VisitTypeModel();
        vtm1.setNAME("未支付");
        vtm1.setPAY_STATUS("0");
        list.add(vtm1);
        vtm2.setNAME("已支付");
        vtm2.setPAY_STATUS("1");
        list.add(vtm2);
        vtm3.setNAME("已结算");
        vtm3.setPAY_STATUS("2");
        list.add(vtm3);
        PVT.update(list);
    }


    /**
     * 筛选回访数据类型
     *
     * @param CDL
     */
    private void Select(List<VisitTypeModel> CDL) {
        for (VisitTypeModel v : CDL) {
            if (v.isSelect()) {
                mLog.e("NAME:" + v.getNAME());
            }
        }
        VTMS.clear();

        for (VisitTypeModel visitTypeModel : CDL) {
            if (visitTypeModel.isSelect()) {
                VTMS.add(visitTypeModel);
            }
        }
        SelectDate();
    }

    /**
     * 数据筛选
     */
    private void SelectDate() {
        VLMSelect.clear();

        List<OrderModel> vlm = new ArrayList<OrderModel>();
        for (OrderModel withdrawalsModel : OMList) {
            withdrawalsModel.setSelelct(false);
            vlm.add(withdrawalsModel);
        }


        mLog.e("VTMS=" + VTMS.size());
        for (OrderModel VL : vlm) {//赛选回访条件
            for (int i = 0; i < VTMS.size(); i++) {
                if (VTMS.get(i).getPAY_STATUS().equals(VL.getPAYSTATUS())) {
                    VL.setSelelct(true);
                }
            }
        }
        for (OrderModel v : vlm) {//赛选结果
            if (v.isSelelct()) {
                v.setSelelct(false);
                VLMSelect.add(v);
            }
        }
        if (VTMS.size() == 0) {//赛选条件为空 不进行赛选
            VLMSelect = new ArrayList<OrderModel>(OMList);
            mLog.e("VLMSelect=" + VLMSelect.size());
        }
        VLA.update(VLMSelect);
        if (VLMSelect.size() == 0) {
            TV_null.setText("筛选后没有符合条件的数据。");
            TV_null.setVisibility(View.VISIBLE);
        } else {
            TV_null.setVisibility(View.GONE);
        }


    }

    /**
     * 加载回访数据列表
     */
    private void SetVisitList() {
        VLA = new OrderAdapter(mActivity, OMList);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RL_Visit.setLayoutManager(linearLayoutManager);
        RL_Visit.addItemDecoration(new RVItemDecoration());
        RL_Visit.setAdapter(VLA);
        RL_Visit.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getBillreCordList(true);
            }

            @Override
            public void onLoadMore() {
                getBillreCordList(false);
            }
        });


    }



    public void getBillreCordList(final boolean isReflish) {
        isgetdata = true;
        mProgressHUD.show();
        if (isReflish) {
            pagenum = 1;
        }
        HashMap<String, String> map = new HashMap<String, String>();
//    {"pagenum":"分页参数(第几页)","pagesize":"分页参数（每页多少条记录）","PAYSTATUS":"支付状态（0 未支付 1 已支付 ）","CHECKSTATUS":"0 未结算 1 已结算"}
//        ｛"SOB":1(暂固定为1),"pagenum":"","pagesize":"",BILLTIME_START    BILLTIME_END｝
        map.put("pagenum", pagenum + "");
        map.put("pagesize", "10");
        map.put("SOB", "1");
        map.put("BILLTIME_START", StartTime);
        map.put("BILLTIME_END", EndTime);
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GETBILLRECORDLIST_FORUSER);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("GETBILLRECORDLIST_FORUSER:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        isgetdata = false;
                        if (isReflish) {
                            RL_Visit.refreshComplete();
                        } else {
                            RL_Visit.loadMoreComplete();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                List<OrderModel> list = mGson.fromJson(data, new TypeToken<List<OrderModel>>() {
                                }.getType());
                                if (isReflish) {
                                    OMList = list;
                                    if (!StartTime.equals("") && !EndTime.equals("")) {
                                        TV_ShowTime.setText(StartTime + "至" + EndTime);
                                        IV_X.setVisibility(View.VISIBLE);
                                    } else {
                                        TV_ShowTime.setText("");
                                        IV_X.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (list != null && list.size() > 0) {
                                        OMList.addAll(list);
                                    }
                                    if (!StartTime.equals("") && !EndTime.equals("")) {
                                        TV_ShowTime.setText(StartTime + "至" + EndTime);
                                        IV_X.setVisibility(View.VISIBLE);
                                    } else {
                                        TV_ShowTime.setText("");
                                        IV_X.setVisibility(View.GONE);
                                    }
                                    if (list.size() == 10) {
                                        pagenum++;
                                    }
                                }
                                VLA.update(OMList);
                                if (OMList.size() == 0) {
                                    TV_null.setVisibility(View.VISIBLE);
                                    TV_null.setText("暂无订单数据！");
                                } else {
                                    TV_null.setVisibility(View.GONE);
                                }
                                SelectDate();
                            } else {
                                Utils.showToast(data);
                            }
                            mProgressHUD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressHUD.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressHUD.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressHUD.dismiss();
                    mLog.e("Http访问结果：" + Finish);

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
            case R.id.IV_X:
                StartTime = "";
                EndTime = "";
                getBillreCordList(true);
                break;
            case R.id.IV_Time:
                dialogShow();
                break;
            case R.id.LL_Title:
                if (PVT.isShowing()) {
                    PVT.dismiss();
                } else {
                    PVT.show(LL_Title);
                }
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (PVT != null && PVT.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }



    class RVItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //设定底部边距为1px
            outRect.set(0, 0, 0, 20);
        }

    }


    @Override
    public void onBackPressed() {
        mLog.e("onBackPressed");
        if (isgetdata) {
            Utils.showToast("正在加载数据,请稍候...");
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!PVT.isShowing()) {
                onBackPressed();
            } else {
                PVT.dismiss();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow() {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(this);
        View color_view = mInflater.inflate(R.layout.layout_withdrawals_time, null);
        TextView TV_StartTime = (TextView) color_view.findViewById(R.id.TV_StartTime);
        TextView TV_EndTime = (TextView) color_view.findViewById(R.id.TV_EndTime);
        TV_StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                StartTimePickerView.show();

            }
        });
        TV_EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                EndTimePickerView.show();
            }
        });
        if (!StartTime.equals("")) {
            TV_StartTime.setText(StartTime);
        }
        if (!EndTime.equals("")) {
            TV_EndTime.setText(EndTime);
        }
        dialogBuilder.isCancelable(false);
        dialogBuilder.setCustomView(color_view, mActivity);
        dialogBuilder.withTitle("选择查询时段")
                .withTitleColor("#333333")
                .withMessage(null)
                .withEffect(effectstype)
                .withButton1Text("取消")
                .withButton2Text("确定")
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StartTime.equals("")) {
                            Utils.showToast("请选择起始时间");
                            return;
                        }
                        if (!Utils.CheckBuyTime(StartTime, ServerTime)) {
                            Utils.showToast("起始时间已超过当前时间");
                            return;
                        }
                        if (EndTime.equals("")) {
                            Utils.showToast("请选择结束时间");
                            return;
                        }
                        if (!Utils.CheckBuyTime(EndTime, ServerTime)) {
                            Utils.showToast("结束时间已超过当前时间");
                            return;
                        }
                        getBillreCordList(true);
                        dialogBuilder.dismiss();
                    }
                })
                .show();


    }

    public  void getServerTime() {
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETSERVERTIME, null, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    ServerTime = result;
                }else{
                    ServerTime=new Date().getTime()+"";
                }
                mLog.e("ServerTime=" + ServerTime);
            }
        });
    }
}

