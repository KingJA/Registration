package com.tdr.registration.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.VisitCenterModel;
import com.tdr.registration.model.VisitListModel;
import com.tdr.registration.util.AppUtil;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class VisitList1Adapter extends RecyclerView.Adapter<VisitList1Adapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemVisitClickLitener mOnItemClickLitener;
    private List<VisitCenterModel.Detail> VLM = new ArrayList<VisitCenterModel.Detail>();


    /**
     * 订制点击事件
     */
    public interface OnItemVisitClickLitener {
        void onItemVisitClick(int position);
    }

    public void setOnItemVisitClickLitener(OnItemVisitClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * @param activity
     */
    public VisitList1Adapter(Activity activity, List<VisitCenterModel.Detail> visitlistmodel) {
        mActivity = activity;
        VLM = visitlistmodel;
        if (VLM == null) {
            VLM = new ArrayList<VisitCenterModel.Detail>();
        }
    }

    public void update(List<VisitCenterModel.Detail> visitlistmodel) {
        VLM = visitlistmodel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return VLM.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_visit1, null));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        VisitCenterModel.Detail data = VLM.get(position);
        holder.TV_Visit_Time.setText(setDate(data.getDate()));
        holder.TV_VisitSum.setText(data.getNeedVisitTotal() + "个");
        holder.TV_ReturnVisit.setText(data.getHasVisitTotal() + "个");
        holder.TV_NotVisit.setText(data.getNoVisitTotal() + "个");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemVisitClick(position);
            }
        });
    }

    private String setDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM月dd日");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(date, pos);
        String dateString = formatter2.format(strtodate);
        return dateString;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_Visit_Time;
        public TextView TV_VisitSum;
        public TextView TV_ReturnVisit;
        public TextView TV_NotVisit;

        public MyViewHolder(View view) {
            super(view);
            TV_Visit_Time = (TextView) view.findViewById(R.id.TV_Visit_Time);
            TV_VisitSum = (TextView) view.findViewById(R.id.TV_VisitSum);
            TV_ReturnVisit = (TextView) view.findViewById(R.id.TV_ReturnVisit);
            TV_NotVisit = (TextView) view.findViewById(R.id.TV_NotVisit);
        }

    }


}
