package com.tdr.registration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tdr.registration.R;
import com.tdr.registration.adapter.HomePagerAdapter;
import com.tdr.registration.fragment.BusinessFragment;
import com.tdr.registration.fragment.InspectFragment;
import com.tdr.registration.fragment.SettingFragment;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.NoScrollViewPager;

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


    private Activity mActivity;
    private List<Fragment> fragments;
    private BusinessFragment BF;
    private InspectFragment IF;
    private SettingFragment SF;

    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity = this;
        initview();
        Utils.CompleteConfig();//补齐缺失字段

    }

    /**
     * 加载视图
     */
    private void initview() {
        LL_business.setOnClickListener(this);
        LL_inspect.setOnClickListener(this);
        LL_setting.setOnClickListener(this);
        String city = ((String) SharedPreferencesUtils.get("locCityName", ""));
        String cityCode = ((String) SharedPreferencesUtils.get("cityCode", ""));
        String UserType = ((String) SharedPreferencesUtils.get("UserType", ""));

        mLog.e("city=" + city);
        mLog.e("cityCode=" + cityCode);
//        if (city.contains("经销商") || cityCode.equals("5301")) {
        if (!UserType.equals("1")) {
            HideFragment(1);
        } else {
            HideFragment(-1);
        }


        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);
        VP_Home.setAdapter(adapter);
//        VP_Home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//            @Override
//            public void onPageSelected(int position) {
//                setTabStatus(position);
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {}
//        });
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
        GetSetting();
    }

    private void GetSetting() {
        mLog.e("GetSetting");
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_GetSetting);
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            @Override
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        Utils.LOGE("Pan", "result:" + result);
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            int errorCode = jsonObject.getInt("ErrorCode");
//                            String data = jsonObject.getString("Data");
//                            if (errorCode == 0) {
////                                CarLabel CL = mGson.fromJson(data, new TypeToken<CarLabel>() {
////                                }.getType());
//
//                            } else if (errorCode==1){
//                                Utils.showToast( data);
//                                SharedPreferencesUtils.put("token","");
//                                ActivityUtil.goActivityAndFinish(HomeActivity.this, LoginActivity.class);
//                            }else{
//                                Utils.showToast(data);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Utils.showToast("JSON解析出错");
//                        }
                    } else {
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mLog.e("Http访问结果：" + Finish);
                }
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
