package com.tdr.registration.activity.tianjin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.PointAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.RegistrationPointModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 备案登记点列表
 */

public class RegistrationPointActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_point)
    ListView listPoint;

    private Context mContext;
    private ZProgressHUD mProgressHUD;

    private List<RegistrationPointModel> pointModels = new ArrayList<>();
    private Gson mGson;
    private PointAdapter mAdapter;
    private Activity mActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_point);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("备案登记点");
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        listPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(RegistrationPointActivity.this, AppointmentQueryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("registrationPoint", (Serializable) pointModels.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("UnitNo",(String)SharedPreferencesUtils.get("regionNo",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final HashMap<String,String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", obj.toString());
        mProgressHUD.show();
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETREGISTERSITELIST, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            pointModels = mGson.fromJson(data, new TypeToken<List<RegistrationPointModel>>() {
                            }.getType());
                            mAdapter = new PointAdapter(mContext,pointModels);
                            listPoint.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        } else {
                            Utils.myToast(mContext, data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.myToast(mContext, "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(RegistrationPointActivity.this, AppointmentQueryActivity.class);
    }

}
