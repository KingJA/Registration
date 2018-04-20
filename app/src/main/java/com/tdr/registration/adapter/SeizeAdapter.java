package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.DistrainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 扣押列表适配器
 * Created by Linus_Xie on 2016/10/8.
 */
public class SeizeAdapter extends BaseAdapter {
    private Context mContext;
    private List<DistrainModel> models = new ArrayList<>();
    private LayoutInflater mInflater = null;

    public SeizeAdapter(Context mContext, List<DistrainModel> models) {
        this.mContext = mContext;
        this.models = models;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class SeizeViewHolder {
        public TextView textSeizeNumber;
        public TextView textIdentity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SeizeViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new SeizeViewHolder();
            convertView = mInflater.inflate(R.layout.seize_item, null);
            viewHolder.textSeizeNumber = (TextView) convertView.findViewById(R.id.text_seizeNumber);
            viewHolder.textIdentity = (TextView) convertView.findViewById(R.id.text_identity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SeizeViewHolder) convertView.getTag();
        }

        viewHolder.textSeizeNumber.setText(models.get(position).getDistrainNoPredix() + models.get(position).getDistrainNoSuffix());
        viewHolder.textIdentity.setText(models.get(position).getIdentityCard());

        return convertView;
    }
}
