package com.tdr.registration.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.VisitListModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.mLog;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class VisitList2Adapter extends RecyclerView.Adapter<VisitList2Adapter.MyViewHolder> {
    private Activity mActivity;
    private OnItemVisitClickLitener mOnItemClickLitener;
    private List<VisitListModel> VLM = new ArrayList<VisitListModel>();


    /**
     * 订制点击事件
     */
    public interface OnItemVisitClickLitener {
        void onItemVisitClick(int position);
    }

    public void setOnItemVisitClickLitener(OnItemVisitClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<VisitListModel> getDate() {
        return VLM;
    }

    /**
     * @param activity
     */
    public VisitList2Adapter(Activity activity, List<VisitListModel> visitlistmodel) {
        mActivity = activity;
        VLM = visitlistmodel;
    }

    public void update(List<VisitListModel> visitlistmodel) {
        VLM = visitlistmodel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return VLM.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_visit2, null));
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        VisitListModel vlm = VLM.get(position);
        holder.itemView.setMinimumHeight(AppUtil.dp2px(70));
        holder.TV_Visit.setVisibility(View.VISIBLE);

        holder.TV_Visit_Title.setText(vlm.getPLATENUMBER() + "(" + vlm.getOWNERNAME() + ")");
        holder.TV_Visit_Type.setText(vlm.getVISITRESULTDES());
        holder.TV_Visit_Phone.setText("联系方式:" + vlm.getPHONE());
        if (vlm.getTALK_TIME()!=null&&!vlm.equals("")) {
            holder.TV_Visit_TalkTime.setVisibility(View.VISIBLE);
            holder.TV_Visit_TalkTime.setText("通话时长:"+vlm.getTALK_TIME());
            if(getSeconds(vlm.getTALK_TIME())>30){
                holder.TV_Visit_TalkTime.setTextColor(Color.parseColor("#4B9DF9"));
                holder.TV_Visit_TalkTime.setBackgroundResource(R.drawable.shape_textview_bkg_blue2);
            }else{
                holder.TV_Visit_TalkTime.setTextColor(Color.parseColor("#ff0000"));
                holder.TV_Visit_TalkTime.setBackgroundResource(R.drawable.shape_textview_bkg_red);
            }
        }else{
            holder.TV_Visit_TalkTime.setVisibility(View.GONE);
        }
        if (vlm.getSTATUS().equals("0")) {
            holder.IV_New.setVisibility(View.VISIBLE);
            holder.TV_Visit_Type.setVisibility(View.GONE);
        } else {
            holder.IV_New.setVisibility(View.GONE);
            holder.TV_Visit_Type.setVisibility(View.VISIBLE);

        }
        if (mOnItemClickLitener != null) {
            holder.TV_Visit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemVisitClick(position);
                }
            });
        }
    }
    private int getSeconds(String date) {
        mLog.e("通话时长："+date);
        int time=0;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("mm分ss秒");
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(date, pos);
            time=strtodate.getSeconds()+strtodate.getMinutes()*60;
        }catch (Exception e){
            return 0;
        }
        return time;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TV_Visit_Title;
        public ImageView IV_New;
        public TextView TV_Visit_Type;
        public TextView TV_Visit_Phone;
        public TextView TV_Visit;
        public TextView TV_Visit_TalkTime;

        public MyViewHolder(View view) {
            super(view);
            TV_Visit_Title = (TextView) view.findViewById(R.id.TV_Visit_Title);
            IV_New = (ImageView) view.findViewById(R.id.IV_New);
            TV_Visit_Type = (TextView) view.findViewById(R.id.TV_Visit_Type);
            TV_Visit_Phone = (TextView) view.findViewById(R.id.TV_Visit_Phone);
            TV_Visit = (TextView) view.findViewById(R.id.TV_Visit);
            TV_Visit_TalkTime = (TextView) view.findViewById(R.id.TV_Visit_TalkTime);


        }

    }


}
