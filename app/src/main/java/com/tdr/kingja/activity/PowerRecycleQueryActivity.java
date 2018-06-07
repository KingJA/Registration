package com.tdr.kingja.activity;

import android.view.View;
import android.widget.EditText;

import com.tdr.kingja.base.BaseTitleActivity;
import com.tdr.kingja.utils.GoUtil;
import com.tdr.registration.R;
import com.tdr.registration.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:电瓶回收-查询
 * Create Time:2018/13/32 66:66
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class PowerRecycleQueryActivity extends BaseTitleActivity {
    @BindView(R.id.et_query_recordId)
    EditText etQueryRecordId;
    @BindView(R.id.et_query_phone)
    EditText etQueryPhone;

    @OnClick({R.id.stv_recycle_query, R.id.tv_recycle_unrecord_query})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.stv_recycle_query:
                GoUtil.goActivity(this, PowerRecycleActivity.class);
                break;
            case R.id.tv_recycle_unrecord_query:
                ToastUtil.showToast("未备案登记");
                break;
            default:
                break;
        }
    }

    @Override
    public void initVariable() {

    }

    @Override
    protected String getContentTitle() {
        return "电瓶回收";
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_power_recycle_query;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initNet() {

    }
}
