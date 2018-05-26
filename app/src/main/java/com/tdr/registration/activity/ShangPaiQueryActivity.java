package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.data.ParsingQR;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.model.ShangPaiInfo;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.ToastUtil;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 免费上牌查询
 */
@ContentView(R.layout.activity_dx_pre_registration_query)
public class ShangPaiQueryActivity extends Activity {

    @ViewInject(R.id.IV_back)
    private ImageView IV_back;
    @ViewInject(R.id.TV_Title)
    private TextView TV_Title;
    @ViewInject(R.id.ET_plateNumber)
    private EditText ET_plateNumber;
    @ViewInject(R.id.IV_Scan)
    private ImageView IV_Scan;
    @ViewInject(R.id.ET_cardid)
    private EditText ET_cardid;
    @ViewInject(R.id.ET_phone)
    private EditText ET_phone;
    @ViewInject(R.id.BT_query)
    private Button BT_query;

    private ZProgressHUD mProgressHUD;

    private Activity mActivity;
    private Gson mGson;
    private List<DX_PreRegistrationModel> PRList;
    private String city;
    private final static int SCANNIN_QR_CODE = 10514;//二维码回调值*
    private ParsingQR mQR;
    private String in = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("当前Activity");
        x.view().inject(this);
        initview();
    }

    private void initview() {
        mActivity = this;
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mQR = new ParsingQR();
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        TV_Title.setText("免费上牌查询");
        IV_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ScanType", 0);
                bundle.putBoolean("isShow", true);
                bundle.putBoolean("isPlateNumber", true);
                bundle.putString("ButtonName", "请输入车牌号");
                ActivityUtil.goActivityForResultWithBundle(ShangPaiQueryActivity.this, QRCodeScanActivity.class,
                        bundle, SCANNIN_QR_CODE);
            }
        });
        BT_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void query() {
        String plateNumber = ET_plateNumber.getText().toString().trim();
        String cardid = ET_cardid.getText().toString().trim();
        String phone = ET_phone.getText().toString().trim();
        if (plateNumber.equals("") && cardid.equals("") && phone.equals("")) {
            Utils.showToast("请至少输入一个查询条件");
            return;
        }
//        mProgressHUD.show();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
//        map.put("plateNumber", plateNumber);
//        map.put("cardid", cardid);
//        map.put("phone", phone);
//        map.put("registerId", "");
//        mLog.e("Pan", "map=" + map);
//        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants
// .WEBSERVER_GETPREREGISTERLIST, map, new WebServiceUtils.WebServiceCallBack() {
//            @Override
//            public void callBack(String result) {
//                Utils.LOGE("Pan","result=" + result);
//                if (result != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        int errorCode = jsonObject.getInt("ErrorCode");
//                        String data = jsonObject.getString("Data");
//                        if (errorCode == 0) {
//                            mProgressHUD.dismiss();
//                            try {
//                                PRList = mGson.fromJson(data, new TypeToken<List<DX_PreRegistrationModel>>() {
//                                }.getType());
//                                if (PRList.size() > 0) {
//                                    TransferUtil.save("PreRegistrationData",PRList);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("in",in);
//                                    ActivityUtil.goActivityWithBundle(ShangPaiQueryActivity.this,
// DX_PreListActivity.class,bundle);
//                                }
//                            }catch (JsonSyntaxException e){
//                                mProgressHUD.dismiss();
//                                Utils.showToast(data);
//                            }
//                        } else {
//                            mProgressHUD.dismiss();
//                            Utils.showToast(data);
//                        }
//                    } catch (JSONException e) {
//                        mProgressHUD.dismiss();
//                        e.printStackTrace();
//                        Utils.showToast("JSON解析出错");
//                    }
//                } else {
//                    mProgressHUD.dismiss();
//                    Utils.showToast("获取数据超时，请检查网络连接");
//                }
//            }
//        });

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlateNumber", plateNumber);
        map.put("CardId", cardid);
        map.put("HASRFID", "0");
        map.put("Phone", phone);
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants
                .HTTP_ElectricPager);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        HttpUtils.postK(RP, new HttpUtils.HttpCallBack() {
            @Override
            public void onSuccess(String result) {
                mProgressHUD.dismiss();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    int errorCode = jsonObject.getInt("ErrorCode");
                    String data = jsonObject.getString("Data");

                    if (errorCode == 0) {
                        int count = jsonObject.getInt("Count");
                        if (count > 0) {
                            List<ShangPaiInfo> shangPaiInfos = mGson.fromJson(data, new TypeToken<List<ShangPaiInfo>>() {
                            }.getType());
                            List<DX_PreRegistrationModel> oldTypeInfos = convertData(shangPaiInfos);
                            if (oldTypeInfos.size() > 0) {
                                TransferUtil.save("PreRegistrationData", oldTypeInfos);
                                Bundle bundle = new Bundle();
                                bundle.putString("in", in);
                                ActivityUtil.goActivityWithBundle(ShangPaiQueryActivity.this,
                                        ShangPaiListActivity.class, bundle);
                            }
                        }else{
                            ToastUtil.showToast("未查到上牌信息");
                        }


                    } else {
                        ToastUtil.showToast(data);
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

    public static  List<DX_PreRegistrationModel> convertData(List<ShangPaiInfo> shangPaiInfos) {
        List<DX_PreRegistrationModel> oldDataInfos = new ArrayList<>();
        for (ShangPaiInfo shangPaiInfo : shangPaiInfos) {
            DX_PreRegistrationModel oldDataInfo = new DX_PreRegistrationModel();
            oldDataInfo.setREGISTERID(shangPaiInfo.getREGISTERID());
            oldDataInfo.setVEHICLETYPE(shangPaiInfo.getVehicleType());
            oldDataInfo.setVEHICLEBRAND(shangPaiInfo.getVehicleBrand());
            oldDataInfo.setVEHICLEBRANDNAME(shangPaiInfo.getVehicleBrandName());
            oldDataInfo.setPLATENUMBER(shangPaiInfo.getPlateNumber());
            oldDataInfo.setCARDTYPE(shangPaiInfo.getCARDTYPE() + "");
            oldDataInfo.setSHELVESNO(shangPaiInfo.getShelvesNo());
            oldDataInfo.setENGINENO(shangPaiInfo.getEngineNo());
            oldDataInfo.setColorName(shangPaiInfo.getColorName());
            oldDataInfo.setCOLORNAME(shangPaiInfo.getColorName());
            oldDataInfo.setCOLORNAME2(shangPaiInfo.getColorName2());
            oldDataInfo.setCOLORID(shangPaiInfo.getColorId());
            oldDataInfo.setCOLORID2(shangPaiInfo.getColorId2());
            oldDataInfo.setECID(shangPaiInfo.getEcId());
            oldDataInfo.setBUYDATE(shangPaiInfo.getBuyDate());
            oldDataInfo.setOWNERNAME(shangPaiInfo.getOwnerName());
            oldDataInfo.setCARDID(shangPaiInfo.getCardId());
            oldDataInfo.setPHONE1(shangPaiInfo.getPhone1());
            oldDataInfo.setPHONE2(shangPaiInfo.getPhone2());
            oldDataInfo.setADDRESS(shangPaiInfo.getResidentAddress());
            oldDataInfo.setREMARK(shangPaiInfo.getRemark());
            oldDataInfo.setPhotoListFile(shangPaiInfo.getPhotoListFile());
            oldDataInfos.add(oldDataInfo);
        }
        return oldDataInfos;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCANNIN_QR_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Utils.showToast("没有扫描到二维码");
                    return;
                } else {
                    Bundle bundle = data.getExtras();
                    String labelNumber = bundle.getString("result");
                    String num = "";
                    boolean isScan = bundle.getBoolean("isScan");
                    if (isScan) {
                        num = labelNumber;
                    } else {
                        num = mQR.plateNumber(labelNumber);
                    }
                    if (!num.equals("-1")) {
                        ET_plateNumber.setText(num);
                    } else {
                        Utils.showToast("二维码不属于车牌");
                    }
                }
            }
        }
    }

}
