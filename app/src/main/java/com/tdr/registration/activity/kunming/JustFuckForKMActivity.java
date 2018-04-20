package com.tdr.registration.activity.kunming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.tdr.registration.R;
import com.tdr.registration.activity.BrandActivity;
import com.tdr.registration.activity.HomeActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 昆明预登记
 */

public class JustFuckForKMActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final static int CAMERA = 1991;//二维码扫描回调

    private final static int BRAND_CODE = 2016;//品牌回调

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.image_scan)
    ImageView imageScan;
    @BindView(R.id.group_vehicletype)
    RadioGroup groupVehicletype;
    @BindView(R.id.rb_modelOne)
    RadioButton rbModelOne;
    @BindView(R.id.rb_modelTwo)
    RadioButton rbModelTwo;
    @BindView(R.id.text_cardType)
    TextView textCardType;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.edit_ownerName)
    EditText editOwnerName;
    @BindView(R.id.edit_ownerPhone1)
    EditText editOwnerPhone1;
    @BindView(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @BindView(R.id.linear_vehicleBrand)
    LinearLayout linearVehicleBrand;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.relative_vehicleBrand)
    RelativeLayout relativeVehicleBrand;
    @BindView(R.id.edit_frame)
    EditText editFrame;
    @BindView(R.id.text_motor)
    TextView textMotor;
    @BindView(R.id.edit_motor)
    EditText editMotor;
    @BindView(R.id.linear_vehicleColor)
    LinearLayout linearVehicleColor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.relative_vehicleColor)
    RelativeLayout relativeVehicleColor;
    @BindView(R.id.linear_modelOne)
    LinearLayout linearModelOne;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.linear_plateNumber)
    LinearLayout linearPlateNumber;
    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.image_scanPlateNumber)
    ImageView imageScanPlateNumber;
    @BindView(R.id.relative_applicationForm)
    ImageView relativeApplicationForm;
    @BindView(R.id.text_applicationForm)
    TextView textApplicationForm;
    @BindView(R.id.relative_plateNum)
    ImageView relativePlateNum;
    @BindView(R.id.text_plateNum)
    TextView textPlateNum;
    @BindView(R.id.relative_identity)
    ImageView relativeIdentity;
    @BindView(R.id.text_identity)
    TextView textIdentity;
    @BindView(R.id.linear_modelTwo)
    LinearLayout linearModelTwo;
    @BindView(R.id.text_showOwnerPhone1)
    TextView textShowOwnerPhone1;
    @BindView(R.id.text_labelA)
    TextView textLabelA;
    @BindView(R.id.relative_labelB)
    ImageView relativeLabelB;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Intent intent;
    private Activity mActivity;
    private Context mContext;


    private ColorAdapter colorsAdapter;// 颜色适配器
    private List<SortModel> colorsList = new ArrayList<SortModel>();// 颜色列表
    private HashMap<Integer, Integer> colorsMap = new HashMap<Integer, Integer>();
    private List<BikeCode> colorList = new ArrayList<BikeCode>();
    private String mColor1 = "";// 颜色1
    private String mColorId1 = "";// 颜色1的ID
    private String mColor2 = "";// 颜色2
    private String mColorId2 = "";// 颜色2的ID
    private String brandCode = "";//品牌code
    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private String mCardType = "";
    private String mCardTypeId = "";

    private DbManager db;
    private CharacterParser characterParser;

    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类

    private String city = "";//当前帐号所在市
    private String carType = "1";//车辆类型

    private String isConfirm = "0";//来历承诺书

    private List<ElectricCarModel> electricCarModelList = new ArrayList<>();
    private ElectricCarModel model;
    private String activity = "";
    private String listId = "";
    private String insurance = "";
    private String ecId = "";

    private TimePickerView timePickerView;

    private ZProgressHUD mProgressHUD;

    private List<UploadInsuranceModel> models = new ArrayList<>();
    private ArrayList list;

    private String modelWay = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justfuckforkm);
        ButterKnife.bind(this);
        mActivity=this;
        mContext = this;
        Bundle bundle = (Bundle) getIntent().getExtras();

        db = x.getDb(DBUtils.getDb());
        intent = new Intent();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        initView();
    }

    private void initView() {
        textTitle.setText("车辆预登记");
        textCardType.setText("身份证");
        mCardTypeId = "1";
        imageScan.setVisibility(View.VISIBLE);
        imageScan.setBackgroundResource(R.mipmap.ocr_scan);
        groupVehicletype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbModelOne.getId()) {
                    linearModelOne.setVisibility(View.VISIBLE);
                    linearModelTwo.setVisibility(View.GONE);
                    modelWay = "1";
                } else {
                    linearModelTwo.setVisibility(View.VISIBLE);
                    linearModelOne.setVisibility(View.GONE);
                    modelWay = "2";
                }
            }
        });
        mProgressHUD = new ZProgressHUD(mContext);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
    }

    @OnClick({R.id.image_back, R.id.image_scan, R.id.text_cardType, R.id.text_vehicleBrand, R.id.text_vehicleColor, R.id.relative_applicationForm, R.id.relative_plateNum, R.id.relative_identity, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.image_scan:
                ActivityUtil.goActivityForResult(JustFuckForKMActivity.this, ACamera.class, CAMERA);
                break;
            case R.id.text_cardType:
                dialogShow(2, "");
                break;
            case R.id.text_vehicleBrand:
                intent.setClass(JustFuckForKMActivity.this, BrandActivity.class);
                startActivityForResult(intent, BRAND_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.text_vehicleColor:
                dialogShow(1, "");
                break;
            case R.id.relative_applicationForm:
                PhotoUtils.TakePicture(mActivity,"applicationForm");
                break;
            case R.id.relative_plateNum:
                PhotoUtils.TakePicture(mActivity,"plateNum");
                break;
            case R.id.relative_identity:
                PhotoUtils.TakePicture(mActivity,"identity");
                break;
            case R.id.btn_next:
                if (!checkData()) {
                    break;
                }
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (modelWay.equals("1")) {
                jsonObject.put("VEHICLETYPE",VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
                jsonObject.put("CARTYPE", "1");
                jsonObject.put("VEHICLEBRAND", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND));
                jsonObject.put("PlateNumber", editPlateNumber.getText().toString());
                jsonObject.put("SHELVESNO", editFrame.getText().toString().toUpperCase());
                jsonObject.put("ENGINENO", editMotor.getText().toString().toUpperCase());
                jsonObject.put("COLORID", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID));
                jsonObject.put("COLORID2", VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR2ID));
                jsonObject.put("BUYDATE", "2015-01-01");
                jsonObject.put("CARDTYPE", mCardTypeId);
                jsonObject.put("OWNERNAME", editOwnerName.getText().toString());
                jsonObject.put("CARDID", editOwnerIdentity.getText().toString().toUpperCase().trim());
                jsonObject.put("PHONE1", editOwnerPhone1.getText().toString().trim());
                jsonObject.put("PHONE2", editOwnerPhone2.getText().toString().trim());
                jsonObject.put("ADDRESS", "");
                jsonObject.put("REMARK", "");
            } else if (modelWay.equals("2")) {
                jsonObject.put("VEHICLETYPE", "1");
                jsonObject.put("CARTYPE", "1");
                jsonObject.put("VEHICLEBRAND", "");
                jsonObject.put("PlateNumber", editPlateNumber.getText().toString());
                jsonObject.put("SHELVESNO", "");
                jsonObject.put("ENGINENO", "");
                jsonObject.put("COLORID", "");
                jsonObject.put("COLORID2", "");
                jsonObject.put("BUYDATE", "2015-01-01");
                jsonObject.put("CARDTYPE", "");
                jsonObject.put("OWNERNAME", "");
                jsonObject.put("CARDID", "");
                jsonObject.put("PHONE1", "");
                jsonObject.put("PHONE2", "");
                jsonObject.put("ADDRESS", "");
                jsonObject.put("REMARK", "");
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject5 = new JSONObject();
                JSONObject jsonObject6 = new JSONObject();
                JSONObject jsonObject7 = new JSONObject();
                jsonObject5.put("INDEX", "5");
                jsonObject5.put("Photo", "");
                jsonObject5.put("PhotoFile", SharedPreferencesUtils.get("applicationForm", ""));
                jsonObject6.put("INDEX", "6");
                jsonObject6.put("Photo", "");
                jsonObject6.put("PhotoFile", SharedPreferencesUtils.get("plateNum", ""));
                jsonObject7.put("INDEX", "7");
                jsonObject7.put("Photo", "");
                jsonObject7.put("PhotoFile", SharedPreferencesUtils.get("identity", ""));
                jsonArray.put(jsonObject5);
                jsonArray.put(jsonObject6);
                jsonArray.put(jsonObject7);
                jsonObject.put("PhotoListFile", jsonArray);
            }
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
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        int errorCode = json.getInt("ErrorCode");
                        String data = json.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            dialogShow(0, "车辆预登记成功！");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(JustFuckForKMActivity.this, LoginActivity.class);
                        } else {
                            Utils.myToast(mContext, data);
                            mProgressHUD.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressHUD.dismiss();
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

    private boolean checkData() {
        if (modelWay.equals("1")) {
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
                Utils.myToast(mContext, "请输入联系方式");
                return false;
            }else{
                if(!phone1.substring(0,1).equals("1")){
                    Utils.myToast(mContext, "请输入正确的手机号码");
                    return false;
                }
                if(phone1.length()!=11){
                    Utils.myToast(mContext, "输入的手机号码长度不符");
                    return false;
                }
            }

            String brand = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND);
            if (brand.equals("") || brand == null) {
                Utils.myToast(mContext, "请选择车辆品牌");
                return false;
            }
            String colorId = VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.COLOR1ID);
            if (colorId.equals("") || colorId == null) {
                Utils.myToast(mContext, "请选择电动车颜色");
                return false;
            }
        } else {
            String plateNum = editPlateNumber.getText().toString().toUpperCase().trim();
            if (plateNum.equals("") || plateNum == null) {
                Utils.myToast(mContext, "请输入车牌");
                return false;
            }
            Pattern pattern = Pattern.compile((String) SharedPreferencesUtils.get("regular1", ""));
            Matcher matcher = pattern.matcher(plateNum + "");
            if (!matcher.matches()) {
                Utils.myToast(mContext, "输入的车牌有误，请重新确认");
                return false;
            }
            String labelBStr = (String) SharedPreferencesUtils.get("applicationForm", "");
            if (labelBStr.equals("")) {
                Utils.myToast(mContext, "请拍摄前侧车身的照片");
                return false;
            }
            String plateNumStr = (String) SharedPreferencesUtils.get("plateNum", "");
            if (plateNumStr.equals("")) {
                Utils.myToast(mContext, "请拍摄后侧车身的照片");
                return false;
            }
            if (carType.equals("")) {
                Utils.myToast(mContext, "请选择车辆类型");
                return false;
            }
            if (isConfirm.equals("")) {
                Utils.myToast(mContext, "请选择是否有来历承诺书");
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
                    VehiclesStorageUtils.clearData();
                    ActivityUtil.goActivityAndFinish(JustFuckForKMActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 1) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            colorsList.clear();
            colorsAdapter = new ColorAdapter(mContext, colorsList,
                    colorsMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(colorsAdapter);
//            colorList = db.findAllByWhere(BikeCode.class, " type='4'");
            try {
                colorList = db.selector(BikeCode.class).where("type", "=", "4").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(colorList==null){
                colorList=new ArrayList<BikeCode>();
            }
            Log.e("颜色数量：", "" + colorList.size());
            for (int i = 0; i < colorList.size(); i++) {
                SortModel sortModel = new SortModel();
                sortModel.setGuid(colorList.get(i).getCode());
                sortModel.setName(colorList.get(i).getName());
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(colorList.get(i)
                        .getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    sortModel.setSortLetters(sortString.toUpperCase());
                } else {
                    sortModel.setSortLetters("#");
                }
                colorsList.add(sortModel);
            }

            colorsAdapter.notifyDataSetChanged();

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mContext);
            dialogBuilder.withTitle("颜色列表").withTitleColor("#333333")
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
                    textVehicleColor.setText(mColor1);
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.COLOR1ID, mColorId1);
                    System.out.println("----------------选中颜色:"
                            + mColorId1);
                }
            }).show();
        } else if (flag == 2) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color2, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors2);
            list_colors1.setOnItemClickListener(this);
            cardTypeList.clear();
            cardTypeAdapter = new ColorAdapter(mContext, cardTypeList,
                    cardTypeMap, R.layout.brand_item,
                    new String[]{"color_name"},
                    new int[]{R.id.text_brandname});
            list_colors1.setAdapter(cardTypeAdapter);
