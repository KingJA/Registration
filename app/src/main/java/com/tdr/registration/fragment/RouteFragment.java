package com.tdr.registration.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tdr.registration.R;
import com.tdr.registration.adapter.RouteAdapter;
import com.tdr.registration.activity.MapActivity;
import com.tdr.registration.model.MonitorModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.WebServiceUtils;
import com.tdr.registration.view.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 轨迹信息
 * Created by Linus_Xie on 2016/10/15.
 */

@SuppressLint("ValidFragment")
public class RouteFragment extends Fragment {
    private static final String TAG = "RouteFragment";
    TextView textSearchTime;
    LinearLayout linearSearchTime;
    LinearLayout linearMapRoute;
    ListView listRoute;

    private String plateNumber;
    private Gson mGson;

    private ZProgressHUD mProgressHUD;

    private List<MonitorModel> monitorModels;

    private RouteAdapter mAdapter;

    public RouteFragment(String plateNumber) {
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
        View view = View.inflate(getActivity(), R.layout.fragment_route, null);
        textSearchTime = (TextView) view.findViewById(R.id.text_searchTime);
        linearSearchTime = (LinearLayout) view.findViewById(R.id.linear_searchTime);
        linearMapRoute = (LinearLayout) view.findViewById(R.id.linear_mapRoute);
        linearMapRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList();
                list.add(monitorModels);
                bundle.putParcelableArrayList("monitorModels", list);
                ActivityUtil.goActivityWithBundle(getActivity(), MapActivity.class, bundle);
            }
        });
        listRoute = (ListView) view.findViewById(R.id.list_route);
        return view;

    }

    private void initData() {
        mProgressHUD.show();
        final HashMap<String, String> map = new HashMap<>();
        map.put("accessToken", (String) SharedPreferencesUtils.get("token", ""));
        map.put("plateNumber", plateNumber);
        map.put("pageIdx", "0");
        map.put("pageSize", "10");
        WebServiceUtils.callWebService((String) SharedPreferencesUtils.get("apiUrl", ""), Constants.WEBSERVER_GETMONITORDATA, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int errorCode = object.getInt("ErrorCode");
                        String data = object.getString("Data");
                        if (errorCode == 0) {
                            mProgressHUD.dismiss();
                            monitorModels = mGson.fromJson(data, new TypeToken<List<MonitorModel>>() {
                            }.getType());
                            mAdapter = new RouteAdapter(getActivity(),monitorModels);
                            listRoute.setAdapter(mAdapter);
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
