package com.tdr.registration.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.tdr.registration.R;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.imagecompress.LGImgCompressor;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AllCapTransformationMethod;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆补办
 */
public class CarReissueActivity2 extends BaseActivity implements LGImgCompressor.CompressListener {
    private static final String TAG = "CarReissueActivity";
    @BindView(R.id.edit_labelA)
    EditText editLabelA;
    @BindView(R.id.edit_labelB)
    EditText editLabelB;
    private String[] permissionArray = new String[]{Manifest.permission.CAMERA};
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
    @BindView(R.id.check_labelA)
    CheckBox checkLabelA;
    @BindView(R.id.check_labelB)
    CheckBox checkLabelB;
    @BindView(R.id.check_plateNum)
    CheckBox checkPlateNum;

    @BindView(R.id.LL_plateNumber)
    LinearLayout LL_plateNumber;

    @BindView(R.id.LL_labelA)
    LinearLayout LL_labelA;

    @BindView(R.id.LL_labelB)
    LinearLayout LL_labelB;


    @BindView(R.id.edit_plateNumber)
    EditText editPlateNumber;
    @BindView(R.id.edit_remarks)
    EditText editRemarks;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @BindView(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;
    private Context mContext;
    private Activity mActivity;
    private ElectricCarModel model = new ElectricCarModel();
    private ZProgressHUD mProgressHUD;
    private int changeType = 0;
    private String city = "";
    private String ChangeType[];
    private Gson mGson;
    private List<PayInsurance> PIlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_reissue2);
        ButterKnife.bind(this);
        GetReady();
        SetPhotoList();
        initView();
        Utils.ClearData();
        initData();
    }

    private DbManager db;
    private List<PhotoListInfo> PLI;
    private PhotoListAdapter PLA;
    private String PhotoConfig = "";
    private LinearLayoutManager linearLayoutManager;

    /**
     * 准备基础数据
     */
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
        mActivity = this;
        mContext = this;
        city = (String) SharedPreferencesUtils.get("locCityName", "");
