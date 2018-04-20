package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ConfirmInsuranceModel;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.model.PreRegistrationModel;
import com.tdr.registration.model.UploadInsuranceModel;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.TransferUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.MyListView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 购买赔付服务
 */
@ContentView(R.layout.activity_confirmation_insurance)
public class ConfirmationInsuranceActivity extends Activity {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.TV_CI_PlateNumber)
    private TextView TV_CI_PlateNumber;

    @ViewInject(R.id.TV_CI_Name)
    private TextView TV_CI_Name;

    @ViewInject(R.id.TV_CI_CardType)
    private TextView TV_CI_CardType;

    @ViewInject(R.id.TV_CI_CardID)
    private TextView TV_CI_CardID;

    @ViewInject(R.id.TV_CI_Phone)
    private TextView TV_CI_Phone;

    @ViewInject(R.id.LV_insurance)
    private MyListView LV_insurance;

    @ViewInject(R.id.CB_ACCEPTCONTRACT)
    private CheckBox CB_ACCEPTCONTRACT;

    @ViewInject(R.id.TV_Confirm)
    private TextView TV_Confirm;

    private Activity mActivity;
    private ConfirmInsuranceModel InsuranceList;
    private MyAdapter MAdapter;
    private Gson mGson;
    private DbManager db;
    private List<BikeCode> cardList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mActivity = this;
        db = x.getDb(DBUtils.getDb());
        mGson = new Gson();
        initview();
        initdata();

    }

    private void initview() {
        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MAdapter = new MyAdapter();
        LV_insurance.setAdapter(MAdapter);
        TV_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!CB_ACCEPTCONTRACT.isChecked()){
                    Utils.showToast("请仔细阅读保险条款后勾选已阅读选框。");
                }else{
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("isChecked", "1");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

    }

    private void initdata() {
        ArrayList list = getIntent().getExtras().getParcelableArrayList("ConfirmInsurance");
        InsuranceList = (ConfirmInsuranceModel) list.get(0);

        String CardType = InsuranceList.getCardType();
        MAdapter.notifyDataSetChanged();
        TV_CI_PlateNumber.setText(InsuranceList.getPlateNumber());
        TV_CI_Name.setText(InsuranceList.getName());
        TV_CI_CardID.setText(InsuranceList.getCardID());
        TV_CI_Phone.setText(InsuranceList.getPhone());


        String ACCEPTCONTRACT = (String) SharedPreferencesUtils.get("ACCEPTCONTRACT", "");
        if(ACCEPTCONTRACT==null||ACCEPTCONTRACT.equals("")){
            CB_ACCEPTCONTRACT.setChecked(true);
            CB_ACCEPTCONTRACT.setVisibility(View.GONE);
        }else{
            CB_ACCEPTCONTRACT.setText(ACCEPTCONTRACT);
        }


        try {
            cardList = db.selector(BikeCode.class).where("type", "=", "6").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (cardList == null) {
            cardList = new ArrayList<BikeCode>();
        }
        mLog.e("cardType:" + InsuranceList.getCardType());
        for (BikeCode bikeCode : cardList) {
            if (CardType.equals(bikeCode.getCode())) {
                TV_CI_CardType.setText(bikeCode.getName());
            }
        }
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            if(InsuranceList==null||InsuranceList.getInsurance()==null){
                return 0;
            }
            return InsuranceList.getInsurance().size();
        }

        @Override
        public Object getItem(int position) {
            return InsuranceList.getInsurance().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ConfirmInsuranceModel.Insurance data = InsuranceList.getInsurance().get(position);
            View v = LayoutInflater.from(mActivity).inflate(R.layout.item_confirm, null);
            TextView TV_Title = (TextView) v.findViewById(R.id.TV_Title);
            TextView TV_Money = (TextView) v.findViewById(R.id.TV_Money);
            TextView TV_SubTitle = (TextView) v.findViewById(R.id.TV_SubTitle);
            TextView TV_Hyperlink = (TextView) v.findViewById(R.id.TV_Hyperlink);
            TV_Title.setText(data.getTitle() + "(" + data.getDeadLine() + "年)");
            TV_Money.setText(formatMoney(data.getMoney()));
            TV_SubTitle.setText(data.getSubTitle());
            if(data.getHyperlink()!=null){
                List<Hyperlink> list = mGson.fromJson(data.getHyperlink(), new TypeToken<List<Hyperlink>>() {
                }.getType());
                String link = "";
                for (Hyperlink hyperlink : list) {
                    link =link+","+ hyperlink.getTITLE() ;
                }
                if(link.substring(0,1).equals(",")){
                    link=link.substring(1, link.length());
                }
                SpannableString ss = new SpannableString(link);
                int index=0;
                String[] aa =link.split(",");
                for (int i = 0; i < aa.length; i++) {
                    int end=aa[i].length();
                    ss.setSpan(new URLSpan(list.get(i).getLINK()), index, end+index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    index=end+index+1;
                }
                TV_Hyperlink.setText(ss);
                //设置TextView可点击
                TV_Hyperlink.setMovementMethod(LinkMovementMethod.getInstance());
            }
            return v;
        }
    }

    public static String formatMoney(String s) {
        if (s == null || s.length() < 1) {
            return "";
        }
        mLog.e("money="+s);
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        mLog.e("num="+num);
        formater = new DecimalFormat("###,###,###.##");
        String result = formater.format(num);
        mLog.e("result="+result);
        if (result.indexOf(".") == -1) {
            result = "¥" + result + ".00";
        } else {
            result = "¥" + result;
        }
        return result;
    }

    class Hyperlink {
        String TITLE;
        String LINK;

        public String getTITLE() {
            return TITLE;
        }

        public void setTITLE(String TITLE) {
            this.TITLE = TITLE;
        }

        public String getLINK() {
            return LINK;
        }

        public void setLINK(String LINK) {
            this.LINK = LINK;
        }
    }
}
