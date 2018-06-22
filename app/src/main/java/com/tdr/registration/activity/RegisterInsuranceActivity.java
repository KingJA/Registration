package com.tdr.registration.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.adapter.InsuranceAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.LinearLayoutForListView;
import com.tdr.registration.view.RadioGroupEx;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdr.registration.util.VehiclesStorageUtils.ENGINENO;
import static com.tdr.registration.util.VehiclesStorageUtils.IDENTITY;
import static com.tdr.registration.util.VehiclesStorageUtils.OWNERNAME;
import static com.tdr.registration.util.VehiclesStorageUtils.PHONE1;
import static com.tdr.registration.util.VehiclesStorageUtils.PLATENUMBER;
import static com.tdr.registration.util.VehiclesStorageUtils.SHELVESNO;
import static com.tdr.registration.util.VehiclesStorageUtils.VEHICLEBRANDNAME;

/**
 * 昆明等地区保险
 * 捆绑型
 */
public class RegisterInsuranceActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "Register保险";
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
    @BindView(R.id.TV_Attention)
    TextView TV_Attention;

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

    private Context mContext;
    private DbManager db;
    private List<InsuranceModel> insuranceModelsList = new ArrayList<>();//保险集合
    private InsuranceAdapter mAdapter;

    private String activity = "";
    private String listId = "";
    private String InvoiceType = "";
    private String EnableInvoice = "";
    private String IsPRE = "";
    private String PhotoConfig = "";
    private String Version = "";
    private String REGISTRATION;
    private ZProgressHUD mProgressHUD;
    private List<UploadInsuranceModel> uploadInsuranceModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_third);
        ButterKnife.bind(this);
        mContext = this;
        db = x.getDb(DBUtils.getDb());
        Version = (String) SharedPreferencesUtils.get("Version", "");
        mLog.e("Version=" + Version);
        TransferUtil.remove("InsuranceList");
        Bundle bundle = (Bundle) getIntent().getExtras();
        activity = bundle.getString("activity");
        listId = bundle.getString("distrainCarListID");
        IsPRE = bundle.getString("IsPRE");
        REGISTRATION = (String) SharedPreferencesUtils.get("REGISTRATION", "");
        if (IsPRE.equals("1")) {
            PhotoConfig = bundle.getString("PhotoConfig");
        }
        try {
            initView();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    private void initView() throws DbException {
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
        String appName = (String) SharedPreferencesUtils.get("appName", "");
        String city = (String) SharedPreferencesUtils.get("locCityName", "");
        if (activity.equals("")) {
            if (!REGISTRATION.equals("")) {
                textTitle.setText(REGISTRATION);
            } else {
                if (city.contains("温州")) {
                    textTitle.setText("登记备案");
                } else {
                    if (appName.contains("防盗")) {
                        textTitle.setText("防盗登记");
                    } else {
                        textTitle.setText("备案登记");
                    }
                }
            }
        } else {
            textTitle.setText("扣押转正式");
        }
        //是否需要开票
        EnableInvoice = (String) SharedPreferencesUtils.get("EnableInvoice", "");
        mLog.e("EnableInvoice=" + EnableInvoice);
        if (EnableInvoice.equals("1")) {
            LL_Invoice.setVisibility(View.VISIBLE);
            RB_Invoice_No.setChecked(true);
            InvoiceType = "0";
        } else if (EnableInvoice.equals("0") || EnableInvoice.equals("")) {
            LL_Invoice.setVisibility(View.GONE);
            InvoiceType = "";
        }

        //TODO 从服务器获取保险数据
        String insurancesStr = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.INSURANCES);
        String interfaceVersion = (String) SharedPreferencesUtils.get("InterfaceVersion", "0");
        if ("1".equals(interfaceVersion)) {
            Log.e(TAG, "interfaceVersion:" + interfaceVersion + " 保险从服务器获取: ");
            if (TextUtils.isEmpty(insurancesStr)) {
                getInsuranceData();
            } else {
                List<InsuranceModel> insurances = new Gson().fromJson(insurancesStr, new
                        TypeToken<List<InsuranceModel>>() {
                        }.getType());
                fillInsuranceData(insurances);
            }

        } else {
            Log.e(TAG, "interfaceVersion:" + interfaceVersion + " 保险从本地获取: ");
            List<InsuranceModel> insuranceModels = db.findAll(InsuranceModel.class);
            if (insuranceModels != null) {
                fillInsuranceData(insuranceModels);
            } else {
                ToastUtil.showToast("无保险信息");
            }

        }

    }

    public String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    private void getInsuranceData() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("BuyDate", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE, getCurrentDate()));
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_PolicyConfig);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        Log.e(TAG, "参数: " + JB.toString());
        HttpUtils.postK(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                mProgressHUD.dismiss();
                Log.e("RegisterInsurance", "onSuccess: " + result);
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    if (errorCode == 0) {
                        String data = jsonObject.getString("Data");
                        if (!TextUtils.isEmpty(data)) {
                            List<InsuranceModel> insurances = new Gson().fromJson(data, new
                                    TypeToken<List<InsuranceModel>>() {
                                    }.getType());
                            if (insurances != null && insurances.size() > 0) {
                                try {
                                    fillInsuranceData(insurances);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Utils.showToast("无保险数据");
                            }
                        } else {
                            Utils.showToast("无data数据");
                        }
                    } else if (errorCode == 1) {
                        mProgressHUD.dismiss();
                        String data = jsonObject.getString("Data");
                        Utils.showToast(data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(RegisterInsuranceActivity.this, LoginActivity
                                .class);
                    } else {
                        //错误
                        String errorMsg = jsonObject.getString("Data");
                        Utils.showToast(errorMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {
                mProgressHUD.dismiss();
            }
        });
    }

    private void fillInsuranceData(List<InsuranceModel> insurances) throws DbException {
        btnSubmit.setVisibility(View.VISIBLE);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.INSURANCES, new Gson().toJson(insurances));

        for (int i = 0; i < insurances.size(); i++) {
            mLog.e(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE) + "VehicleType:" +
                    insurances.get(i).getVehicleType());
            if (insurances.get(i).getVehicleType() == null || insurances.get(i).getVehicleType().equals("")
                    || insurances.get(i).getVehicleType().equals("0")) {
                insuranceModelsList.add(insurances.get(i));
            } else {
                if (insurances.get(i).getVehicleType() != null && !insurances.get(i).getVehicleType()
                        .equals("")) {

                    if (insurances.get(i).getVehicleType().equals(VehiclesStorageUtils.getVehiclesAttr
                            (VehiclesStorageUtils.VEHICLETYPE))) {
                        insuranceModelsList.add(insurances.get(i));
                    }
                } else {
                    insuranceModelsList.add(insurances.get(i));
                }
            }
        }
        List<DetailBean> detailBeanList;
        for (InsuranceModel insuranceModel : insuranceModelsList) {
            String ListID = "";
            if (Version.equals("1")) {//新老版本走不同的ID
                ListID = insuranceModel.getListID();
                detailBeanList = db.selector(DetailBean.class).where("CID", "=", ListID).findAll();
            } else {
                ListID = insuranceModel.getRemarkID();
                detailBeanList = db.selector(DetailBean.class).where("RemarkID", "=", ListID).findAll();
            }
            List<DetailBean> DBList = new ArrayList<DetailBean>();
            for (DetailBean detailBean : detailBeanList) {
                mLog.e("IsValid=" + detailBean.getIsValid());
                if (!"0".equals(detailBean.getIsValid())) {
                    DBList.add(detailBean);
                }
            }
            insuranceModel.setDetail(DBList);
        }

        mAdapter = new InsuranceAdapter(mContext, insuranceModelsList);
        mLog.e("insuranceModelsList=" + insuranceModelsList.size());
        listInsurance.setAdapter(mAdapter);
        if (insuranceModelsList.size() == 0) {
            listInsurance.setVisibility(View.GONE);
            layoutPrintContent.setVisibility(View.VISIBLE);
            textOwnerIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(IDENTITY));
            textOwnerName.setText(VehiclesStorageUtils.getVehiclesAttr(OWNERNAME));
            textPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(PHONE1));
            textVehicleBrand.setText(VehiclesStorageUtils.getVehiclesAttr(VEHICLEBRANDNAME));
            textPlateNumber.setText(VehiclesStorageUtils.getVehiclesAttr(PLATENUMBER));
            textVehicleFrame.setText(VehiclesStorageUtils.getVehiclesAttr(SHELVESNO));
            textVehicleMotor.setText(VehiclesStorageUtils.getVehiclesAttr(ENGINENO));
        }
        if (insuranceModelsList.size() > 0) {
            String s = (String) SharedPreferencesUtils.get("INSURANCEREMARK", "");
            SpannableString spannableString = new SpannableString("备注：" + s);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF2D4B"));
            spannableString.setSpan(colorSpan, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            TV_Attention.setText(spannableString);
            TV_Attention.setVisibility(View.VISIBLE);
        }

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


    @Override
    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        uploadInsuranceModels = new ArrayList<>();
        ConfirmInsuranceModel CIM = new ConfirmInsuranceModel();
        List<ConfirmInsuranceModel.Insurance> CIMIL = new ArrayList<>();
        ConfirmInsuranceModel.Insurance CIMI = null;
        for (int i = 0; i < listInsurance.getChildCount(); i++) {
            String RemarkID = "";
            InsuranceModel IM = insuranceModelsList.get(i);
            UploadInsuranceModel uploadInsuranceModel = new UploadInsuranceModel();
            View v = listInsurance.getChildAt(i);
            CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
            RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);

            CIMI = new ConfirmInsuranceModel.Insurance();

            CIMI.setHyperlink(IM.getContract());
            CIMI.setTitle(IM.getName());
            CIMI.setSubTitle(IM.getSubTitle());
            for (int i1 = 0; i1 < IM.getDetail().size(); i1++) {
                RadioButton RB = (RadioButton) group.getChildAt(i1);
                if (RB.isChecked()) {
                    if (Version.equals("1")) {
                        RemarkID = IM.getDetail().get(i1).getRemarkID();
                    } else {
                        RemarkID = IM.getDetail().get(i1).getDeadLine();
                    }
                    CIMI.setMoney(IM.getDetail().get(i1).getPrice());
                    CIMI.setDeadLine(IM.getDetail().get(i1).getDeadLine());
                }
            }


            mLog.e(i + "RemarkID:" + RemarkID);
            if (checkInsurance.isChecked()) {
                if (RemarkID.equals("")) {
                    Utils.myToast(mContext, "请选择年限");
                    return;
                } else {
                    if (Version.equals("1")) {
                        uploadInsuranceModel.setPOLICYID(insuranceModelsList.get(i).getListID());
                        uploadInsuranceModel.setREMARKID(RemarkID);
                    } else {
                        uploadInsuranceModel.setPOLICYID("");
                        uploadInsuranceModel.setType(insuranceModelsList.get(i).getTypeId());
                        uploadInsuranceModel.setREMARKID(insuranceModelsList.get(i).getRemarkID());
                        uploadInsuranceModel.setDeadLine(RemarkID);
                        uploadInsuranceModel.setBUYDATE(Utils.getNowDate());
                    }
                    uploadInsuranceModels.add(uploadInsuranceModel);
                    CIMIL.add(CIMI);
                }
            }
            if (EnableInvoice.equals("1")) {
                LL_Invoice.setVisibility(View.VISIBLE);
            }
        }
        mLog.e("CIMIL=" + CIMIL.size());
        CIM.setInsurance(CIMIL);
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = tm.getDeviceId();

        for (UploadInsuranceModel uploadInsuranceModel : uploadInsuranceModels) {
            mLog.e("POLICYID= " + uploadInsuranceModel.getPOLICYID());
            mLog.e("Type= " + uploadInsuranceModel.getType());
            mLog.e("REMARKID= " + uploadInsuranceModel.getREMARKID());
            mLog.e("DeadLine= " + uploadInsuranceModel.getDeadLine());
            mLog.e("BUYDATE= " + uploadInsuranceModel.getBUYDATE());
            mLog.e("===========================================");
            String msg = "  POLICYID= " + uploadInsuranceModel.getPOLICYID() +
                    "  Type= " + uploadInsuranceModel.getType() +
                    "  REMARKID= " + uploadInsuranceModel.getREMARKID() +
                    "  DeadLine= " + uploadInsuranceModel.getDeadLine() +
                    "  BUYDATE= " + uploadInsuranceModel.getBUYDATE();
            MobclickAgent.reportError(getApplicationContext(), "手机IMEI=" + imei + "   保险数据{" + msg + "}");
        }

        if (uploadInsuranceModels.size() == 0) {
            MobclickAgent.reportError(getApplicationContext(), "保险数据赋值错误：手机IMEI=" + imei + "  登录账号：" + (String)
                    SharedPreferencesUtils.get("UP", "私有空间本地缓存读取失败"));
            try {
                insuranceModelsList.clear();
                initView();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Utils.showToast("检测到保险数据被手机后台清理掉了，请从新选择保险。");
            return;
        }
        SendMSG();
    }

    private void initInsuranceData() {
        uploadInsuranceModels = new ArrayList<>();
        ConfirmInsuranceModel CIM = new ConfirmInsuranceModel();
        List<ConfirmInsuranceModel.Insurance> CIMIL = new ArrayList<>();
        ConfirmInsuranceModel.Insurance CIMI = null;
        for (int i = 0; i < listInsurance.getChildCount(); i++) {
            String RemarkID = "";
            InsuranceModel IM = insuranceModelsList.get(i);
            UploadInsuranceModel uploadInsuranceModel = new UploadInsuranceModel();
            View v = listInsurance.getChildAt(i);
            CheckBox checkInsurance = (CheckBox) v.findViewById(R.id.check_insurance);
            RadioGroupEx group = (RadioGroupEx) v.findViewById(R.id.group_insurance);

            CIMI = new ConfirmInsuranceModel.Insurance();

            CIMI.setHyperlink(IM.getContract());
            CIMI.setTitle(IM.getName());
            CIMI.setSubTitle(IM.getSubTitle());
            for (int i1 = 0; i1 < IM.getDetail().size(); i1++) {
                RadioButton RB = (RadioButton) group.getChildAt(i1);
                if (RB.isChecked()) {
                    if (Version.equals("1")) {
                        RemarkID = IM.getDetail().get(i1).getRemarkID();
                    } else {
                        RemarkID = IM.getDetail().get(i1).getDeadLine();
                    }
                    CIMI.setMoney(IM.getDetail().get(i1).getPrice());
                    CIMI.setDeadLine(IM.getDetail().get(i1).getDeadLine());
                }
            }

            mLog.e(i + "RemarkID:" + RemarkID);
            if (checkInsurance.isChecked()) {
                if (RemarkID.equals("")) {
                    Utils.myToast(mContext, "请选择年限");
                    return;
                } else {
                    if (Version.equals("1")) {
                        uploadInsuranceModel.setPOLICYID(insuranceModelsList.get(i).getListID());
                        uploadInsuranceModel.setREMARKID(RemarkID);
                    } else {
                        uploadInsuranceModel.setPOLICYID("");
                        uploadInsuranceModel.setType(insuranceModelsList.get(i).getTypeId());
                        uploadInsuranceModel.setREMARKID(insuranceModelsList.get(i).getRemarkID());
                        uploadInsuranceModel.setDeadLine(RemarkID);
                        uploadInsuranceModel.setBUYDATE(Utils.getNowDate());
                    }
                    uploadInsuranceModels.add(uploadInsuranceModel);
                    CIMIL.add(CIMI);
                }
            }
            if (EnableInvoice.equals("1")) {
                LL_Invoice.setVisibility(View.VISIBLE);
            }
        }
        mLog.e("CIMIL=" + CIMIL.size());
        CIM.setInsurance(CIMIL);
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = tm.getDeviceId();

        for (UploadInsuranceModel uploadInsuranceModel : uploadInsuranceModels) {
            mLog.e("POLICYID= " + uploadInsuranceModel.getPOLICYID());
            mLog.e("Type= " + uploadInsuranceModel.getType());
            mLog.e("REMARKID= " + uploadInsuranceModel.getREMARKID());
            mLog.e("DeadLine= " + uploadInsuranceModel.getDeadLine());
            mLog.e("BUYDATE= " + uploadInsuranceModel.getBUYDATE());
            mLog.e("===========================================");
            String msg = "  POLICYID= " + uploadInsuranceModel.getPOLICYID() +
                    "  Type= " + uploadInsuranceModel.getType() +
                    "  REMARKID= " + uploadInsuranceModel.getREMARKID() +
                    "  DeadLine= " + uploadInsuranceModel.getDeadLine() +
                    "  BUYDATE= " + uploadInsuranceModel.getBUYDATE();
            MobclickAgent.reportError(getApplicationContext(), "手机IMEI=" + imei + "   保险数据{" + msg + "}");
        }

        if (uploadInsuranceModels.size() == 0) {
            MobclickAgent.reportError(getApplicationContext(), "保险数据赋值错误：手机IMEI=" + imei + "  登录账号：" + (String)
                    SharedPreferencesUtils.get("UP", "私有空间本地缓存读取失败"));
            try {
                insuranceModelsList.clear();
                initView();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Utils.showToast("检测到保险数据被手机后台清理掉了，请从新选择保险。");
            return;
        }
    }

    private ConfirmInsuranceModel ConfirmInsuranceList;
    private final static int CONFIRMATION_INSURANCE = 1212;//确认保险

    private void SendMSG() {
        String IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
//        if (IsConfirm.equals("1")) {
//            ConfirmInsuranceList.setPlateNumber(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
//            ConfirmInsuranceList.setName(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
//            ConfirmInsuranceList.setCardType(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE));
//            ConfirmInsuranceList.setCardID(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
//            ConfirmInsuranceList.setPhone(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
//            mLog.e("PlateNumber=" + ConfirmInsuranceList.getPlateNumber());
//            mLog.e("Name=" + ConfirmInsuranceList.getName());
//            mLog.e("CardType=" + ConfirmInsuranceList.getCardType());
//            mLog.e("CardID=" + ConfirmInsuranceList.getCardID());
//            mLog.e("Phone=" + ConfirmInsuranceList.getPhone());
//
//            Bundle bundle = new Bundle();
//            ArrayList list = new ArrayList();
//            list.add(ConfirmInsuranceList);
//            bundle.putParcelableArrayList("ConfirmInsurance", list);
//            ActivityUtil.goActivityForResultWithBundle(this, ConfirmationInsuranceActivity.class,
//                    bundle, CONFIRMATION_INSURANCE);
//        } else {
//            sendMsg();
//        }
        sendMsg();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONFIRMATION_INSURANCE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String isChecked = bundle.getString("isChecked");
                if (isChecked.equals("1")) {
                    sendMsg();
                }
            }
        }
    }

    private List<PhotoListInfo> PLI;

    private void sendMsg() {

        JSONObject obj = new JSONObject();
        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;

        String vehicletype = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE);
        if (vehicletype == null || vehicletype.equals("")) {
            String userName = (String) SharedPreferencesUtils.get("userName", "");
            String city = (String) SharedPreferencesUtils.get("locCityName", "");
            MobclickAgent.reportError(RegisterInsuranceActivity.this, "车辆类型数据丢失" + userName + "_" + city);
            Utils.showToast("车辆类型数据被回收,请重新登记。");

        }
        try {
            //TODO 照片上传
            List<PhotoListInfo> photoList = null;
            try {
                photoList = getPhotoList();
            } catch (DbException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < photoList.size(); i++) {
                index = photoList.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
//                JB.put("PhotoFile", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
//                Log.e("Pan", i + "  photo:" + index + "=\n" + PhotoFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e("Pan", "JA=" + JA.length());
        String isFreeShangPai = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IS_FREE_SHANGPAI);
        String ecId = "";
        if ("1".equals(isFreeShangPai)) {
            //免费上牌转备案登记
            ecId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ECID);
        } else {
            ecId = UUID.randomUUID().toString().toUpperCase();
        }

        try {
            obj.put("EcId", ecId);
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.ECID, ecId);
            obj.put("HasRFID", "1");// 是否有有源标签,1有，0无
            obj.put("VehicleModels", "");
