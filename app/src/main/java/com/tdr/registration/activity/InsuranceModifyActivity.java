package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tdr.registration.R;
import com.tdr.registration.adapter.InsuranceAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppManager;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.LinearLayoutForListView;
import com.tdr.registration.view.RadioGroupEx;
import com.tdr.registration.view.ZProgressHUD;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 保险查询--修改
 */
public class InsuranceModifyActivity extends BaseActivity {
    private static final String TAG = "InsuranceModifyActivity";

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_insurance)
    LinearLayoutForListView listInsurance;
    @BindView(R.id.TV_Attention)
    TextView TV_Attention;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private List<ElectricCarModel.PolicysBean> policysBeanList = new ArrayList<>();
    private List<PhotoModel> photoModelList = new ArrayList<>();
    private DbManager db;

    private List<InsuranceModel> insuranceModelsList = new ArrayList<>();//保险集合

    private InsuranceAdapter mAdapter;
    private Gson mGson;
    private Activity mActivity;
    private boolean canPolicyEdit = false;
    private ElectricCarModel model;
    private String ecId = "";
    private String insurerType = "";
    private String Version = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_modify);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
        mGson = new Gson();
        Version = (String) SharedPreferencesUtils.get("Version", "");
        db = x.getDb(DBUtils.getDb());
        try {
            initView();
        } catch (DbException e) {
            e.printStackTrace();
        }
        initData();
    }

    private void initView() throws DbException {
        textTitle.setText("套餐变更");
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");

        Bundle bundle = this.getIntent().getExtras();
        canPolicyEdit = bundle.getBoolean("canPolicyEdit");
        model = (ElectricCarModel) bundle.getSerializable("model");
        mLog.e("model=" + model.toString());
        ecId = model.getEcId();
        insurerType = model.getINSURERTYPE();
        policysBeanList = model.getPOLICYS();
        mLog.e("policysBeanList" + policysBeanList.size());

        List<InsuranceModel> InsuranceModels = null;
        InsuranceModels = db.findAll(InsuranceModel.class);
        if (InsuranceModels == null) {
            InsuranceModels = new ArrayList<InsuranceModel>();
        }

        for (int i = 0; i < InsuranceModels.size(); i++) {
            if (InsuranceModels.get(i).getVehicleType() == null || InsuranceModels.get(i).getVehicleType().equals("") || InsuranceModels.get(i).getVehicleType().equals("0")) {
                insuranceModelsList.add(InsuranceModels.get(i));
            } else {
                if (InsuranceModels.get(i).getVehicleType() != null && !InsuranceModels.get(i).getVehicleType().equals("")) {
                    if (InsuranceModels.get(i).getVehicleType().equals(model.getVehicleType())) {
                        insuranceModelsList.add(InsuranceModels.get(i));
                    }
                } else {
                    insuranceModelsList.add(InsuranceModels.get(i));
                }
            }
        }

        List<DetailBean> detailBeanList;
        for (InsuranceModel insuranceModel : insuranceModelsList) {
            String ListID = "";
            if (Version.equals("1")) {
                ListID = insuranceModel.getListID();
                detailBeanList = db.selector(DetailBean.class).where("CID", "=", ListID).findAll();
            } else {
                ListID = insuranceModel.getRemarkID();
                detailBeanList = db.selector(DetailBean.class).where("RemarkID", "=", ListID).findAll();
            }
            List<DetailBean> DBList=new ArrayList<DetailBean>();
            for (DetailBean detailBean : detailBeanList) {
                mLog.e("IsValid=" + detailBean.getIsValid());
                if(!"0".equals(detailBean.getIsValid())){
                    DBList.add(detailBean);
                }
            }
            insuranceModel.setDetail(DBList);
        }

        mLog.e("insuranceModelsList=" + insuranceModelsList.size());
        mAdapter = new InsuranceAdapter(mContext, insuranceModelsList);
        listInsurance.setAdapter(mAdapter);
        mLog.e("listInsurance=" + listInsurance.getChildCount());
    }

    private void initData() {
        for (int i = 0; i < policysBeanList.size(); i++) {
            String typePolicy = policysBeanList.get(i).getTYPE();
            for (int j = 0; j < insuranceModelsList.size(); j++) {
                String typeInsurance = insuranceModelsList.get(j).getTypeId();
                //判断保险类型是否一致
                if (typePolicy.equals(typeInsurance)) {
                    View v = listInsurance.getChildAt(j);
                    RelativeLayout relativeInsurance = (RelativeLayout) v.findViewById(R.id.relative_insurance);
                    CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
                    RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);


                    String REMARKID = "";//保险ID
                    if (Version.equals("1")) {
                        REMARKID = policysBeanList.get(i).getREMARKID();
                    } else {
                        REMARKID = policysBeanList.get(i).getDEADLINE();
                    }
                    mLog.e("REMARKID=" + REMARKID);
                    for (int i1 = 0; i1 < insuranceModelsList.get(j).getDetail().size(); i1++) {
                        RadioButton RB = (RadioButton) group.getChildAt(i1);
                        String id = "";
                        if (Version.equals("1")) {
                            id = insuranceModelsList.get(j).getDetail().get(i1).getRemarkID();
                        } else {
                            id = insuranceModelsList.get(j).getDetail().get(i1).getDeadLine();
                        }
                        if (id.equals(REMARKID)) {
                            RB.setChecked(true);
                            checkInsurance.setChecked(true);
                        }
                    }
                    if (!canPolicyEdit) {
                        relativeInsurance.setFocusable(false);
                        checkInsurance.setFocusable(false);
                        for (int i1 = 0; i1 < insuranceModelsList.get(j).getDetail().size(); i1++) {
                            ((RadioButton) group.getChildAt(i1)).setFocusable(false);
                        }
                        btnSubmit.setText("返回");
                    }
                }
            }
        }
        if (insuranceModelsList.size() > 0) {
            String s= (String)SharedPreferencesUtils.get("INSURANCEREMARK","");
            SpannableString spannableString = new SpannableString("备注："+s);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF2D4B"));
            spannableString.setSpan(colorSpan, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            TV_Attention.setText(spannableString);
            TV_Attention.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit(){
        if (canPolicyEdit) {
            //编辑信息

            JSONObject object = new JSONObject();

            try {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = null;

                for (int i = 0; i < listInsurance.getChildCount(); i++) {
                    String remarkid = "";
                    View v = listInsurance.getChildAt(i);
                    InsuranceModel IM =insuranceModelsList.get(i);
                    CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
                    RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);


                    for (int i1 = 0; i1 < IM.getDetail().size(); i1++) {
                        RadioButton RB=(RadioButton)group.getChildAt(i1);
                        mLog.e(i1+"RadioButton=" + RB.isChecked());
                        if(RB.isChecked()){
                            if(Version.equals("1")){
                                remarkid= IM.getDetail().get(i1).getRemarkID();
                            }else{
                                remarkid= IM.getDetail().get(i1).getDeadLine();
                            }

                        }
                    }
                    mLog.e(i+"checkInsurance=" + checkInsurance.isChecked());
                    mLog.e(i+"remarkid=" + remarkid);
                    if (checkInsurance.isChecked()) {
                        if (remarkid.equals("")) {
                            Utils.myToast(mContext, "请选择年限");
                            return;
                        } else {
                            jsonObject = new JSONObject();
                            if (Version.equals("1")) {
                                jsonObject.put("CID", insuranceModelsList.get(i).getListID());
                                jsonObject.put("REMARKID", remarkid);
                            } else {
                                jsonObject.put("POLICYID", "");
                                jsonObject.put("Type", insuranceModelsList.get(i).getTypeId());
                                jsonObject.put("REMARKID", insuranceModelsList.get(i).getRemarkID());
                                jsonObject.put("DeadLine", remarkid);
                                jsonObject.put("BUYDATE", Utils.getNowDate());
                            }
                            jsonArray.put(jsonObject);
                        }
                    }
                }
                object.put("ECID", ecId);
                object.put("INSURERTYPE", insurerType);
                object.put("POLICYS", jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgressHUD.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
            map.put("infoJsonStr", object.toString());
            WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_EDITELECTRICCARPOLICY, map, new WebServiceUtils.WebServiceCallBack() {
                @Override
                public void callBack(String result) {
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                mProgressHUD.dismiss();
                                Utils.myToast(mContext, data);
                                ActivityUtil.goActivityAndFinish(InsuranceModifyActivity.this, HomeActivity.class);
                            } else if (errorCode == 1) {
                                mProgressHUD.dismiss();
                                Utils.myToast(mContext, data);
                                //AppManager.getAppManager().finishActivity(HomeActivity.class);
                                SharedPreferencesUtils.put("token", "");
                                ActivityUtil.goActivityAndFinish(InsuranceModifyActivity.this, LoginActivity.class);
                            } else {
                                Utils.myToast(mContext, data);
                                mProgressHUD.dismiss();
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
        } else {
            ActivityUtil.goActivityAndFinish(InsuranceModifyActivity.this, HomeActivity.class);
        }
    }
    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(InsuranceModifyActivity.this, HomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
