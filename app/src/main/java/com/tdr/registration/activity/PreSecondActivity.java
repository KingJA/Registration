package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;
import com.yunmai.android.idcard.ACamera;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tdr.registration.util.VehiclesStorageUtils.clearData;

/**
 * 预登记第二页
 */

public class PreSecondActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.TV_Name)
    TextView TV_Name;
    @BindView(R.id.text_cardType)
    TextView textCardType;
    @BindView(R.id.edit_ownerName)
    EditText editOwnerName;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.edit_ownerPhone1)
    EditText editOwnerPhone1;
    @BindView(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @BindView(R.id.text_currentAddressStart)
    TextView textCurrentAddressStart;
    @BindView(R.id.edit_ownerCurrentAddress)
    EditText editOwnerCurrentAddress;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.linear_identity)
    LinearLayout linearIdentity;
    @BindView(R.id.image_identity)
    ImageView imageIdentity;
    @BindView(R.id.relative_commission)
    RelativeLayout relativeCommission;

    @BindView(R.id.btn_next)
    Button btnNext;

    private Context mContext;
    private DbManager db;
    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private String mCardType = "";
    private String mCardTypeId = "";
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private ZProgressHUD mProgressHUD;

    /*OCRCode*/
    private final static int CAMERA = 0316;
    private List<PhotoListInfo> PLI;
    private String locCityName = "";
    private Activity mActivity;
    private String in="";
    private String ShowQR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        db = x.getDb(DBUtils.getDb());
        ShowQR = (String) SharedPreferencesUtils.get("ShowQR", "1");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        Bundle bundle = (Bundle) getIntent().getExtras();
        initView();
        initData();
        getPrephotoConfig();
    }

    private void initView() {
        textTitle.setText("车辆预登记");
        btnNext.setText("提交");
        in=getIntent().getExtras().getString("in");
        imageScan.setBackgroundResource(R.mipmap.ocr_scan);
        relativeCommission.setVisibility(View.GONE);
        linearIdentity.setVisibility(View.GONE);
        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("昆明") || locCityName.contains("南宁")) {
            textCurrentAddressStart.setVisibility(View.GONE);
            linearIdentity.setVisibility(View.GONE);
        }
        if(in.equals("TJ") ){
            TV_Name.setText("购买人姓名");
            textTitle.setText("免费上牌");
        }
    }

    private void initData() {
        mCardTypeId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARDTYPE);
        try {
            cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (cardList == null) {
            cardList = new ArrayList<BikeCode>();
        }

        Log.e("Pan","cardType:"+mCardTypeId);
        for (BikeCode bikeCode : cardList) {
            if(mCardTypeId.equals(bikeCode.getCode())){
                textCardType.setText(bikeCode.getName());
            }
        }

        editOwnerName.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
        editOwnerIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
        editOwnerPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
        editOwnerPhone2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
        editOwnerCurrentAddress.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
        editRemarks.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));
    }

    @OnClick({R.id.image_back, R.id.text_cardType, R.id.btn_next, R.id.image_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.text_cardType:
                dialogShow(0, "");
                break;
            case R.id.image_scan:
                ActivityUtil.goActivityForResult(PreSecondActivity.this, ACamera.class, CAMERA);
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                mLog.e("VEHICLETYPE="+ VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
                sendData();
                break;
        }
    }
    private void sendData(){
        JSONObject jsonObject = new JSONObject();
        try {

            JSONArray JA = new JSONArray();
            JSONObject JB;
            String index;
            String PhotoFile;
            for (int i = 0; i < PLI.size(); i++) {
                index = PLI.get(i).getINDEX();
                PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                JB = new JSONObject();
                JB.put("INDEX", index);
                JB.put("Photo", "");
                JB.put("PhotoFile", PhotoFile);
                JA.put(JB);
//                Log.e("Pan", i + "  photo:" + index + "=\n" + PhotoFile);
            }

            jsonObject.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            jsonObject.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            jsonObject.put("VEHICLEBRAND", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            jsonObject.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            jsonObject.put("SHELVESNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            jsonObject.put("ENGINENO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            jsonObject.put("COLORID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            jsonObject.put("COLORID2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            jsonObject.put("BUYDATE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));

            jsonObject.put("CARDTYPE", mCardTypeId);
            jsonObject.put("OWNERNAME", editOwnerName.getText().toString());
            jsonObject.put("CARDID", editOwnerIdentity.getText().toString().toUpperCase().trim());
            jsonObject.put("PHONE1", editOwnerPhone1.getText().toString().trim());
            jsonObject.put("PHONE2", editOwnerPhone2.getText().toString().trim());
            jsonObject.put("ADDRESS", editOwnerCurrentAddress.getText().toString().trim());
            jsonObject.put("REMARK", editRemarks.getText().toString().trim());
            jsonObject.put("PhotoListFile", JA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ADDPREREGISTER, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                mLog.e("result="+result);
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        String ID = json.getString("Id");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            if(ShowQR.equals("0")){
                                dialogShow(1, data);
                            }else {
                                if (ID != null && !ID.equals("")) {
                                    ID = "TDR_APP" + ID;
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("IsCanBack", false);
                                    bundle.putString("QRCodeID", ID);
                                    ActivityUtil.goActivityWithBundle(mActivity, QRCodeCreateActivity.class, bundle);
                                    finish();
                                } else {
                                    dialogShow(1, data);
                                }
                            }
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(PreSecondActivity.this, LoginActivity.class);
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA:
                if (RESULT_OK == resultCode) {
                    String identity = data.getStringExtra("card");
                    editOwnerIdentity.setText(identity);
                    String name = data.getStringExtra("name");
                    editOwnerName.setText(name);
                    String address = data.getStringExtra("address");
                    editOwnerCurrentAddress.setText(address);
                    Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra("img"));
                    if (bitmap == null) {
                        Utils.myToast(mContext, "SD写入问题无法获取图片");
                    }
                }
                break;
        }
    }

    private boolean checkData() {
        if (mCardTypeId.equals("")) {
            Utils.myToast(mContext, "请选择证件类型");
            return false;
        }
        String ownerName = editOwnerName.getText().toString().trim();
        if (ownerName.equals("")) {
            Utils.myToast(mContext, "请输入车主姓名");
            return false;
        }
        String ownerIdentity = editOwnerIdentity.getText().toString().toUpperCase().trim();
        if (ownerIdentity.equals("")) {
            Utils.myToast(mContext, "请输入证件号码");
            return false;
        }
        if (mCardTypeId.equals("1")) {//如果车主证件类型为身份证（1）
            if (!Utils.isIDCard18(ownerIdentity)) {
                Utils.myToast(mContext, "输入的身份证号码格式有误");
                return false;
            }
        }
        String phone1 = editOwnerPhone1.getText().toString().trim();
        if (phone1.equals("")) {
            Utils.myToast(mContext, "请输入联系方式1");
            return false;
        }

        if (!locCityName.contains("昆明") && !locCityName.contains("天津") && !locCityName.contains("南宁")) {
            String address = editOwnerCurrentAddress.getText().toString().trim();
            if (address.equals("")) {
                Utils.myToast(mContext, "请输入车主现住址");
                return false;
            }
        }
        return true;
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, final String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            cardTypeList.clear();
            cardTypeAdapter = new ColorAdapter(mContext, cardTypeList,
                    cardTypeMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter);
            for (int i = 0; i < cardList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(cardList.get(i).getCode());
                sortModel.setName(cardList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(cardList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                cardTypeList.add(sortModel);
            }

            cardTypeAdapter.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mContext);
            dialogBuilder.withTitle("证件类型").withTitleColor("#333333")
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
                    textCardType.setText(mCardType);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(msg)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("取消")
                    .setCustomView(R.layout.custom_view, mContext).withButton2Text("确认").setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            }).setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                    //清空图片缓存
                    SharedPreferencesUtils.put("guleCar", "");
                    SharedPreferencesUtils.put("guleBattery", "");
                    SharedPreferencesUtils.put("labelA", "");
                    SharedPreferencesUtils.put("labelB", "");
                    SharedPreferencesUtils.put("plateNum", "");
                    SharedPreferencesUtils.put("identity", "");
                    SharedPreferencesUtils.put("applicationForm", "");
                    SharedPreferencesUtils.put("invoice", "");
                    SharedPreferencesUtils.put("install", "");

                    for (int i = 0; i < PLI.size(); i++) {
                        SharedPreferencesUtils.put("Photo:" + PLI.get(i).getINDEX(), "");
                        com.orhanobut.logger.Logger.d("清理 Photo:" + PLI.get(i).getINDEX());
                    }

                    clearData();
                    ActivityUtil.goActivityAndFinish(PreSecondActivity.this, HomeActivity.class);
                }
            }).show();
        }

    }

    @Override
    public void onBackPressed() {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, editOwnerIdentity.getText().toString().toUpperCase().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, editOwnerName.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, editOwnerPhone1.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, editOwnerPhone2.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, editOwnerCurrentAddress.getText().toString().trim());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, editRemarks.getText().toString().trim());
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                cardTypeMap.clear();
                cardTypeMap.put(position, 100);
                cardTypeAdapter.notifyDataSetChanged();
                mCardType = cardTypeList.get(position).getName();
                mCardTypeId = cardTypeList.get(position).getGuid();
                if (mCardTypeId.equals("1")) {
                    imageScan.setVisibility(View.VISIBLE);
                } else {
                    imageScan.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void getPrephotoConfig(){
       String city = (String) SharedPreferencesUtils.get("locCityName", "");
        List<BaseInfo> ResultList = null;
        PLI=new ArrayList<PhotoListInfo>();
        try {
            ResultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (ResultList == null) {
            ResultList = new ArrayList<BaseInfo>();
        }
        BaseInfo BI = ResultList.get(0);
        try {
            JSONArray JA = new JSONArray(BI.getPrephotoConfig());
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
        } catch (Exception e) {
            mLog.e("数据异常2");
            e.printStackTrace();
        }
    }
}