//            obj.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));

            obj.put("VEHICLETYPE", vehicletype);

            obj.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
            obj.put("THEFTNO2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO2));
            obj.put("Photo1File", "");
            obj.put("Photo2File", "");
            obj.put("Photo3File", "");
            obj.put("Photo4File", "");
            obj.put("REGISTERID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REGISTERID));
            String carType = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE);
            obj.put("CARTYPE", TextUtils.isEmpty(carType) ? "1" : carType);
            String isConfirm = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCONFIRM);
            obj.put("ISCONFIRM", TextUtils.isEmpty(isConfirm) ? "0" : isConfirm);
            obj.put("VehicleBrand", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            obj.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            String platetype = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATETYPE);
            if (platetype == null || platetype.equals("")) {
                platetype = "1";
            }
            obj.put("PlateType", platetype);
            obj.put("ShelvesNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            obj.put("EngineNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            obj.put("ColorId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            obj.put("ColorId2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            obj.put("BuyDate", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
            obj.put("Price", "");
            obj.put("CARDTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE));
            obj.put("OwnerName", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
            obj.put("CardId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
            obj.put("Phone1", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
            obj.put("Phone2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
            obj.put("ResidentAddress", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
            obj.put("Remark", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
            obj.put("INSURERTYPE", "2");// 保险公司

            obj.put("PhotoListFile", JA);
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jsonObject1 = null;

            for (UploadInsuranceModel uploadInsuranceModel : uploadInsuranceModels) {
//                {"CID":"保险配置字段里的CID(意义：险种ID)","REMARKID":"保险配置字段里的REMARKID(意义：险种下面的 各种规格ID)"}
                jsonObject1 = new JSONObject();
                if (Version.equals("1")) {
                    jsonObject1.put("CID", uploadInsuranceModel.getPOLICYID());
                    jsonObject1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                } else {
                    jsonObject1.put("POLICYID", "");
                    jsonObject1.put("Type", uploadInsuranceModel.getType());
                    jsonObject1.put("REMARKID", uploadInsuranceModel.getREMARKID());
                    jsonObject1.put("DeadLine", uploadInsuranceModel.getDeadLine());
                    jsonObject1.put("BUYDATE", uploadInsuranceModel.getBUYDATE());
                }
                jsonArray1.put(jsonObject1);

            }
            obj.put("POLICYS", jsonArray1);
//            mLog.e("POLICYS:"+jsonArray1.toString());
            //代办人信息
            obj.put("InvoiceOp", InvoiceType);
            obj.put("AGENTNAME", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONNAME));
            obj.put("AGENTCARDTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONCARDTYPE));
            obj.put("AGENTCARDID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONIDENTITY));
            obj.put("AGENTPHONE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONPHONE1));
            obj.put("AGENTADDR", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONADDRESS));
            obj.put("AGENTREMARK", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COMMISSIONREMARK));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", obj.toString());
        String functionName = "";
        if (listId.equals("")) {
            functionName = Constants.WEBSERVER_ADDELECTRICCAR;
        } else {
            map.put("DistrainCarListID", listId);
            functionName = Constants.WEBSERVER_ADDELECTRICCARFROMDISTRAIN;

        }
        mLog.e("apiUrl:" + (String) SharedPreferencesUtils.get("apiUrl", ""));

        if ("1".equals(isFreeShangPai)) {
            //免费上牌转备案登记
            Log.e(TAG, "免费上牌转备案登记: ");
            sendByHttp(obj);
        } else {
            Log.e(TAG, "备案登记: ");
            sendByWebService(map, functionName);
        }

    }

    private void sendByHttp(JSONObject obj) {
        mProgressHUD.show();
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_ToElectricCar);
        RP.setAsJsonContent(true);
        RP.setBodyContent(obj.toString());
        HttpUtils.postK(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                mProgressHUD.dismiss();
                Logger.d("进入成功");
                if (result != null) {
                    Logger.json(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            Logger.d("登记成功");
                            if ("登记成功".equals(data)||"转换成功".equals(data)) {
                                showSuccess();
                                return;
                            }
                            List<PayInsurance> payInsurances = new Gson().fromJson(data, new
                                    TypeToken<List<PayInsurance>>
                                            () {
                                    }.getType());

                            if (payInsurances == null) {
                                return;
                            }
                            if (payInsurances.size() == 1) {
                                PayInsurance payInsurance = payInsurances.get(0);
                                if (payInsurance.getPaymentWay() == 2) {
                                    //二维码支付
                                    PayQcodeActivity.goActivity(RegisterInsuranceActivity.this, payInsurance
                                                    .getContent(),
                                            payInsurance.getTotal_Amount(), payInsurance.getPlateNumber(),
                                            payInsurance.getPayNo(), PayQcodeActivity.FORM_REGISTER);
                                } else {
                                    //直接支付
                                    Bundle bundle = new Bundle();
                                    bundle.putString("UnPaid", "0");
                                    bundle.putString("PayDate", data);
                                    ArrayList list = new ArrayList();
                                    list.add(payInsurances);
                                    bundle.putParcelableArrayList("PayDate", list);
                                    ActivityUtil.goActivityWithBundle(RegisterInsuranceActivity.this,
                                            PayActivity.class, bundle);
                                }

                            } else if (payInsurances.size() > 1) {
                                List<PayInsurance> PI = new Gson().fromJson(data, new
                                        TypeToken<List<PayInsurance>>() {
                                        }.getType());
                                SharedPreferencesUtils.put("preregisters", "");
                                SharedPreferencesUtils.put("preregistration", "");
                                SharedPreferencesUtils.put("PhotoListFile", "");
                                Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
                                        (VehiclesStorageUtils
                                                .PLATENUMBER) + "  电动车信息上传成功！");
                                Bundle bundle = new Bundle();
                                bundle.putString("UnPaid", "2");
                                ArrayList list = new ArrayList();
                                list.add(PI);
                                bundle.putParcelableArrayList("PayDate", list);
                                ActivityUtil.goActivityWithBundle(RegisterInsuranceActivity.this,
                                        UnpaidActivity.class, bundle);
                                RegisterInsuranceActivity.this.finish();

                            } else {
//                                        TODO 显示
                                Utils.showToast("电动车信息上传成功！");
                                dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils
                                        .PLATENUMBER) + "  电动车信息上传成功！");
                            }
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(RegisterInsuranceActivity.this, LoginActivity
                                    .class);
                        } else {
                            Utils.showToast(data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
                        Utils.showToast("JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast("获取数据超时，请检查网络连接");
                }
            }

            @Override
            public void onError(Throwable ex) {
                mProgressHUD.dismiss();
            }
        });
    }
    private void sendByWebService(HashMap<String, String> map, String functionName) {
        WebServiceUtils.callWebService(RegisterInsuranceActivity.this, (String) SharedPreferencesUtils.get("apiUrl",
                ""), functionName,
                map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        if (result != null) {
                            Logger.json(result);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.INSURANCES, "");
                                    mProgressHUD.dismiss();
                                    if ("登记成功".equals(data)) {
                                        showSuccess();
                                        return;
                                    }
                                    List<PayInsurance> payInsurances = new Gson().fromJson(data, new
                                            TypeToken<List<PayInsurance>>
                                                    () {
                                            }.getType());

                                    if (payInsurances == null) {
                                        return;
                                    }
                                    if (payInsurances.size() == 1) {
                                        PayInsurance payInsurance = payInsurances.get(0);
                                        if (payInsurance.getPaymentWay() == 2) {
                                            //二维码支付
                                            PayQcodeActivity.goActivity(RegisterInsuranceActivity.this, payInsurance
                                                            .getContent(),
                                                    payInsurance.getTotal_Amount(), payInsurance.getPlateNumber(),
                                                    payInsurance.getPayNo(), PayQcodeActivity.FORM_REGISTER);
                                        } else {
                                            //直接支付
                                            Bundle bundle = new Bundle();
                                            bundle.putString("UnPaid", "0");
                                            bundle.putString("PayDate", data);
                                            ArrayList list = new ArrayList();
                                            list.add(payInsurances);
                                            bundle.putParcelableArrayList("PayDate", list);
                                            ActivityUtil.goActivityWithBundle(RegisterInsuranceActivity.this,
                                                    PayActivity.class, bundle);
                                        }

                                    } else if (payInsurances.size() > 1) {
                                        List<PayInsurance> PI = new Gson().fromJson(data, new
                                                TypeToken<List<PayInsurance>>() {
                                                }.getType());
                                        SharedPreferencesUtils.put("preregisters", "");
                                        SharedPreferencesUtils.put("preregistration", "");
                                        SharedPreferencesUtils.put("PhotoListFile", "");
                                        Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
                                                (VehiclesStorageUtils
                                                        .PLATENUMBER) + "  电动车信息上传成功！");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("UnPaid", "2");
                                        ArrayList list = new ArrayList();
                                        list.add(PI);
                                        bundle.putParcelableArrayList("PayDate", list);
                                        ActivityUtil.goActivityWithBundle(RegisterInsuranceActivity.this,
                                                UnpaidActivity.class, bundle);
                                        RegisterInsuranceActivity.this.finish();

                                    } else {
//                                        TODO 显示
                                        Utils.showToast("电动车信息上传成功！");
                                        dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils
                                                .PLATENUMBER) + "  电动车信息上传成功！");
                                    }
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.showToast(data);
                                    SharedPreferencesUtils.put("token", "");
                                    ActivityUtil.goActivityAndFinish(RegisterInsuranceActivity.this, LoginActivity
                                            .class);
                                } else {
                                    Utils.showToast(data);
                                    mProgressHUD.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mProgressHUD.dismiss();
                                Utils.showToast("JSON解析出错");
                            }
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast("获取数据超时，请检查网络连接");
                        }

                    }
                });
    }

    private void showSuccess() {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        dialogBuilder.withTitle("提示")
                .withTitleColor("#333333")
                .withMessage("登记成功")
                .isCancelableOnTouchOutside(false)
                .withEffect(NiftyDialogBuilder.Effectstype.Fadein)
                .withButton1Text("確定")
                .setCustomView(R.layout.custom_view, this)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        SharedPreferencesUtils.put("preregisters", "");
                        SharedPreferencesUtils.put("preregistration", "");
                        SharedPreferencesUtils.put("PhotoListFile", "");
                        VehiclesStorageUtils.clearData();
                        ActivityUtil.goActivityAndFinish(RegisterInsuranceActivity.this, HomeActivity.class);
                    }
                });
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
    }

    private void dialogShow(int flag, String msg) {
        final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (flag == 0) {
            dialogBuilder.withTitle("提示")
                    .withTitleColor("#333333")
                    .withMessage(msg)
                    .isCancelableOnTouchOutside(false)
                    .withEffect(NiftyDialogBuilder.Effectstype.Fadein)
                    .withButton1Text("確定")
                    .setCustomView(R.layout.custom_view, this)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            SharedPreferencesUtils.put("preregisters", "");
                            SharedPreferencesUtils.put("preregistration", "");
                            SharedPreferencesUtils.put("PhotoListFile", "");
                            VehiclesStorageUtils.clearData();
                            ActivityUtil.goActivityAndFinish(RegisterInsuranceActivity.this, HomeActivity.class);
                        }
                    }).show();
        }
    }

    public List<PhotoListInfo> getPhotoList() throws DbException, JSONException {
        List<PhotoListInfo> PLI = new ArrayList<>();
        List<BaseInfo> ResultList = db.selector(BaseInfo.class).where("cityName", "=", (String)
                SharedPreferencesUtils.get("locCityName", "")).findAll();
        if (ResultList == null) {
            return PLI;
        }
        JSONArray JA = new JSONArray(ResultList.get(0).getPhotoConfig());
        JSONObject JB;
        PhotoListInfo pli;
        for (int i = 0; i < JA.length(); i++) {
            JB = new JSONObject(JA.get(i).toString());
            pli = new PhotoListInfo();
            pli.setINDEX(JB.getString("INDEX"));
            pli.setREMARK(JB.getString("REMARK"));
            pli.setValid(JB.getBoolean("IsValid"));
            pli.setRequire(JB.getBoolean("IsRequire"));
            if (pli.isValid()) {
                PLI.add(pli);
            }
        }
        return PLI;
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

