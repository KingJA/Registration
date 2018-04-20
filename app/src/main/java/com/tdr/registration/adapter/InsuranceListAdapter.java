package com.tdr.registration.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.InsuranceRenewModel;
import com.tdr.registration.model.VisitListModel;
import com.tdr.registration.util.AppUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/17.
 */

public class InsuranceListAdapter extends RecyclerView.Adapter<InsuranceListAdapter.MyViewHolder> {
    private Activity mActivity;
    private List<InsuranceRenewModel> VLM=new ArrayList<InsuranceRenewModel>();

    /**
     * @param activity
     */
    public InsuranceListAdapter(Activity activity, List<InsuranceRenewModel> visitlistmodel) {
        mActivity = activity;
        VLM=  visitlistmodel;
    }
    public void update(List<InsuranceRenewModel> visitlistmodel){
        VLM=  visitlistmodel;
        notifyDataSetChanged();
    }
    @Override
    public void onViewAttachedToWindow(MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return VLM.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_insurance_renew, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            InsuranceRenewModel vlm=VLM.get(position);
            holder.itemView.setMinimumHeight(AppUtil.dp2px(122));
            holder.TV_Insurance_Title.setText(vlm.getTitle());
            holder.TV_Insurance_Life.setText(vlm.getLife());
            holder.TV_Insurance_Expiration.setText(vlm.getExpiration());
            if(vlm.getIsBuy().equals("1")){
                holder.TV_Insurance_Buy.setText("已购买");
            }else{
                holder.TV_Insurance_Buy.setText("未购买");
            }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_Insurance_Title;
        public TextView TV_Insurance_Life;
        public TextView TV_Insurance_Expiration;
        public TextView TV_Insurance_Buy;
        public MyViewHolder(View view) {
            super(view);
            TV_Insurance_Title = (TextView) view.findViewById(R.id.TV_Insurance_Title);
            TV_Insurance_Buy= (TextView) view.findViewById(R.id.TV_Insurance_Buy);
            TV_Insurance_Life = (TextView) view.findViewById(R.id.TV_Insurance_Life);
            TV_Insurance_Expiration = (TextView) view.findViewById(R.id.TV_Insurance_Expiration);
        }

    }


}
