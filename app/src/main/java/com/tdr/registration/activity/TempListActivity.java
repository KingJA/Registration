package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.BlackCarAdapter;
import com.tdr.registration.adapter.SeizeAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.DistrainModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;



import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 临时布控
 */

public class TempListActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.linear_addTemp)
    LinearLayout linearAddTemp;
    @BindView(R.id.list_tempCar)
    ListView listTempCar;

    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private ElectricCarModel model;

    private BlackCarAdapter mAdapter;
    private List<ElectricCarModel> models = new ArrayList<>();
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_list);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson = new Gson();
        initView();
        mAdapter = new BlackCarAdapter(mContext, models);
        /*models = db.findAll(ElectricCarModel.class);
        if (models.size() > 0) {
            listTempCar.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }*/
    }

    private void initView() {
        textTitle.setText("临时布控");
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
    }

    @OnClick({R.id.image_back, R.id.linear_addTemp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.linear_addTemp:
                //if (models.size() > 10) {
                //    Utils.myToast(mContext, "最多添加10条临时布控车辆，将替换最旧的一条临时布控");
                //} else {
                dialogShow(1, "临时布控");
                //}

                break;
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View identityView = mInflater.inflate(R.layout.layout_query_identity, null);
            TextView textName = (TextView) identityView.findViewById(R.id.text_name);
            textName.setText("车牌号：");
            final EditText editQueryIdentity = (EditText) identityView
                    .findViewById(R.id.edit_queryIdentity);
            editQueryIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(identityView, mContext);
            dialogBuilder.withTitle(message).withTitleColor("#333333")
                    .withButton1Text("取消").withButton2Text("选择")
                    .withMessage(null).withEffect(effectstype)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();

                        }
                    }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    String queryIdentity = editQueryIdentity.getText().toString().toUpperCase();
                    queryPlateNumber(queryIdentity);

                }
            }).show();
        }

    }

    private void queryPlateNumber(String queryIdentity) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", queryIdentity);
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            String ElectricCar = jsonObject.getString("ElectricCar");
                            model = mGson.fromJson(ElectricCar, new TypeToken<ElectricCarModel>() {
                            }.getType());
                            if (models.size() == 0) {
                                models.add(model);
                                listTempCar.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                for (ElectricCarModel electricCarModel : models) {
                                    if (!electricCarModel.getPlateNumber().equals(model.getPlateNumber())) {
                                        models.add(model);
                                        listTempCar.setAdapter(mAdapter);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(TempListActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
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
        Intent intent = new Intent();
        intent.setClass(TempListActivity.this, StatisticActivity.class);
        Bundle bundle = new Bundle();
        ArrayList list = new ArrayList();
        list.add(models);
        bundle.putParcelableArrayList("blackCars", list);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
