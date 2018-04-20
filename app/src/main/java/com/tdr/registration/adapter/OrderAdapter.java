package com.tdr.registration.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.OrderModel;
import com.tdr.registration.model.WithdrawalsModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemVisitClickLitener mOnItemClickLitener;
    private List<OrderModel> VLM = new ArrayList<OrderModel>();


    /**
     * 订制点击事件
     */
    public interface OnItemVisitClickLitener {
        void onItemVisitClick(OrderModel WM);
    }

    public void setOnItemVisitClickLitener(OnItemVisitClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * @param activity
     */
    public OrderAdapter(Activity activity, List<OrderModel> visitlistmodel) {
        mActivity = activity;
        VLM = visitlistmodel;
        if (VLM == null) {
            VLM = new ArrayList<OrderModel>();
        }
    }

    public void update(List<OrderModel> visitlistmodel) {
        VLM = visitlistmodel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return VLM.size();
    }

    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderAdapter.MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_withdrawals, null));
    }


    @Override
    public void onBindViewHolder(final OrderAdapter.MyViewHolder holder, int position) {
        final OrderModel WM = VLM.get(position);
        holder.itemView.setMinimumHeight(AppUtil.dp2px(90));
        holder.TV_Money.setText(WM.getREMARK());
        holder.TV_Time.setText(WM.getBILLTIME());
        holder.TV_OrderMoney.setVisibility(View.VISIBLE);
        String money=WM.getMONEY();
        if(!money.substring(0,1).equals("+")&&!money.substring(0,1).equals("-")){
            money="+"+money;
        }
        holder.TV_OrderMoney.setText(money);
        switch (WM.getPAYSTATUS()) {
            case "0":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_red);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.red));
                holder.TV_Type.setText("未支付");
                break;
            case "1":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_blue2);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.colorTitle));
                holder.TV_Type.setText("已支付");
                break;
            case "2":
                holder.TV_Type.setBackgroundResource(R.drawable.shape_textview_bkg_green);
                holder.TV_Type.setTextColor(mActivity.getResources().getColor(R.color.green));
                holder.TV_Type.setText("已结算");

                break;

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickLitener!=null){
                    mOnItemClickLitener.onItemVisitClick(WM);
                }
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
        public TextView TV_OrderMoney;

        public MyViewHolder(View view) {
            super(view);
            TV_Money = (TextView) view.findViewById(R.id.TV_Money);
            TV_Time = (TextView) view.findViewById(R.id.TV_Time);
            TV_Type = (TextView) view.findViewById(R.id.TV_Type);
            TV_OrderMoney= (TextView) view.findViewById(R.id.TV_OrderMoney);
        }

    }
}
