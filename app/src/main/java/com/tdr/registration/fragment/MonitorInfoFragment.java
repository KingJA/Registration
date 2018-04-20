package com.tdr.registration.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseFragment;
import com.tdr.registration.model.CommandModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 布控信息
 * Created by Linus_Xie on 2016/10/15.
 */

@SuppressLint("ValidFragment")
public class MonitorInfoFragment extends Fragment {
    private static final String TAG = "MonitorInfoFragment";
    private TextView textDispatchedName;
    private TextView textDispatchedTime;
    private TextView textDispatchedPhone;
    private TextView textDispatchedType;
    private TextView textAlarmTime;
    private TextView textStartAlarmTime;
    private TextView textEndAlarmTime;
    private TextView textResponsibilityUnit;

    private String plateNumber;
    private CommandModel.CarDeployBean carDeploy;

    private ZProgressHUD mProgressHUD;
    private Gson mGson;

    public MonitorInfoFragment(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
        mProgressHUD = new ZProgressHUD(getActivity());
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_info, container, false);
        textDispatchedName = (TextView) view.findViewById(R.id.text_dispatchedName);
        textDispatchedTime = (TextView) view.findViewById(R.id.text_dispatchedTime);
        textDispatchedPhone = (TextView) view.findViewById(R.id.text_dispatchedPhone);
        textDispatchedType = (TextView) view.findViewById(R.id.text_dispatchedType);
        textAlarmTime = (TextView) view.findViewById(R.id.text_alarmTime);
        textStartAlarmTime = (TextView) view.findViewById(R.id.text_startAlarmTime);
        textEndAlarmTime = (TextView) view.findViewById(R.id.text_endAlarmTime);
        textResponsibilityUnit = (TextView) view.findViewById(R.id.text_responsibilityUnit);
        return view;

    }

    private void initData() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("W_CPH", plateNumber);
        map.put("W_FDJH", "");
        map.put("W_CJH", "");
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_CHECKSTOLENVEHICLE, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int errorCode = jsonObject.getInt("ErrorCode");
                        String data = jsonObject.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            JSONObject json = jsonObject.getJSONObject("ElectricCar");
                            JSONObject object = jsonObject.getJSONObject("CarDeploy");
                            carDeploy = mGson.fromJson(object.toString(), new TypeToken<CommandModel.CarDeployBean>() {
                            }.getType());
                            textDispatchedName.setText(carDeploy.getALARM_USERNAME());
                            textDispatchedTime.setText(carDeploy.getDEPLOY_TIME());
                            textDispatchedPhone.setText(carDeploy.getALARM_PHONE3());
                            if (carDeploy.getDEPLOY_TYPE().equals("1")) {
                                textDispatchedType.setText("被盗");
                            } else {
                                textDispatchedType.setText("其他");
                            }
                            textAlarmTime.setText(carDeploy.getALARM_DATE());
                            textStartAlarmTime.setText(carDeploy.getALARM_DATE2());
                            textEndAlarmTime.setText(carDeploy.getALARM_DATE3());
                            textResponsibilityUnit.setText(carDeploy.getDUTY_UNIT());
                        } else if (errorCode == 1) {
                            mProgressHUD.dismiss();
                            Utils.myToast(getActivity(), data);
                        } else {
                            mProgressHUD.dismiss();
                            Utils.myToast(getActivity(), data);
                        }
                    } catch (JSONException e) {
                        mProgressHUD.dismiss();
                        e.printStackTrace();
                        Utils.myToast(getActivity(), "JSON解析出错");
                    }
                } else {
                    mProgressHUD.dismiss();
                    Utils.myToast(getActivity(), "获取数据超时，请检查网络连接");
                }
            }
        });

    }

}
