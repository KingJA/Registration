package com.tdr.registration.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.czy1121.view.CornerLabelView;
import com.tdr.registration.R;
import com.tdr.registration.view.SquareLayout;

/**
 * Created by Linus_Xie on 2016/9/14.
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private int[] imgs;
    private String[] tags;

    private int clickTemp = -1;

    public GridAdapter(Context mContext, int[] imgs, String[] tags) {
        this.mContext = mContext;
        this.imgs = imgs;
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return imgs.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item2, null);
            holder.squareBg = (SquareLayout) convertView.findViewById(R.id.square_bg);
            holder.imageTag = (CornerLabelView) convertView.findViewById(R.id.image_tag);
            holder.imageItem = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(holder);
        } else {
            holder = (GridViewHolder) convertView.getTag();
        }

       /* if (clickTemp == position) {
            holder.squareBg.setBackgroundColor(mContext.getResources().getColor(R.color.colorBg));
        } else {
            holder.squareBg.setBackgroundColor(Color.WHITE);
        }*/

        if (tags[position].equals("hot")) {
            holder.imageTag.setVisibility(View.VISIBLE);
            holder.imageTag.setText1("hot");

        } else if (tags[position].equals("new")) {
            holder.imageTag.setVisibility(View.VISIBLE);
            holder.imageTag.setText1("new");
//            holder.imageTag.setImageResource(R.mipmap.ic_new);

        }
        holder.imageItem.setBackgroundResource(imgs[position]);
        return convertView;
    }

    public void setSeclection(int position) {
        clickTemp = position;
    }
}
