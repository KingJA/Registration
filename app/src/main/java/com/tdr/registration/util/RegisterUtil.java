package com.tdr.registration.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.AutoRegister.AutoRegister_Car_Activity;
import com.tdr.registration.activity.AutoRegister.AutoRegister_Insurance_Activity;
import com.tdr.registration.activity.AutoRegister.AutoRegister_Personnel_Activity;
import com.tdr.registration.activity.ConfirmationInsuranceActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.activity.PayActivity;
import com.tdr.registration.activity.PayQcodeActivity;
import com.tdr.registration.activity.QRCodeScanActivity;
import com.tdr.registration.activity.UnpaidActivity;
import com.tdr.registration.activity.longyan.PreToOfficialSecondLongYanActivity;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.MyApplication;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.Order;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RegisterUtil {


    public final static int SCANNIN_GREQUEST_CODE = 1991;//二维码回调值
    public final static int SCANNIN_QR_CODE = 0514;//二维码回调值*
    public final static int BRAND_CODE = 2016;//品牌回调
    public final static int CONFIRMATION_INSURANCE = 1212;//确认保险
    public final static int PRE_SHOW_CODE = 1314;//预登记展示回调值


    public MyApplication BA;
    public ZProgressHUD mProgressHUD;
    private Activity mActivity;
    public ParsingQR mQR;
    public Gson mGson;
    public CharacterParser characterParser;
    public DbManager db;
    public String Version;
    public String appName;
    public String city;
    public String isScanLabel;
    public String isScanCard;
    public String IsConfirm;
    public String REGISTRATION;
    public String REGULAR;
    public String REGULAR2;
    public String ISDOUBLESIGN;
    public String CarRegular;
    public String IsScanDjh;
    public String IsScanCjh;
    public String preregisters;
    public String preregistration;
    public String identity;
    public String HasAgent;
    public String EnableInvoice;


    public List<InsuranceModel> insuranceModelsList;
    public ConfirmInsuranceModel ConfirmInsuranceList;
    NiftyDialogBuilder dialogBuilder;
    NiftyDialogBuilder.Effectstype effectstype;
    List<Order> listOrder;
    Order CAR = null;
    Order Insurance = null;
    Order Personnel = null;

    public RegisterUtil(Activity AC) {
        mActivity = AC;
        db = x.getDb(DBUtils.getDb());
        mGson = new Gson();
        mQR = new ParsingQR();
        BA = ((MyApplication) mActivity.getApplicationContext());
        mProgressHUD = new ZProgressHUD(mActivity);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        listOrder = mGson.fromJson((String) SharedPreferencesUtils.get("Order", ""), new TypeToken<List<Order>>() {
        }.getType());

        for (Order order : listOrder) {
            if (order.getName().equals("Car")) {
                CAR = order;
            } else if (order.getName().equals("Insurance")) {
                Insurance = order;
            } else if (order.getName().equals("Personnel")) {
                Personnel = order;
            }
        }
        Version = (String) SharedPreferencesUtils.get("Version", "");
        appName = (String) SharedPreferencesUtils.get("appName", "");
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        isScanLabel = (String) SharedPreferencesUtils.get("isScanLabel", "");
        isScanCard = (String) SharedPreferencesUtils.get("isScanCard", "");
        IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
        REGISTRATION = (String) SharedPreferencesUtils.get("REGISTRATION", "");
        REGULAR = (String) SharedPreferencesUtils.get("REGULAR", "");
        REGULAR2 = (String) SharedPreferencesUtils.get("REGULAR2", "");
        ISDOUBLESIGN = (String) SharedPreferencesUtils.get("ISDOUBLESIGN", "");
        CarRegular = (String) SharedPreferencesUtils.get("PlatenumberRegular" + BA.getRD().getVEHICLETYPE(), "");
        IsScanDjh = (String) SharedPreferencesUtils.get("IsScanDjh", "");
        IsScanCjh = (String) SharedPreferencesUtils.get("IsScanCjh", "");
        preregisters = (String) SharedPreferencesUtils.get("preregisters", "");
        preregistration = (String) SharedPreferencesUtils.get("preregistration", "");


        HasAgent = (String) SharedPreferencesUtils.get("HasAgent", "");
        identity = (String) SharedPreferencesUtils.get("identity", "");
        EnableInvoice = (String) SharedPreferencesUtils.get("EnableInvoice", "");

        characterParser = CharacterParser.getInstance();
        dialogBuilder = NiftyDialogBuilder.getInstance(mActivity);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;

        //判断这个城市是否有保险
        try {
            insuranceModelsList = db.findAll(InsuranceModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (insuranceModelsList == null) {
            insuranceModelsList = new ArrayList<InsuranceModel>();
        }

        BA.getRD().setIsommission("1");
        BA.getRD().setCARDTYPE("1");

    }

    public List<PhotoListInfo> getPhotoList() throws DbException, JSONException {
        List<PhotoListInfo> PLI = new ArrayList<>();
        List<BaseInfo> ResultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
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

    public List<PhotoModel> getPhotoFile() {
        ArrayList<PhotoListAdapter.DrawableList> DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        List<PhotoModel> pm = new ArrayList<PhotoModel>();

        if (preregisters.equals("") && preregistration.equals("")) {

        }
        if (!preregisters.equals("")) {
            PreRegistrationModel preForKMModel = mGson.fromJson(preregisters, new TypeToken<PreRegistrationModel>() {
            }.getType());
            mLog.e("Pan", "preForKMModel=" + preForKMModel.getColorName());

            for (PhotoModel photoModel : preForKMModel.getPhotoListFile()) {
                PhotoModel PM = new PhotoModel();
                PM.setINDEX(photoModel.getINDEX());
                PM.setPhoto(photoModel.getPhoto());
                PM.setPhotoFile(photoModel.getPhotoFile());
                PM.setRemark(photoModel.getRemark());
                pm.add(PM);
            }
        } else if (!preregistration.equals("")) {
            List<PhotoModel> list = mGson.fromJson(preregistration, new TypeToken<List<PhotoModel>>() {
            }.getType());
            mLog.e("Pan", "list.size=" + list.size());

            for (PhotoModel photoModel : list) {
                PhotoModel PM = new PhotoModel();
                PM.setINDEX(photoModel.getINDEX());
                PM.setPhoto(photoModel.getPhoto());
                PM.setPhotoFile(photoModel.getPhotoFile());
                PM.setRemark(photoModel.getRemark());
                pm.add(PM);
            }
        }
        return pm;

    }

    public void SendMSG() {
        if (IsConfirm.equals("1")) {

            ConfirmInsuranceList.setPlateNumber(BA.getRD().getPlateNumber());
            ConfirmInsuranceList.setName(BA.getRD().getOwnerName());
            ConfirmInsuranceList.setCardType(BA.getRD().getCARDTYPE());
            ConfirmInsuranceList.setCardID(BA.getRD().getIdentity());
            ConfirmInsuranceList.setPhone(BA.getRD().getPhone1());
            mLog.e("PlateNumber=" + ConfirmInsuranceList.getPlateNumber());
            mLog.e("Name=" + ConfirmInsuranceList.getName());
            mLog.e("CardType=" + ConfirmInsuranceList.getCardType());
            mLog.e("CardID=" + ConfirmInsuranceList.getCardID());
            mLog.e("Phone=" + ConfirmInsuranceList.getPhone());

            Bundle bundle = new Bundle();
            ArrayList list = new ArrayList();
            list.add(ConfirmInsuranceList);
            bundle.putParcelableArrayList("ConfirmInsurance", list);
            ActivityUtil.goActivityForResultWithBundle(mActivity, ConfirmationInsuranceActivity.class, bundle,
                    CONFIRMATION_INSURANCE);
        } else {
            sendMsg();
        }
    }

    public void ActivityFinish_Car() {
        if (CAR.getOrder() + 1 == Personnel.getOrder()) {
            ActivityUtil.goActivity(mActivity, AutoRegister_Personnel_Activity.class);
        }
        if (CAR.getOrder() + 1 == Insurance.getOrder()) {
            if (insuranceModelsList.size() > 0) {//该城市有保险
                ActivityUtil.goActivity(mActivity, AutoRegister_Insurance_Activity.class);
            } else {
                ActivityUtil.goActivity(mActivity, AutoRegister_Car_Activity.class);
            }
        }
        if (CAR.getOrder() == (Insurance.getOrder() + Personnel.getOrder())) {
            SendMSG();
        }

    }

    public void ActivityFinish_Personnel() {
        if (Personnel.getOrder() + 1 == Insurance.getOrder()) {
            mLog.e("insuranceModelsList=" + insuranceModelsList.size());
            if (insuranceModelsList.size() > 0) {//该城市有保险
                ActivityUtil.goActivity(mActivity, AutoRegister_Insurance_Activity.class);
            } else {
                ActivityUtil.goActivity(mActivity, AutoRegister_Car_Activity.class);
            }
        }
        if (Personnel.getOrder() + 1 == CAR.getOrder()) {
            ActivityUtil.goActivity(mActivity, AutoRegister_Car_Activity.class);
        }
        if (Personnel.getOrder() == (CAR.getOrder() + Insurance.getOrder())) {
            SendMSG();
        }

    }

    public void ActivityFinish_Insurance() {
        mLog.e("VEHICLETYPE=" + BA.getRD().getVEHICLETYPE());
        if (BA.getIsPRE().equals("1")) {
            ActivityUtil.goActivity(mActivity, PreToOfficialSecondLongYanActivity.class);
        } else {
            if (Insurance.getOrder() + 1 == Personnel.getOrder()) {
                ActivityUtil.goActivity(mActivity, AutoRegister_Personnel_Activity.class);
            }
            if (Insurance.getOrder() + 1 == CAR.getOrder()) {
                ActivityUtil.goActivity(mActivity, AutoRegister_Car_Activity.class);
            }
            if (Insurance.getOrder() == (CAR.getOrder() + Personnel.getOrder())) {
                SendMSG();
            }
        }

    }

    public void sendMsg() {

        JSONObject obj = new JSONObject();
        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;


        if (BA.getRD().getVEHICLETYPE() == null || BA.getRD().getVEHICLETYPE().equals("")) {
            String userName = (String) SharedPreferencesUtils.get("userName", "");
            String city = (String) SharedPreferencesUtils.get("locCityName", "");
            MobclickAgent.reportError(mActivity, "车辆类型数据丢失" + userName + "_" + city);
            Utils.showToast("车辆类型数据被回收,请重新登记。");

        }


        try {
            List<PhotoListInfo> PLI = getPhotoList();
            for (int i = 0; i < getPhotoList().size(); i++) {
                index = PLI.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
//                Log.e("Pan", i + "  photo:" + index + "=\n" + PhotoFile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            obj.put("EcId", UUID.randomUUID().toString().toUpperCase());
            obj.put("HasRFID", "1");// 是否有有源标签,1有，0无
            obj.put("VehicleModels", "");
            obj.put("VEHICLETYPE", BA.getRD().getVEHICLETYPE());

            obj.put("THEFTNO", BA.getRD().getTHEFTNO());
            obj.put("THEFTNO2", BA.getRD().getTHEFTNO2());
            obj.put("Photo1File", "");
            obj.put("Photo2File", "");
            obj.put("Photo3File", "");
            obj.put("Photo4File", "");
            obj.put("REGISTERID", BA.getRD().getREGISTERID());
            obj.put("CARTYPE", BA.getRD().getCARTYPE());
            obj.put("ISCONFIRM", BA.getRD().getISCONFIRM());
            obj.put("VehicleBrand", BA.getRD().getVehicleBrand());
            obj.put("PlateNumber", BA.getRD().getPlateNumber());

            String platetype = BA.getRD().getPlateType();
            if (platetype == null || platetype.equals("")) {
                platetype = "1";
            }
            obj.put("PlateType", platetype);
            obj.put("ShelvesNo", BA.getRD().getShelvesNo());
            obj.put("EngineNo", BA.getRD().getEngineNo());
            obj.put("ColorId", BA.getRD().getColorId());
            obj.put("ColorId2", BA.getRD().getColorId2());
            obj.put("BuyDate", BA.getRD().getBuyDate());
            obj.put("Price", "");
            obj.put("CARDTYPE", BA.getRD().getCARDTYPE());
            obj.put("OwnerName", BA.getRD().getOwnerName());
            obj.put("CardId", BA.getRD().getIdentity());
            obj.put("Phone1", BA.getRD().getPhone1());
            obj.put("Phone2", BA.getRD().getPhone2());
            obj.put("ResidentAddress", BA.getRD().getCurrentAddress());
            obj.put("Remark", BA.getRD().getRemark());
            obj.put("INSURERTYPE", "2");// 保险公司

            obj.put("PhotoListFile", JA);
            JSONArray jsonArray1 = new JSONArray();
            JSONObject jsonObject1 = null;

            for (UploadInsuranceModel uploadInsuranceModel : BA.getUploadInsuranceModels()) {
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
            obj.put("InvoiceOp", BA.getRD().getInvoiceOp());
            obj.put("AGENTNAME", BA.getRD().getCommissionName());
            obj.put("AGENTCARDTYPE", BA.getRD().getCommissionCardType());
            obj.put("AGENTCARDID", BA.getRD().getCommissionIdentity());
            obj.put("AGENTPHONE", BA.getRD().getCommissionPhone1());
            obj.put("AGENTADDR", BA.getRD().getCommissionAddress());
            obj.put("AGENTREMARK", BA.getRD().getCommissionRemark());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", obj.toString());
        String functionName = "";
        if (BA.getDistrainCarListID().equals("")) {
            functionName = Constants.WEBSERVER_ADDELECTRICCAR;
        } else {
            map.put("DistrainCarListID", BA.getDistrainCarListID());
            functionName = Constants.WEBSERVER_ADDELECTRICCARFROMDISTRAIN;

        }
        Utils.LOGE("Pan", map.toString());
        mLog.e("apiUrl:" + (String) SharedPreferencesUtils.get("apiUrl", ""));
        mProgressHUD.show();
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), functionName,
                map, new WebServiceUtils.WebServiceCallBack() {
                    @Override
                    public void callBack(String result) {
                        mLog.e("result:" + result);
                        Utils.LOGE("Pan", result);
                        if (result != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                int errorCode = jsonObject.getInt("ErrorCode");
                                String data = jsonObject.getString("Data");
                                if (errorCode == 0) {
                                    mProgressHUD.dismiss();
                                    List<PayInsurance> payInsurances = mGson.fromJson(data, new
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
                                            PayQcodeActivity.goActivity(mActivity, payInsurance.getContent(),
                                                    payInsurance.getTotal_Amount(), payInsurance.getPlateNumber(),
                                                    payInsurance.getPayNo(),PayQcodeActivity.FORM_REGISTER);
                                        } else {
                                            //直接支付
                                            Bundle bundle = new Bundle();
                                            bundle.putString("UnPaid", "0");
                                            bundle.putString("PayDate", data);
                                            ArrayList list = new ArrayList();
                                            list.add(payInsurances);
                                            bundle.putParcelableArrayList("PayDate", list);
                                            ActivityUtil.goActivityWithBundle(mActivity, PayActivity.class, bundle);
                                        }

                                    } else if (payInsurances.size() > 1) {
                                        List<PayInsurance> PI = mGson.fromJson(data, new
                                                TypeToken<List<PayInsurance>>() {
                                                }.getType());
                                        SharedPreferencesUtils.put("preregisters", "");
                                        SharedPreferencesUtils.put("preregistration", "");
                                        Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
                                                (VehiclesStorageUtils
                                                        .PLATENUMBER) + "  电动车信息上传成功！");
                                        Bundle bundle = new Bundle();
                                        bundle.putString("UnPaid", "2");
                                        ArrayList list = new ArrayList();
                                        list.add(PI);
                                        bundle.putParcelableArrayList("PayDate", list);
                                        ActivityUtil.goActivityWithBundle(mActivity, UnpaidActivity.class, bundle);
                                        mActivity.finish();

                                    } else {
                                        dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils
                                                .PLATENUMBER) + "  电动车信息上传成功！");
                                    }
//                                    if (checkJson(data) == 0) {
//                                        //一条订单
//                                        List<PayInsurance> PI = mGson.fromJson(data, new
//                                                TypeToken<List<PayInsurance>>() {
//                                        }.getType());
//                                        SharedPreferencesUtils.put("preregisters", "");
//                                        Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
//                                                (VehiclesStorageUtils
//                                                .PLATENUMBER) + "  电动车信息上传成功！");
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("UnPaid", "0");
////                                bundle.putString("PayDate", data);
//                                        ArrayList list = new ArrayList();
//                                        list.add(PI);
//                                        bundle.putParcelableArrayList("PayDate", list);
//                                        ActivityUtil.goActivityWithBundle(mActivity, PayActivity.class, bundle);
//                                        mActivity.finish();
//                                    } else if (checkJson(data) == 1) {
//                                        //多条订单
//                                        List<PayInsurance> PI = mGson.fromJson(data, new
//                                                TypeToken<List<PayInsurance>>() {
//                                        }.getType());
//                                        SharedPreferencesUtils.put("preregisters", "");
//                                        SharedPreferencesUtils.put("preregistration", "");
//                                        Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr
//                                                (VehiclesStorageUtils
//                                                .PLATENUMBER) + "  电动车信息上传成功！");
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("UnPaid", "2");
//                                        ArrayList list = new ArrayList();
//                                        list.add(PI);
//                                        bundle.putParcelableArrayList("PayDate", list);
//                                        ActivityUtil.goActivityWithBundle(mActivity, UnpaidActivity.class, bundle);
//                                        mActivity.finish();
//                                    } else {
//                                        dialogShow(0, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr
// (VehiclesStorageUtils
//                                                .PLATENUMBER) + "  电动车信息上传成功！");
//                                    }
                                } else if (errorCode == 1) {
                                    mProgressHUD.dismiss();
                                    Utils.showToast(data);
                                    SharedPreferencesUtils.put("token", "");
                                    ActivityUtil.goActivityAndFinish(mActivity, LoginActivity.class);
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

    private void dialogShow(int flag, final String msg) {
        if (flag == 0) {
            dialogBuilder.withTitle("提示")
                    .withTitleColor("#333333")
                    .withMessage(msg)
                    .isCancelableOnTouchOutside(false)
                    .withEffect(effectstype)
                    .withButton1Text("確定")
                    .setCustomView(R.layout.custom_view, mActivity)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            SharedPreferencesUtils.put("preregisters", "");
                            SharedPreferencesUtils.put("preregistration", "");
                            VehiclesStorageUtils.clearData();
                            ActivityUtil.goActivityAndFinish(mActivity, HomeActivity.class);
                        }
                    }).show();
        } else if (flag == 1) {
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mActivity).withButton2Text("确认").setButton1Click(new View
                    .OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    SharedPreferencesUtils.put("preregisters", "");
                    SharedPreferencesUtils.put("preregistration", "");
                    mActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    TransferUtil.remove("PhotoList");
                    BA.getACList().clear();
                    if (BA.getInType().equals("PreRegistration")) {
                        mActivity.finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(mActivity, HomeActivity.class);
                        mActivity.startActivity(intent);
                        mActivity.finish();
                    }
                }
            }).show();
        }

    }

    /**
     * 扫码
     *
     * @param ScanType   扫描类型
     * @param isshow     是否显示录入框
     * @param isPlate    是否扫描车牌
     * @param ButtonName 按钮文本
     */
    public void Scan(int ScanType, boolean isshow, boolean isPlate, String ButtonName) {
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", ScanType);
        bundle.putBoolean("isShow", isshow);
        bundle.putBoolean("isPlateNumber", isPlate);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(mActivity, QRCodeScanActivity.class, bundle, SCANNIN_QR_CODE);
    }

    public void CheckPlate(String ButtonName) {
        Bundle bundle = new Bundle();
        bundle.putInt("ScanType", 0);
        bundle.putBoolean("isShow", true);
        bundle.putBoolean("isPlateNumber", true);
        bundle.putString("ButtonName", ButtonName);
        ActivityUtil.goActivityForResultWithBundle(mActivity, QRCodeScanActivity.class, bundle, SCANNIN_GREQUEST_CODE);
    }

    public void BackPressed() {
        if (BA.getACList().contains(mActivity)) {
            if (BA.getACList().size() > 1) {
                BA.getACList().remove(mActivity);
                mActivity.finish();
            } else {
                dialogShow(1, "信息编辑中，确认离开该页面？");
            }
        }

    }
}
