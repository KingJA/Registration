package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.tdr.registration.R;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.MonitorModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆轨迹地图
 */
@ContentView(R.layout.activity_map)
public class MapActivity extends Activity implements View.OnClickListener {
    @ViewInject(R.id.IV_back)
    ImageView IV_back;
    @ViewInject(R.id.TV_title)
    TextView TV_title;
    @ViewInject(R.id.TV_deal)
    TextView TV_deal;
    @ViewInject(R.id.map)
    MapView mapView;

    private Context mContext;

    private AMap mAmap;

    private BlackCarModel blackCar;
    private boolean BleState=false;
    private BitmapDescriptor BF_Start;
    private BitmapDescriptor BF_End;
    private BitmapDescriptor BF_After;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        initView();
        initMap(savedInstanceState);
        initData();
    }

    private void initView() {
        blackCar = (BlackCarModel) getIntent().getExtras().getSerializable("BlackCar");
        BleState = getIntent().getExtras().getBoolean("BleState");
        IV_back.setOnClickListener(this);
        TV_deal.setOnClickListener(this);

        BF_Start=BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_startpoint));
        BF_End = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_lastpoint));
        BF_After= BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_blue_point));
    }

    private void initMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAmap = mapView.getMap();
        mAmap.setTrafficEnabled(true);// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        mAmap.setMapType(AMap.MAP_TYPE_NORMAL);// 卫星地图模式

    }

    private void initData() {
        ArrayList list = getIntent().getExtras().getParcelableArrayList("monitorModels");
        List<MonitorModel> monitorModels = (List<MonitorModel>) list.get(0);
        List<LatLng> latLngs = new ArrayList<LatLng>();
        LatLng LL;
        for (int i = 0; i < monitorModels.size(); i++) {
             LL= new LatLng(monitorModels.get(i).getLAT(),monitorModels.get(i).getLNG());
            latLngs.add(LL);
            if(i==0){
//                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngZoom(LL, mAmap.getCameraPosition().zoom);
//                CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(LL,15,0,0));
                mAmap.animateCamera(mCameraUpdate);
                mAmap.addMarker(new MarkerOptions().position(LL).icon(BF_End).draggable(true).zIndex(40));
            }else if (i==monitorModels.size()-1){
                mAmap.addMarker(new MarkerOptions().position(LL).icon(BF_Start).draggable(true).zIndex(20));
            }else{
                mAmap.addMarker(new MarkerOptions().position(LL).icon(BF_After).draggable(true));
            }
        }
        mAmap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).color(Color.parseColor("#4B9DF9")));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.IV_back:
                finish();
                break;
            case R.id.TV_deal:
                if(BleState){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BlackCar", blackCar);
                    ActivityUtil.goActivityWithBundle(MapActivity.this, SeekActivity2.class, bundle);
                }else{
                    Utils.showToast("请先连接稽查设备");
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

}
