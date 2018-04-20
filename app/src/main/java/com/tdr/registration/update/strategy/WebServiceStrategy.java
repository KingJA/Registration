package com.tdr.registration.update.strategy;

import android.util.Log;

import com.tdr.registration.update.Constants;
import com.tdr.registration.util.mLog;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WebServiceStrategy implements LoadStrategy {
    private final String WEBSERVER_NAMESPACE = "http://tempuri.org/";// 命名空间
    private final String VERSION_URL = "http://dmi.tdr-cn.com/WebServiceAPKRead.asmx";// 获取版本号地址
    private final String METHOD = "GetCode";// 获取版本号Webservice方法

    @Override
    public int getVersionCode(String fileName) {
        int versionCode = -1;
        SoapObject request = new SoapObject(WEBSERVER_NAMESPACE,
                METHOD);
        request.addProperty(Constants.FILE_NAME, fileName);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;

        HttpTransportSE ht = new HttpTransportSE(
                VERSION_URL);
        try {
            ht.call(WEBSERVER_NAMESPACE + METHOD, envelope);
            String result = envelope.getResponse().toString();
            mLog.e("WebServiceStrategy", "result:" + result);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            versionCode = jsonObject.getInt(Constants.ANDROID_VERSIONCODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }


}
