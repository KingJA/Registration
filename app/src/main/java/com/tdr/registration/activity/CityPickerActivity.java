package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tdr.registration.R;
import com.tdr.registration.adapter.CityListAdapter;
import com.tdr.registration.adapter.ResultListAdapter;
import com.tdr.registration.model.BaseInfo;
import com.tdr.registration.model.LocateState;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.LocationUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.SideLetterBar;


import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 城市选择
 */
public class CityPickerActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "CityPickerActivity";

    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";

    private Context mContext;

    private CityListAdapter mCityListAdapter;
    private ResultListAdapter mResultListAdapter;
    private List<BaseInfo> mAllCities;
    private List<BaseInfo> TestCities;
    private DbManager db;
    private CharacterParser characterParser;
    private long lastClickTime = 0;
    private int clickNum = 0;
    private boolean isOpenBackDoor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);

        mContext = this;

        db = x.getDb(DBUtils.getDb());


        initData();
        initView();
        LocationUtil.getLocation(new LocationUtil.OnLocationListener() {
            @Override
            public void Location(String City) {
                if (City.equals("")) {
                    mCityListAdapter.updateLocateState(LocateState.FAILED, null);
                } else {
                    mCityListAdapter.updateLocateState(LocateState.SUCCESS, City);
                }
            }
        });
    }

    private ImageView image_back;
    private TextView text_title;

    private EditText edit_search;
    private ImageView image_searchClear;
    private ListView list_allCity;
    private TextView text_letterOverlay;
    private SideLetterBar side_letter_bar;
    private ListView list_searchResult;
    private ViewGroup linear_empty;

    private void initView() {
        image_back = (ImageView) findViewById(R.id.image_back);
        image_back.setOnClickListener(this);
        text_title = (TextView) findViewById(R.id.text_title);
        text_title.setText("选择城市");
        text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBackDoor();
            }
        });
        list_allCity = (ListView) findViewById(R.id.list_allCity);
        list_allCity.setAdapter(mCityListAdapter);

        text_letterOverlay = (TextView) findViewById(R.id.text_letterOverlay);
        side_letter_bar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        side_letter_bar.setOverlay(text_letterOverlay);
        side_letter_bar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityListAdapter.getLetterPosition(letter);
                list_allCity.setSelection(position);
            }
        });
        edit_search = (EditText) findViewById(R.id.edit_search);

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });

        linear_empty = (ViewGroup) findViewById(R.id.linear_empty);
        list_searchResult = (ListView) findViewById(R.id.list_searchResult);
        list_searchResult.setAdapter(mResultListAdapter);
        list_searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityBack(mResultListAdapter.getItem(position));
            }
        });

        image_searchClear = (ImageView) findViewById(R.id.image_searchClear);
        image_searchClear.setOnClickListener(this);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        filterStr = filterStr.toLowerCase();
        if (TextUtils.isEmpty(filterStr)) {
            image_searchClear.setVisibility(View.GONE);
            linear_empty.setVisibility(View.GONE);
            list_searchResult.setVisibility(View.GONE);
        } else {
            image_searchClear.setVisibility(View.VISIBLE);
            list_searchResult.setVisibility(View.VISIBLE);

            List<BaseInfo> filterDateList = new ArrayList<BaseInfo>();
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mAllCities;
            } else {
                filterDateList.clear();
                for (BaseInfo mAllCity : mAllCities) {
                    String name = mAllCity.getCityName();
                    if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(new BaseInfo(mAllCity.getCityName(), mAllCity.getFullSpell()));
                    }
                }
            }

            // 根据a-z进行排序
            Collections.sort(filterDateList, new CityComparator());
            if (filterDateList == null || filterDateList.size() == 0) {
                linear_empty.setVisibility(View.VISIBLE);
            } else {
                linear_empty.setVisibility(View.GONE);
                mResultListAdapter.changeData(filterDateList);
            }
        }
    }

    private void openBackDoor() {
        if (isOpenBackDoor) {
            return;
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long timeDiff = currentTime - lastClickTime;
        if (timeDiff < 700) {
            clickNum++;
        } else if (timeDiff > 3000) {
            clickNum = 0;
        }
        lastClickTime = currentTime;
        mLog.e(timeDiff + "clickNum=" + clickNum);
        if (clickNum > 5) {
            if (clickNum < 10) {
                Toast.makeText(mContext, "再点击" + (10 - clickNum) + "下开启测试模式", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "测试模式已开启", Toast.LENGTH_LONG).show();
                isOpenBackDoor = true;
                mAllCities.addAll(TestCities);
                mLog.e("mAllCities" + mAllCities.size());
                Collections.sort(mAllCities, new CityComparator());
                mCityListAdapter.UpdateCities(mAllCities);
            }
        }
    }

    /**
     * 单击选择城市
     *
     * @param city
     */
    private void cityBack(BaseInfo city) {
        String cityName = city.getCityName();
        Utils.myToast(mContext, "选择的城市：" + cityName);
        Intent intent = new Intent();
        intent.setClass(CityPickerActivity.this, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("city", (Serializable) city);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void initData() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        TestCities = new ArrayList<BaseInfo>();
        mAllCities = new ArrayList<BaseInfo>();
        List<BaseInfo> Cities = new ArrayList<BaseInfo>();
        try {
            Cities = db.findAll(BaseInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (Cities == null) {
            Cities = new ArrayList<BaseInfo>();
        }
//        mAllCities = db.findAll(BaseInfo.class);
        mLog.e("总数=" + Cities.size());
        for (BaseInfo b : Cities) {
            if (b != null) {
                if (b.getCityCode() != null && b.getCityCode().equals("0000")) {
                    mLog.e("CityName=" + b.getCityName());
                    mLog.e("CityCode=" + b.getCityCode());
                    TestCities.add(b);
                } else {
                    mAllCities.add(b);
                }
            }
        }

        mLog.e("显示城市=" + mAllCities.size());
        mLog.e("隐藏城市=" + TestCities.size());

        Collections.sort(mAllCities, new CityComparator());
        mCityListAdapter = new CityListAdapter(this, mAllCities);
        mCityListAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                Log.e(TAG, name);
//                List<BaseInfo> resultList = db.findAllByWhere(BaseInfo.class, " cityName=\"" + name + "\"");
                List<BaseInfo> resultList = null;
                try {
                    resultList = db.selector(BaseInfo.class).where("cityName", "=", name).findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (resultList == null) {
                    resultList = new ArrayList<BaseInfo>();
                }
                if (resultList.size() != 0) {
                    cityBack(resultList.get(0));
                } else {
                    Utils.myToast(mContext, name + "即将开放");
                }
            }

            @Override
            public void onLocateClick() {
                Log.e(TAG, "重新定位...");
                mCityListAdapter.updateLocateState(LocateState.LOCATING, null);
            }
        });
        mResultListAdapter = new ResultListAdapter(this, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                finish();
                break;

            case R.id.image_searchClear:
                edit_search.setText("");
                image_searchClear.setVisibility(View.GONE);
                linear_empty.setVisibility(View.GONE);
                list_searchResult.setVisibility(View.GONE);
                break;
        }
    }




    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<BaseInfo> {
        @Override
        public int compare(BaseInfo lhs, BaseInfo rhs) {
            String a = lhs.getFullSpell().substring(0, 1);
            String b = rhs.getFullSpell().substring(0, 1);
            return a.compareTo(b);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
