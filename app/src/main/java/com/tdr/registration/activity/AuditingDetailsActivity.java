package com.tdr.registration.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.fragment.CommandFragment;
import com.tdr.registration.fragment.MonitorInfoFragment;
import com.tdr.registration.fragment.NoticeFragment;
import com.tdr.registration.fragment.RouteFragment;
import com.tdr.registration.fragment.VehicleDetailsFragment;
import com.tdr.registration.model.CommandModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 黑车详情展示
 */

public class AuditingDetailsActivity extends BaseActivity {

    private static final String TAG = "AuditingDetailsActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_deal)
    TextView textDeal;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private PagerAdapter pagerAdapter;
    private Context mContext;
    private String plateNumber = "";
    private ElectricCarModel electricCarModel;
    private CommandModel.CarDeployBean carDeployBean;
    private Gson mGson;

    private ZProgressHUD mProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        ButterKnife.bind(this);
        mContext = this;
        mGson = new Gson();
        Bundle bundle = (Bundle) getIntent().getExtras();
        plateNumber = (String) bundle.getString("plateNumber");
        initView();
    }

    private void initView() {
        textTitle.setText("黑车详情");
        textDeal.setVisibility(View.VISIBLE);
        textDeal.setText("稽查");
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), AuditingDetailsActivity.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    @OnClick({R.id.image_back, R.id.text_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.text_deal:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(AuditingDetailsActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    class PagerAdapter extends FragmentPagerAdapter {
        String tabTitles[] = new String[]{"车辆信息", "布控信息", "轨迹信息"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new VehicleDetailsFragment(plateNumber);

                case 1:
                    return new MonitorInfoFragment(plateNumber);

                case 2:
                    return new RouteFragment(plateNumber);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(AuditingDetailsActivity.this).inflate(R.layout.layout_tablayout_title, null);
            TextView textTabTitle = (TextView) tab.findViewById(R.id.text_tabTitle);
            textTabTitle.setText(tabTitles[position]);
            return tab;
        }
    }
}