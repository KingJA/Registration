package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.ResultInsuranceModel;



import java.util.List;

/**
 * 保险显示adapter
 * Created by Linus_Xie on 2016/10/7.
 */
public class InsuranceShowAdapter extends BaseAdapter {

    private Context mContext;
    private List<ResultInsuranceModel.ItemsBean.SubItemsBean> subItemsBean;
    private LayoutInflater mInflater = null;


    public InsuranceShowAdapter(Context mContext, List<ResultInsuranceModel.ItemsBean.SubItemsBean> subItemsBean) {
        this.mContext = mContext;
        this.subItemsBean = subItemsBean;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subItemsBean.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class ViewHolder {
        public TextView textInsuranceNme;
        public TextView textInsuranceCount;
        public TextView textInsuranceMoney;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.insurance_show_item, null);
            viewHolder.textInsuranceNme = (TextView) convertView.findViewById(R.id.text_insuranceNme);
            viewHolder.textInsuranceCount = (TextView) convertView.findViewById(R.id.text_insuranceCount);
            viewHolder.textInsuranceMoney = (TextView) convertView.findViewById(R.id.text_insuranceMoney);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //for (int j = 0; j < subItemsBean.size(); j++) {
//        if (subItemsBean.get(position).getDeadline().equals("0")) {
//            viewHolder.textInsuranceNme.setText(subItemsBean.get(position).getTitle() + "总数");
//        } else {
//
//            viewHolder.textInsuranceNme.setText(subItemsBean.get(position).getTitle() + subItemsBean.get(position).getDeadline());
//        }
        viewHolder.textInsuranceNme.setText(subItemsBean.get(position).getTitle());

        viewHolder.textInsuranceCount.setText("数量:"+subItemsBean.get(position).getCount());
        viewHolder.textInsuranceMoney.setText("金额:"+subItemsBean.get(position).getMoney());
        // }


        return convertView;
    }


}
