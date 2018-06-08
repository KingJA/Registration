package com.tdr.registration.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.PhotoListAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.ImageInfo;
import com.tdr.registration.model.PhotoListInfo;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.model.SignType;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.InterfaceChecker;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.DragImageView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车辆查询展示界面
 */
public class CheckShowActivity extends BaseActivity {
    private static final String TAG = "CheckShowActivity";



    @BindView(R.id.image_scan)
    ImageView imageScan;

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_deal)
    TextView textDeal;

    @BindView(R.id.text_cardType)
    TextView textCardType;
    @BindView(R.id.image_vehicleMsg)
    ImageView imageVehicleMsg;
    @BindView(R.id.linear_vehicleMsg)
    LinearLayout linearVehicleMsg;
    @BindView(R.id.text_vehicleType)
    TextView textVehicleType;
    @BindView(R.id.text_vehiclePromise)
    TextView textVehiclePromise;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.text_plateNumber)
    TextView textPlateNumber;
    @BindView(R.id.text_vehicleFrame)
    TextView textVehicleFrame;
    @BindView(R.id.text_vehicleMotor)
    TextView textVehicleMotor;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.text_vehicleColor2)
    TextView textVehicleColor2;
    @BindView(R.id.text_vehicleBuyTime)
    TextView textVehicleBuyTime;
    @BindView(R.id.relative_ownerMsg)
    RelativeLayout relativeOwnerMsg;
    @BindView(R.id.image_ownerMsg)
    ImageView imageOwnerMsg;
    @BindView(R.id.linear_ownerMsg)
    LinearLayout linearOwnerMsg;
    @BindView(R.id.text_ownerName)
    TextView textOwnerName;
    @BindView(R.id.text_ownerIdentity)
    TextView textOwnerIdentity;
    @BindView(R.id.text_phone1)
    TextView textPhone1;
    @BindView(R.id.text_phone2)
    TextView textPhone2;
    @BindView(R.id.text_address)
    TextView textAddress;
    @BindView(R.id.text_remarks)
    TextView textRemarks;


    @BindView(R.id.text_theftNo)
    TextView textTheftNo;
    @BindView(R.id.text_theftNo2)
    TextView textTheftNo2;

    @BindView(R.id.text_PlateType)
    TextView textPlateType;
    @BindView(R.id.rl_PlateType)
    RelativeLayout relativePlateType;
    @BindView(R.id.rl_vehicleColor2)
    RelativeLayout rl_vehicleColor2;
    @BindView(R.id.rv_PhotoList)
    RecyclerView RV_PhotoList;

    @BindView(R.id.tv_lable)
    TextView TV_lable;
    @BindView(R.id.tv_lable2)
    TextView TV_lable2;
    private Context mConext;
    private Gson mGson;
    private ElectricCarModel model;
    private String electricCar;
    private List<PhotoModel> photoModels = new ArrayList<>();


    private String photoList = "";

    private List<PhotoListInfo> PLI;
    private LinearLayoutManager linearLayoutManager;
    private PhotoListAdapter PLA;
    private List<PhotoListAdapter.DrawableList> DrawableList;
    private ArrayList<ImageInfo> data = new ArrayList<>();

    private String locCityName = "";//当前城市
    private int type = 0;
    private Activity mActivity;
    private DbManager db;

    private FrameLayout showLayout;
    private DragImageView showPic;
    private ImageView closeImg;
    private int window_width, window_height;// 控件宽度
    private int state_height;// 状态栏的高度
    private ViewTreeObserver viewTreeObserver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_show2);
        ButterKnife.bind(this);
        Utils.ClearData();
        GetReady();
        SetPhotoList();
        VehiclesStorageUtils.clearData();
        initView();
        initData();
    }
    private void GetReady() {
        db = x.getDb(DBUtils.getDb());
        mConext = this;
        mActivity=this;
        mGson = new Gson();
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
//        List<BaseInfo> ResultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + locCityName + "\"");
        List<BaseInfo> ResultList  = null;
        try {
            ResultList = db.selector(BaseInfo.class).where("cityName","=",locCityName).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(ResultList==null){
            ResultList=new ArrayList<BaseInfo>();
        }
        BaseInfo BI = ResultList.get(0);
        DrawableList = new ArrayList<PhotoListAdapter.DrawableList>();
        PLI = new ArrayList<PhotoListInfo>();
//        Log.e("Pan", "PhotoConfig" + BI.getPhotoConfig());
        try {
            JSONArray JA = new JSONArray(BI.getPhotoConfig());
            JSONObject JB;
            PhotoListInfo pli;
            for (int i = 0; i < JA.length(); i++) {
                JB = new JSONObject(JA.get(i).toString());
                pli = new PhotoListInfo();
                pli.setINDEX(JB.getString("INDEX"));
//                Log.e("Pan","INDEX"+pli.getINDEX());
                pli.setREMARK(JB.getString("REMARK"));
//                Log.e("Pan","REMARK"+pli.getREMARK());
                pli.setValid(JB.getBoolean("IsValid"));
//                Log.e("Pan","Valid"+pli.isValid());
                pli.setRequire(JB.getBoolean("IsRequire"));
//                Log.e("Pan","Require"+pli.isRequire());
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
//        Log.e("Pan", "----------smoothScrollToPosition---------");
        PLA.setOnItemClickLitener(new PhotoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                for (PhotoListAdapter.DrawableList drawableList : DrawableList) {
                    if(drawableList.getIndex().equals(PLI.get(position).getINDEX())){
                        Bitmap bm = ((BitmapDrawable) drawableList.getDrawable()).getBitmap();
                        if(bm!=null){
                            showLayout.setVisibility(View.VISIBLE);
                            showLayout.setBackgroundColor(Color.BLACK);
                            showPic.setImageBitmap(bm);
                        }else{
                            Utils.showToast("无照片");
                        }
                    }
                }
//                Toast.makeText(mActivity,PLI.get(position).getREMARK(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position) {}
        });
        RV_PhotoList.setAdapter(PLA);
    }
    private void initView() {
        showLayout = (FrameLayout) findViewById(R.id.show_layout);
        showPic = (DragImageView) findViewById(R.id.show_pic);
        closeImg = (ImageView) findViewById(R.id.close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLayout.setVisibility(View.GONE);
                showLayout.setBackgroundColor(Color.parseColor("#00000000"));
            }
        });
        showPic.setmActivity(this);
        WindowManager manager = getWindowManager();
        window_width = manager.getDefaultDisplay().getWidth();
        window_height = manager.getDefaultDisplay().getHeight();
        /** 测量状态栏高度 **/
        viewTreeObserver = showPic.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (state_height == 0) {
                    // 获取状况栏高度
                    Rect frame = new Rect();
                    getWindow().getDecorView()
                            .getWindowVisibleDisplayFrame(frame);
                    state_height = frame.top;
                    showPic.setScreen_H(window_height - state_height);
                    showPic.setScreen_W(window_width);
                }
            }
        });
        textTitle.setText("车辆详情");
        textDeal.setText("申请撤控");
        imageScan.setVisibility(View.GONE);
        imageScan.setBackgroundResource(R.mipmap.qr_scan);
        if (locCityName.contains("昆明")) {
            linearVehicleMsg.setVisibility(View.GONE);
        }

        if (locCityName.contains("天津")) {
            rl_vehicleColor2.setVisibility(View.GONE);
            relativePlateType.setVisibility(View.VISIBLE);
        }

        if (InterfaceChecker.isNewInterface()) {
            Log.e(TAG, "新接口: " );
            Log.e(TAG, "车辆类型: " +VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));
            //新接口方式
            List<SignType> signTypes = InterfaceChecker.getSignTypes(VehiclesStorageUtils.getVehiclesAttr
                    (VehiclesStorageUtils.VEHICLETYPE));

            Log.e(TAG, "标签数: "+signTypes.size() );
            if (signTypes.size() == 1) {
                Log.e(TAG, "1个标签: " );
                TV_lable.setText(signTypes.get(0).getName());
            } else if (signTypes.size() == 2||signTypes.size() == 3) {
                Log.e(TAG, "2个标签: " );
                TV_lable.setText(signTypes.get(0).getName());
                TV_lable2.setText(signTypes.get(1).getName());
            }
        }

    }


    private void initData() {
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            electricCar = bundle.getString("ElectricCar");
            Log.e("Pan","electricCar"+electricCar);
            type = bundle.getInt("type");
        }

