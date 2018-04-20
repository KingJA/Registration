package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.DX_PreAdapter;
import com.tdr.registration.adapter.PreAdapter;
import com.tdr.registration.model.DX_PreRegistrationModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.tdr.registration.R.id.IV_Scan;

/**
 * 电信预登记列表
 */
@ContentView(R.layout.activity_pre_list)
public class DX_PreListActivity extends Activity {

    @ViewInject(R.id.image_back)
    ImageView imageBack;
    @ViewInject(R.id.text_title)
    TextView textTitle;
    @ViewInject(R.id.list_pre)
    ListView listPre;

    private List<DX_PreRegistrationModel> PRList = new ArrayList<DX_PreRegistrationModel>();
    private DX_PreAdapter mAdapter;


    private Context mContext;
    private String city;
    private String in="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        in=getIntent().getExtras().getString("in");
        if(in==null){
            in="";
        }
        if(in.equals("TJ")){
            textTitle.setText("登记上牌列表");
        }else{
            textTitle.setText("预登记列表");
        }
        city = (String) SharedPreferencesUtils.get("locCityName", "");

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listPre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (in.equals("TJ")) {
                    ShowCar(PRList.get(position));
                } else {
                    openitem_qrcode(position);
                }
            }
        });
    }
    private void ShowCar(DX_PreRegistrationModel dx_preRegistrationModel){
        TransferUtil.save("CarData",dx_preRegistrationModel);
        ActivityUtil.goActivity(DX_PreListActivity.this, PreCheckShowActivity.class);
    }
    private void openitem_qrcode(int position){
        if(PRList.get(position).getREGISTERID()!=null&&!PRList.get(position).getREGISTERID().equals("")){
            String ID="TDR_APP"+PRList.get(position).getREGISTERID();
            Bundle bundle=new Bundle();
            bundle.putBoolean("IsCanBack", true);
            bundle.putString("QRCodeID", ID);
            ActivityUtil.goActivityWithBundle(DX_PreListActivity.this, QRCodeCreateActivity.class, bundle);
        }
    }


    private void initData() {
//        Bundle bundle = getIntent().getExtras();
//        ArrayList list = bundle.getParcelableArrayList("electricCarModelList");
//        PRList = (List<DX_PreRegistrationModel>) list.get(0);
        PRList=(List<DX_PreRegistrationModel>)TransferUtil.retrieve("PreRegistrationData");
        if(PRList==null){
            PRList=new ArrayList<>();
        }else{
            TransferUtil.remove("PreRegistrationData");
        }

        mAdapter = new DX_PreAdapter(mContext, PRList,in);
        listPre.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
