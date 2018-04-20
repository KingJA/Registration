package com.tdr.registration.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ColorAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.CarLabel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.model.TransferModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.PermissionUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;



import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆过户
 */
public class CarTransferActivity extends BaseActivity implements LGImgCompressor.CompressListener, AdapterView.OnItemClickListener {
    private static final String TAG = "CarTransferActivity";
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_PlateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_brand)
    TextView textBrand;
    @BindView(R.id.text_color)
    TextView textColor;
    @BindView(R.id.text_owner)
    TextView textOwner;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.text_identityShow)
    TextView textIdentityShow;
    @BindView(R.id.edit_ownerName)
    EditText editOwnerName;
    @BindView(R.id.edit_ownerIdentity)
    EditText editOwnerIdentity;
    @BindView(R.id.relative_identity)
    RelativeLayout relativeIdentity;
    @BindView(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @BindView(R.id.edit_ownerPhone1)
    EditText editOwnerPhone1;
    @BindView(R.id.edit_ownerPhone2)
    EditText editOwnerPhone2;
    @BindView(R.id.edit_ownerCurrentAddress)
    EditText editOwnerCurrentAddress;
    @BindView(R.id.edit_reason)
    EditText editReason;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.text_cardType)
    TextView textCardType;
    private Activity mActivity;
    private Context mContext;
    private ElectricCarModel model = new ElectricCarModel();

    private String[] permissionArray = new String[]{Manifest.permission.CAMERA};


    private ZProgressHUD mProgressHUD;

    private ColorAdapter cardTypeAdapter;
    private List<SortModel> cardTypeList = new ArrayList<SortModel>();
    private List<BikeCode> cardList = new ArrayList<BikeCode>();
    private HashMap<Integer, Integer> cardTypeMap = new HashMap<Integer, Integer>();
    private String mCardType = "";
    private String mCardTypeId = "";

    private DbManager db;

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;// 根据拼音来排列ListView里面的数据类
    private String city;
    private String IsTransferReserve;
    private Gson mGson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartransfer);
        ButterKnife.bind(this);
        mContext = this;
        mActivity=this;
        mGson=new Gson();
        db = x.getDb(DBUtils.getDb());
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        initData();
    }

    private void initData() {
        cardTypeAdapter = new ColorAdapter(mContext, cardTypeList,
                cardTypeMap, R.layout.brand_item,
                new String[]{"color_name"},
                new int[]{R.id.text_brandname});
        try {
            cardList=  db.selector(BikeCode.class).where("type","=","6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(cardList==null){
            cardList=new ArrayList<BikeCode>();
        }
        Log.e("cardlist：", "" + cardList.size());
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

        textTitle.setText("车辆过户");
        editOwnerIdentity.setTransformationMethod(new AllCapTransformationMethod(true));
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.model = (ElectricCarModel) bundle.getSerializable("model");
        }
        if(!city.contains("昆明")){
            textOwnerIdentity.setText("证件照");
        }
        textPlateNumber.setText(model.getPlateNumber());
        textBrand.setText(model.getVehicleBrandName());
        textColor.setText(model.getColorName());
        textOwner.setText(model.getOwnerName());
        textPhone.setText(model.getPhone1());
        textIdentityShow.setText(model.getCardId());
        IsTransferReserve= (String)SharedPreferencesUtils.get("IsTransferReserve", "");
        if(IsTransferReserve.equals("1")){
            QueryCar(model.getPlateNumber());
        }
    }
    private void QueryCar(String plateNumber){
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlateNumber",plateNumber);
        JSONObject JB = new JSONObject(map);

        String a="http://hrb.test.iotone.cn/api/TransferAppointment/GetModelByPlateNumber";
        String http=(String) SharedPreferencesUtils.get("httpUrl", "")+Constants.HTTP_GetModelByPlateNumber;
        mLog.e(a.equals(http)?"T":"F");
        mLog.e("长度="+a.length()+"A="+a);
        mLog.e("长度="+http.length()+"B="+http);
        mLog.e("A1="+a.charAt(0)+"=AL="+a.charAt(a.length()-1));
        mLog.e("B1="+http.charAt(0)+"=BL="+http.charAt(http.length()-1));

        RequestParams RP = new RequestParams(http.trim());
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("-------------过户预约:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        mLog.e("过户预约:" + result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                TransferModel CL = mGson.fromJson(data, new TypeToken<TransferModel>() {
                                }.getType());
                                editOwnerName.setText(CL.getOwnerName());
                                for (SortModel sortModel : cardTypeList) {
                                    if(sortModel.getGuid().equals(CL.getCardType())){
                                        mCardTypeId=sortModel.getGuid();
                                        mCardType=sortModel.getName();
                                        textCardType.setText(mCardType);
                                    }
                                }
                                editOwnerIdentity.setText(CL.getCardId());
                                editOwnerPhone1.setText(CL.getPhone());
                                editReason.setText(CL.getTransfer_Reason());

                            }else{
                                Utils.showToast(data);
                            }
                            mProgressHUD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressHUD.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressHUD.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressHUD.dismiss();
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }

    @OnClick({R.id.image_back, R.id.relative_identity, R.id.btn_submit, R.id.text_cardType})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;
            case R.id.relative_identity:
                PermissionUtils.checkPermissionArray(CarTransferActivity.this, permissionArray, PermissionUtils.PERMISSION_REQUEST_CODE);
                PhotoUtils.TakePicture(mActivity,"identity");
                break;
            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                dialogShow(0, "");
                break;

            case R.id.text_cardType:
                dialogShow(2, "");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtil.goActivityAndFinish(CarTransferActivity.this, HomeActivity.class);
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(final int flag, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle("提示").withTitleColor("#333333").withMessage("确认过户该车辆？")
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
                    SharedPreferencesUtils.put("guleCar", "");
                    SharedPreferencesUtils.put("guleBattery", "");
                    SharedPreferencesUtils.put("labelA", "");
                    SharedPreferencesUtils.put("labelB", "");
                    SharedPreferencesUtils.put("plateNum", "");
                    SharedPreferencesUtils.put("identity", "");
                    SharedPreferencesUtils.put("applicationForm", "");
                    SharedPreferencesUtils.put("invoice", "");
                    SharedPreferencesUtils.put("install", "");
                    ActivityUtil.goActivityAndFinish(CarTransferActivity.this, HomeActivity.class);
                }
            }).show();
        } else if (flag == 2) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            LayoutInflater mInflater = LayoutInflater.from(this);
            View color_view = mInflater.inflate(R.layout.layout_color, null);
            ListView list_colors1 = (ListView) color_view
                    .findViewById(R.id.list_colors1);
            list_colors1.setOnItemClickListener(this);
            list_colors1.setAdapter(cardTypeAdapter);

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
        try {
            jsonObject.put("ECID", model.getEcId());
            jsonObject.put("Photo2File", SharedPreferencesUtils.get("identity", ""));
            jsonObject.put("CARDTYPE", mCardTypeId);
            jsonObject.put("ColorId2", "");
            jsonObject.put("OwnerName", editOwnerName.getText().toString().trim());
            jsonObject.put("CardId", editOwnerIdentity.getText().toString().trim());
            jsonObject.put("Phone1", editOwnerPhone1.getText().toString().trim());
            jsonObject.put("Phone2", editOwnerPhone2.getText().toString().trim());
            jsonObject.put("ResidentAddress", editOwnerCurrentAddress.getText().toString().trim());
            jsonObject.put("TransactMan", model.getTRANSACTMAN());
            jsonObject.put("Reason", editReason.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity,(String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_ELECTRICCARTRANSFER, map, new WebServiceUtils.WebServiceCallBack() {
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
                            dialogShow(1, "车牌号：" + model.getPlateNumber() + "  电动车信息过户成功！");
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            SharedPreferencesUtils.put("token","");
                            ActivityUtil.goActivityAndFinish(CarTransferActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(mContext, data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.PERMISSION_REQUEST_CODE:
                if (PermissionUtils.verifyPermissions(grantResults)) {

                } else {
                    Toast.makeText(this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarTransferActivity.this);
                    builder.setTitle("帮助");
                    builder.setMessage("当前应用缺少必要权限。点击设置打开权限设置页。");

                    // 拒绝, 退出应用
                    builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //setResult(PERMISSIONS_DENIED);
                            //finish();
                        }
                    });

                    builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    });

                    builder.setCancelable(false);

                    builder.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private static final String PACKAGE_URL_SCHEME = "package:";

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtils.PERMISSION_SETTING_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {

                } else {
                    Toast.makeText(this, "not has setting permission", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        } else if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                LGImgCompressor.getInstance(this).withListener(this).
                        starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
            }
        }

    }

    @Override
    public void onCompressStart() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCompressEnd(LGImgCompressor.CompressResult compressResult) {
        if (compressResult.getStatus() == LGImgCompressor.CompressResult.RESULT_ERROR)//压缩失败
            return;

        File file = new File(compressResult.getOutPath());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            Drawable drawable = new BitmapDrawable(bitmap);
            String path = file.getPath();
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            if (path.contains("identity")) {
                relativeIdentity.setBackground(drawable);
                SharedPreferencesUtils.put("identity", photoStr);
                textOwnerIdentity.setTextColor(getResources().getColor(R.color.white));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkData() {
        String ownerName = editOwnerName.getText().toString().trim();
        if (ownerName.equals("")) {
            Utils.myToast(mContext, "请输入车主姓名");
            return false;
        }

        String textcardtype = textCardType.getText().toString().trim();
        mLog.e("textcardtype="+textcardtype);
            if (textcardtype.equals("")) {
                Utils.myToast(mContext, "请选择证件类型");
                return false;
            }
        String ownerIdentity = editOwnerIdentity.getText().toString().trim();
        mLog.e("mCardTypeId="+mCardTypeId+"  ownerIdentity="+ownerIdentity);

            if(ownerIdentity.equals("")){
                Utils.myToast(mContext, "请输入证件号");
                return false;
            }else{
                if (mCardTypeId.equals("1")) {//如果车主证件类型为身份证（1）
                    if (!Utils.isIDCard18(ownerIdentity)) {
                        Utils.myToast(mContext, "输入的身份证号码格式有误");
                        return false;
                    }
                }
            }


        if (ownerIdentity.equals(model.getCardId())) {
            Utils.myToast(mContext, "原车主与新车主身份证不能相同");
            return false;
        }
        String identityStr = (String) SharedPreferencesUtils.get("identity", "");
        if (identityStr.equals("")) {

            Utils.myToast(mContext, "请拍摄"+textOwnerIdentity.getText().toString().trim()+"照");
            return false;
        }
        String phone1 = editOwnerPhone1.getText().toString().trim();
        if (phone1.equals("")) {
            Utils.myToast(mContext, "请输入联系方式1");
            return false;
        }
//        String ownerCurrentAddress = editOwnerCurrentAddress.getText().toString().trim();
//        if (ownerCurrentAddress.equals("")) {
//            Utils.myToast(mContext, "请输入车主现住址");
//            return false;
//        }

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
