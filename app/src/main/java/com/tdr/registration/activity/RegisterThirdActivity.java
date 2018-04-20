package com.tdr.registration.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.tdr.registration.activity.kunming.RegisterFirstKunMingActivity;
import com.tdr.registration.activity.longyan.PreToOfficialSecondLongYanActivity;
import com.tdr.registration.activity.normal.RegisterFirstNormalActivity2;
import com.tdr.registration.adapter.InsuranceAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.jpush.ExampleUtil;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.FieldSettingModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.LinearLayoutForListView;
import com.tdr.registration.view.RadioGroupEx;
import com.umeng.analytics.MobclickAgent;


import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

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
public class RegisterThirdActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterThirdActivity";
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


        List<InsuranceModel> InsuranceModels = null;
        InsuranceModels = db.findAll(InsuranceModel.class);
        if (InsuranceModels == null) {
            InsuranceModels = new ArrayList<InsuranceModel>();
        }
//        insuranceModelsList = db.findAll(InsuranceModel.class);
//        String CarType = (String) SharedPreferencesUtils.get("MyCarType", "");
        for (int i = 0; i < InsuranceModels.size(); i++) {
            mLog.e(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE) + "VehicleType:" + InsuranceModels.get(i).getVehicleType());
            if (InsuranceModels.get(i).getVehicleType() == null || InsuranceModels.get(i).getVehicleType().equals("") || InsuranceModels.get(i).getVehicleType().equals("0")) {
                insuranceModelsList.add(InsuranceModels.get(i));
            } else {
                if (InsuranceModels.get(i).getVehicleType() != null && !InsuranceModels.get(i).getVehicleType().equals("")) {

                    if (InsuranceModels.get(i).getVehicleType().equals(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE))) {
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
//        mAdapter.setOnCheckBoxClickLitener(new InsuranceAdapter.OnCheckBoxClickLitener() {
//            @Override
//            public void onCheckBoxClick(int position) {
//                updataItem(position);
//            }
//
//            @Override
//            public void onRadioButtonClick(int position, RadioButton radiobutton) {
//                updataItem2(position);
//            }
//        });
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


    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
//        String deadLine = "";//保险年限

        List<UploadInsuranceModel> uploadInsuranceModels = new ArrayList<>();
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
        Bundle bundle = new Bundle();
        bundle.putString("activity", "");
        bundle.putString("InvoiceType", InvoiceType);
        bundle.putString("distrainCarListID", listId);
        ArrayList list = new ArrayList();
        list.add(uploadInsuranceModels);
        list.add(CIM);
        bundle.putParcelableArrayList("insurance", list);
//                if (locCityName.contains("昆明")) {
//                    //前往昆明界面
//                    ActivityUtil.goActivityWithBundle(RegisterThirdActivity.this, RegisterFirstKunMingActivity.class, bundle);
//                } else {
//                    ActivityUtil.goActivityWithBundle(RegisterThirdActivity.this, RegisterFirstNormalActivity.class, bundle);
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
            MobclickAgent.reportError(getApplicationContext(), "保险数据赋值错误：手机IMEI=" + imei + "  登录账号：" + (String) SharedPreferencesUtils.get("UP", "私有空间本地缓存读取失败"));
            try {
                insuranceModelsList.clear();
                initView();
            } catch (DbException e) {
                e.printStackTrace();
            }
            Utils.showToast("检测到保险数据被手机后台清理掉了，请从新选择保险。");
            return;
        }

        mLog.e("VEHICLETYPE=" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
        if (IsPRE.equals("1")) {
            bundle.putString("PhotoConfig", PhotoConfig);
            ActivityUtil.goActivityWithBundle(RegisterThirdActivity.this, PreToOfficialSecondLongYanActivity.class, bundle);
        } else {
            ActivityUtil.goActivityWithBundle(RegisterThirdActivity.this, RegisterFirstNormalActivity2.class, bundle);
        }
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

