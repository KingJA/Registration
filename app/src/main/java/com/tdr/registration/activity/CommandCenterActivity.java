package com.tdr.registration.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.fragment.CommandFragment;
import com.tdr.registration.fragment.NoticeFragment;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 指令中心
 */

public class CommandCenterActivity extends BaseActivity {
    private static final String TAG = "CommandCenterActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.linear_command)
    LinearLayout linearCommand;
    @BindView(R.id.image_scan)
    ImageView imageScan;

    private PagerAdapter pagerAdapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        ButterKnife.bind(this);
        mContext = this;
        initView();

    }

    private void initView() {
        textTitle.setText("指令中心");
        //imageScan.setVisibility(View.VISIBLE);
        //imageScan.setBackgroundResource(R.drawable.seach_change);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), CommandCenterActivity.this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;

            //case R.id.image_scan:
            //    dialogShow("指令搜索");
            //    break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(CommandCenterActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    class PagerAdapter extends FragmentPagerAdapter {
        String tabTitles[] = new String[]{"指令", "通知"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CommandFragment();

                case 1:
                    return new NoticeFragment();
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
            View tab = LayoutInflater.from(CommandCenterActivity.this).inflate(R.layout.layout_tablayout_title, null);
            TextView textTabTitle = (TextView) tab.findViewById(R.id.text_tabTitle);
            textTabTitle.setText(tabTitles[position]);
            return tab;
        }
    }
}
