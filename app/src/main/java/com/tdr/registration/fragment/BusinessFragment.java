package com.tdr.registration.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tdr.registration.R;
import com.tdr.registration.activity.DX_PreRegistrationQueryActivity;
import com.tdr.registration.activity.DX_PreRegistration_Car_Activity;
import com.tdr.registration.activity.DX_PreRegistration_Statistics_Activity;
import com.tdr.registration.activity.ElectricInfoSearchActivity;
import com.tdr.registration.activity.LabelBindingCarQueryActivity;
import com.tdr.registration.activity.PersonalStatisticActivity;
import com.tdr.registration.activity.PreFirstActivity;
import com.tdr.registration.activity.ShangPaiCarActivity;
import com.tdr.registration.activity.ShangPaiQueryActivity;
import com.tdr.registration.activity.StatisticActivity;
import com.tdr.registration.activity.VehicleMonitorActivity;
import com.tdr.registration.activity.Visit_1_Activity;
import com.tdr.registration.activity.kunming.JustFuckForKMActivity;
import com.tdr.registration.activity.longyan.PreSearchActivity;
import com.tdr.registration.activity.normal.RegisterCarActivity;
import com.tdr.registration.activity.tianjin.AppointmentQueryActivity;
import com.tdr.registration.activity.tianjin.DX_PreRegistration_Statistics_Tj_Activity;
import com.tdr.registration.activity.tianjin.PreFirstActivity_TJ;
import com.tdr.registration.adapter.MainRecyclerAdapter;
import com.tdr.registration.adapter.RecyclerAdapter;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.ItemModel;
import com.tdr.registration.model.Order;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.ItemClickListener;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.VehiclesStorageUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.ItemDecorationDivider;
import com.tdr.registration.view.MyRecyclerView;
import com.tdr.registration.view.dialog.DialogRegistration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务Fragment
 * Created by Liuns_Xie on 2016/9/14.
 */
@ContentView(R.layout.fragment_business)
public class BusinessFragment extends Fragment {
    @ViewInject(R.id.RL_square)
    private MyRecyclerView RL_square;

    @ViewInject(R.id.RV_func)
    private MyRecyclerView RV_func;

    @ViewInject(R.id.image_appName)
    private ImageView imageAppName;

    @ViewInject(R.id.text_appName)
    private TextView textAppName;

    @ViewInject(R.id.linear_title)
    private LinearLayout linear_title;

    @ViewInject(R.id.image_desc)
    private ImageView image_desc;


    private int[] funImgs = {
            R.mipmap.ic_scrap, R.mipmap.ic_plateagain,
            R.mipmap.ic_transfer, R.mipmap.ic_monitor,
            R.mipmap.ic_statistics, R.mipmap.ic_pre,
            R.mipmap.ic_insurance_claims, R.mipmap.ic_return,
            R.mipmap.ic_appointment, R.mipmap.ic_visit,
            R.mipmap.ic_insurance_claims, R.mipmap.registration_tj,
            R.mipmap.registration_tj_query, R.mipmap.ic_binding};
    private String[] funTitles = {
            "车辆报废", "车牌补办",
            "车辆过户", "车辆布控",
            "备案统计", "车辆预登记",
            "被盗申报", "车辆发还",
            "预约查询", "车辆回访",
            "服务延期", "标签绑定"};//ic小标签
    private int[] funInsurance = {R.mipmap.ic_insurance_modify};
    private String[] funInsuranceTitles = {"套餐变更"};
    private int[] funImgsNone = {R.mipmap.ic_none};
    private String[] funTitlesNone = {""};

    private String rolePowers = "";//权限
    String[] s1 = {
            "109", "105",
            "108", "1001",
            "3700", "2700",
            "113", "1300106",
            "600", "199",
            "114", "122"};

    private MainRecyclerAdapter MRAdapter;
    private RecyclerAdapter mRecyclerAdapter;
    private StaggeredGridLayoutManager manager;

    private DialogRegistration dialogRegistration;


