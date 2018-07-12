package com.tdr.registration.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tdr.kingja.entity.GetSettingInfo;
import com.tdr.registration.R;
import com.tdr.registration.adapter.HomePagerAdapter;
import com.tdr.registration.fragment.BusinessFragment;
import com.tdr.registration.fragment.InspectFragment;
import com.tdr.registration.fragment.SettingFragment;
import com.tdr.registration.model.SignTypeInfo;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.InterfaceChecker;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.SpSir;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.NoScrollViewPager;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    /**
     * ViewPager主页滑动控件
     */
    @ViewInject(R.id.VP_Home)
    private NoScrollViewPager VP_Home;
    /**
     * 业务界面
     */
    @ViewInject(R.id.LL_business)
    private LinearLayout LL_business;
    @ViewInject(R.id.IV_business)
    private ImageView IV_business;

    /**
     * 查缉界面
     */
    @ViewInject(R.id.LL_inspect)
    private LinearLayout LL_inspect;
    @ViewInject(R.id.IV_inspect)
    private ImageView IV_inspect;

    /**
     * 设置界面
     */
    @ViewInject(R.id.LL_setting)
    private LinearLayout LL_setting;
    @ViewInject(R.id.IV_setting)
    private ImageView IV_setting;


    private List<Fragment> fragments;
    private BusinessFragment BF;
    private InspectFragment IF;
    private SettingFragment SF;

    private long firstTime;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mGson = new Gson();
        initview();
        Utils.CompleteConfig();//补齐缺失字段
        if (InterfaceChecker.isNewInterface()) {
            GetSetting();
        }
    }

    /**
     * 加载视图
     */
    private void initview() {
        LL_business.setOnClickListener(this);
        LL_inspect.setOnClickListener(this);
        LL_setting.setOnClickListener(this);
        String UserType = ((String) SharedPreferencesUtils.get("UserType", ""));
        if (!UserType.equals("1")) {
            HideFragment(1);
        } else {
            HideFragment(-1);
        }
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);
        VP_Home.setAdapter(adapter);
    }

    /**
     * 选择Fragment隐藏，-1为不隐藏，0、1、2 对应 业务、查缉、设置
     *
     * @param index
     */
    private void HideFragment(int index) {
        List<LinearLayout> LLlist = new ArrayList<LinearLayout>();
        LLlist.add(LL_business);
        LLlist.add(LL_inspect);
        LLlist.add(LL_setting);

        List<Fragment> list = new ArrayList<Fragment>();
        BF = new BusinessFragment();
        IF = new InspectFragment();
        SF = new SettingFragment();
        list.add(BF);
        list.add(IF);
        list.add(SF);

        fragments = new ArrayList<Fragment>();
        for (int i = 0; i < list.size(); i++) {
            if (i != index) {
                fragments.add(list.get(i));
                LLlist.get(i).setVisibility(View.VISIBLE);
            } else {
                LLlist.get(i).setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置底部按钮颜色变换
     *
     * @param position
     */
    private void setTabStatus(int position) {
        IV_business.setBackgroundResource(position == fragments.indexOf(BF) ? R.mipmap.business_on : R.mipmap
                .business_off);
        IV_inspect.setBackgroundResource(position == fragments.indexOf(IF) ? R.mipmap.inspect_on : R.mipmap
                .inspect_off);
        IV_setting.setBackgroundResource(position == fragments.indexOf(SF) ? R.mipmap.setting_on : R.mipmap
                .setting_off);
    }

    /**
     * 底部按钮点击事件
     *
     * @param index
     */
    private void selectFragment(int index) {
        VP_Home.setCurrentItem(index);
        setTabStatus(index);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.LL_business:
                selectFragment(fragments.indexOf(BF));
                break;
            case R.id.LL_inspect:
                selectFragment(fragments.indexOf(IF));
                break;
            case R.id.LL_setting:
                selectFragment(fragments.indexOf(SF));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.getServerTime();

    }

    private void GetSetting() {
        mLog.e("获取配置接口");
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetSetting);
        HttpUtils.get(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject resultObject = new JSONObject(result);
                    int errorCode = resultObject.getInt("ErrorCode");
                    String data = resultObject.getString("Data");
                    if (errorCode == 0) {
//                        JSONObject dataObject = new JSONObject(resultObject.getString("Data"));
//                        String engineNoRegular = dataObject.getString("EngineNoRegular");
//                        String shelvesNoRegular = dataObject.getString("ShelvesNoRegular");
//                        boolean blackCheck = dataObject.getBoolean("BlackCheck");
//                        SpSir.getDefault().setBlackCheck(blackCheck);
//                        SpSir.getDefault().setEngineNoRegular(engineNoRegular);
//                        SpSir.getDefault().setShelvesNoRegular(shelvesNoRegular);
//                        String signTypes = dataObject.getString("SignTypeInfo");
//                        String BatteryTHEFTNO = dataObject.getString("BatteryTHEFTNO");
//                        SpSir.getDefault().setBatteryTHEFTNO(BatteryTHEFTNO);
//                        List<SignTypeInfo> signTypeInfoList = mGson.fromJson(signTypes, new
// TypeToken<List<SignTypeInfo>>() {
//                        }.getType());
//                        InterfaceChecker.setElectroCar(signTypeInfoList);
//                        mLog.e("获取配置完成：");
//

                        GetSettingInfo settingInfo = new Gson().fromJson(resultObject.getString("Data"), GetSettingInfo
                                .class);
                        String engineNoRegular = settingInfo.getEngineNoRegular();
                        String shelvesNoRegular = settingInfo.getShelvesNoRegular();
                        GetSettingInfo.BatteryTHEFTNOBean batteryTHEFTNO = settingInfo.getBatteryTHEFTNO();
                        if (batteryTHEFTNO != null) {
                            String batteryRegular = batteryTHEFTNO.getRegular();
                            SpSir.getDefault().setBatteryTHEFTNO(batteryRegular);
                        }
                        boolean blackCheck = settingInfo.isBlackCheck();
                        SpSir.getDefault().setBlackCheck(blackCheck);
                        SpSir.getDefault().setEngineNoRegular(engineNoRegular);
                        SpSir.getDefault().setShelvesNoRegular(shelvesNoRegular);

                        List<SignTypeInfo> signTypeInfoList = settingInfo.getSignType();
                        InterfaceChecker.setElectroCar(signTypeInfoList);
                        mLog.e("获取配置完成：");
                    } else {
                        ToastUtil.showToast(data);
                    }
                } catch (JSONException e) {
                    mLog.e("获取配置接口错误：" + e.toString());
//                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                Logger.d("ex:" + ex.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if ((secondTime - firstTime) > 2000) {
            Utils.myToast(this, getResources().getString(R.string.click_again_quit));
            firstTime = secondTime;
        } else {
            finish();
        }
    }
}
