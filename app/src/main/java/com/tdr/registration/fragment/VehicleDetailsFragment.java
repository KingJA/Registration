package com.tdr.registration.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.activity.CarCheckActivity;
import com.tdr.registration.activity.CheckShowActivity;
import com.tdr.registration.activity.LoginActivity;
import com.tdr.registration.base.BaseFragment;
import com.tdr.registration.model.CommandModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
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
 * 车辆信息
 * Created by Linus_Xie on 2016/10/15.
 */

@SuppressLint("ValidFragment")
public class VehicleDetailsFragment extends Fragment {
    private static final String TAG = "MonitorInfoFragment";
    TextView textPlateNumber;
    TextView textVehicleMotor;
    TextView textVehicleFrame;
    TextView textVehicleBrand;
    TextView textVehicleColor;
    TextView textVehicleColor2;
    TextView textVehicleBuyTime;
    TextView textOwnerName;
    TextView textPhone1;
    TextView textPhone2;
    ImageView relativeGuleCar;
    TextView textGuleCar;
    ImageView relativeGuleBattery;
    TextView textGuleBattery;
    ImageView relativeLabelA;
    TextView textLabelA;
    ImageView relativeLabelB;
    TextView textLabelB;
    ImageView relativeApplicationForm;
    TextView textApplicationForm;
    ImageView relativePlateNum;
    TextView textPlateNum;
    ImageView relativeIdentity;
    TextView textIdentity;
    ImageView relativeInvoice;
    TextView textInvoice;

    private String plateNumber;
    private ElectricCarModel model;
    private Gson mGson;

    private ZProgressHUD mProgressHUD;

    public VehicleDetailsFragment(String plateNumber) {
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
        View view = View.inflate(getActivity(), R.layout.fragment_vehicle_details, null);
        textPlateNumber = (TextView) view.findViewById(R.id.text_plateNumber);
        textVehicleMotor = (TextView) view.findViewById(R.id.text_vehicleMotor);
        textVehicleFrame = (TextView) view.findViewById(R.id.text_vehicleFrame);
        textVehicleBrand = (TextView) view.findViewById(R.id.text_vehicleBrand);
        textVehicleColor = (TextView) view.findViewById(R.id.text_vehicleColor);
        textVehicleColor2 = (TextView) view.findViewById(R.id.text_vehicleColor2);
        textVehicleBuyTime = (TextView) view.findViewById(R.id.text_vehicleBuyTime);
        textOwnerName = (TextView) view.findViewById(R.id.text_ownerName);
        textPhone1 = (TextView) view.findViewById(R.id.text_phone1);
        textPhone2 = (TextView) view.findViewById(R.id.text_phone2);
        //relativeGuleCar = (ImageView) view.findViewById(R.id.relative_guleCar);
        //textGuleCar = (TextView) view.findViewById(R.id.text_guleCar);
        //relativeGuleBattery = (ImageView) view.findViewById(R.id.relative_guleBattery);
        //textGuleBattery = (TextView) view.findViewById(R.id.text_guleBattery);
        relativeLabelA = (ImageView) view.findViewById(R.id.relative_labelA);
        textLabelA = (TextView) view.findViewById(R.id.text_labelA);
        relativeLabelB = (ImageView) view.findViewById(R.id.relative_labelB);
        textLabelB = (TextView) view.findViewById(R.id.text_labelB);
        relativeApplicationForm = (ImageView) view.findViewById(R.id.relative_applicationForm);
        textApplicationForm = (TextView) view.findViewById(R.id.text_applicationForm);
        relativePlateNum = (ImageView) view.findViewById(R.id.relative_plateNum);
        textPlateNum = (TextView) view.findViewById(R.id.text_plateNum);
        relativeIdentity = (ImageView) view.findViewById(R.id.relative_identity);
        textIdentity = (TextView) view.findViewById(R.id.text_identity);
        relativeInvoice = (ImageView) view.findViewById(R.id.relative_invoice);
        textInvoice = (TextView) view.findViewById(R.id.text_invoice);
        return view;
    }

    public void initData() {
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
                            model = mGson.fromJson(json.toString(), new TypeToken<ElectricCarModel>() {
                            }.getType());
                            textPlateNumber.setText(model.getPlateNumber());
                            textVehicleMotor.setText(model.getEngineNo());
                            textVehicleFrame.setText(model.getShelvesNo());
                            textVehicleBrand.setText(model.getVehicleBrandName());
                            textVehicleColor.setText(model.getColorName());
                            textVehicleColor2.setText(model.getColorName2());
                            textVehicleBuyTime.setText(Utils.dateWithoutTime(model.getBuyDate()));
                            textOwnerName.setText(model.getOwnerName());
                            textPhone1.setText(model.getPhone1());
                            textPhone2.setText(model.getPhone2());
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