//        if (type == 0) {
//            textDeal.setVisibility(View.GONE);
//        } else {
//            textDeal.setVisibility(View.VISIBLE);
//        }
        textDeal.setVisibility(View.GONE);

        model = mGson.fromJson(electricCar, new TypeToken<ElectricCarModel>() {
        }.getType());

        List<PhotoModel> pm=  model.getPhotoListFile();
//        List<PhotoModel> photolist=new ArrayList<PhotoModel>();
        for (int i = 0; i <PLI.size() ; i++) {
            for (int j = 0; j <pm.size(); j++) {
                if(PLI.get(i).getINDEX().equals(pm.get(j).getINDEX())){
//                    photolist.add(pm.get(i));
                    initImages(pm.get(j).getINDEX(), pm.get(j).getPhoto());
                }
            }
        }
//        photoList = Utils.initNullStr(model.getPhotoListFile());
//        if (!photoList.equals("")) {
//            try {
//                JSONArray jsonArray = new JSONArray(photoList);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject json = jsonArray.getJSONObject(i);
//                    final String INDEX = json.getString("INDEX");
//                    final String Photo = json.getString("Photo");
//                    String PhotoFile = json.getString("PhotoFile");
//                    String Remark = json.getString("Remark");
////                    Log.e("Pan", "Index=" + INDEX);
//                    initImages(INDEX, Photo);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        Log.e("Pan","CARTYPE:"+model.getCARTYPE());
        if (model.getCARTYPE().equals("0")) {
            textVehicleType.setText("新车");
        } else {
            textVehicleType.setText("旧车");
        }
        if (model.getISCONFIRM().equals("1")) {
            textVehiclePromise.setText("是");
        } else {
            textVehiclePromise.setText("否");
        }
        if (model.getPLATETYPE() != null) {
            if (model.getPLATETYPE().equals("1")) {
                textPlateType.setText("正式");
            } else {
                textPlateType.setText("临时");
            }
        }

        List<BikeCode> brandsList=null;
        try {
            brandsList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (brandsList == null) {
            brandsList = new ArrayList<BikeCode>();
        }
        String cardType = model.getCARDTYPE();
        Log.e("Pan","cardType:"+cardType);
        for (BikeCode bikeCode : brandsList) {
            if(cardType.equals(bikeCode.getCode())){
                textCardType.setText(bikeCode.getName());
            }
//            Log.e("Pan","bikeCode.getCode():"+bikeCode.getCode());
//            Log.e("Pan","bikeCode.getId():"+bikeCode.getId());
//            Log.e("Pan","bikeCode.getName():"+bikeCode.getName());
//            Log.e("Pan","bikeCode.getType():"+bikeCode.getType());
//            Log.e("Pan","bikeCode.getCodeId():"+bikeCode.getCodeId());
//            Log.e("Pan","bikeCode.getInTime():"+bikeCode.getInTime());
//            Log.e("Pan","bikeCode.getRemark():"+bikeCode.getRemark());
//            Log.e("Pan","bikeCode.getSort():"+bikeCode.getSort());
//            Log.e("Pan","bikeCode.getUpdateTime():"+bikeCode.getUpdateTime());
        }

        textVehicleBrand.setText(model.getVehicleBrandName());
        textPlateNumber.setText(model.getPlateNumber());
        textVehicleFrame.setText(model.getShelvesNo());
        textVehicleMotor.setText(model.getEngineNo());
        textVehicleColor.setText(model.getColorName());
        textVehicleColor2.setText(model.getColorName2());
        textVehicleBuyTime.setText(Utils.dateWithoutTime(model.getBuyDate()));
        textTheftNo.setText(model.getRESERVE3());
        textTheftNo2.setText(model.getRESERVED4());

        textOwnerName.setText(model.getOwnerName());
        textOwnerIdentity.setText(Utils.hideID(model.getCardId()));
        textPhone1.setText(model.getPhone1());
        textPhone2.setText(model.getPhone2());
        textAddress.setText(model.getResidentAddress());
        textRemarks.setText(model.getRemark());
    }


    /**
     * 获取图片
     */
    private void initImages(final String index, String id) {
        Log.e("Pan","index="+index);
        Log.e("Pan","id="+id);
        HashMap<String, String> map = new HashMap<>();
        map.put("pictureGUID", id);
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETPICTURE, map, new WebServiceUtils.WebServiceCallBack() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void callBack(final String result) {
                Log.e("Pan","result="+result);
                if (result != null) {
                    Bitmap bitmap = Utils.stringtoBitmap(result);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    DrawableList.add(new PhotoListAdapter.DrawableList(index, drawable));
                    PLA.UpDate(DrawableList);
                }
            }
        });
    }


    @OnClick({R.id.image_back, R.id.text_deal,   R.id.relative_vehicleMsg, R.id.relative_ownerMsg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_deal:
                Bundle bundle = new Bundle();
                bundle.putString("plateNumber", model.getPlateNumber());
                ActivityUtil.goActivityWithBundle(CheckShowActivity.this, FeedBackCommandActivity.class, bundle);
                break;


            case R.id.relative_vehicleMsg:
                if (linearVehicleMsg.getVisibility() == View.GONE) {
                    linearVehicleMsg.setVisibility(View.VISIBLE);
                    imageVehicleMsg.setBackgroundResource(R.mipmap.ic_up);
                } else {
                    linearVehicleMsg.setVisibility(View.GONE);
                    imageVehicleMsg.setBackgroundResource(R.mipmap.ic_down);
                }
                break;
            case R.id.relative_ownerMsg:
                if (linearOwnerMsg.getVisibility() == View.GONE) {
                    linearOwnerMsg.setVisibility(View.VISIBLE);
                    imageOwnerMsg.setBackgroundResource(R.mipmap.ic_up);
                } else {
                    linearOwnerMsg.setVisibility(View.GONE);
                    imageOwnerMsg.setBackgroundResource(R.mipmap.ic_down);
                }
                break;
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