//        List<BaseInfo> ResultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + city + "\"");
        List<BaseInfo> ResultList = null;
        try {
            ResultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (ResultList == null) {
            ResultList = new ArrayList<BaseInfo>();
        }
        BaseInfo BI = ResultList.get(0);
        PLI = new ArrayList<PhotoListInfo>();
        PhotoConfig = BI.getPhotoConfig();
        mLog.e("PhotoConfig:" + PhotoConfig);
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
//                pli.setRequire(JB.getBoolean("IsRequire"));
                pli.setRequire(false);
                if (pli.isValid()) {
                    PLI.add(pli);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载图片列表
     */
    private void SetPhotoList() {
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RV_PhotoList.setLayoutManager(linearLayoutManager);

        RV_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());

        PLA = new PhotoListAdapter(mActivity, PLI);
//        PLA=new PhotoListAdapter(mActivity,PLI,DrawableList);
        RV_PhotoList.smoothScrollToPosition(PLI.size());
        RV_PhotoList.smoothScrollToPosition(0);
        mLog.e("----------smoothScrollToPosition---------");
        PLA.setOnItemClickLitener(new PhotoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String PhotoName = "Photo:" + PLI.get(position).getINDEX() + ":" + position;
                if (PhotoUtils.CurrentapiVersion > 20) {
                    PhotoUtils.TakePicture(mActivity, PhotoName);
                } else {
                    PhotoUtils.TakePicture2(mActivity, PhotoName);
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(mActivity, position + " long click",Toast.LENGTH_SHORT).show();

            }
        });
        RV_PhotoList.setAdapter(PLA);
    }

    private void initView() {
        mGson = new Gson();
        textTitle.setText("车辆补办");
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        if (city.contains("天津")) {
            editPlateNumber.setHint("请输入电动自行车车牌");
        }
        String changetype = (String) SharedPreferencesUtils.get("ChangeType", "1,2,4");
        mLog.e("changetype:" + changetype);
        ChangeType = changetype.split(",");
        mLog.e("ChangeType" + ChangeType);
        //1.车牌2.车辆标签4.电池标签
        if (!check("1")) {
            checkPlateNum.setVisibility(View.GONE);
            LL_plateNumber.setVisibility(View.GONE);
        }
        if (!check("2")) {
            checkLabelA.setVisibility(View.GONE);
            LL_labelA.setVisibility(View.GONE);
        }
        if (!check("4")) {
            checkLabelB.setVisibility(View.GONE);
            LL_labelB.setVisibility(View.GONE);
        }
        editPlateNumber.setTransformationMethod(new AllCapTransformationMethod(true));
        checkPlateNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 1;
                    editPlateNumber.setEnabled(true);
                } else {
                    changeType -= 1;
                    editPlateNumber.setEnabled(false);
                    editPlateNumber.setText("");
                }
            }
        });

        checkLabelA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 2;
                    editLabelA.setEnabled(true);
                } else {
                    changeType -= 2;
                    editLabelA.setEnabled(false);
                    editLabelA.setText("");
                }
            }
        });

        checkLabelB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeType += 4;
                    editLabelB.setEnabled(true);
                } else {
                    changeType -= 4;
                    editLabelB.setEnabled(false);
                    editLabelB.setText("");
                }
            }
        });
    }

    private boolean check(String in) {
        for (String s : ChangeType) {
            if (s.equals(in)) {
                return true;
            }
        }
        return false;
    }

    private void initData() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.model = (ElectricCarModel) bundle.getSerializable("model");
        }
        Logger.d("HasRFID:"+model.getHasRFID());
        if ("0".equals(model.getHasRFID())) {
            checkLabelA.setVisibility(View.GONE);
            checkLabelB.setVisibility(View.GONE);
            LL_labelA.setVisibility(View.GONE);
            LL_labelB.setVisibility(View.GONE);
        }
        textPlateNumber.setText(model.getPlateNumber());
        textBrand.setText(model.getVehicleBrandName());
        textOwner.setText(model.getOwnerName());
        textPhone.setText(model.getPhone1());
        textIdentityShow.setText(model.getCardId());
        textColor.setText(model.getColorName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtils.CAMERA_REQESTCODE) {
            if (resultCode == RESULT_OK) {
                if (PhotoUtils.CurrentapiVersion > 20) {
                    LGImgCompressor.getInstance(this).withListener(this).
                            starCompress(Uri.fromFile(PhotoUtils.imageFile).toString(), 480, 600, 100);
                } else {
                    GetPhoto(data);
                }
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
        if (PhotoUtils.CurrentapiVersion > 20) {
            UpDatePhotoItem(new File(compressResult.getOutPath()));
        } else {
            UpDatePhotoItem2(new File(compressResult.getOutPath()));
        }
    }

    private void GetPhoto(Intent data) {
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
        PhotoUtils.sevephoto(bitmap);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        LGImgCompressor.getInstance(this).withListener(this).
                starCompress(uri.toString(), 480, 600, 100);
    }

    private void UpDatePhotoItem(File file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            Drawable drawable = new BitmapDrawable(bitmap);
            String path = file.getPath();
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            mLog.e(file.getName() + "  path=" + path);
            String PhotoName = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
            mLog.e("PhotoName=" + PhotoName);
            String Photoindex[] = PhotoName.split(":");
            mLog.e("Position=" + Photoindex[2] + "  Index=" + Photoindex[1]);
            PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
            my.Photo.setBackgroundDrawable(drawable);
            PLA.SevePhoto(Photoindex[1], photoStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void UpDatePhotoItem2(File file) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
            String photoStr = Utils.Byte2Str(Utils.Bitmap2Bytes(bitmap));
            Drawable drawable = new BitmapDrawable(bitmap);
            String Photoindex[] = PhotoUtils.mPicName.split(":");
            PhotoListAdapter.MyViewHolder my = (PhotoListAdapter.MyViewHolder) RV_PhotoList.findViewHolderForAdapterPosition(Integer.parseInt(Photoindex[2]));
            my.Photo.setBackgroundDrawable(drawable);
            my.PhotoName.setTextColor(Color.WHITE);
            PhotoUtils.sevephoto(bitmap);
            PLA.SevePhoto(Photoindex[1], photoStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.image_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                onBackPressed();
                break;

            case R.id.btn_submit:
                if (!checkData()) {
                    break;
                }
                sendMsg();
                break;
        }
    }

    /**
     * 补办信息上传
     */
    private void sendMsg() {
        mProgressHUD.show();
        JSONObject jsonObject = new JSONObject();
        JSONArray JA = new JSONArray();
        JSONObject JB;
        String index;
        String PhotoFile;
        try {
            for (int i = 0; i < PLI.size(); i++) {
                mLog.e("========" + PLA.checkItemDate2(i));
                if (PLA.checkItemDate2(i)) {
                    index = PLI.get(i).getINDEX();
                    PhotoFile = (String) SharedPreferencesUtils.get("Photo:" + index, "");
                    JB = new JSONObject();
                    JB.put("INDEX", index);
                    JB.put("Photo", "");
                    JB.put("PhotoFile", PhotoFile);
                    JA.put(JB);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put("EcId", model.getEcId());
            jsonObject.put("CHANGETYPE", changeType);
            jsonObject.put("PlateNumber", editPlateNumber.getText().toString().toUpperCase().trim());
            jsonObject.put("Remark", editRemarks.getText().toString());
            jsonObject.put("OwnerName", model.getOwnerName());
            jsonObject.put("CardId", model.getCardId());
            jsonObject.put("VehicleBrand", model.getVehicleBrandName());
            jsonObject.put("ColorId", model.getCardId());
            jsonObject.put("Phone1", model.getPhone1());
            jsonObject.put("ResidentAddress", model.getResidentAddress());
            if (checkLabelA.isChecked()) {
                String labelA = editLabelA.getText().toString().trim();
                jsonObject.put("THEFTNO1", labelA);
            }
            if (checkLabelB.isChecked()) {
                String labelB = editLabelB.getText().toString().trim();
                jsonObject.put("THEFTNO2", labelB);
            }
            jsonObject.put("Photo1File", "");
            jsonObject.put("Photo2File", "");
            jsonObject.put("Photo3File", "");
            jsonObject.put("Photo4File", "");
            jsonObject.put("PhotoListFile", JA);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("infoJsonStr", jsonObject.toString());
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHANGEPLATENUMBER, map, new WebServiceUtils.WebServiceCallBack() {


            @Override
            public void callBack(String result) {
                if (result != null) try {
                    mLog.e("更换车牌："+result);
                    JSONObject json = new JSONObject(result);
                    int errorCode = json.getInt("ErrorCode");
                    String data = json.getString("Data");
                    if (errorCode == 0) {
                        int D=checkJson(data);
                        if ( D== 1) {
                            PayData msg = mGson.fromJson(data, new TypeToken<PayData>() {
                            }.getType());
                            if (msg.getPIList().size() > 0) {
                                PIlist=new ArrayList<>();
                                PIlist = msg.getPIList();
                                dialogShow(1, "车牌补办成功", msg.getMsg());
                            } else {
                                dialogShow(0, "车牌补办成功", msg.getMsg());
                            }
                        }else if( D== 0){
                            dialogShow(0, "车牌补办成功", "");
                        } else {
                            dialogShow(0, "车牌补办成功", data);
                        }
                    } else if (errorCode == 1) {
                        Utils.myToast(mContext, data);
                        SharedPreferencesUtils.put("token", "");
                        ActivityUtil.goActivityAndFinish(CarReissueActivity2.this, LoginActivity.class);
                    } else {
                        Utils.myToast(mContext, data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.myToast(mContext, "JSON解析出错");
                }
                else {
                    Utils.myToast(mContext, "获取数据超时，请检查网络连接");
                }
                mProgressHUD.dismiss();
            }
        });
    }

    private int checkJson(String json) {
        try {
            PayData msg = mGson.fromJson(json, new TypeToken<PayData>() {
            }.getType());
            if (msg.getMsg() != null && !msg.getMsg().equals("")) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 2;
        }
    }

    class PayData {
        String Msg;
        List<PayInsurance> PayBillList;

        public String getMsg() {
            return Msg;
        }

        public void setMsg(String msg) {
            this.Msg = msg;
        }

        public List<PayInsurance> getPIList() {
            return PayBillList;
        }

        public void setPIList(List<PayInsurance> PIList) {
            this.PayBillList = PIList;
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int flag, String title, String message) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;

        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        if (flag == 0) {
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle(title).withTitleColor("#333333").withMessage(message)
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
                    ActivityUtil.goActivityAndFinish(CarReissueActivity2.this, HomeActivity.class);
                }
            }).show();
        }else if(flag == 1){
            effectstype = NiftyDialogBuilder.Effectstype.Fadein;
            dialogBuilder.withTitle(title).withTitleColor("#333333").withMessage(message)
                    .isCancelableOnTouchOutside(false).withEffect(effectstype).withButton1Text("确认")
                    .setCustomView(R.layout.custom_view, mContext).setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferencesUtils.put("preregisters", "");
                    Utils.showToast("车牌号：" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.PLATENUMBER) + "  电动车信息上传成功！");
                    Bundle bundle = new Bundle();
                    bundle.putString("UnPaid", "2");
//                                bundle.putString("PayDate", data);
                    ArrayList list = new ArrayList();
                    list.add(PIlist);
                    bundle.putParcelableArrayList("PayDate", list);
                    ActivityUtil.goActivityWithBundle(CarReissueActivity2.this, UnpaidActivity.class, bundle);
                    dialogBuilder.dismiss();
                    finish();
                }
            }).show();
        }
    }

    private boolean checkData() {
        if (!checkPlateNum.isChecked() && !checkLabelA.isChecked() && !checkLabelB.isChecked()) {
            Utils.myToast(mContext, "请选择变更类型");
            return false;
        } else {
            for (int i = 0; i < PLI.size(); i++) {
                if (!PLA.checkItemDate(i)) {
                    Utils.myToast(mContext, "请拍摄" + PLI.get(i).getREMARK());
                    return false;
                }
            }
            if (checkPlateNum.isChecked()) {
                String plateNum = editPlateNumber.getText().toString().toUpperCase().trim();
                if (plateNum.equals("")) {
                    Utils.myToast(mContext, "请输入车牌号");
                    return false;
                }
            }
            if (checkLabelA.isChecked()) {
                String labelA = editLabelA.getText().toString().trim();
                if (labelA.equals("")) {
                    Utils.myToast(mContext, "请输入车辆标签编号");
                    return false;
                }
            }
            if (checkLabelB.isChecked()) {
                String labelB = editLabelB.getText().toString().trim();
                if (labelB.equals("")) {
                    Utils.myToast(mContext, "请输入电池标签编号");
                    return false;
                }
            }
        }
        return true;
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
