package com.tdr.registration.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.RegisterFirstKunMingActivity;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.DistrainModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 扣押详情
 */
public class SeizureShowActivity extends BaseActivity {
    private static final String TAG = "SeizureShowActivity";
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_concernedName)
    TextView textConcernedName;
    @BindView(R.id.text_concernedPhone)
    TextView textConcernedPhone;
    @BindView(R.id.text_concernedIdentity)
    TextView textConcernedIdentity;
    @BindView(R.id.text_vehicleBrand)
    TextView textVehicleBrand;
    @BindView(R.id.text_vehicleColor)
    TextView textVehicleColor;
    @BindView(R.id.text_frame)
    TextView textFrame;
    @BindView(R.id.text_motor)
    TextView textMotor;
    @BindView(R.id.text_isTraffic)
    TextView textIsTraffic;
    @BindView(R.id.text_seizeUnit)
    TextView textSeizeUnit;
    @BindView(R.id.text_seizeTime)
    TextView textSeizeTime;
    @BindView(R.id.text_remarks)
    TextView textRemarks;
    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_deal)
    TextView textDeal;
    @BindView(R.id.text_seizureNumber)
    TextView textSeizureNumber;
    @BindView(R.id.text_registrant)
    TextView textRegistrant;
    @BindView(R.id.text_registrTime)
    TextView textRegistrTime;
    @BindView(R.id.text_registrUnit)
    TextView textRegistrUnit;

    private DistrainModel model;
    private ElectricCarModel electricCarModel = new ElectricCarModel();

    private String locCityName = "";//当前帐号所在城市

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seizure_show);
        ButterKnife.bind(this);
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        initView();
        initData();
    }

    private void initView() {
        textTitle.setText("扣押详情");
        if (locCityName.contains("昆明")) {

        } else {
            textDeal.setText("扣押转正式");
            textDeal.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        Bundle bundle = (Bundle) getIntent().getExtras();
        if (bundle != null) {
            model = (DistrainModel) bundle.getSerializable("seizeMsg");
        }
        textSeizureNumber.setText(model.getDistrainNo());
        textConcernedName.setText(model.getOwnerName());
        textConcernedPhone.setText(model.getPhone());
        textConcernedIdentity.setText(model.getIdentityCard());
        textVehicleBrand.setText(model.getVehicleBrandName());
        textVehicleColor.setText(model.getColorName());
        textFrame.setText(model.getShelvesNo());
        textMotor.setText(model.getEngineNo());
        if (model.getIsTraffic().equals("1")) {
            textIsTraffic.setText("交警扣押");
        } else {
            textIsTraffic.setText("派出所扣押");
        }
        textSeizeUnit.setText(model.getDistrainUnit());
        textSeizeTime.setText(Utils.dateWithoutTime(model.getDistrainTime()));
        textRegistrant.setText(model.getOpeartorName());
        textRegistrTime.setText(model.getOperatTime());
        textRegistrUnit.setText(model.getUnitName());
        textRemarks.setText(model.getRemark());
    }

    @OnClick({R.id.image_back, R.id.text_deal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.text_deal:
                Bundle bundle = new Bundle();
                electricCarModel.setOwnerName(textConcernedName.getText().toString());
                electricCarModel.setPhone1(textConcernedPhone.getText().toString());
                electricCarModel.setCardId(textConcernedIdentity.getText().toString());
                electricCarModel.setVehicleBrand(model.getVehicleBrand());
                electricCarModel.setVehicleBrandName(textVehicleBrand.getText().toString());
                electricCarModel.setColorId(model.getColorId());
                electricCarModel.setColorName(model.getColorName());
                electricCarModel.setShelvesNo(textFrame.getText().toString());
                electricCarModel.setEngineNo(textMotor.getText().toString());
                bundle.putSerializable("model", electricCarModel);
                bundle.putString("InType","Registration");
                bundle.putString("activity", "seizure");
                bundle.putString("distrainCarListID", model.getListId());
                ActivityUtil.goActivityWithBundle(SeizureShowActivity.this, RegisterSecondActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (locCityName.contains("昆明")) {
            ActivityUtil.goActivityAndFinish(SeizureShowActivity.this, HomeActivity.class);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }


}