    private String[] registrationType = {"1", "2", "3"};//1电动车，2助力车，3摩托车
    private String locCityName = "";//当前城市 /0
    private DbManager db;
    private List<BaseInfo> baseInfos = new ArrayList<>();
    private List<BikeCode> CarTypeList = new ArrayList<BikeCode>();
    private View DialogView;
    private myAdapter mAdapter;
    private String Register = "备案登记";
    private String PreRegister = "预登记";
    private String PreRegister_TJ = "登记上牌";
    private String PreRegister_TJ_FREE = "免费上牌";


    private String cardTypes;
    String[] CarType;
    private List<ItemModel> listModel = new ArrayList<ItemModel>();
    private ArrayList<String> MainrolePowers;
    private ArrayList<String> hot_new_mvp;
    private boolean IsDX_PR = false;
    private String city;
    private String REGISTRATION;
    private Activity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = x.view().inject(this, inflater, container);
        mActivity = this.getActivity();
        setTitileName();
        initView();
        initData();
        return v;
    }

    /**
     * 设置标题
     */
    private void setTitileName() {
        String appName = (String) SharedPreferencesUtils.get("appName", "");
        if (appName.equals("")) {
            imageAppName.setVisibility(View.VISIBLE);
            textAppName.setVisibility(View.GONE);
        } else {
            linear_title.setVisibility(View.GONE);
            imageAppName.setVisibility(View.GONE);
            textAppName.setVisibility(View.VISIBLE);
            Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
            ImageSpan imgSpan = new ImageSpan(getActivity(), b);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textAppName.setText(spanString);
            textAppName.append("   " + appName);
            if (appName.contains("防盗")) {
                image_desc.setBackgroundResource(R.mipmap.home_desc_fd);
            }
        }
    }

    public void initData() {
        SharedPreferencesUtils.put("preregisters", "");
        SharedPreferencesUtils.put("preregistration", "");
        SharedPreferencesUtils.put("PhotoListFile", "");
        cardTypes = (String) SharedPreferencesUtils.get("CarTypesList", "");
        if (cardTypes.equals("")) {
            getdata();
        }

        List<BikeCode> list = new ArrayList<BikeCode>();
        try {
            list = db.selector(BikeCode.class).where("type", "=", "13").findAll();
            if (list == null) {
                list = new ArrayList<BikeCode>();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }


        mLog.e(cardTypes + "==========" + list.size());
        CarTypeList = new ArrayList<BikeCode>();
        CarType = cardTypes.split(",");
        for (String s : CarType) {
            for (BikeCode bikeCode : list) {
                if (bikeCode.getCode().equals(s)) {
                    CarTypeList.add(bikeCode);
                }
            }
        }
        mLog.e("CarTypeList:" + CarTypeList.size());
        for (BikeCode bikeCode : CarTypeList) {
            mLog.e("CarTypeList:" + bikeCode.getName());
        }

        mAdapter = new myAdapter(Register);
        DialogView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_registeration, null);
        DialogView.setMinimumHeight(AppUtil.dp2px(CarTypeList.size() * 50));
        dialogRegistration = new DialogRegistration(getContext(), mAdapter, DialogView);


        dialogRegistration.setOnClickListener(new DialogRegistration.OnClickListener() {
            @Override
            public void onRegisterationClick(int position) {
                VehiclesStorageUtils.clearData();
                String type = CarTypeList.get(position).getCode();
                mLog.e("CarType=" + type);
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, type);
                if (IsDX_PR) {
                    ActivityUtil.goActivity(getActivity(), DX_PreRegistration_Car_Activity.class);
                } else {
                    Bundle bundle = new Bundle();
                    if (mAdapter.Name.equals(Register)) {
                        ActivityUtil.goActivity(getActivity(), RegisterCarActivity.class);
                    } else if (mAdapter.Name.equals(PreRegister)) {
                        if (locCityName.contains("昆明")) {
                            ActivityUtil.goActivity(getActivity(), JustFuckForKMActivity.class);
                        } else {
                            bundle.putString("in", "");
                            ActivityUtil.goActivityWithBundle(getActivity(), PreFirstActivity.class, bundle);

                        }
                    } else if (mAdapter.Name.equals(PreRegister_TJ)) {
                        bundle.putString("in", "TJ");
                        ActivityUtil.goActivityWithBundle(getActivity(), PreFirstActivity.class, bundle);
                    } else if (mAdapter.Name.equals(PreRegister_TJ_FREE)) {
                        ActivityUtil.goActivity(getActivity(), ShangPaiCarActivity.class);
                    }
                }
                mLog.e("VEHICLETYPE=" + VehiclesStorageUtils.getVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE));
                dialogRegistration.dismiss();
            }
        });
    }


    private void initView() {
        db = x.getDb(DBUtils.getDb());
        listModel.clear();
        locCityName = (String) SharedPreferencesUtils.get("locCityName", "");
        rolePowers = (String) SharedPreferencesUtils.get("rolePowers", "");
        city = (String) SharedPreferencesUtils.get("locCityName", "");
        REGISTRATION = (String) SharedPreferencesUtils.get("REGISTRATION", "");
        if (!REGISTRATION.equals("")) {
            Register = REGISTRATION;
        }
        mLog.e("rolePowers=" + rolePowers);
        try {
            baseInfos = db.selector(BaseInfo.class).where("cityName", "=", SharedPreferencesUtils.get("locCityName",
                    "")).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (baseInfos == null) {
            baseInfos = new ArrayList<BaseInfo>();
        }
        String[] powers = rolePowers.split(",");
        for (int i = 0; i < powers.length; i++) {
            for (int j = 0; j < s1.length; j++) {
                if (powers[i].equals(s1[j])) {
                    mLog.e("权限=" + powers[i]);
                    ItemModel model = new ItemModel();
                    model.setRolePower(s1[j]);
                    model.setItemName(funTitles[j]);
                    model.setItemBitResc(funImgs[j]);
                    listModel.add(model);
                }
            }
        }
        MainrolePowers = new ArrayList<String>();
        hot_new_mvp = new ArrayList<String>();

        boolean a = false;
        boolean b = false;
        boolean c = false;
        for (String power : powers) {
            if (power.equals("4001")) {
                a = true;
            }
            if (power.equals("4002")) {
                b = true;
            }
        }
        if (a && b) {
            c = true;
        }

        for (String power : powers) {
            if (Constants.JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE.equals(power)) {
                VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.HAS_FREE_SHANGPAI,"1");
            }
            for (Constants.Jurisdiction jurisdiction : Constants.JURISDICTIONS) {
                if (jurisdiction.getJur().equals(power)) {

                    if (power.equals("101")) {
                        MainrolePowers.add(power);
                        hot_new_mvp.add("hot");
                    } else if (power.equals("4001")) {
                        if (!c) {
                            MainrolePowers.add(power);
                            hot_new_mvp.add("");
                        }
                    } else {
                        MainrolePowers.add(power);
                        hot_new_mvp.add("");
                    }
                }
            }
        }
        mLog.e("MainrolePowers" + MainrolePowers.size());
        for (String mainrolePower : MainrolePowers) {
            mLog.e("MainrolePowers=" + mainrolePower);
        }


//        mLog.e(baseInfos.get(0).getCityName()+"权限CanPolicyEdit="+baseInfos.get(0).getCanPolicyEdit());
        if (baseInfos.size() > 0 && baseInfos.get(0).getCanPolicyEdit().equals("1")) {
            ItemModel model = new ItemModel();
            model.setRolePower("CanPolicyEdit");
            model.setItemName(funInsuranceTitles[0]);
            model.setItemBitResc(funInsurance[0]);
            listModel.add(model);
        }

        if (listModel.size() % 2 != 0) {
            ItemModel model = new ItemModel();
            model.setRolePower("");
            model.setItemName(funTitlesNone[0]);
            model.setItemBitResc(funImgsNone[0]);
            listModel.add(model);
        }

        setMainRecycler(MainrolePowers, hot_new_mvp);
        setStaggeredGridLayoutRecyclerView();


        Order CAR = new Order();
        CAR.setName("Car");
        CAR.setOrder(1);

        Order Personnel = new Order();
        Personnel.setName("Personnel");
        Personnel.setOrder(2);

        Order Insurance = new Order();
        Insurance.setName("Insurance");
        Insurance.setOrder(3);

        List<Order> ListO = new ArrayList<>();
        ListO.add(CAR);
        ListO.add(Personnel);
        ListO.add(Insurance);
        Gson G = new Gson();
        SharedPreferencesUtils.put("Order", G.toJson(ListO));
    }

    private void setMainRecycler(List<String> J, List<String> C) {
        //使用RecyclerView提供的默认的动画效果
        manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        RL_square.setLayoutManager(manager);
        //使用RecyclerView提供的默认的动画效果
        RL_square.setItemAnimator(new DefaultItemAnimator());
        //为Item添加分割线
        RL_square.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider,
                LinearLayoutManager.VERTICAL));
        RL_square.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider,
                LinearLayoutManager.HORIZONTAL));
        MRAdapter = new MainRecyclerAdapter(getActivity(), J, C);
        MRAdapter.setOnItemClickLitener(new MainRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                MainRecyclerItemClick(view);
            }
        });
        // 设置Adapter
        RL_square.setAdapter(MRAdapter);
    }

    private void MainRecyclerItemClick(View view) {
        String Jurisdiction = (String) view.getTag();
        Bundle bundle = new Bundle();
        switch (Jurisdiction) {
            case Constants.JURISDICTION_PRE_REGISTRATION://电信预登记
                IsDX_PR = true;
                goPreRegistration();
                break;
            case Constants.JURISDICTION_PRE_REGISTRATION_QUERY://电信预登记查询
                Bundle bundlet = new Bundle();
                bundlet.putString("in", "");
                ActivityUtil.goActivityWithBundle(getActivity(), DX_PreRegistrationQueryActivity.class, bundlet);
                break;
            case Constants.JURISDICTION_PRE_REGISTRATION_STATISTICS://电信预登记统计
                ActivityUtil.goActivity(getActivity(), DX_PreRegistration_Statistics_Activity.class);
                break;
            case Constants.JURISDICTION_REGISTRATION://备案登记
                IsDX_PR = false;
                goRegistration();
                break;
            case Constants.JURISDICTION_CHANGE_REGISTRATION://备案登记信息变更
                bundle.putString("rolePower", "ChangeFirstNormal");
                ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                break;
            case Constants.JURISDICTION_STATISTICS://备案登记个人统计
                bundle.putString("rolePower", "");
                ActivityUtil.goActivity(getActivity(), PersonalStatisticActivity.class);
                break;
            case Constants.JURISDICTION_PRE_REGISTRATION_SHANGPAI_STATISTICS://上牌统计
                bundle.putString("rolePower", "");
                if (locCityName.startsWith("天津")) {
                    ActivityUtil.goActivity(getActivity(), DX_PreRegistration_Statistics_Tj_Activity.class);
                } else {
                    ActivityUtil.goActivity(getActivity(), DX_PreRegistration_Statistics_Activity.class);
                }
                break;
            case Constants.JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE://天津车辆预登记（免费上牌）
                IsDX_PR = false;
                goShangPai();
                break;
            case Constants.JURISDICTION_PRE_REGISTRATION_SHANGPAI_FREE_QUERY://天津车辆预登记查询
                ActivityUtil.goActivity(getActivity(), ShangPaiQueryActivity.class);
                break;

            default:
                break;
        }
    }

    private void goRegistration() {
        VehiclesStorageUtils.clearData();
        String hasPreregister = (String) SharedPreferencesUtils.get("hasPreregister", "");
        if (hasPreregister.equals("1")) {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
            ActivityUtil.goActivity(getActivity(), PreSearchActivity.class);
        } else {
            if (CarType.length > 1) {
                mAdapter.setName(Register);
                dialogRegistration.show();
            } else {
                if (CarType[0].equals("")) {
                    Utils.showToast("车辆类型数据被清除，请重新登录以获取最新数据。");
                } else {
                    VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
                    ActivityUtil.goActivity(getActivity(), RegisterCarActivity.class);//车辆信息
                }
            }
            mLog.e("MyCarType=" + CarType[0]);
        }
    }

    private void goPreRegistration() {
        VehiclesStorageUtils.clearData();
        if (CarType.length > 1) {
            mAdapter.setName(PreRegister);
            dialogRegistration.show();
        } else {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
            ActivityUtil.goActivity(getActivity(), DX_PreRegistration_Car_Activity.class);
        }
        mLog.e("MyCarType=" + CarType[0]);
    }

    private void goShangPai() {
        VehiclesStorageUtils.clearData();
        if (CarType.length > 1) {
            mAdapter.setName(PreRegister_TJ_FREE);
            dialogRegistration.show();
        } else {
            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
            ActivityUtil.goActivity(getActivity(), ShangPaiCarActivity.class);
        }
    }

    private void setStaggeredGridLayoutRecyclerView() {
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        RV_func.setLayoutManager(manager);
        //使用RecyclerView提供的默认的动画效果
        RV_func.setItemAnimator(new DefaultItemAnimator());
        //为Item添加分割线
        RV_func.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider,
                LinearLayoutManager.VERTICAL));
        RV_func.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider,
                LinearLayoutManager.HORIZONTAL));
        // 创建Adapter，并指定数据集
        mRecyclerAdapter = new RecyclerAdapter(getActivity(), listModel);
        // 为Item具体实例点击3种事件
        mRecyclerAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSubViewClick(View view, int postion) {
            }

            @Override
            public void onItemLongClick(View view, int postion) {
            }

            @Override
            public void onItemClick(View view, int postion) {
                String rolePower = listModel.get(postion).getRolePower();
                mLog.e("rolePower=" + rolePower);
                Bundle bundle = new Bundle();
                switch (rolePower) {
                    case "CanPolicyEdit"://保险变更
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
//                        ActivityUtil.goActivity(getActivity(), InsuranceQueryActivity.class);
                        break;
                    case "101"://添加电动车
                        break;
                    case "102"://
                        break;
                    case "103"://删除电动车
                        break;
                    case "104"://查看电动车
                        break;
                    case "105"://车牌变更,车牌补办
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        //ActivityUtil.goActivityWithBundle(getActivity(), PreSearchActivity.class, bundle);
                        break;
                    case "108"://车辆过户
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        break;

                    case "109"://车辆报废
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        break;

                    case "1001"://车辆布控
                        ActivityUtil.goActivityWithBundle(getActivity(), VehicleMonitorActivity.class, bundle);
                        break;

                    case "3700"://保险信息统计
                        ActivityUtil.goActivityWithBundle(getActivity(), StatisticActivity.class, bundle);
                        break;
                    case "2700"://民警现场预登记
                        VehiclesStorageUtils.clearData();
                        IsDX_PR = false;
                        if (CarType.length > 1) {
                            mAdapter.setName(PreRegister);
                            dialogRegistration.show();
                        } else {
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
                            if (locCityName.contains("昆明")) {
                                ActivityUtil.goActivity(getActivity(), JustFuckForKMActivity.class);
                            } else {
                                Bundle bundlet = new Bundle();
                                bundlet.putString("in", "");
                                ActivityUtil.goActivityWithBundle(getActivity(), PreFirstActivity.class, bundlet);
                            }
                        }
                        mLog.e("MyCarType=" + CarType[0]);
                        //ActivityUtil.goActivity(getActivity(), MapActivity.class);
                        break;

                    case "113"://保险理赔
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        break;

                    case "1300106":
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        break;
                    case "600"://预约查询
                        ActivityUtil.goActivity(getActivity(), AppointmentQueryActivity.class);
                        break;
                    case "199"://车辆回访
                        ActivityUtil.goActivity(getActivity(), Visit_1_Activity.class);
                        break;
                    case "114"://服务延期
                        bundle.putString("rolePower", rolePower);
                        ActivityUtil.goActivityWithBundle(getActivity(), ElectricInfoSearchActivity.class, bundle);
                        break;
                    case "2713"://天津车辆预登记（登记上牌）
                        IsDX_PR = false;
                        if (CarType.length > 1) {
                            mAdapter.setName(PreRegister_TJ);
                            dialogRegistration.show();
                        } else {
                            VehiclesStorageUtils.setVehiclesAttr(VehiclesStorageUtils.VEHICLETYPE, CarType[0]);
                            Bundle bundlet = new Bundle();
                            bundlet.putString("in", "TJ");
                            ActivityUtil.goActivityWithBundle(getActivity(), PreFirstActivity.class, bundlet);
                        }
                        mLog.e("MyCarType=" + CarType[0]);
                        break;
                    case "2714"://天津车辆预登记查询
                        Bundle bundlet = new Bundle();
                        bundlet.putString("in", "TJ");
                        ActivityUtil.goActivityWithBundle(getActivity(), DX_PreRegistrationQueryActivity.class,
                                bundlet);
                        break;
                    case "122"://更换标签

                        ActivityUtil.goActivity(getActivity(), LabelBindingCarQueryActivity.class);
                        break;

                    default:
                        break;
                }
            }
        });
        // 设置Adapter
        RV_func.setAdapter(mRecyclerAdapter);
        mLog.e("333333333333333333333333");
    }

    public class myAdapter extends BaseAdapter {
        String Name = "";

        public myAdapter(String name) {
            Name = name;
        }

        public void setName(String name) {
            Name = name;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return CarTypeList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(getActivity(), R.layout.layout_dialog_item, null);
            TextView TV_cartype = (TextView) v.findViewById(R.id.TV_cartype);
            v.setMinimumHeight(AppUtil.dp2px(50));
            TV_cartype.setText(CarTypeList.get(position).getName() + Name);
            return v;
        }

    }

    private void getdata() {
        String city = (String) SharedPreferencesUtils.get("locCityName", "");
        List<BaseInfo> resultList = new ArrayList<BaseInfo>();
        try {
            resultList = db.selector(BaseInfo.class).where("cityName", "=", city).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (resultList == null) {
            resultList = new ArrayList<BaseInfo>();
        }
//        Log.e("Pan","resultList:"+resultList.toString());
        if (resultList.size() != 0) {
            String plateNumberRegular = resultList.get(0).getPlatenumberRegular();
//            Log.e("Pan","plateNumberRegular="+plateNumberRegular);
            try {
                JSONArray array = new JSONArray(plateNumberRegular);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);
                    String carType = json.getString("CarType");
//                    SharedPreferencesUtils.put("carType", carType);//备案登记车种
                    String regular = json.getString("Regular");
                    SharedPreferencesUtils.put("PlatenumberRegular" + carType, regular);//车牌正则
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String bluetoothRegular = resultList.get(0).getBluetooth_Regular();
//            mLog.e("bluetoothRegular=" +bluetoothRegular);
            try {
                JSONArray jsonArray = new JSONArray(bluetoothRegular);
                for (int i = 0; i < jsonArray.length(); i++) {
// [{"KEY":"100000000188",
// "DEVICETYPE":"8001,8002",
// "CONTENT":"ELDER",
// "PCCODE":"0802",
// "PROVINCEABBR":"GX",
// "CITYABBR":"LZ",
// "XQCode":""}]
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("KEY");
                    SharedPreferencesUtils.put("key", key);
                    String deviceType = jsonObject.getString("DEVICETYPE");
                    SharedPreferencesUtils.put("deviceType", deviceType);
                    String content = jsonObject.getString("CONTENT");
                    SharedPreferencesUtils.put("content", content);
                    String pcCode = jsonObject.getString("PCCODE");
                    SharedPreferencesUtils.put("pcCode", pcCode);
                    String provinceAbbr = jsonObject.getString("PROVINCEABBR");
                    SharedPreferencesUtils.put("provinceAbbr", provinceAbbr);
                    String cityAbbr = jsonObject.getString("CITYABBR");
                    SharedPreferencesUtils.put("cityAbbr", cityAbbr);
                    String XQCode = jsonObject.getString("XQCode");
                    SharedPreferencesUtils.put("XQCode", XQCode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String appConfig = resultList.get(0).getAppConfig();
            SharedPreferencesUtils.put("isChecked", "");
            SharedPreferencesUtils.put("whiteListUrl", "");
            SharedPreferencesUtils.put("hasPreregister", "");
            SharedPreferencesUtils.put("isScanLabel", "");
            SharedPreferencesUtils.put("isScanCard", "");
            try {
                JSONArray jsonArray = new JSONArray(appConfig);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    switch (key) {
                        case "WhiteListApp":
                            String isChecked = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isChecked", isChecked);
                            break;
                        case "WhiteListUrl":
                            String whiteListUrl = jsonObject.getString("value");
                            SharedPreferencesUtils.put("whiteListUrl", whiteListUrl);
                            break;
                        case "HasPreregister":
                            String hasPreregister = jsonObject.getString("value");
                            SharedPreferencesUtils.put("hasPreregister", hasPreregister);
                            break;
                        case "IsScanLabel":
                            String isScanLabel = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isScanLabel", isScanLabel);
                            break;
                        case "IsScanCard":
                            String isScanCard = jsonObject.getString("value");
                            SharedPreferencesUtils.put("isScanCard", isScanCard);
                            break;
                        case "THEFTNO1_REGULAR":
                            String REGULAR = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR", REGULAR);
                            break;
                        case "THEFTNO2_REGULAR":
                            String REGULAR2 = jsonObject.getString("value");
                            SharedPreferencesUtils.put("REGULAR2", REGULAR2);
                            break;
                        case "IsDoubleSign":
                            String ISDOUBLESIGN = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsDoubleSign", ISDOUBLESIGN);
                            break;
                        case "EnableInvoice":
                            String EnableInvoice = jsonObject.getString("value");
                            SharedPreferencesUtils.put("EnableInvoice", EnableInvoice);
                            break;
                        case "IsShowPay":
                            String IsShowPay = jsonObject.getString("value");
                            SharedPreferencesUtils.put("IsShowPay", IsShowPay);
                            break;
                        case "ChangeType":
                            String ChangeType = jsonObject.getString("value");
                            SharedPreferencesUtils.put("ChangeType", ChangeType);
                            mLog.e("ChangeType" + ChangeType);
                            break;
                        case "InterfaceVersion":
                            String InterfaceVersion = jsonObject.getString("value");
                            SharedPreferencesUtils.put("InterfaceVersion", InterfaceVersion);
                            mLog.e("InterfaceVersion" + InterfaceVersion);
                            break;

                        default:
                            break;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Utils.CompleteConfig();//补齐缺失字段


            SharedPreferencesUtils.put("appName", resultList.get(0).getAppName());
            SharedPreferencesUtils.put("apiUrl", resultList.get(0).getApiUrl());
            SharedPreferencesUtils.put("CarTypesList", resultList.get(0).getCardType());
            SharedPreferencesUtils.put("fullSpell", resultList.get(0).getFullSpell());
            SharedPreferencesUtils.put("cityCode", resultList.get(0).getCityCode());
            String v = resultList.get(0).getVersion();
            if (v == null) {
                mLog.e("Version值为null");
                v = "";
            }
            SharedPreferencesUtils.put("Version", v);
            mLog.e("Version=" + resultList.get(0).getVersion());

        }
    }
}
