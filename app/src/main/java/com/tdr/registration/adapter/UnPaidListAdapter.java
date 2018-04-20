package com.tdr.registration.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.PayInsurance;
import com.tdr.registration.model.policys;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.mLog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/17.
 */

public class UnPaidListAdapter extends RecyclerView.Adapter<UnPaidListAdapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemClickLitener mOnItemClickLitener;
    private List<PayInsurance> PIList;

    /**
     * 订制点击事件
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * @param activity
     */
    public UnPaidListAdapter(Activity activity) {
        mActivity = activity;
    }

    public UnPaidListAdapter(Activity activity, List<PayInsurance> piList) {
        mActivity = activity;
        PIList = piList;
    }

    public void UpDate(List<PayInsurance> piList) {
        PIList = piList;
        notifyDataSetChanged();
    }

    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


    @Override
    public int getItemCount() {
        return PIList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_unpaid, null));
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        PayInsurance PI = PIList.get(position);
        holder.LL_UnPaid_InsuranceLsit.removeAllViews();
        for (int j = 0; j < PI.getPolicys().size(); j++) {
            View v= getView(PI.getPolicys().get(j));
            holder.LL_UnPaid_InsuranceLsit.addView(v);
        }
        holder.TV_CreateTime.setText(PI.getCreateTime());
        holder.TV_UnPaid_needpay.setText("¥" + FormatAmount(PI.getTotal_Amount()));
        holder.TV_UnPaid_CarPlate.setText(PI.getPlateNumber());
    }
    private View getView(policys PIp){
        View v = LayoutInflater.from(mActivity).inflate(R.layout.item_unpaid_insurance, null);
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, AppUtil.dp2px(60)));
        TextView TV_InsuranceName = (TextView) v.findViewById(R.id.TV_InsuranceName);
        TextView TV_InsuranceAmount = (TextView) v.findViewById(R.id.TV_InsuranceAmount);
        TextView TV_InsuranceExplain = (TextView) v.findViewById(R.id.TV_InsuranceExplain);
        TextView TV_InsuranceYear = (TextView) v.findViewById(R.id.TV_InsuranceYear);
        TV_InsuranceName.setText(PIp.getTypeName());
        TV_InsuranceAmount.setText("¥" + FormatAmount(PIp.getPRICE()));
        TV_InsuranceExplain.setText(PIp.getSubTitle());
        if(PIp.getDEADLINE()==null||PIp.getDEADLINE().equals("")||PIp.getDEADLINE().equals("null")){
            TV_InsuranceYear.setText("");
        }else{
            TV_InsuranceYear.setText("x" + PIp.getDEADLINE() + "年");
        }
        return v;
    }
    private String FormatAmount(String Amount) {
        double a = Double.parseDouble(Amount);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(a);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_Pay;
        public TextView TV_UnPaid_CarPlate;
        public LinearLayout LL_UnPaid_InsuranceLsit;
        public TextView TV_UnPaid_needpay;
        public TextView TV_CreateTime;

        public MyViewHolder(View view) {
            super(view);
            TV_UnPaid_CarPlate = (TextView) view.findViewById(R.id.TV_UnPaid_CarPlate);
            LL_UnPaid_InsuranceLsit = (LinearLayout) view.findViewById(R.id.LL_UnPaid_InsuranceLsit);
            TV_UnPaid_needpay = (TextView) view.findViewById(R.id.TV_UnPaid_needpay);
            TV_CreateTime = (TextView) view.findViewById(R.id.TV_CreateTime);
            TV_Pay = (TextView) view.findViewById(R.id.TV_Pay);
            TV_Pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickLitener != null) {
                        mOnItemClickLitener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
