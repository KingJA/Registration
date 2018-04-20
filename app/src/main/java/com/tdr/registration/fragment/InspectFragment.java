package com.tdr.registration.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.CarCheckActivity;
import com.tdr.registration.activity.CommandCenterActivity;
import com.tdr.registration.activity.PatrolActivity;
import com.tdr.registration.activity.PatrolActivity2;
import com.tdr.registration.activity.SeizureSearchActivity;
import com.tdr.registration.activity.VehiclesSeizeActivity;
import com.tdr.registration.adapter.GridAdapter;
import com.tdr.registration.adapter.RecyclerAdapter;
import com.tdr.registration.model.ItemModel;
import com.tdr.registration.util.ActivityUtil;
import com.tdr.registration.util.ItemClickListener;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.view.ItemDecorationDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 稽查Fragment
 */
@ContentView(R.layout.fragment_inspect)
public class InspectFragment extends Fragment  {

    @ViewInject(R.id.GV_square)
    private GridView GV_square;

    @ViewInject(R.id.RV_func)
    private RecyclerView RV_func;

    @ViewInject(R.id.image_appName)
    private ImageView imageAppName;

    @ViewInject(R.id.text_appName)
    private TextView textAppName;

    @ViewInject(R.id.linear_title)
    private LinearLayout linear_title;

    @ViewInject(R.id.image_desc)
    private ImageView image_desc;


    private int[] gridImgs = {R.mipmap.ic_inspect, R.mipmap.ic_seizure, R.mipmap.ic_seizuremanager};
    private String[] gridTags = {"new", "", ""};//ic小标签

    private int[] funImgs = {R.mipmap.ic_query, R.mipmap.ic_command, R.mipmap.ic_patrol};
    private int[] funImgsNone = {R.mipmap.ic_none};
    private String[] funTitles = {"车辆查询", "指令中心","巡逻模式"};//ic小标签
    private String[] funTitlesNone = {""};

    private String rolePowers = "";//权限
    String[] s1 = {"104", "1300103", ""};
    private GridAdapter mGridAdapter;
    private StaggeredGridLayoutManager manager;
    private RecyclerAdapter mRecyclerAdapter;
    private ArrayList<ItemModel> listModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        setTitileName();
        setdate();
        setAdapter();
        return view;
    }

    /**
     * 设置标题
     */
    private void setTitileName(){
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
            textAppName.append("   "+appName);
            if (appName.contains("防盗")) {
                image_desc.setBackgroundResource(R.mipmap.home_desc_fd);
            }
        }
    }

    /**
     * 加载数据
     */
    private void setdate(){
        rolePowers = (String) SharedPreferencesUtils.get("rolePowers", "");
        listModel = new ArrayList<ItemModel>();
        String[] powers = rolePowers.split(",");
        for (int i = 0; i < powers.length; i++) {
            for (int j = 0; j < s1.length; j++) {
                if (powers[i].equals(s1[j])) {
                    ItemModel model = new ItemModel();
                    model.setRolePower(s1[j]);
                    model.setItemName(funTitles[j]);
                    model.setItemBitResc(funImgs[j]);
                    listModel.add(model);
                }
            }
        }
        if (listModel.size() % 2 != 0) {//说明权限是奇数个，则最后一个补空白
            ItemModel model = new ItemModel();
            model.setRolePower("");
            model.setItemName(funTitlesNone[0]);
            model.setItemBitResc(funImgsNone[0]);
            listModel.add(model);
        }

    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        mGridAdapter = new GridAdapter(getActivity(), gridImgs, gridTags);
        GV_square.setAdapter(mGridAdapter);
        GV_square.setOnItemClickListener(new GridViewItemClick());

        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        RV_func.setLayoutManager(manager);
        //创建数据集

        //使用RecyclerView提供的默认的动画效果
        RV_func.setItemAnimator(new DefaultItemAnimator());
        //为Item添加分割线
        RV_func.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider, LinearLayoutManager.VERTICAL));
        RV_func.addItemDecoration(new ItemDecorationDivider(getActivity(), R.drawable.recycler_divider, LinearLayoutManager.HORIZONTAL));
        // 创建Adapter，并指定数据集
        mRecyclerAdapter = new RecyclerAdapter(getActivity(), listModel);
        // 为Item具体实例点击3种事件
        mRecyclerAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSubViewClick(View view, int postion) {}
            @Override
            public void onItemLongClick(View view, int postion) {}
            @Override
            public void onItemClick(View view, int postion) {
                RecyclerItemClick(postion);
            }
        });
        // 设置Adapter
        RV_func.setAdapter(mRecyclerAdapter);
    }

    /**
     * 瀑布流item点击事件
     * @param index
     */
    private void RecyclerItemClick(int index) {
        String rolePower = listModel.get(index).getRolePower();
        Bundle bundle = new Bundle();
        switch (rolePower) {
            case "104"://车辆查询
                ActivityUtil.goActivity(getActivity(), CarCheckActivity.class);
                break;
            case "1300103"://指令中心
                ActivityUtil.goActivity(getActivity(), CommandCenterActivity.class);
                break;
            case "1100":
//                ActivityUtil.goActivity(getActivity(), PatrolActivity.class);
                ActivityUtil.goActivity(getActivity(), PatrolActivity2.class);
                break;
        }
    }
    class GridViewItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i) {
                case 0:
//                    ActivityUtil.goActivity(getActivity(), PatrolActivity.class);
                    ActivityUtil.goActivity(getActivity(), PatrolActivity2.class);
                    break;
                case 1:
                    ActivityUtil.goActivity(getActivity(), VehiclesSeizeActivity.class);
                    break;
                case 2:
                    ActivityUtil.goActivity(getActivity(), SeizureSearchActivity.class);
                    break;
            }
        }
    }
}
