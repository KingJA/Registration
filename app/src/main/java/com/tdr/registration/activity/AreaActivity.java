package com.tdr.registration.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.SortAdapter;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.model.AreaModel;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.SideBar;
import com.tdr.registration.view.ZProgressHUD;


import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 当前城市区域选择器
 */
public class AreaActivity extends BaseActivity implements Handler.Callback {
    private static final String TAG = "AreaActivity";

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.image_searchClear)
    ImageView imageSearchClear;
    @BindView(R.id.list_brand)
    ListView listBrand;
    @BindView(R.id.text_dialog)
    TextView textDialog;
    @BindView(R.id.sidrbar)
    SideBar sidrbar;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> sortModelList;
    private SortAdapter adapter;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private List<AreaModel> areaModels = new ArrayList<AreaModel>();
    private ZProgressHUD mProgressHUD;
    private DbManager db;
    private Context mContext;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        ButterKnife.bind(this);
        mHandler = new Handler(this);
        mContext = this;
        db = x.getDb(DBUtils.getDb());

        initView();
        initData();
    }

    private void initView() {
        mProgressHUD = new ZProgressHUD(AreaActivity.this);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
        mProgressHUD.show();
        textTitle.setText("辖区选择");
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sidrbar.setTextView(textDialog);
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listBrand.setSelection(position);
                }
            }
        });
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SortModel> mSortList = new ArrayList<SortModel>();
//                areaModels = db.findAllByWhere(AreaModel.class, " unitno like \'" + SharedPreferencesUtils.get("regionNo", "") + "%\'");
                try {
                    mLog.e("regionNo"+SharedPreferencesUtils.get("regionNo", ""));
                    areaModels= db.selector(AreaModel.class).where("unitno", "LIKE", SharedPreferencesUtils.get("regionNo", "")+"%").findAll();
                    List<AreaModel> AM  =db.findAll(AreaModel.class);
                    for (AreaModel areaModel : AM) {
                        mLog.e("Name"+areaModel.getName());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(areaModels==null){
                    areaModels=new ArrayList<AreaModel>();
                }
                mLog.e("当前帐号辖区数量：", "" + areaModels.size());
                try {
                    for (int i = 0; i < areaModels.size(); i++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setGuid(areaModels.get(i).getValue());
                        sortModel.setName(areaModels.get(i).getName());
                        String pinyin = characterParser
                                .getSelling(areaModels.get(i).getName());
                        String sortString = pinyin.substring(0, 1)
                                .toUpperCase();
                        // 正则表达式，判断首字母是否是英文字母
                        if (sortString.matches("[A-Z]")) {
                            sortModel.setSortLetters(sortString
                                    .toUpperCase());
                        } else {
                            sortModel.setSortLetters("#");
                        }
                        mSortList.add(sortModel);
                    }
                    // 根据a-z进行排序源数据
                    Collections.sort(mSortList, pinyinComparator);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(-1);
                }
                Message message = new Message();
                message.what = 1;
                message.obj = mSortList;
                mHandler.sendMessage(message);
            }
        }).start();

        listBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(AreaActivity.this, StatisticActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("areaName", ((SortModel) adapter.getItem(position)).getName());
                bundle.putString("areaValue", ((SortModel) adapter.getItem(position)).getGuid());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterData(String s) {
        List<SortModel> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = sortModelList;
        } else {
            filterDataList.clear();
            for (SortModel sortModel : sortModelList) {
                String name = sortModel.getName();
                if (name.indexOf(s.toString()) != -1 || characterParser.getSelling(name).startsWith(s.toString())) {
                    filterDataList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDataList, pinyinComparator);
        adapter.updateListView(filterDataList);
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case -1:
                mProgressHUD.dismiss();
                Utils.myToast(mContext, "辖区加载失败，请重新读取！");
                break;

            case 0:

                break;

            case 1:
                mProgressHUD.dismiss();
                sortModelList = (List<SortModel>) msg.obj;
                if (areaModels.size() > 0) {
                    adapter = new SortAdapter(this, sortModelList);
                    listBrand.setAdapter(adapter);
                } else {

                }

                break;
        }
        return false;
    }
}
