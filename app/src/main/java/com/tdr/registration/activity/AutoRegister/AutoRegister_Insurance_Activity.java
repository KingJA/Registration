package com.tdr.registration.activity.AutoRegister;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
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

import com.tdr.registration.R;
import com.tdr.registration.activity.RegisterThirdActivity;
import com.tdr.registration.activity.longyan.PreToOfficialSecondLongYanActivity;
import com.tdr.registration.activity.normal.RegisterFirstNormalActivity2;
import com.tdr.registration.adapter.InsuranceAdapter;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.RegisterUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.LinearLayoutForListView;
import com.tdr.registration.view.RadioGroupEx;
import com.umeng.analytics.MobclickAgent;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


/**
 * 备案登记动态排序_保险信息
 */
@ContentView(R.layout.activity_register_third)
public class AutoRegister_Insurance_Activity extends Activity implements View.OnClickListener{

    @ViewInject(R.id.image_back)
    ImageView imageBack;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.list_insurance)
    LinearLayoutForListView listInsurance;
    @ViewInject(R.id.btn_submit)
    Button btnSubmit;
    @ViewInject(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @ViewInject(R.id.text_ownerName)
    TextView textOwnerName;
    @ViewInject(R.id.text_phone1)
    TextView textPhone1;
    @ViewInject(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @ViewInject(R.id.text_plateNumber)
    TextView textPlateNumber;
    @ViewInject(R.id.text_vehicleFrame)
    TextView textVehicleFrame;
    @ViewInject(R.id.text_vehicleMotor)
    TextView textVehicleMotor;
    @ViewInject(R.id.layout_printContent)
    LinearLayout layoutPrintContent;
    @ViewInject(R.id.TV_Attention)
    TextView TV_Attention;

    @ViewInject(R.id.LL_Invoice)
    LinearLayout LL_Invoice;
    @ViewInject(R.id.RG_Invoice)
    RadioGroup RG_Invoice;
    @ViewInject(R.id.RB_Invoice_No)
    RadioButton RB_Invoice_No;
    @ViewInject(R.id.RB_Invoice_Personal)
    RadioButton RB_Invoice_Personal;
    @ViewInject(R.id.RB_Invoice_Enterprise)
    RadioButton RB_Invoice_Enterprise;
    
    private Activity mActivity;
    private RegisterUtil RU;
    private List<InsuranceModel> insuranceModelsList = new ArrayList<>();//保险集合
    private InsuranceAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("【当前Activity】", "AutoRegister_Insurance_Activity" );
        x.view().inject(this);
        mActivity = this;
        RU = new RegisterUtil(this);
        RU.BA.getACList().add(this);
        try {
            initView();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    private void initView() throws DbException {
        imageBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        if(RU.BA.getACList().size()==3){
            btnSubmit.setText("提交");
        }
        if (RU.BA.getTitleType().equals("")) {
            if (!RU.REGISTRATION.equals("")) {
                textTitle.setText(RU.REGISTRATION);
            } else {
                if (RU.city.contains("温州")) {
                    textTitle.setText("登记备案");
                } else {
                    if (RU.appName.contains("防盗")) {
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
        if (RU.EnableInvoice.equals("1")) {
            LL_Invoice.setVisibility(View.VISIBLE);
            RB_Invoice_No.setChecked(true);
            RU.BA.getRD().setInvoiceOp("0");
        } else if (RU.EnableInvoice.equals("0") || RU.EnableInvoice.equals("")) {
            LL_Invoice.setVisibility(View.GONE);
            RU.BA.getRD().setInvoiceOp("");
        }


        List<InsuranceModel> InsuranceModels = null;
        InsuranceModels = RU.db.findAll(InsuranceModel.class);
        if (InsuranceModels == null) {
            InsuranceModels = new ArrayList<InsuranceModel>();
        }
        mLog.e("InsuranceModels:" +InsuranceModels.size());
        for (int i = 0; i < InsuranceModels.size(); i++) {
            mLog.e(RU.BA.getRD().getVEHICLETYPE()+ "VehicleType:" + InsuranceModels.get(i).getVehicleType());
            if (InsuranceModels.get(i).getVehicleType() == null || InsuranceModels.get(i).getVehicleType().equals("") || InsuranceModels.get(i).getVehicleType().equals("0")) {
                insuranceModelsList.add(InsuranceModels.get(i));
            } else {
                if (InsuranceModels.get(i).getVehicleType() != null && !InsuranceModels.get(i).getVehicleType().equals("")) {
                    if (InsuranceModels.get(i).getVehicleType().equals(RU.BA.getRD().getVEHICLETYPE())) {
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
            if (RU.Version.equals("1")) {//新老版本走不同的ID
                ListID = insuranceModel.getListID();
                detailBeanList = RU.db.selector(DetailBean.class).where("CID", "=", ListID).findAll();
            } else {
                ListID = insuranceModel.getRemarkID();
                detailBeanList = RU.db.selector(DetailBean.class).where("RemarkID", "=", ListID).findAll();
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

        mAdapter = new InsuranceAdapter(mActivity, insuranceModelsList);

        listInsurance.setAdapter(mAdapter);
        if (insuranceModelsList.size() == 0) {
            listInsurance.setVisibility(View.GONE);
            layoutPrintContent.setVisibility(View.VISIBLE);
            textOwnerIdentity.setText(RU.BA.getRD().getIdentity());
            textOwnerName.setText(RU.BA.getRD().getOwnerName());
            textPhone1.setText(RU.BA.getRD().getPhone1());
            textVehicleBrand.setText(RU.BA.getRD().getVehiclebrandName());
            textPlateNumber.setText(RU.BA.getRD().getPlateNumber());
            textVehicleFrame.setText(RU.BA.getRD().getShelvesNo());
            textVehicleMotor.setText(RU.BA.getRD().getEngineNo());
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
                    RU.BA.getRD().setInvoiceOp("0");
                } else if (checkedId == RB_Invoice_Personal.getId()) {
                    RU.BA.getRD().setInvoiceOp("1");
                }
                if (checkedId == RB_Invoice_Enterprise.getId()) {
                    RU.BA.getRD().setInvoiceOp("2");
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                RU.BackPressed();
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
                    if (RU.Version.equals("1")) {
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
                    Utils.showToast( "请选择年限");
                    return;
                } else {
                    if (RU.Version.equals("1")) {
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
            if (RU.EnableInvoice.equals("1")) {
                LL_Invoice.setVisibility(View.VISIBLE);
            }
        }
        mLog.e("CIMIL=" + CIMIL.size());
        CIM.setInsurance(CIMIL);

        RU.BA.setUploadInsuranceModels(uploadInsuranceModels);
        RU.BA.setCIM(CIM);
        TelephonyManager tm = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
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


        RU.ActivityFinish_Insurance();
    }
    @Override
    public void onBackPressed() {
        RU.BackPressed();
        return;
    }

}
