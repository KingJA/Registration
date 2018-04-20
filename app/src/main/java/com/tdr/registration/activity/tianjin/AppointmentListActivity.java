package com.tdr.registration.activity.tianjin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.AppointmentAdapter;
import com.tdr.registration.adapter.PreAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.RcPreRateModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约列表
 */

public class AppointmentListActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_appointmentList)
    ListView listAppointmentList;

    private Context mContext;

    private List<RcPreRateModel> models;
    private AppointmentAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("预约列表");
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("rcPreRateModels");
        models = (List<RcPreRateModel>) list.get(0);
        mAdapter = new AppointmentAdapter(mContext, models);
        listAppointmentList.setAdapter(mAdapter);
    }
}
