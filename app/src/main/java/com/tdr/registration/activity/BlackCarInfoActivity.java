package com.tdr.registration.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.ShowPhotoListAdapter;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.CarDeployModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.MonitorModel;
import com.tdr.registration.model.PhotoModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.PhotoUtils;
import com.tdr.registration.util.RecyclerViewItemDecoration;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 黑车详情
 */
@ContentView(R.layout.activity_blackcarinfo)
public class BlackCarInfoActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.TV_Search)
    private TextView TV_Search;

    @ViewInject(R.id.TV_V1)
    private TextView TV_V1;

    @ViewInject(R.id.TV_V2)
    private TextView TV_V2;

    @ViewInject(R.id.TV_V3)
    private TextView TV_V3;

    @ViewInject(R.id.IV_Cursor)
    private ImageView IV_Cursor;
    @ViewInject(R.id.VP_CarInfo)
    private ViewPager VP_CarInfo;

    @ViewInject(R.id.RL_ShowPhoto)
    RelativeLayout RL_ShowPhoto;
    @ViewInject(R.id.RL_Show)
    RelativeLayout RL_Show;
    @ViewInject(R.id.IV_back)
    ImageView IV_back;


    private List<View> viewList;
    private Activity mActivity;
    private int bmpw = 0; // 游标宽度
    private int offset = 0;// // 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private View view1;
    private View view2;
    private View view3;
    private Gson mGson;
    private ZProgressHUD mProgressHUD;
    private BlackCarModel blackCar;
    private MyPagerAdapter PA;
    private boolean BleState=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
        getCardata();
        GetMonitorData();
    }

    private void initview() {
        mActivity = this;
        blackCar = (BlackCarModel) getIntent().getExtras().getSerializable("BlackCar");
        BleState = getIntent().getExtras().getBoolean("BleState");
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        view1 = getLayoutInflater().inflate(R.layout.l1, null);
        view2 = getLayoutInflater().inflate(R.layout.l2, null);
        view3 = getLayoutInflater().inflate(R.layout.l3, null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        initCursorPos();
        PA = new MyPagerAdapter(viewList);
        VP_CarInfo.setAdapter(PA);
        VP_CarInfo.setOnPageChangeListener(new MyPageChangeListener());

        IV_Back.setOnClickListener(this);
        IV_back.setOnClickListener(this);
        TV_Search.setOnClickListener(this);
        TV_V1.setOnClickListener(this);
        TV_V2.setOnClickListener(this);
        TV_V3.setOnClickListener(this);
    }


    private void getCardata() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", blackCar.getPLATENUMBER());
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService(mActivity, (String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            String ElectricCar = jsonObject.getString("ElectricCar");
                            ElectricCarModel ECM = mGson.fromJson(ElectricCar, new TypeToken<ElectricCarModel>() {
                            }.getType());
                            setV1(ECM);
                            String CarDeploy = jsonObject.getString("CarDeploy");
                            mLog.e("CarDeploy="+CarDeploy);
                            CarDeployModel CDM = mGson.fromJson(CarDeploy, new TypeToken<CarDeployModel>() {
                            }.getType());
                            setV2(CDM);
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(BlackCarInfoActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
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
    private void GetMonitorData() {
        mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", blackCar.getPLATENUMBER());
        map.put("pageIdx", "0");
        map.put("pageSize", "10");
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETMONITORDATA, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int errorCode = object.getInt("ErrorCode");
                        String data = object.getString("Data");
                        mLog.e("Monitor="+data);
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            List<MonitorModel> monitorModels = mGson.fromJson(data, new TypeToken<List<MonitorModel>>() {
                            }.getType());

                            setV3(monitorModels);

                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                            SharedPreferencesUtils.put("token", "");
                            ActivityUtil.goActivityAndFinish(BlackCarInfoActivity.this, LoginActivity.class);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.showToast(data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();

                        Utils.showToast( "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.showToast( "获取数据超时，请检查网络连接");
                }
            }
        });
    }

    private void setV1(ElectricCarModel ECM) {
        ((TextView) view1.findViewById(R.id.TV_PlateNumber)).setText(ECM.getPlateNumber());
        ((TextView) view1.findViewById(R.id.TV_EngineNo)).setText(ECM.getEngineNo());
        ((TextView) view1.findViewById(R.id.TV_ShelvesNo)).setText(ECM.getShelvesNo());
        ((TextView) view1.findViewById(R.id.TV_VehicleModels)).setText(ECM.getVehicleModels());
        ((TextView) view1.findViewById(R.id.TV_VehiclebrandName)).setText(ECM.getVehicleBrandName());
        ((TextView) view1.findViewById(R.id.TV_Color1Nanme)).setText(ECM.getColorName());
        ((TextView) view1.findViewById(R.id.TV_CarType)).setText(ECM.getCARTYPE().equals("0") ? "新车" : "旧车");
        ((TextView) view1.findViewById(R.id.TV_BuyDate)).setText(ECM.getBuyDate());
        ((TextView) view1.findViewById(R.id.TV_OwnerName)).setText(ECM.getOwnerName());
        ((TextView) view1.findViewById(R.id.TV_Phone1)).setText(ECM.getPhone1());

        RecyclerView RV_PhotoList = (RecyclerView) view1.findViewById(R.id.RV_PhotoList);

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RV_PhotoList.setLayoutManager(linearLayoutManager);
        RV_PhotoList.addItemDecoration(new RecyclerViewItemDecoration());
        List<String> SPLADL = new ArrayList<>();
        String http = (String) SharedPreferencesUtils.get("httpUrl", "");
        for (PhotoModel photoModel : ECM.getPhotoListFile()) {
            if (photoModel.getPhoto() != null && !photoModel.getPhoto().equals("")) {
                SPLADL.add(http + Constants.HTTP_GetPhotoUrl + photoModel.getPhoto());
            }
        }
        ShowPhotoListAdapter SPLA = new ShowPhotoListAdapter(mActivity, SPLADL);
        SPLA.setOnItemClickLitener(new ShowPhotoListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(ImageView view, int position) {
                ShowPhoto((Bitmap) view.getTag());
            }
        });
        RV_PhotoList.setAdapter(SPLA);
        viewList.set(0, view1);
        PA.notifyDataSetChanged();
    }

    private void setV2(CarDeployModel ECM) {
        if (ECM != null) {
            mLog.e("CarDeploy=" + ECM.getDeployUnitName());
            ((TextView) view2.findViewById(R.id.TV_DeployUserName)).setText(ECM.getDeployUserName());
            ((TextView) view2.findViewById(R.id.TV_DEPLOY_TIME)).setText(ECM.getDEPLOY_TIME());
            ((TextView) view2.findViewById(R.id.TV_ALARM_PHONE)).setText(ECM.getALARM_PHONE());
            ((TextView) view2.findViewById(R.id.TV_DEPLOY_STATUS)).setText(ECM.getDEPLOY_STATUS().equals("1") ? "已布控" : "已撤控");
            ((TextView) view2.findViewById(R.id.TV_ALARM_DATE)).setText(ECM.getALARM_DATE());
            ((TextView) view2.findViewById(R.id.TV_ALARM_DATE2)).setText(ECM.getALARM_DATE2());
            ((TextView) view2.findViewById(R.id.TV_ALARM_DATE3)).setText(ECM.getALARM_DATE3());
            ((TextView) view2.findViewById(R.id.TV_DutyUnitName)).setText(ECM.getDutyUnitName());
        }
        viewList.set(1, view2);
        PA.notifyDataSetChanged();
    }
    private void setV3(final List<MonitorModel> monitorModels) {
        ((TextView) view3.findViewById(R.id.TV_GJDT)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList();
                list.add(monitorModels);
                bundle.putParcelableArrayList("monitorModels", list);
                bundle.putSerializable("BlackCar", blackCar);
                bundle.putBoolean("BleState", BleState);
                ActivityUtil.goActivityWithBundle(BlackCarInfoActivity.this, MapActivity.class, bundle);
            }
        });
        for (MonitorModel monitorModel : monitorModels) {
            mLog.e("MONITORTIME="+ monitorModel.getMONITORTIME());
        }
        RouteAdapter mAdapter = new RouteAdapter( monitorModels);
        ListView LV_GJ = (ListView) view3.findViewById(R.id.LV_GJ);
        LV_GJ.setAdapter(mAdapter);
        viewList.set(2, view3);
        PA.notifyDataSetChanged();
    }

    private void ShowPhoto(Bitmap bp){
        if(bp==null){
            return;
        }
        int Proportion = PhotoUtils.calculateInSampleSize(mActivity,bp);
        Drawable drawable2 = new BitmapDrawable(bp);
        ImageView IV2 = new ImageView(mActivity);
        IV2.setBackground(drawable2);
        IV2.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams RLLP = new RelativeLayout.LayoutParams(bp.getWidth() * Proportion, bp.getHeight() * Proportion);
        RL_Show.addView(IV2, RLLP);
        RL_ShowPhoto.setVisibility(View.VISIBLE);
    }
    //初始化指示器位置
    public void initCursorPos() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        bmpw = screenW / 3;
        IV_Cursor.setMinimumWidth(bmpw);
        offset = (screenW / viewList.size() - bmpw) / 2;// 计算偏移量

        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        IV_Cursor.setImageMatrix(matrix);// 设置动画初始位置
        TV_V1.setTextColor(Color.parseColor("#333333"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.IV_Back:
                finish();
                break;
            case R.id.IV_back:
                RL_Show.removeAllViews();
                RL_ShowPhoto.setVisibility(View.GONE);
                break;
            case R.id.TV_Search:
                if(BleState){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BlackCar", blackCar);
                    ActivityUtil.goActivityWithBundle(BlackCarInfoActivity.this, SeekActivity2.class, bundle);
                }else{
                    Utils.showToast("请先连接稽查设备");
                }
                break;
            case R.id.TV_V1:
                VP_CarInfo.setCurrentItem(0);
                break;
            case R.id.TV_V2:
                VP_CarInfo.setCurrentItem(1);
                break;
            case R.id.TV_V3:
                VP_CarInfo.setCurrentItem(2);
                break;

        }
    }

    //页面改变监听器
    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        int one = offset * 2 + bmpw;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    TV_V1.setTextColor(Color.parseColor("#333333"));
                    TV_V2.setTextColor(Color.parseColor("#999999"));
                    TV_V3.setTextColor(Color.parseColor("#999999"));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    TV_V1.setTextColor(Color.parseColor("#999999"));
                    TV_V2.setTextColor(Color.parseColor("#333333"));
                    TV_V3.setTextColor(Color.parseColor("#999999"));
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    TV_V1.setTextColor(Color.parseColor("#999999"));
                    TV_V2.setTextColor(Color.parseColor("#999999"));
                    TV_V3.setTextColor(Color.parseColor("#333333"));
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            IV_Cursor.startAnimation(animation);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    class RouteAdapter extends BaseAdapter {


        private List<MonitorModel> models = new ArrayList<>();

        public RouteAdapter(List<MonitorModel> models) {
            this.models = models;
        }

        @Override
        public int getCount() {
            return models.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public final class RouteViewHolder {
            public TextView textTime;
            public TextView textStationAddress;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           RouteViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new RouteViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.route_item, null);
                viewHolder.textTime = (TextView) convertView.findViewById(R.id.text_time);
                viewHolder.textStationAddress = (TextView) convertView.findViewById(R.id.text_stationAddress);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (RouteViewHolder) convertView.getTag();
            }
            mLog.e("MONITORTIME2="+models.get(position).getMONITORTIME());
            viewHolder.textTime.setText(models.get(position).getMONITORTIME());
            viewHolder.textStationAddress.setText(models.get(position).getStationName());

            return convertView;
        }
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position));
            return mListViews.get(position);
        }
    }
}
