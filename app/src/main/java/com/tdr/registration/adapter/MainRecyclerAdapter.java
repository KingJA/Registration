package com.tdr.registration.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.czy1121.view.CornerLabelView;
import com.tdr.registration.R;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.mLog;
import com.tdr.registration.view.SquareLayout;

import java.util.List;


/**
 * Created by Administrator on 2017/3/17.
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MyViewHolder> {
    private String REGISTRATION;
    private List<String> Jurisdiction;
    private List<String>  CornerLabel;
    private Activity mContext;

    private OnItemClickLitener mOnItemClickLitener;

    /**
     * 订制点击事件
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public MainRecyclerAdapter(Activity mContext, List<String>  jurisdiction,List<String>  cornerlabel) {
        this.mContext = mContext;
        this.Jurisdiction = jurisdiction;
        this.CornerLabel = cornerlabel;
        REGISTRATION= (String)SharedPreferencesUtils.get("REGISTRATION","");
        mLog.e("MainRecyclerAdapter" + Jurisdiction.size());
        mLog.e("REGISTRATION=" + REGISTRATION);
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder ) {
        super.onViewAttachedToWindow(holder);
    }
    @Override
    public int getItemCount() {
//        mLog.e("getItemCount" + Jurisdiction.size());
        return Jurisdiction.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLog.e("onCreateViewHolder");
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.grid_item, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        mLog.e("onBindViewHolder");
//        holder.itemView.setMinimumHeight(AppUtil.dp2px(80));
//        holder.itemView.setMinimumWidth(AppUtil.dp2px(80));
        for (Constants.Jurisdiction jurisdiction : Constants.JURISDICTIONS) {
            if (jurisdiction.getJur().equals(Jurisdiction.get(position))) {
                holder.imageItem.setBackgroundResource(jurisdiction.getIc());
                holder.TV_ItemName.setText(jurisdiction.getName());

                if(jurisdiction.getJur().equals(Constants.JURISDICTION_REGISTRATION)){
                    if(!REGISTRATION.equals("")){
                        holder.TV_ItemName.setText(REGISTRATION);
                    }
                }
            }
        }
        if (!CornerLabel.get(position).equals("")) {
            holder.imageTag.setVisibility(View.VISIBLE);
            holder.imageTag.setText1(CornerLabel.get(position));
        }

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.squareBg.setTag(Jurisdiction.get(position));
                    mOnItemClickLitener.onItemClick(holder.squareBg, position);
                }
            });

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public SquareLayout squareBg;
        public CornerLabelView imageTag;
        public ImageView imageItem;
        public TextView TV_ItemName;


        public MyViewHolder(View view) {
            super(view);
            squareBg = (SquareLayout) view.findViewById(R.id.square_bg);
            imageTag = (CornerLabelView) view.findViewById(R.id.image_tag);
            imageItem = (ImageView) view.findViewById(R.id.image_item);
            TV_ItemName = (TextView) view.findViewById(R.id.TV_ItemName);
        }
    }


}
