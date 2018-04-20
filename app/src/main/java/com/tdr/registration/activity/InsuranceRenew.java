package com.tdr.registration.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.InsuranceListAdapter;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.InsuranceRenewModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.view.RadioGroupEx;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;


/**
 * 保险详情
 */
@ContentView(R.layout.activity_insurance_renew)
public class InsuranceRenew extends Activity implements View.OnClickListener{

    @ViewInject(R.id.image_back) private ImageView image_back;

    @ViewInject(R.id.text_title) private TextView text_title;

    @ViewInject(R.id.text_deal) private TextView text_deal;

    @ViewInject(R.id.TV_PlateNum) private TextView TV_PlateNum;

    @ViewInject(R.id.TV_Name) private TextView TV_Name;


    @ViewInject(R.id.RL_InsuranceList) private RelativeLayout RL_InsuranceList;
    @ViewInject(R.id.IV_InsuranceList) private ImageView IV_InsuranceList;

    @ViewInject(R.id.RV_InsuranceList) private RecyclerView RV_InsuranceList;

//    @ViewInject(R.id.btn_search) private Button btn_search;

    private Activity mActivity;
    private  InsuranceListAdapter ILA;
    private List<InsuranceRenewModel> InsuranceList=new ArrayList<InsuranceRenewModel>();
    private ElectricCarModel ECM = new ElectricCarModel();
    public static InsuranceRenew mInsuranceRenew = null;
    private String Version="";
    private String IsConfirm="";
    private ConfirmInsuranceModel CIM=new ConfirmInsuranceModel();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity=this;
        mInsuranceRenew=this;
        initview();
        initdate();
    }

    private void initview(){
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RV_InsuranceList.setLayoutManager(linearLayoutManager);
        RV_InsuranceList.addItemDecoration(new RecyclerViewItemDecoration());
        ILA = new InsuranceListAdapter(mActivity,InsuranceList);
        RV_InsuranceList.setAdapter(ILA);

        text_deal.setVisibility(View.VISIBLE);
        image_back.setOnClickListener(this);
        RL_InsuranceList.setOnClickListener(this);
        text_deal.setOnClickListener(this);
    }
    private void initdate(){
        Version = (String) SharedPreferencesUtils.get("Version", "");
        IsConfirm = (String) SharedPreferencesUtils.get("IsConfirm", "");
        text_title.setText("服务延期");
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ECM = (ElectricCarModel) bundle.getSerializable("model");
        }
        CIM.setPlateNumber(ECM.getPlateNumber());
        CIM.setName(ECM.getOwnerName());
        CIM.setCardType(ECM.getCARDTYPE());
        CIM.setCardID(ECM.getCardId());
        CIM.setPhone(ECM.getPhone1());
        TV_PlateNum.setText(ECM.getPlateNumber());
        TV_Name.setText(ECM.getOwnerName());
        for (ElectricCarModel.PolicysBean policysBean : ECM.getPOLICYS()) {
            InsuranceRenewModel IRM=new InsuranceRenewModel();
            IRM.setTitle(policysBean.getTypeName());
            IRM.setIsBuy(policysBean.getISBUY());
            IRM.setLife(policysBean.getDEADLINE());
            IRM.setExpiration(policysBean.getENDDATE());
            InsuranceList.add(IRM);
        }
        ILA.update(InsuranceList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.RL_InsuranceList:
                if(RV_InsuranceList.getVisibility()==View.GONE){
                    RV_InsuranceList.setVisibility(View.VISIBLE);
                    IV_InsuranceList.setBackgroundResource(R.mipmap.ic_down);
                }else if(RV_InsuranceList.getVisibility()==View.VISIBLE){
                    RV_InsuranceList.setVisibility(View.GONE);
                    IV_InsuranceList.setBackgroundResource(R.mipmap.ic_up);
                }
                break;
            case R.id.text_deal:
                Bundle bundle = new Bundle();
                bundle.putString("EcId", ECM.getEcId());
                bundle.putString("VehicleType",  ECM.getVehicleType());
                ArrayList list = new ArrayList();
                list.add(CIM);
                bundle.putParcelableArrayList("insurance", list);
                ActivityUtil.goActivityWithBundle(InsuranceRenew.this, BuyInsuranceActivity.class, bundle);
                break;
        }
    }

    public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //设定底部边距为1px
            outRect.set(0, 0, 0,15);
        }

    }
}
