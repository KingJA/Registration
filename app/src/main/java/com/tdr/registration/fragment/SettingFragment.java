package com.tdr.registration.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.ModifyPwdActivity;
import com.tdr.registration.activity.UnpaidActivity;
import com.tdr.registration.activity.WalletBalanceActivity;
import com.tdr.registration.event.FleshOrderCountEvent;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 设置fragment
 */
@ContentView(R.layout.fragment_settting)
public class SettingFragment extends Fragment implements View.OnClickListener {
    @ViewInject(R.id.text_userName)
    private TextView textUserName;
    @ViewInject(R.id.text_police)
    private TextView textPolice;
    @ViewInject(R.id.relative_modifyPwd)
    private RelativeLayout relativeModifyPwd;
    @ViewInject(R.id.relative_directions)
    private RelativeLayout relativeDirections;
    @ViewInject(R.id.relative_feedBack)
    private RelativeLayout relativeFeedBack;
    @ViewInject(R.id.text_currentVersion)
    private TextView textCurrentVersion;
    @ViewInject(R.id.linear_quit)
    private LinearLayout linearQuit;
    @ViewInject(R.id.relative_paylsit)
    private RelativeLayout relative_paylsit;
    @ViewInject(R.id.text_pay)
    private TextView text_pay;
    @ViewInject(R.id.RL_WalletBalance)
    private RelativeLayout RL_WalletBalance;

    private String IsShowPay;
    private Gson mGson;
    private ArrayList<PayInsurance> PIL;
    private String ShowWallet;

    private static final int Update = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Update: {//刷新view 显示有多少条未支付订单
                    if (PIL != null && PIL.size() > 0 && text_pay != null) {
                        text_pay.setVisibility(View.VISIBLE);
                        if (PIL.size() > 9) {
                            text_pay.setText("9+");
                        } else {
                            text_pay.setText("" + PIL.size());
                        }
                    } else {
                        text_pay.setVisibility(View.GONE);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (relative_paylsit.getVisibility() == View.VISIBLE) {//有支付列表的功能权限则获取未支付订单数据
            initdate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = x.view().inject(this, inflater, container);
        initview();
        return view;
    }

    /**
     * 加载视图
     */
    private void initview() {
        mGson = new Gson();
        IsShowPay = (String) SharedPreferencesUtils.get("IsShowPay", "");
        ShowWallet = (String) SharedPreferencesUtils.get("ShowWallet", "");
        textUserName.setText((String) SharedPreferencesUtils.get("userName", ""));
        textPolice.setText((String) SharedPreferencesUtils.get("regionName", ""));
        textCurrentVersion.setText(Utils.getVersion(getActivity()).toString());
        text_pay.setVisibility(View.GONE);
        if (IsShowPay.equals("1")) {//是否有支付列表功能的权限
            relative_paylsit.setVisibility(View.VISIBLE);
        } else {
            relative_paylsit.setVisibility(View.GONE);
        }

        ShowWallet = "1";
        if (ShowWallet.equals("1")) {//是否有钱包功能的权限
            RL_WalletBalance.setVisibility(View.VISIBLE);
        } else {
            RL_WalletBalance.setVisibility(View.GONE);
        }

//        if(relative_paylsit.getVisibility()==View.VISIBLE){//有支付列表的功能权限则获取未支付订单数据
//            initdate();
//        }
        relativeModifyPwd.setOnClickListener(this);
        relativeDirections.setOnClickListener(this);
        relativeFeedBack.setOnClickListener(this);
        linearQuit.setOnClickListener(this);
        relative_paylsit.setOnClickListener(this);
        RL_WalletBalance.setOnClickListener(this);

    }

    /**
     * 获取未支付列表的数据
     */
    private void initdate() {
        mLog.e("-------------");
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("PAYNO", "");
        map.put("PLATENUMBER", "");
        map.put("STATUS", "0");
        WebServiceUtils.callWebService(getActivity(), (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
                        .WEBSERVER_GETPAYBILLLIST, map,
                new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            Utils.LOGE("Pan", result);
                            try {
                                JSONObject JB = new JSONObject(result);
                                int errorCode = JB.getInt("ErrorCode");
                                String data = JB.getString("Data");
                                if (errorCode == 0) {
                                    PIL = mGson.fromJson(data, new TypeToken<List<PayInsurance>>() {
                                    }.getType());
                                    mHandler.sendEmptyMessage(Update);
                                } else {
                                    Utils.showToast(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_modifyPwd:
                ActivityUtil.goActivity(getActivity(), ModifyPwdActivity.class);
                break;
            case R.id.relative_directions:

                break;
            case R.id.relative_feedBack:

                break;
            case R.id.relative_paylsit:
                Bundle bundle = new Bundle();
                bundle.putString("UnPaid", "0");
                ActivityUtil.goActivityWithBundle(getActivity(), UnpaidActivity.class, bundle);
                break;
            case R.id.RL_WalletBalance:
                ActivityUtil.goActivity(getActivity(), WalletBalanceActivity.class);
                break;
            case R.id.linear_quit:
                SharedPreferencesUtils.put("token", "");
                SharedPreferencesUtils.put("CarTypesList", "");
                ActivityUtil.goActivityAndFinish(getActivity(), LoginActivity.class);
                break;
            default:
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refleshOrderCount(FleshOrderCountEvent event) {
        initdate();
    }


}

