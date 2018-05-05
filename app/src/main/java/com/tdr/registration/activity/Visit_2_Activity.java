package com.tdr.registration.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tdr.registration.R;
import com.tdr.registration.adapter.VisitList2Adapter;
import com.tdr.registration.model.CallInfo;
import com.tdr.registration.model.VisitListModel;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.CallInfoService;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.tdr.registration.view.popwindow.PopRelationPhoneNumber;
import com.tdr.registration.view.popwindow.PopVisit;
import com.tdr.registration.view.popwindow.PopVisitResult;
import com.tdr.registration.view.popwindow.PopVisitType;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 回访中心子页
 */
@ContentView(R.layout.activity_2_visit)
public class Visit_2_Activity extends Activity implements View.OnClickListener, Handler.Callback {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;
    @ViewInject(R.id.LL_Title)
    private LinearLayout LL_Title;
    @ViewInject(R.id.TV_title)
    private TextView TV_title;
    @ViewInject(R.id.IV_Triangle)
    private ImageView IV_Triangle;
    @ViewInject(R.id.ET_Search)
    private EditText ET_Search;
    @ViewInject(R.id.LL_Search)
    private LinearLayout LL_Search;


    @ViewInject(R.id.XRL_Visit)
    private XRecyclerView RL_Visit;
    @ViewInject(R.id.TV_null)
    private TextView TV_null;


