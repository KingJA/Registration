package com.tdr.registration.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.czy1121.view.CornerLabelView;
import com.tdr.registration.R;
import com.tdr.registration.util.Constants;
import com.tdr.registration.view.SquareLayout;

/**
 * 主页主业务适配器
 */
public class GridAdapter2 extends BaseAdapter {

    private final String[] Jurisdiction;
    private final String[] CornerLabel;
    private Context mContext;



    public GridAdapter2(Context mContext, String[] jurisdiction,String[] cornerlabel) {
        this.mContext = mContext;
        this.Jurisdiction = jurisdiction;
        this.CornerLabel=cornerlabel;
    }

    @Override
    public int getCount() {
        return Jurisdiction.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class GridViewHolder {
        public SquareLayout squareBg;
        public CornerLabelView imageTag;
        public ImageView imageItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder = null;
        if (convertView == null) {
            holder = new GridViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, null);
            holder.squareBg = (SquareLayout) convertView.findViewById(R.id.square_bg);
            holder.imageTag = (CornerLabelView) convertView.findViewById(R.id.image_tag);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }
        for (Constants.Jurisdiction jurisdiction : Constants.JURISDICTIONS) {
            if(jurisdiction.getJur().equals(Jurisdiction[position])){
                holder.imageItem.setBackgroundResource(jurisdiction.getIc());
            }
        }
        if (!CornerLabel[position].equals("")) {
            holder.imageTag.setVisibility(View.VISIBLE);
            holder.imageTag.setText1(CornerLabel[position]);
        }
        return convertView;
    }

}
