package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;


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
 * 信息变更第二页
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ChangeSecondActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "RegisterSecondActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
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
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.text_cardType)
    TextView textCardType;

    @BindView(R.id.linear_identity)
    LinearLayout linear_identity;

    @BindView(R.id.relative_commission)
    RelativeLayout relativeCommission;

    private String isCommission = "1";//1，没有代办人；0，没有代办人
    private String commissionCardType = "";//代办人证件类型

    private Context mContext;
    private Activity mActivity;
    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private String mCardType = "";
    private String mCardTypeId = "";
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类
    private DbManager db;
    /*OCRCode*/
    private final static int CAMERA = 0316;

    private String locCityName = "";

    private ZProgressHUD mProgressHUD;
    private String PhotoConfig="";
    private List<PhotoListInfo> PLI;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        db = x.getDb(DBUtils.getDb());
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initView();

    }

    private void initView() {
        PLI=new ArrayList<PhotoListInfo>();
        Bundle bundle = getIntent().getExtras();
        PhotoConfig = bundle.getString("PhotoConfig");
        try {
            JSONArray JA = new JSONArray(PhotoConfig);
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
//                    Log.e("Pan","PhotoConfig.size="+PLI.size());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
        textTitle.setText("信息变更");
        linear_identity.setVisibility(View.GONE);
        btnNext.setText("提交");
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("昆明")) {
            textCurrentAddressStart.setVisibility(View.GONE);
        }
        editOwnerName.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.OWNERNAME));
        editOwnerIdentity.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.IDENTITY));
        editOwnerPhone1.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE1));
        editOwnerPhone2.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PHONE2));
        editOwnerCurrentAddress.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS));
        editRemarks.setText(VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.REMARK));

        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);

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


//        String identityStr = (String) SharedPreferencesUtils.get("identity", "");
//        if (!identityStr.equals("")) {
//            Bitmap bitmap = Utils.stringtoBitmap(identityStr);
//            Drawable bd = new BitmapDrawable(bitmap);
//            relativeIdentity.setBackground(bd);
//            textIdentity.setTextColor(getResources().getColor(R.color.white));
//        }

        relativeCommission.setVisibility(View.GONE);
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        if (locCityName.contains("昆明")) {
            textCurrentAddressStart.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_back, R.id.text_cardType, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.text_cardType:
                dialogShow(2, "");
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                dialogShow(0, "");
                break;
        }
    }


    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("确认重新提交信息？")
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
                    sendMsg();
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage(message)
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
                    Utils.ClearData();
                    VehiclesStorageUtils.clearData();
                    ActivityUtil.goActivityAndFinish(ChangeSecondActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 2) {
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
//            Log.e("颜色数量：", "" + cardList.size());
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
        }
    }

    private void sendMsg() {
        JSONObject jsonObject = new JSONObject();

        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;
        try {
//            Log.e("Pan","PLI="+PLI.size());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e("Pan", "JA=" + JA.length());

        try {
            jsonObject.put("EcId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ECID));
            jsonObject.put("HasRFID", "1");// 是否有有源标签,1有，0无
            jsonObject.put("VehicleModels", "");

            jsonObject.put("Photo1File", "");
            jsonObject.put("Photo2File", "");
            jsonObject.put("Photo3File", "");
            jsonObject.put("Photo4File", "");

            jsonObject.put("CARTYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.CARTYPE));
            jsonObject.put("PlateType", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATETYPE));
            jsonObject.put("ISCONFIRM", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ISCONFIRM));
            jsonObject.put("VehicleBrand", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
            jsonObject.put("THEFTNO", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO));
            jsonObject.put("THEFTNO2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.THEFTNO2));
            jsonObject.put("PlateNumber", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER));
            jsonObject.put("ShelvesNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.SHELVESNO));
            jsonObject.put("EngineNo", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.ENGINENO));
            jsonObject.put("ColorId", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
            jsonObject.put("ColorId2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
            jsonObject.put("BuyDate", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.BUYDATE));
            jsonObject.put("Price", "");

            jsonObject.put("CARDTYPE", mCardTypeId);
            jsonObject.put("OwnerName", editOwnerName.getText().toString());
            jsonObject.put("CardId", editOwnerIdentity.getText().toString());
            jsonObject.put("Phone1", editOwnerPhone1.getText().toString());
            jsonObject.put("Phone2", editOwnerPhone2.getText().toString());
            jsonObject.put("ResidentAddress", editOwnerCurrentAddress.getText().toString());
            jsonObject.put("Remark", editRemarks.getText().toString());
            jsonObject.put("VEHICLETYPE", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
            mLog.e("jsonObject="+jsonObject.toString());
            jsonObject.put("PhotoListFile", JA);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_EDITELECTRICCAR, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                mLog.e(result==null?"信息变更接口返回null":"信息变更接口返回");
                if (result != null) {
                    mLog.e("信息变更"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(1, "车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息编辑成功！");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            //AppManager.getAppManager().finishActivity(HomeActivity.class);
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(ChangeSecondActivity.this, LoginActivity.class);
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
                    editOwnerIdentity.setText(data.getStringExtra("card"));
                    editOwnerName.setText(data.getStringExtra("name"));
                    editOwnerCurrentAddress.setText(data.getStringExtra("address"));
                    Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra("img"));
                    if (bitmap == null) {
                        Utils.myToast(mContext, "SD写入问题无法获取图片");
                    } else {
                        bitmap = Utils.thumbnailBitmap(bitmap);
                        SharedPreferencesUtils.put("identity", Utils.Bitmap2Bytes(bitmap));
                    }
                }
                break;
            case PhotoUtils.CAMERA_REQESTCODE:
//                int degree = PhotoUtils.readPictureDegree(PhotoUtils.imageFile.getAbsolutePath());
//                Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
//                        PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile, 480, 620));
//                String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
//                String path = PhotoUtils.imageFile.getPath();
//                String name = Utils.getFileName(path);
//                Drawable bd = new BitmapDrawable(bitmap);
//                switch (name) {
//                    case "identity":
//                        relativeIdentity.setBackground(bd);
//                        SharedPreferencesUtils.put("identity", photoStr);
//                        textIdentity.setTextColor(getResources().getColor(R.color.white));
//                        break;
//                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CARDTYPE, mCardTypeId);
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.OWNERNAME, editOwnerName.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.IDENTITY, editOwnerIdentity.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE1, editOwnerPhone1.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.PHONE2, editOwnerPhone2.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.CURRENTADDRESS, editOwnerCurrentAddress.getText().toString());
        VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.REMARK, editRemarks.getText().toString());
        finish();
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
            Utils.myToast(mContext, "请输入车主证件号码");
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
            Utils.myToast(mContext, "至少输入一个联系方式");
            return false;
        }
        if (!locCityName.contains("昆明")) {
            String address = editOwnerCurrentAddress.getText().toString().trim();
            if (address.equals("")) {
                Utils.myToast(mContext, "请输入车主现住址");
                return false;
            }
        }
        return true;
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
                break;
            default:
                break;
        }
    }
}
