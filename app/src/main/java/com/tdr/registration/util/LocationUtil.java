package com.tdr.registration.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tdr.registration.base.MyApplication;

/**
 * Created by Administrator on 2017/12/13.
 */

public class LocationUtil {

    public interface OnLocationListener {
        void Location(String City);
    }

    public static void getLocation(final OnLocationListener OLL) {
//初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(MyApplication.getContext());
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //异步获取定位结果
        AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //解析定位结果
                        OLL.Location(amapLocation.getCity());
                    }
                }
            }
        };

        //获取一次定位结果：
        mLocationOption.setOnceLocation(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
        //启动定位
        mLocationClient.startLocation();

    }
}
