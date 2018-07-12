package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.model.CarLabel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.HttpUtils;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ZProgressHUD;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;


/**
 * 更换标签列表
 */
@ContentView(R.layout.activity_label_binding_list)
public class LabelBindingListActivity extends Activity {

    @ViewInject(R.id.IV_Back)
    ImageView IV_Back;
    @ViewInject(R.id.TV_Title)
    TextView TV_Title;
    @ViewInject(R.id.IV_Add)
    ImageView IV_Add;
    @ViewInject(R.id.LV_BindingList)
    ListView LV_BindingList;


    private Context mContext;
    private ZProgressHUD mProgressHUD;
    private Gson mGson;
    private Activity mActivity;
    private MyAdapter MA;
    private CarLabel CL;
    private boolean isFirst =true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        mContext = this;
        mActivity = this;
        CL = (CarLabel) getIntent().getExtras().getSerializable("CarInfo");
        TV_Title.setText(CL.getPlateNumber());
        mGson = new Gson();
        MA = new MyAdapter();
        mProgressHUD = new ZProgressHUD(this);
        mProgressHUD.setMessage("");
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        LV_BindingList.setAdapter(MA);
        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IV_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("InType", "Add");
                bundle.putSerializable("CarInfo", CL);
                ActivityUtil.goActivityWithBundle(LabelBindingListActivity.this, CarLabelBindingActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirst){
            isFirst=false;
        }else{
            QueryCar();
        }
    }

    private void QueryCar() {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("PlateNumber", CL.getPlateNumber());
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim() + Constants.HTTP_GetCarInfo);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("GetCarInfo:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                CL = mGson.fromJson(data, new TypeToken<CarLabel>() {
                                }.getType());
                                MA.notifyDataSetChanged();
                            } else {
                                Utils.showToast(data);
                            }
                            mProgressHUD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressHUD.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressHUD.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressHUD.dismiss();
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }

    private void UnBind(final int position) {
        mProgressHUD.show();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("LISTID", CL.getEquipments().get(position).getLISTID());
        JSONObject JB = new JSONObject(map);
        RequestParams RP = new RequestParams(((String) SharedPreferencesUtils.get("httpUrl", "")).trim()+ Constants.HTTP_UnBind);
        RP.setAsJsonContent(true);
        RP.setBodyContent(JB.toString());
        mLog.e("UnBind:" + JB.toString());
        HttpUtils.post(RP, new HttpUtils.HttpPostCallBack() {
            public void postcallback(String Finish, String result) {
                if (Finish.equals(HttpUtils.Success)) {
                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            int errorCode = jsonObject.getInt("ErrorCode");
                            String data = jsonObject.getString("Data");
                            if (errorCode == 0) {
                                CL.getEquipments().remove(position);
                                MA.notifyDataSetChanged();
                            } else {
                                Utils.showToast(data);
                            }
                            mProgressHUD.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mProgressHUD.dismiss();
                            Utils.showToast("JSON解析出错");
                        }
                    } else {
                        mProgressHUD.dismiss();
                        Utils.showToast("获取数据超时，请检查网络连接");
                    }
                } else {
                    mProgressHUD.dismiss();
                    mLog.e("Http访问结果：" + Finish);
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (CL.getEquipments() == null) {
                return 0;
            }
            return CL.getEquipments().size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public final class ViewHolder {

            public LinearLayout LL_;
            public TextView TV_ID;
            public TextView TV_LabelName;
            public TextView TV_BindingTime;
            public TextView TV_Replace;
            public TextView TV_Unbundling;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            CarLabel.equipments Label = CL.getEquipments().get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mActivity.getLayoutInflater().inflate(R.layout.item_label_list, null);
                viewHolder.LL_ = (LinearLayout) convertView.findViewById(R.id.LL_);
                viewHolder.TV_ID = (TextView) convertView.findViewById(R.id.TV_ID);
                viewHolder.TV_LabelName = (TextView) convertView.findViewById(R.id.TV_LabelName);
                viewHolder.TV_BindingTime = (TextView) convertView.findViewById(R.id.TV_BindingTime);
                viewHolder.TV_Replace = (TextView) convertView.findViewById(R.id.TV_Replace);
                viewHolder.TV_Unbundling = (TextView) convertView.findViewById(R.id.TV_Unbundling);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.LL_.setMinimumHeight(AppUtil.dp2px(80));
            viewHolder.TV_ID.setText(Label.getORI_THEFTNO());
            viewHolder.TV_LabelName.setText(Label.getSIGNTYPENAME());
            viewHolder.TV_BindingTime.setText("绑定时间:" + Label.getBindTime());
            if (Label.getSIGNTYPE() < 3) {
                viewHolder.TV_Replace.setVisibility(View.GONE);
                viewHolder.TV_Unbundling.setVisibility(View.GONE);
            }else{
                viewHolder.TV_Replace.setVisibility(View.VISIBLE);
                viewHolder.TV_Unbundling.setVisibility(View.VISIBLE);
            }

            viewHolder.TV_Replace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("InType", "Change");
                    bundle.putInt("Position", position);
                    bundle.putSerializable("CarInfo", CL);
                    ActivityUtil.goActivityWithBundle(LabelBindingListActivity.this, CarLabelBindingActivity.class, bundle);
                }
            });
            viewHolder.TV_Unbundling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogShow(position);
                }
            });
            return convertView;
        }
    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(final int position) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(this);

        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        dialogBuilder.withTitle("解绑标签")
                .withTitleColor("#333333")
                .withMessage("确定要解绑该标签吗？")
                .isCancelableOnTouchOutside(false)
                .withEffect(effectstype)
                .withButton1Text("取消")
                .withButton2Text("确定")
                .setCustomView(R.layout.custom_view, mActivity)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
                UnBind(position);
            }
        }).show();


    }
}
