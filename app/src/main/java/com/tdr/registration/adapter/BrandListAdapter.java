package com.tdr.registration.adapter;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.BikeCode;
import com.tdr.registration.model.SortModel;
import com.tdr.registration.view.WrapHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 品牌列表
 * Created by Linus_Xie on 2016/11/29.
 */

public class BrandListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 2;

    private Context mConext;
    private List<SortModel> sortModels;
    private List<SortModel> hotModels;
    private LayoutInflater mInflater;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnBrandClickListener onBrandClickListener;

    public BrandListAdapter(Context mConext, List<SortModel> sortModels, List<SortModel> hotModels) {
        this.mConext = mConext;
        this.sortModels = sortModels;
        this.hotModels = hotModels;
        this.mInflater = LayoutInflater.from(mConext);
        if (sortModels == null) {
            sortModels = new ArrayList<>();
        }
        sortModels.add(0, new SortModel("热门品牌", "0"));
        int size = sortModels.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前品牌拼音首字母
            String currentLetter = sortModels.get(index).getSortLetters();
            //上个首字母，如果不存在设为“”
            String previousLetter = index >= 1 ? sortModels.get(index - 1).getSortLetters() : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<SortModel> list){
        this.sortModels = list;
        if(sortModels.size()==1){
            sortModels.add(list.get(0));
        }
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return sortModels.size();
    }

    @Override
    public Object getItem(int position) {
        return sortModels == null ? null : sortModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public static class BrandViewHolder {
        TextView textCatalog;
        TextView textBrandName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        BrandViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:
                convertView = mInflater.inflate(R.layout.layout_view_hot_brand, null);
                WrapHeightGridView gridView = (WrapHeightGridView) convertView.findViewById(R.id.gridview_hotBrand);
                final HotBrandGidAdapter hotBrandGidAdapter = new HotBrandGidAdapter(mConext, hotModels.subList(0, 12));
                gridView.setAdapter(hotBrandGidAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onBrandClickListener != null) {
                            onBrandClickListener.onBrandClick(hotModels.get(position));
                        }
                    }
                });
                break;

            case 1:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.brand_item, parent, false);
                    holder = new BrandViewHolder();
                    holder.textCatalog = (TextView) convertView.findViewById(R.id.text_catalog);
                    holder.textBrandName = (TextView) convertView.findViewById(R.id.text_brandName);
                    convertView.setTag(holder);
                } else {
                    holder = (BrandViewHolder) convertView.getTag();
                }
                if (position >= 1) {
                    final String brandName = sortModels.get(position).getName();
                    holder.textBrandName.setText(brandName);
                    String cuurrentLetter = sortModels.get(position).getSortLetters();
                    String previousLetteer = position >= 1 ? sortModels.get(position - 1).getSortLetters() : "";
                    if (!TextUtils.equals(cuurrentLetter, previousLetteer)) {
                        holder.textCatalog.setVisibility(View.VISIBLE);
                        holder.textCatalog.setText(cuurrentLetter);
                    } else {
                        holder.textCatalog.setVisibility(View.GONE);
                    }
                    holder.textBrandName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onBrandClickListener != null) {
                                onBrandClickListener.onBrandClick(sortModels.get(position));
                            }
                        }
                    });
                }
                break;

        }
        return convertView;
    }

    public void setOnBrandClickListener(OnBrandClickListener listener) {
        this.onBrandClickListener = listener;
    }

    public interface OnBrandClickListener {
        void onBrandClick(SortModel sortModel);
    }

}
