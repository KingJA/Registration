package com.tdr.registration.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class ConditionListAdapter extends RecyclerView.Adapter<ConditionListAdapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemClickLitener mOnItemClickLitener;
    List<VisitTypeModel> conditionlist = new ArrayList<VisitTypeModel>();

    /**
     * 订制点击事件
     */
    public interface OnItemClickLitener {
        void onItemClick(List<VisitTypeModel> conditionlist);
    }

    public void setItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * @param activity
     */
    public ConditionListAdapter(Activity activity, List<VisitTypeModel> CDL) {
        mActivity = activity;
        conditionlist = CDL;
    }
    public void update( List<VisitTypeModel> CDL){
        conditionlist = CDL;
        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return conditionlist.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.visitview, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.itemView.setMinimumWidth(AppUtil.dp2px(78));
        holder.itemView.setMinimumHeight(AppUtil.dp2px(33));
        VisitTypeModel CDL = conditionlist.get(position);
        holder.TV_Name.setText(CDL.getNAME());
        setitem(CDL.isSelect(),holder);

        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (conditionlist.get(position).isSelect()) {
                        conditionlist.get(position).setSelect(false);
                    } else {
                        conditionlist.get(position).setSelect(true);
                    }
                    setitem(conditionlist.get(position).isSelect(),holder);
                    mOnItemClickLitener.onItemClick(conditionlist);
                }
            });
        }
    }
    private void setitem(boolean select,MyViewHolder holder){
        if (select) {
            holder.IV_tick.setVisibility(View.VISIBLE);
            holder.IV_BKG.setBackgroundResource(R.drawable.shape_textview_bkg_blue2);
            holder.TV_Name.setTextColor(Color.parseColor("#4B9DF9"));
        } else {
            holder.IV_tick.setVisibility(View.GONE);
            holder.IV_BKG.setBackgroundResource(R.drawable.shape_textview_bkg_blue3);
            holder.TV_Name.setTextColor(Color.parseColor("#A0A0A0"));
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_Name;
        public ImageView IV_BKG;
        public ImageView IV_tick;

        public MyViewHolder(View view) {
            super(view);
            IV_BKG = (ImageView) view.findViewById(R.id.IV_BKG);
            IV_tick = (ImageView) view.findViewById(R.id.IV_tick);
            TV_Name = (TextView) view.findViewById(R.id.TV_Name);
        }
    }


}
