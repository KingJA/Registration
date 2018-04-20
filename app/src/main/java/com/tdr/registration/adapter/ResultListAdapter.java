package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.BaseInfo;

import java.util.List;

/**
 * 查询结果适配器
 * Created by Linus_Xie on 2016/9/18.
 */
public class ResultListAdapter extends BaseAdapter {
    private Context mContext;
    private List<BaseInfo> mCities;

    public ResultListAdapter(Context mContext, List<BaseInfo> mCities) {
        this.mCities = mCities;
        this.mContext = mContext;
    }

    public class ViewHolder {
        public TextView text_resultName;
    }

    public void changeData(List<BaseInfo> list) {
        if (mCities == null) {
            mCities = list;
        } else {
            mCities.clear();
            mCities.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public BaseInfo getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.resultcity_item, null);
            holder = new ViewHolder();
            holder.text_resultName = (TextView) convertView.findViewById(R.id.text_resultName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text_resultName.setText(mCities.get(position).getCityName());
        return convertView;
    }
}
