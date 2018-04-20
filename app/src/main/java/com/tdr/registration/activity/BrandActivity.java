package com.tdr.registration.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tdr.registration.R;
import com.tdr.registration.activity.kunming.RegisterFirstKunMingActivity;
import com.tdr.registration.adapter.BrandListAdapter;
import com.tdr.registration.adapter.ResultBrandListAdapter;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.util.BrandSortUtils;
import com.tdr.registration.util.CharacterParser;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.PinYinUtil;
import com.tdr.registration.util.PinyinComparator;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.SideBar;
import com.tdr.registration.view.ZProgressHUD;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 品牌列表
 */
public class BrandActivity extends Activity implements Handler.Callback {

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
    //@BindView(R.id.list_resultBrand)
    //ListView listResultBrand;

    private ViewGroup emptyView;

    /**
     * 汉字转换成拼音的类
     */
//    private CharacterParser characterParser;
    private List<SortModel> sortModelList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private static PinyinComparator pinyinComparator = new PinyinComparator();

    private List<BikeCode> brandsList = new ArrayList<BikeCode>();
    private List<SortModel> hotSortList = new ArrayList<SortModel>();

    private ZProgressHUD mProgressHUD;

    private Context mContext;
    private Handler mHandler;
    private DbManager db;

    private BrandListAdapter mBrandListAdapter;
    private ResultBrandListAdapter mResultBrandListAdapter;
    private final int LOAD_FAILED = -1;
    private final int LOAD_SUCCESS = 1;
    private final int TRIGGER_SERACH = 0;
    private final int REFLISH = 3;
    private final int UPDATEUI = 2;
    private SearchCar SC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        ButterKnife.bind(this);
        mContext = this;
        mHandler = new Handler(this);
        db = x.getDb(DBUtils.getDb());
        SC=new SearchCar();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SortModel> mSortList = new ArrayList<SortModel>();
//                brandsList = db.findAllByWhere(BikeCode.class, " type='1'");
                try {
                    brandsList = db.selector(BikeCode.class).where("type", "=", "1").findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if (brandsList == null) {
                    brandsList = new ArrayList<BikeCode>();
                }
                try {
                    for (int i = 0; i < brandsList.size(); i++) {
                        SortModel sortModel = new SortModel();
                        sortModel.setGuid(brandsList.get(i).getCode());
                        sortModel.setName(brandsList.get(i).getName());
                        sortModel.setSort(brandsList.get(i).getSort());
//                        mLog.e("品牌名字：" + brandsList.get(i).getName());
                        if (isSpecialChar(brandsList.get(i).getName())) {
                            mLog.e("异常" + brandsList.get(i).getName());
                        } else {
                            String pinyin = PinYinUtil.getPinYin(brandsList.get(i).getName());
                            String sortString = pinyin.substring(0, 1).toUpperCase();
                            // 正则表达式，判断首字母是否是英文字母
                            if (sortString.matches("[A-Z]")) {
                                sortModel.setSortLetters(sortString
                                        .toUpperCase());
                            } else {
                                sortModel.setSortLetters("#");
                            }
                            mSortList.add(sortModel);
                            hotSortList.add(sortModel);
                        }

                    }

                    // 根据a-z进行排序源数据
                    Collections.sort(mSortList, pinyinComparator);
                    //热门品牌排序
                    Collections.sort(hotSortList, new BrandSortUtils());
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(LOAD_FAILED);
                }
                Message message = new Message();
                message.what = LOAD_SUCCESS;
                message.obj = mSortList;
                mHandler.sendMessage(message);
            }
        }).start();


    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ ＊_`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
    private CharacterParser characterParser;
    private List<SortModel> filterData(String s) {
        mLog.e("开始搜索"+s);
        List<SortModel> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = sortModelList;
        } else {
            filterDataList.clear();
            for (SortModel sortModel : sortModelList) {
                String name = sortModel.getName();
//                mLog.e(s+"  name:"+sortModel.getName());
//                boolean a=name.indexOf(s.toString()) != -1;
//                boolean b=characterParser.getSelling(name).startsWith(s.toString());
//                mLog.e("name:"+name+"  A："+a+"  B："+b);
                if (name.indexOf(s.toString()) != -1 || PinYinUtil.getPinYin(name).startsWith(s.toString())) {
                    filterDataList.add(sortModel);
                }
                if (name.toUpperCase().indexOf(
                        s.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(s.toString().toUpperCase())) {
//                if (PinYinUtil.checkname(name, s.toString())) {
//                    mLog.e("check", "条件：" + name + "   本地：" + s);
                    filterDataList.add(sortModel);
//                }
                }
            }
            mLog.e("-----------------------------------------");
            for (SortModel sortModel : filterDataList) {
                mLog.e(s + "  add:" + sortModel.getName());
            }
            mLog.e("-----------------------------------------");
            for (SortModel sortModel : sortModelList) {
                if (sortModel.getName().contains(s)) {
                    mLog.e(s + "  name:" + sortModel.getName());
                }
            }
            mLog.e("-----------------------------------------");
//            mLog.e("李马大1 "+filterDataList.size());
        }
        // 根据a-z进行排序
        Collections.sort(filterDataList, pinyinComparator);
        Message message = new Message();
        message.what = UPDATEUI;
        message.obj =filterDataList;
        mLog.e("搜索结束"+s);
        mHandler.sendMessage(message);
//        mBrandListAdapter.updateListView(filterDataList);
        return filterDataList;
//        mLog.e("李马大2 "+filterDataList.size());
    }


    private void selectedBrand(SortModel sortModel) {
        Intent intent = new Intent();
        intent.setClass(BrandActivity.this, RegisterFirstKunMingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("brandName", sortModel.getName());
        bundle.putString("brandCode", sortModel.getGuid());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initView() {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textTitle.setText("车辆品牌");
        mProgressHUD = new ZProgressHUD(BrandActivity.this);
        mProgressHUD.setSpinnerType(ZProgressHUD.SIMPLE_ROUND_SPINNER);
        mProgressHUD.setMessage("");
        mProgressHUD.show();

//        pinyinComparator = new PinyinComparator();
        sidrbar.setTextView(textDialog);
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mBrandListAdapter.getLetterPosition(s);
                if (position == -1) {
                    position += 1;
                }
                listBrand.setSelection(position);
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mHandler.hasMessages(TRIGGER_SERACH)) {
                    mHandler.removeMessages(TRIGGER_SERACH);
                }
                Message message = new Message();
                if (!TextUtils.isEmpty(s.toString())) {
                    message.what = TRIGGER_SERACH;
                    message.obj = s.toString();
                    mHandler.sendMessage(message);
                }else{
                    message.what = REFLISH;
                    mHandler.sendMessage(message);
                }

            }
        });
    }

    @Override
    public boolean handleMessage( Message msg) {
        switch (msg.what) {
            case LOAD_FAILED:
                mProgressHUD.dismiss();
                mLog.e("1111");
                Utils.myToast(mContext, "车辆品牌加载失败，请重新读取！");
                break;
            case UPDATEUI:
                mBrandListAdapter.updateListView((List<SortModel>) msg.obj);
//                mProgressHUD.dismiss();
                break;
            case TRIGGER_SERACH:
//                mProgressHUD.show();
//                SC.START((String) msg.obj);
                SC.setSearch((String) msg.obj);
                new Thread(SC).start();
                break;

            case REFLISH:
                mBrandListAdapter.updateListView(sortModelList);
                break;
            case LOAD_SUCCESS:
                mLog.e("2222");
                mProgressHUD.dismiss();
                sortModelList = (List<SortModel>) msg.obj;
                if (brandsList.size() > 0) {
                    mBrandListAdapter = new BrandListAdapter(this, sortModelList, hotSortList);
                    listBrand.setAdapter(mBrandListAdapter);
                    mBrandListAdapter.setOnBrandClickListener(new BrandListAdapter.OnBrandClickListener() {
                        @Override
                        public void onBrandClick(SortModel sortModel) {
                            selectedBrand(sortModel);
                        }
                    });
                } else {
                    Utils.myToast(mContext, "车辆品牌加载失败，请重新读取！");
                }

                break;
        }

        return false;
    }

    public class SearchCar implements Runnable{
        Lock lock;
        private String Search="";

        public  SearchCar(){

            lock = new ReentrantLock();// 锁对象
        }
        public void setSearch(String search){
            Search=search;
        }

        @Override
        public void run() {
                lock.lock();      // 得到锁
                try {
                    filterData(Search);
                } finally {
                    lock.unlock();// 释放锁
                }
        }
    }

}
