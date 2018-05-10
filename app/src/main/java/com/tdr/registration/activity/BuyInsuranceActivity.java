package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.InsuranceRenewAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.LinearLayoutForListView;
import com.tdr.registration.view.RadioGroupEx;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 续保购买界面
 */
public class BuyInsuranceActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.list_insurance)
    LinearLayoutForListView listInsurance;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @BindView(R.id.text_ownerName)
    TextView textOwnerName;
    @BindView(R.id.text_phone1)
    TextView textPhone1;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_vehicleFrame)
    TextView textVehicleFrame;
    @BindView(R.id.text_vehicleMotor)
    TextView textVehicleMotor;
    @BindView(R.id.layout_printContent)
    LinearLayout layoutPrintContent;
    @BindView(R.id.ll_Attention)
    LinearLayout ll_Attention;

    @BindView(R.id.LL_Invoice)
    LinearLayout LL_Invoice;
    @BindView(R.id.RG_Invoice)
    RadioGroup RG_Invoice;
    @BindView(R.id.RB_Invoice_No)
    RadioButton RB_Invoice_No;
    @BindView(R.id.RB_Invoice_Personal)
    RadioButton RB_Invoice_Personal;
    @BindView(R.id.RB_Invoice_Enterprise)
    RadioButton RB_Invoice_Enterprise;

    private Activity mActivity;
    private List<InsuranceModel> insuranceModelsList;//保险集合
    private InsuranceRenewAdapter mAdapter;

    private String EcId = "";
    private String InvoiceType = "";
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private String VehicleType = "";
    private String IsConfirm = "";
    private ConfirmInsuranceModel CIM;
    private final static int CONFIRMATION_INSURANCE = 1212;//确认保险

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_renew_buy);
        ButterKnife.bind(this);
        mActivity = this;
        mGson = new Gson();
        Bundle bundle = (Bundle) getIntent().getExtras();
        EcId = bundle.getString("EcId");
        VehicleType = bundle.getString("VehicleType");
        ArrayList list = bundle.getParcelableArrayList("insurance");
        CIM = (ConfirmInsuranceModel) list.get(0);
        IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
        initView();
        initdate();
    }


    private void initView() {
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        textTitle.setText("服务延期");
        insuranceModelsList = new ArrayList<>();
        mLog.e("1insuranceModelsList=" + insuranceModelsList.size());

        RG_Invoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == RB_Invoice_No.getId()) {
                    InvoiceType = "0";
                } else if (checkedId == RB_Invoice_Personal.getId()) {
                    InvoiceType = "1";
                }
                if (checkedId == RB_Invoice_Enterprise.getId()) {
                    InvoiceType = "2";
                }
            }
        });

    }

    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_submit:
                SendMSG();
