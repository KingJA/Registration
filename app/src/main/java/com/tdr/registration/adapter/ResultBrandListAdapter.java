package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.model.SortModel;

import java.util.List;

/**
 * 品牌搜索结果
 * Created by Linus_Xie on 2016/11/29.
 */

public class ResultBrandListAdapter extends BaseAdapter {
    private Context mContext;
    private List<SortModel> sortModels;

    public ResultBrandListAdapter(List<SortModel> sortModels, Context mContext) {
        this.sortModels = sortModels;
        this.mContext = mContext;
    }

    public void changeData(List<SortModel> list){
        if (sortModels == null){
            sortModels = list;
        }else{
            sortModels.clear();
            sortModels.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sortModels == null ? 0 : sortModels.size();
    }

    @Override
    public SortModel getItem(int position) {
        return sortModels == null ? null : sortModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ResultViewHolder{
        TextView textBrandName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultViewHolder holder;
        if (convertView == null){
        }
        return null;
    }
}
