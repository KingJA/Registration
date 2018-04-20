package com.tdr.registration.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.VisitCenterModel;
import com.tdr.registration.model.WithdrawalsModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class WithdrawalsAdapter extends RecyclerView.Adapter<WithdrawalsAdapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemVisitClickLitener mOnItemClickLitener;
    private List<WithdrawalsModel> VLM = new ArrayList<WithdrawalsModel>();


    /**
     * 订制点击事件
     */
    public interface OnItemVisitClickLitener {
        void onItemVisitClick(WithdrawalsModel  WM);
    }

    public void setOnItemVisitClickLitener(OnItemVisitClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * @param activity
     */
    public WithdrawalsAdapter(Activity activity, List<WithdrawalsModel> visitlistmodel) {
        mActivity = activity;
        VLM = visitlistmodel;
        if (VLM == null) {
            VLM = new ArrayList<WithdrawalsModel>();
        }
    }

    public void update(List<WithdrawalsModel> visitlistmodel) {
        VLM = visitlistmodel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return VLM.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_withdrawals, null));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final WithdrawalsModel WM = VLM.get(position);
        holder.itemView.setMinimumHeight(AppUtil.dp2px(90));
        holder.TV_Money.setText("-" + Utils.fmtMicrometer(WM.getAPPLYAMOUNT()));
        holder.TV_Time.setText(WM.getAPPLYTIME());
        switch (WM.getSTATE()) {
            case "0":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_blue2);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.colorTitle));
                holder.TV_Type.setText("未发放");
                break;
            case "1":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_green);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.green));
                holder.TV_Type.setText("已发放");
                break;
            case "2":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_red);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.red));
                holder.TV_Type.setText("已驳回");
                break;

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickLitener.onItemVisitClick(WM);
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
        public TextView TV_Money;
        public TextView TV_Time;
        public TextView TV_Type;

        public MyViewHolder(View view) {
            super(view);
            TV_Money = (TextView) view.findViewById(R.id.TV_Money);
            TV_Time = (TextView) view.findViewById(R.id.TV_Time);
            TV_Type = (TextView) view.findViewById(R.id.TV_Type);
        }

    }


}
