package com.tdr.registration.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.DetailBean;
import com.tdr.registration.model.InsuranceModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.DBUtils;
import com.tdr.registration.util.mLog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 保险续费
 */
public class InsuranceRenewAdapter extends BaseAdapter {

    private Context mContext;
    private List<InsuranceModel> modelList;
    private LayoutInflater mInflater = null;

//    private OnCheckBoxClickLitener mOnCheckBoxClickLitener;

    public InsuranceRenewAdapter(Context mContext, List<InsuranceModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void UpDateList(List<InsuranceModel> list){
        modelList = list;
        notifyDataSetChanged();
        mLog.e("2insuranceModelsList=" + modelList.size());
    }

//    public interface OnCheckBoxClickLitener {
//        void onCheckBoxClick(int position);
//        void onRadioButtonClick(int position, RadioButton radiobutton, Boolean isCheck);
//    }

//    public void setOnCheckBoxClickLitener(OnCheckBoxClickLitener mOnItemClickLitener) {
//        this.mOnCheckBoxClickLitener = mOnItemClickLitener;
//    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mLog.e("getView"+position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.insurance_item, null);
            holder.textInsuranceNme = (TextView) convertView.findViewById(R.id.text_insuranceNme);
            holder.textIsMandatory = (TextView) convertView.findViewById(R.id.tv_isMandatory);
            holder.textInsuranceSubTitle = (TextView) convertView.findViewById(R.id.text_insuranceSubTitle);
            holder.linearInstructionsDesc = (LinearLayout) convertView.findViewById(R.id.linear_instructionsDesc);
            holder.groupInsurance = (RadioGroup) convertView.findViewById(R.id.group_insurance);
            holder.checkInsurance = (CheckBox) convertView.findViewById(R.id.check_insurance);
            holder.relativeInsurance = (RelativeLayout) convertView.findViewById(R.id.relative_insurance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setholder(position,holder);
        return convertView;
    }
    private void setholder( int position, final ViewHolder holder ){
        InsuranceModel IM  =  modelList.get(position);
        holder.textInsuranceNme.setText(IM.getName());
        holder.textInsuranceSubTitle.setText(IM.getSubTitle());
        if ("1".equals(IM.getIsChoose())) {
            holder.textIsMandatory.setVisibility(View.VISIBLE);
            holder.checkInsurance.setChecked(true);
            holder.checkInsurance.setFocusable(false);
            holder.checkInsurance.setEnabled(false);
        } else {
            holder.textIsMandatory.setVisibility(View.GONE);
            holder.checkInsurance.setFocusable(true);
            holder.checkInsurance.setEnabled(true);
        }

//        View v = mInflater.inflate(R.layout.radiobutton, null);
        int id=0;
        mLog.e("Detail().size="+IM.getDetail().size());
        for (DetailBean detailBean : IM.getDetail()) {
            mLog.e("Detail="+detailBean.getRemarkID());
            String RemarkID="";
                RemarkID= detailBean.getRemarkID();
            if(RemarkID==null){
                RemarkID="";
            }
            RadioButton tempButton =new RadioButton(mContext);
            tempButton.setId(id);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(AppUtil.dp2px(85),AppUtil.dp2px(20));
            layoutParams.setMargins(0, 0, 20, 0);
            tempButton.setLayoutParams(layoutParams);
            Drawable Dlaft = mContext.getResources().getDrawable(R.drawable.radiobutton_change);
            Dlaft.setBounds(0, 0, Dlaft.getMinimumWidth(), Dlaft.getMinimumHeight());
            tempButton.setBackground(null);
            tempButton.setButtonDrawable(R.drawable.radiobutton_change);
            tempButton.setTextSize(14);
            tempButton.setText("  "+detailBean.getName());
//            tempButton.setCompoundDrawables(Dlaft, null, null, null); //设置左图标
            tempButton.setTag(RemarkID);
            if("0".equals(detailBean.getIsValid())){
                tempButton.setTextColor(Color.parseColor("#eeeeee"));
                tempButton.setClickable(false);
            }
            mLog.e("TAG="+RemarkID);
            holder.groupInsurance.addView(tempButton);
            id++;
        }
        holder.checkInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mLog.e("isChecked="+isChecked);
                if (!holder.checkInsurance.isChecked()) {
                    holder.groupInsurance.clearCheck();
                    holder.checkInsurance.setChecked(false);
                }
            }
        });

        holder.groupInsurance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId!= -1) {
                    holder.checkInsurance.setChecked(true);
                }
            }
        });
    }


    public final class ViewHolder {
        public TextView textInsuranceNme;
        public TextView textIsMandatory;
        public TextView textInsuranceSubTitle;
        public LinearLayout linearInstructionsDesc;
        public RadioGroup groupInsurance;
        public CheckBox checkInsurance;
        public RelativeLayout relativeInsurance;

    }

}
