package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.SortModel;

import java.util.List;

/**
 * 热门品牌
 * Created by Linus_Xie on 2016/11/29.
 */

public class HotBrandGidAdapter extends BaseAdapter {

    private Context mContext;
    private List<SortModel> sortModels;

    public HotBrandGidAdapter(Context mContext, List<SortModel> sortModels) {
        this.mContext = mContext;
        this.sortModels = sortModels;
    }

    @Override
    public int getCount() {
        return sortModels == null ? 0 : sortModels.size();
    }

    @Override
    public Object getItem(int position) {
        return sortModels == null ? null : sortModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class HotBrandViewHolder {
        TextView textBrandName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HotBrandViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_hot_brand, parent, false);
            holder = new HotBrandViewHolder();
            holder.textBrandName = (TextView) convertView.findViewById(R.id.text_brandName);
            convertView.setTag(holder);
        } else {
            holder = (HotBrandViewHolder) convertView.getTag();
        }
        holder.textBrandName.setText(sortModels.get(position).getName());
        return convertView;
    }
}