    final static int REQUEST_CODE_ASK_CALL_PHONE = 100;
    private ZProgressHUD mProgressHUD;
    private VisitList2Adapter VLA;
    private Activity mActivity;
    private PopVisitType PVT;//筛选回访列表
    private PopVisitResult PVR;//回访类型
    private PopVisit PV;//回访
    private PopRelationPhoneNumber PRPN;//关联手机号
    private Gson mGson;
    private int datesize = 0;
    private int startIdx = 0;
    private int endIdx = 50;
    private String CallPhone = "";
    private List<VisitTypeModel> VTM = new ArrayList<VisitTypeModel>();
    private List<VisitTypeModel> VTMS = new ArrayList<VisitTypeModel>();//所选筛选条件
    private List<VisitListModel> VLM = new ArrayList<VisitListModel>();
    private List<VisitListModel> VLMSelect = new ArrayList<VisitListModel>();//赛选后的数据
    private VisitListModel VL;
    private boolean isVisit;
    private boolean isgetdata = false;
    private String date = "";
    private List<CallInfo> PhoneCallInfoList;
    private CallInfo mCallInfo;
    private boolean isCall = false;
    private Handler mHandler;
    private final int SERACH = 0;
    private boolean isruning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        initdata();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SERACH:
                if(isruning){
                    if (mHandler.hasMessages(SERACH)) {
                        mHandler.removeMessages(SERACH);
                    }
                    mHandler.sendEmptyMessageDelayed(SERACH,500);
                }else{
                    Search((String) msg.obj);
                }
                break;
        }
        return false;
    }

    /**
     * 加载视图
     */
    private void initview() {
        mActivity = this;
        mGson = new Gson();
        mHandler = new Handler(this);
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        IV_Back.setOnClickListener(this);
        LL_Title.setOnClickListener(this);
        date = getIntent().getExtras().getString("date");
        TV_title.setText(date);
        setPopupWindow();
        ET_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mHandler.hasMessages(SERACH)) {
                    mHandler.removeMessages(SERACH);
                }
                Message message = new Message();
                if (!TextUtils.isEmpty(s.toString())) {
                    LL_Search.setVisibility(View.GONE);
                    message.what = SERACH;
                    message.obj = s.toString();
                    mHandler.sendMessage(message);
                } else {
                    LL_Search.setVisibility(View.VISIBLE);
                    VLA.update(VLM);
                }

            }
        });
    }

    private void Search(String s) {
        mLog.e("s="+s);
        List<VisitListModel> vlm = new ArrayList<>();
        for (VisitListModel visitListModel : VLM) {
            if ((visitListModel.getPLATENUMBER()!=null&&visitListModel.getPLATENUMBER().contains(s))
                    ||  (visitListModel.getOWNERNAME()!=null&&visitListModel.getOWNERNAME().contains(s))) {
                vlm.add(visitListModel);
            }
        }
        VLA.update(vlm);
        isruning=false;
    }

    /**
     * 加载数据
     */
    private void initdata() {
        SetVisitList();
        setVisitType();
        try {
            PhoneCallInfoList = CallInfoService.getCallInfo(this);
            mLog.e("---------------今日电话记录");
            for (CallInfo info : PhoneCallInfoList) {
                mLog.e("号码：" + info.number + "  日期：" + info.date + "  类型：" + info.type + "  时长：" + info.Time);
            }
            mLog.e("---------------");
        } catch (Exception e) {
            CallInfoService.getPermissions(this);
        }
        PRPN.UpDate(CallInfoService.getCallInfo(this, false));
    }

    /**
     * 获取回访类型并加载
     */
    private void setVisitType() {
        VTM = (List<VisitTypeModel>) TransferUtil.retrieve("VisitType");
        if (VTM == null) {
            getVistiType();
        } else {
            List<VisitTypeModel> list = new ArrayList<VisitTypeModel>();
            VisitTypeModel vtm = new VisitTypeModel();
            vtm.setNAME("尚未回访");
            vtm.setCASE_STATUS("-1");
            vtm.setREASONID("-1");
            list.add(vtm);
            for (VisitTypeModel visitTypeModel : VTM) {
                list.add(visitTypeModel);
            }
            PVT.update(list);
            PVR.update(VTM);
        }
    }

    /**
     * 设置弹窗
     */
    private void setPopupWindow() {
        //回访筛选弹窗
        PVT = new PopVisitType(mActivity, new PopVisitType.OnPopCallBack() {
            @Override
            public void onBack(List<VisitTypeModel> CDL) {
                Select(CDL);
            }
        });
        //回访结果选择弹窗
        PVR = new PopVisitResult(mActivity, new PopVisitResult.OnPopCallBack() {
            @Override
            public void onBack(VisitTypeModel CDL, String remak) {
                mLog.e("CDL=" + CDL.getNAME() + "   remak=" + remak + "   Phone=" + CallPhone);
                Visit(CDL, remak);
            }
        });
        //回访操作弹窗
        PV = new PopVisit(mActivity, new PopVisit.OnPopCallBack() {
            @Override
            public void onRelationPhoneNumber() {
                PRPN.UpDate(CallInfoService.getCallInfo(mActivity, false));
                PRPN.show();
            }

            @Override
            public void onResult() {
                if (PVR.isShowing()) {
                    PVR.dismiss();
                } else {
                    PVR.show();
                }
            }

            @Override
            public void onCall() {
                requestPermissionOrCallPhone();
            }

            @Override
            public void onBack() {
                if (mCallInfo != null && isCall) {
                    dialogShow(1, "您有一条电话记录尚未回访，确定要退出吗？");
                } else {
                    PV.dismiss();
                }
            }
        });
        PRPN = new PopRelationPhoneNumber(mActivity, PhoneCallInfoList, new PopRelationPhoneNumber.OnReltionListener() {
            @Override
            public void onRelationPhoneNumber(CallInfo callinfo) {
                mCallInfo = callinfo;
                Utils.showToast("电话号码："+mCallInfo.number+"关联到 " + VL.getPLATENUMBER()+" 成功。请及时提交回访结果！");
            }
        });
    }

    /**
     * 筛选回访数据类型
     *
     * @param CDL
     */
    private void Select(List<VisitTypeModel> CDL) {
        if (CDL.size() > 0) {
            isVisit = CDL.get(0).isSelect();
        }
        for (VisitTypeModel v : CDL) {
            if (v.isSelect()) {
                mLog.e("NAME:" + v.getNAME());
            }
        }
        VTMS.clear();
        for (int i = 1; i < CDL.size(); i++) {//初始化赛选条件
            if (CDL.get(i).isSelect()) {
                VTMS.add(CDL.get(i));
            }
        }
        SelectDate();
    }

    /**
     * 加载回访数据列表
     */
    private void SetVisitList() {
        VLA = new VisitList2Adapter(mActivity, VLM);
        VLA.setOnItemVisitClickLitener(new VisitList2Adapter.OnItemVisitClickLitener() {
            @Override
            public void onItemVisitClick(int position) {
                VL = VLM.get(position);
                CallPhone = VL.getPHONE();
//                CallPhone = "15888457103";
                mLog.e(position + "CallPhone=" + CallPhone);
                if (PV.isShowing()) {
                    PV.dismiss();
                } else {
                    PV.show();
                }
            }
        });

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RL_Visit.setLayoutManager(linearLayoutManager);
        RL_Visit.addItemDecoration(new RVItemDecoration());
        RL_Visit.setAdapter(VLA);
        RL_Visit.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getVistiList(true);
            }

            @Override
            public void onLoadMore() {
                getVistiList(false);
            }
        });
        RL_Visit.refresh();

    }

    /**
     * 获取回访类型
     */
    private void getVistiType() {

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBACKVISITRESULTDIC, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("回访类型列表：" + result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    VTM = mGson.fromJson(data, new TypeToken<List<VisitTypeModel>>() {
                                    }.getType());
                                    List<VisitTypeModel> list = new ArrayList<VisitTypeModel>();
                                    VisitTypeModel vtm = new VisitTypeModel();
                                    vtm.setNAME("尚未回访");
                                    vtm.setCASE_STATUS("-1");
                                    vtm.setREASONID("-1");
                                    list.add(vtm);
                                    for (VisitTypeModel visitTypeModel : VTM) {
                                        list.add(visitTypeModel);
                                    }
                                    PVT.update(list);
                                    PVR.update(VTM);
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
     * 获取回访列表
     */
    private void getVistiList(final boolean isReflish) {
        isgetdata = true;
        String start = "0";
        String end = "50";
        if (!isReflish) {
            start = (startIdx + datesize) + "";
            end = (endIdx + datesize) + "";
        }
        TV_null.setText("正在获取数据");
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("date", date);
        map.put("startIdx", start);
        map.put("endIdx", end);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETBACKVISITBYDATEPAGER, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("回访车辆列表：" + result);
                        isgetdata = false;
                        if (isReflish) {
                            RL_Visit.refreshComplete();
                        } else {
                            RL_Visit.loadMoreComplete();
                        }
                        if (result != null) {
                            String data = "";
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    List<VisitListModel> vlm = mGson.fromJson(data, new TypeToken<List<VisitListModel>>() {
                                    }.getType());
                                    if (isReflish) {
                                        VLM = vlm;
                                    } else {
                                        if (vlm.size() == 0 || vlm.size() < endIdx) {
                                            Utils.showToast("没有更多的数据了");
                                        }
                                        for (VisitListModel v : vlm) {
                                            VLM.add(v);
                                        }
                                    }
                                    datesize = VLM.size();
                                    mLog.e(VLM.size() + "datesize=" + datesize);
                                    VLA.update(VLM);
                                    if (VLM.size() == 0) {
                                        TV_null.setVisibility(View.VISIBLE);
                                        TV_null.setText("暂无回访数据！");
                                    } else {
                                        TV_null.setVisibility(View.GONE);
                                    }
                                    mLog.e(VLM.size() + "   数据=" + (endIdx + datesize));
                                    SelectDate();

                                } else {
                                    Utils.showToast(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utils.showToast("JSON解析出错");
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                Utils.showToast("JSON解析出错\n" + data);
                            }
                        } else {
                            Utils.showToast("获取数据超时，请检查网络连接");
                        }
                    }
                });
    }

    /**
     * 回访
     */
    private void Visit(final VisitTypeModel CDL, String remark) {
        final String time = getCallTime();
//        final String time="16秒";
        mProgressHUD.show();
        JSONObject JB = new JSONObject();
        try {
            JB.put("CASEID", VL.getCASEID());
            JB.put("STATUS", CDL.getCASE_STATUS());
            JB.put("REASONID", CDL.getREASONID());
            JB.put("REMARK", remark);
            JB.put("ECID", VL.getECID());
            if (mCallInfo == null) {
                mLog.e("111111111");
                JB.put("DIAL_PHONE", CallPhone);
                JB.put("TALK_TIME", time);
                JB.put("DIAL_TIME", getCallDate());
                JB.put("DIAL_TYPE", getCallType());

            } else {
                mLog.e("222222222");
                JB.put("DIAL_PHONE", mCallInfo.number);
                JB.put("TALK_TIME", getCallTime2());
                JB.put("DIAL_TIME", getCallDate(mCallInfo.date));
                JB.put("DIAL_TYPE", mCallInfo.type);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", JB.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ADDBACKVISITRECORD, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("车辆回访：" + result);
                        if (result != null) {
                            mProgressHUD.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                Utils.showToast(data);
                                if (errorCode == 0) {
                                    mLog.e("VL=" + VL.getCASEID());
                                    for (VisitListModel visitListModel : VLM) {
                                        mLog.e("visitListModel=" + visitListModel.getCASEID());
                                        if (visitListModel.getCASEID().equals(VL.getCASEID())) {
                                            mLog.e("visitListModel=VL");
                                            visitListModel.setSTATUS("1");
                                            visitListModel.setVISITRESULT(CDL.getCASE_STATUS());
                                            visitListModel.setVISITRESULTDES(CDL.getNAME());
                                            if (time != null && !time.equals("")) {
                                                visitListModel.setTALK_TIME(time);
                                            }
                                        }
                                    }
                                    mCallInfo = null;
                                    isCall = false;
                                    CallPhone="";
                                    VLA.update(VLM);
                                }
                                PV.dismiss();
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
     * 数据筛选
     */
    private void SelectDate() {
        VLMSelect.clear();
        List<VisitListModel> vlm = new ArrayList<VisitListModel>();
        for (VisitListModel V : VLM) {//初始化赛选数据
            V.setSelelct(false);
            vlm.add(V);
        }
        mLog.e("isVisit=" + isVisit);
        if (isVisit) {
            for (VisitListModel v : vlm) {//赛选是否已回访
                if (v.getSTATUS().equals("0")) {
                    v.setSelelct(true);
                }
            }
        }
        mLog.e("VTMS=" + VTMS.size());
        for (VisitListModel VL : vlm) {//赛选回访条件
            for (int i = 0; i < VTMS.size(); i++) {
                if (VTMS.get(i).getCASE_STATUS().equals(VL.getVISITRESULT())) {
                    VL.setSelelct(true);
                }
            }
        }
        for (VisitListModel v : vlm) {//赛选结果
            if (v.isSelelct()) {
                v.setSelelct(false);
                VLMSelect.add(v);
            }
        }
        if (!isVisit && VTMS.size() == 0) {//赛选条件为空 不进行赛选

            VLMSelect = new ArrayList<VisitListModel>(VLM);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
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
        if (PV != null && PV.isShowing()) {
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


    /**
     * 申请权限
     */
    private void requestPermissionOrCallPhone() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE);

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                callPhone();
            }
        } else {
            callPhone();
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case CallInfoService.MY_PERMISSIONS_REQUESTS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，开始获取通话记录
                    mLog.e("所需权限授权成功！");
                    PhoneCallInfoList = CallInfoService.getCallInfo(this);
                    for (CallInfo info : PhoneCallInfoList) {
                        mLog.e("记录：\n号码：" + info.number + "  日期：" + info.date + "  类型：" + info.type + "  时长：" + info.Time);
                    }
                }
                break;
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    Utils.showToast("无拨号权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private String getCallTime2() {
        String time = "";
        PhoneCallInfoList = CallInfoService.getCallInfo(this,false);
        for (CallInfo callInfo : PhoneCallInfoList) {
//            mLog.e("1号码=" + callInfo.number + " " + callInfo.date);
            if (mCallInfo.number.equals(callInfo.number)&&callInfo.date==mCallInfo.date) {
                time = callInfo.Time;
                mLog.e("号码=" + callInfo.number + "  时长=" + time);
                break;
            }
        }
        return time;
    }

    private String getCallTime() {
        String time = "";
        PhoneCallInfoList = CallInfoService.getCallInfo(this);
        for (CallInfo callInfo : PhoneCallInfoList) {
            if (CallPhone.equals(callInfo.number)) {
                time = callInfo.Time;
                mLog.e("号码=" + callInfo.number + "  时长=" + time);
                break;
            }
        }
        return time;
    }

    private String getCallDate() {
        String time = "";
        PhoneCallInfoList = CallInfoService.getCallInfo(this);
        for (CallInfo callInfo : PhoneCallInfoList) {
            if (CallPhone.equals(callInfo.number)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date dt = new Date(callInfo.date);
                time = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
                mLog.e("号码=" + callInfo.number + "  通话时间=" + time);
                break;
            }
        }
        return time;
    }

    private String getCallDate(long t) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date dt = new Date(t);
        String time = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
        return time;
    }

    private String getCallType() {
        int type = 0;
        PhoneCallInfoList = CallInfoService.getCallInfo(this);
        for (CallInfo callInfo : PhoneCallInfoList) {
            if (CallPhone.equals(callInfo.number)) {
                type = callInfo.type;
                mLog.e("号码=" + callInfo.number + "  通话类型=" + type);
                break;
            }
        }
        return type + "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        PhoneCallInfoList = CallInfoService.getCallInfo(this);

        if (PhoneCallInfoList != null && PhoneCallInfoList.size() > 0 && isCall) {
            mLog.e("1number="+PhoneCallInfoList.get(0).number);
            mLog.e("2number="+PhoneCallInfoList.get(PhoneCallInfoList.size()-1).number);
            if(PhoneCallInfoList.get(0).number.equals(CallPhone)){
                mCallInfo = PhoneCallInfoList.get(0);
            }else{
                mCallInfo = PhoneCallInfoList.get(PhoneCallInfoList.size()-1);
            }
            mLog.e("number="+mCallInfo.number+"  Time="+mCallInfo.Time);
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
            if (!PVR.isShowing() && !PV.isShowing() && !PVT.isShowing() && !PRPN.isShowing()) {
                onBackPressed();
            }
            if (PV.isShowing()) {
                PV.dismiss();
                mCallInfo = null;
                isCall = false;
            }
            if (PVT.isShowing()) {
                PVT.dismiss();
            }
            if (PVR.isShowing()) {
                PVR.dismiss();
            }
            if (PRPN.isShowing()) {
                PRPN.dismiss();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 拨号方法
     */
    private void callPhone() {
        int Type = CheckCallTime();
        if (Type == 0) {
            Call();
        } else if (Type == 1) {
            dialogShow(0, "您在早间时段(6:00-12:00)已呼叫过" + CallPhone + "该机主,为避免影响该用户的生活，请等待至午间时段再呼叫该用户。");
        } else if (Type == 2) {
            dialogShow(0, "您在午间时段(12:00-23:00)已呼叫过" + CallPhone + "该机主,为避免影响该用户的生活，请等待至明天再呼叫该用户  。");
        }
//        else if (Type == 3) {
//            dialogShow("您在晚间时段(19:00-23:00)已呼叫过"+CallPhone+"该机主,为避免影响该用户的生活，请等待至明天再呼叫该用户。");
//        }

    }

    private void Call() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + CallPhone));
        startActivity(intent);
        isCall = true;
    }

    /**
     * 检查通话时间再哪个时间段  1早上(6-12)、2下午(13-18)、3晚上(19-23)
     *
     * @return
     */
    private int CheckCallTime() {
        PhoneCallInfoList = CallInfoService.getCallInfo(this);
        for (CallInfo callInfo : PhoneCallInfoList) {
            if (CallPhone.equals(callInfo.number)) {
                Date currentTime = new Date(callInfo.date);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                int hour = Integer.parseInt(dateString.substring(11, 13));
                if (hour >= 6 && hour < 12) {
                    return 1;
                }
                if (hour >= 12 && hour <= 23) {
                    return 2;
                }
//                if (hour >= 19 && hour <= 23) {
//                    return 3;
//                }
//                mLog.e("号码=" + callInfo.number + "  时长=" + time);
            }
        }
        return 0;
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int type, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        effectstype = NiftyDialogBuilder.Effectstype.Shake;
        if (type == 0) {
            dialogBuilder.setCustomView(R.layout.custom_view, mActivity)
                    .withTitle("请勿连续拨打同一个机主")
                    .withTitleColor("#333333")
                    .withMessage(msg)
                    .isCancelableOnTouchOutside(false)
                    .withEffect(effectstype)
                    .withButton1Text("我需要拨打")
                    .withButton2Text("我知道了")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            Call();
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).show();
        } else if (type == 1) {
            dialogBuilder.setCustomView(R.layout.custom_view, mActivity)
                    .withTitle("退出本次回访")
                    .withTitleColor("#333333")
                    .withMessage(msg)
                    .isCancelableOnTouchOutside(false)
                    .withEffect(effectstype)
                    .withButton1Text("取消回访")
                    .withButton2Text("马上回访")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            PV.dismiss();
                            mCallInfo = null;
                            isCall = false;
                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    PVR.show();
                }
            }).show();
        }
    }

}