//                submit();
                break;
        }
    }

    private void SendMSG() {
        if (IsConfirm.equals("1")) {
            if (getdata()) {
                mLog.e("PlateNumber=" + CIM.getPlateNumber());
                mLog.e("Name=" + CIM.getName());
                mLog.e("CardType=" + CIM.getCardType());
                mLog.e("CardID=" + CIM.getCardID());
                mLog.e("Phone=" + CIM.getPhone());
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList();
                list.add(CIM);
                bundle.putParcelableArrayList("ConfirmInsurance", list);
                ActivityUtil.goActivityForResultWithBundle(BuyInsuranceActivity.this, ConfirmationInsuranceActivity.class, bundle, CONFIRMATION_INSURANCE);
            }
        } else {
            submit();
        }
    }

    private boolean getdata() {

        List<ConfirmInsuranceModel.Insurance> CIMIL = new ArrayList<>();
        ConfirmInsuranceModel.Insurance CIMI = null;
        for (int i = 0; i < listInsurance.getChildCount(); i++) {
            String RemarkID = "";
            View v = listInsurance.getChildAt(i);
            InsuranceModel IM = insuranceModelsList.get(i);
            CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
            RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);
            mLog.e("IM="+mGson.toJson(IM).toString());
            CIMI = new ConfirmInsuranceModel.Insurance();
            CIMI.setHyperlink(IM.getContract());
            CIMI.setTitle(IM.getName());
            CIMI.setSubTitle(IM.getSubTitle());

            for (int i1 = 0; i1 < IM.getDetail().size(); i1++) {
                RadioButton RB = (RadioButton) group.getChildAt(i1);
                if (RB.isChecked()) {
                    RemarkID = IM.getDetail().get(i1).getRemarkID();
                    CIMI.setMoney(IM.getDetail().get(i1).getPrice());
                    CIMI.setDeadLine(IM.getDetail().get(i1).getDeadLine());
                }
            }
            if (checkInsurance.isChecked()) {
                if (RemarkID.equals("")) {
                    Utils.showToast("请选择年限");
                    return false;
                } else {
                    CIMIL.add(CIMI);
                }
            }
        }
        mLog.e("CIMIL=" + CIMIL.size());
        CIM.setInsurance(CIMIL);
        return true;
    }

    private void submit() {

        List<UploadInsuranceModel> uploadInsuranceModels = new ArrayList<>();
        for (int i = 0; i < listInsurance.getChildCount(); i++) {
            String RemarkID = "";
            UploadInsuranceModel uploadInsuranceModel = new UploadInsuranceModel();
            View v = listInsurance.getChildAt(i);
            InsuranceModel IM = insuranceModelsList.get(i);
            CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
            RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);
            for (DetailBean detailBean : IM.getDetail()) {
                mLog.e("RemarkID=" + detailBean.getRemarkID());
                mLog.e("DeadLine=" + detailBean.getDeadLine());
            }
            for (int i1 = 0; i1 < IM.getDetail().size(); i1++) {
                RadioButton RB = (RadioButton) group.getChildAt(i1);
                mLog.e("isChecked=" + RB.isChecked());
                if (RB.isChecked()) {
                    RemarkID = IM.getDetail().get(i1).getRemarkID();
                }
            }
            if (checkInsurance.isChecked()) {
                if (RemarkID.equals("")) {
                    Utils.showToast("请选择年限");
                    return;
                } else {
                    uploadInsuranceModel.setPOLICYID(insuranceModelsList.get(i).getListID());
                    uploadInsuranceModel.setREMARKID(RemarkID);
                    uploadInsuranceModels.add(uploadInsuranceModel);
                }
            }
        }
        for (int i = 0; i < uploadInsuranceModels.size(); i++) {
            mLog.e("CID=" + uploadInsuranceModels.get(i).getPOLICYID());
            mLog.e("REMARKID=" + uploadInsuranceModels.get(i).getREMARKID());
        }
        if (uploadInsuranceModels.size() == 0) {
            Utils.showToast("请选择需要续费的保险");
            return;
        }
        Buy(uploadInsuranceModels);

    }

    private void initdate() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETRENEWALINSURANCECONFIGURE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    mLog.e("result=" + result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            List<InsuranceModel> IM = mGson.fromJson(data, new TypeToken<List<InsuranceModel>>() {
                            }.getType());
                            if (IM != null && IM.size() > 0) {
                                for (InsuranceModel insuranceModel : IM) {
                                    if (VehicleType.equals(insuranceModel.getVehicleType())) {
                                        insuranceModelsList.add(insuranceModel);
                                    }
                                }
                            }
                            if (mAdapter == null) {
                                mAdapter = new InsuranceRenewAdapter(mActivity, insuranceModelsList);
                                listInsurance.setAdapter(mAdapter);
                            } else {
                                mAdapter.UpDateList(insuranceModelsList);
                            }
                            if (insuranceModelsList.size() > 0) {
                                ll_Attention.setVisibility(View.VISIBLE);
                            }
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }


    private void Buy(List<UploadInsuranceModel> UpLoadIM) {
//        EcId 	是 	string 	电动车主键
//        INSURERTYPE 	是 	string 	保险公司（1大地，2人保）
//        POLICYS 	是 	object 	保险列表
//        CID 	是 	string 	保险险种主键
//        REMARKID 	是 	string 	保险种类主键
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();

        JSONArray JA = new JSONArray();
        JSONObject JB1;
        JSONObject JB2 = new JSONObject();
        try {
            for (UploadInsuranceModel uploadInsuranceModel : UpLoadIM) {
                JB1 = new JSONObject();
                JB1.put("CID", uploadInsuranceModel.getPOLICYID());
                JB1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                JA.put(JB1);
            }
            JB2.put("EcId", EcId);
            JB2.put("INSURERTYPE", "2");
            JB2.put("POLICYS", JA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", JB2.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_INSUREDRENEWAL, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    Utils.LOGE("Pan", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            if (checkJson(data) == 0) {
                                Bundle bundle = new Bundle();
                                bundle.putString("UnPaid", "0");
                                bundle.putString("PayDate", data);
                                ActivityUtil.goActivityWithBundle(BuyInsuranceActivity.this, PayActivity.class, bundle);
                                finish();
                            } else if (checkJson(data) == 1) {
                                SharedPreferencesUtils.put("preregisters", "");
                                SharedPreferencesUtils.put("preregistration", "");
                                SharedPreferencesUtils.put("PhotoListFile", "");
                                Bundle bundle = new Bundle();
                                bundle.putString("UnPaid", "2");
                                bundle.putString("PayDate", data);
                                ActivityUtil.goActivityWithBundle(BuyInsuranceActivity.this, UnpaidActivity.class, bundle);
                                finish();
                            } else {
                                dialogShow(data);
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private void dialogShow(final String msg) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        final NiftyDialogBuilder.Effectstype effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        dialogBuilder.withTitle("提示")
                .withTitleColor("#333333")
                .withMessage(msg)
                .isCancelableOnTouchOutside(false)
                .withEffect(effectstype)
                .withButton1Text("确认")
                .setCustomView(R.layout.custom_view, mActivity)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        InsuranceRenew.mInsuranceRenew.finish();
                        finish();
                    }
                }).show();
    }

    private int checkJson(String json) {
        try {
            List<PayInsurance> list = mGson.fromJson(json, new TypeToken<List<PayInsurance>>() {
            }.getType());
            if (list.size() > 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONFIRMATION_INSURANCE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String isChecked = bundle.getString("isChecked");
                if (isChecked.equals("1")) {
                    submit();
                }
            }
        }
    }
}