//            cardList = db.findAllByWhere(BikeCode.class, " type='6'");
            try {
                cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
            } catch (DbException e) {
                e.printStackTrace();
            }
            if(cardList==null){
                cardList=new ArrayList<BikeCode>();
            }
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
                    if (mCardTypeId.equals("1")) {
                        imageScan.setVisibility(View.VISIBLE);
                    } else {
                        imageScan.setVisibility(View.GONE);
                    }
                }
            }).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                int degree = PhotoUtils.readPictureDegree();
                Bitmap bitmap = PhotoUtils.rotaingImageView(degree,
                        PhotoUtils.getBitmapFromFile(PhotoUtils.imageFile));
                String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
                String path = PhotoUtils.imageFile.getPath();
                String name = Utils.getFileName(path);
                Drawable bd = new BitmapDrawable(bitmap);
                switch (name) {
                    case "plateNum":
                        relativePlateNum.setBackground(bd);
                        SharedPreferencesUtils.put("plateNum", photoStr);
                        textPlateNum.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "applicationForm":
                        relativeApplicationForm.setBackground(bd);
                        SharedPreferencesUtils.put("applicationForm", photoStr);
                        textApplicationForm.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case "identity":
                        relativeIdentity.setBackground(bd);
                        SharedPreferencesUtils.put("identity", photoStr);
                        textIdentity.setTextColor(getResources().getColor(R.color.white));
                        break;

                }
            }
        } else if (requestCode == BRAND_CODE) {
            if (resultCode == RESULT_OK) {
                String brandName = data.getStringExtra("brandName");
                textVehicleBrand.setText(brandName);
                brandCode = data.getStringExtra("brandCode");
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRAND, brandCode);
                // 储显示的车辆品牌
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLEBRANDNAME, brandName);
            }
        } else if (requestCode == CAMERA) {
            if (RESULT_OK == resultCode) {
                String identity = data.getStringExtra("card");
                editOwnerIdentity.setText(identity);
                String name = data.getStringExtra("name");
                editOwnerName.setText(name);
                Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra("img"));
                if (bitmap == null) {
                    Utils.myToast(mContext, "SD写入问题无法获取图片");
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(JustFuckForKMActivity.this, HomeActivity.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list_colors1:
                colorsMap.clear();
                colorsMap.put(position, 100);
                colorsAdapter.notifyDataSetChanged();
                mColor1 = colorsList.get(position).getName();
                mColorId1 = colorsList.get(position).getGuid();
                break;
            case R.id.list_colors2:
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
