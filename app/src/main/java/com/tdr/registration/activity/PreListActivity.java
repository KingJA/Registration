package com.tdr.registration.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.PreAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预登记列表
 */

public class PreListActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_pre)
    ListView listPre;

    private List<PreRegistrationModel> models = new ArrayList<>();
    private PreAdapter mAdapter;


    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_list);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("预登记列表");

        listPre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(PreListActivity.this, RegisterPersonalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("preModels", models.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("electricCarModelList");
        models = (List<PreRegistrationModel>) list.get(0);
        mAdapter = new PreAdapter(mContext, models);
        listPre.setAdapter(mAdapter);
    }

    @OnClick(R.id.image_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(PreListActivity.this, RegisterPersonalActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
