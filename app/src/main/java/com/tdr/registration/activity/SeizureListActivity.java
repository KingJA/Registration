package com.tdr.registration.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.SeizeAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.DistrainModel;
import com.tdr.registration.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 扣押列表
 */
public class SeizureListActivity extends BaseActivity  {
    private static final String TAG = "SeizureListActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_seize)
    ListView listSeize;

    private Context mContext;

    private List<DistrainModel> models = new ArrayList<>();
    private SeizeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seizure_list);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("扣押列表");

        listSeize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("seizeMsg", models.get(position));
                ActivityUtil.goActivityWithBundle(SeizureListActivity.this, SeizureShowActivity.class, bundle);
            }
        });
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("distrain");
        models = (List<DistrainModel>) list.get(0);
        mAdapter = new SeizeAdapter(mContext, models);
        listSeize.setAdapter(mAdapter);
    }


    @OnClick(R.id.image_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
