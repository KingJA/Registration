package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.CallInfo;
import com.tdr.registration.util.AppUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class PopRelationPhoneNumber extends PopupWindow {


    private ImageView IV_Back;
    private ListView LV_VisitRelationList;
    private Activity mActivity;
    private OnReltionListener reltionlistener;
    private View contentView;
    private List<CallInfo> CallInfoList;
    private MyAdapter mAdapter;

    public PopRelationPhoneNumber(Activity A, List<CallInfo> CI, OnReltionListener ORL) {
        mActivity = A;
        reltionlistener = ORL;
        CallInfoList = CI;
        if(CallInfoList==null){
            CallInfoList=new ArrayList<>();
        }
        initview();
        setPop();
    }

    private void setPop() {
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);


        //点击外面popupWindow消失
        setOutsideTouchable(false);
        //popupWindow获取焦点
        setFocusable(false);
        setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {

            }
        });
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        setBackgroundDrawable(mActivity.getResources().getDrawable(R.color.transparent));
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        contentView.measure(widthSpec, heightSpec);
        setAnimationStyle(R.style.AnimationBottomFade);
    }

    private void initview() {
        contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_visit_relation, null);

        IV_Back = (ImageView) contentView.findViewById(R.id.IV_Back);
        LV_VisitRelationList = (ListView) contentView.findViewById(R.id.LV_VisitRelationList);

        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mAdapter = new MyAdapter();
        LV_VisitRelationList.setAdapter(mAdapter);

    }
    public void UpDate(List<CallInfo> CI){
        CallInfoList = CI;
        mAdapter.notifyDataSetChanged();
    }
    public void show() {
        showAtLocation(mActivity.getLayoutInflater().inflate(R.layout.activity_2_visit, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return CallInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return CallInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CallInfo callinfo = CallInfoList.get(position);
            View v = LayoutInflater.from(mActivity).inflate(R.layout.item_visit_relation, null);
            v.setMinimumHeight(AppUtil.dp2px(60));
            TextView TV_PhoneNumber = (TextView) v.findViewById(R.id.TV_PhoneNumber);
            TextView TV_Time = (TextView) v.findViewById(R.id.TV_Time);
            TextView TV_Relation = (TextView) v.findViewById(R.id.TV_Relation);
            TV_PhoneNumber.setText(callinfo.number);
            TV_Time.setText(gettiem(callinfo.date));
            TV_Relation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reltionlistener != null) {
                        reltionlistener.onRelationPhoneNumber(callinfo);
                        dismiss();
                    }
                }
            });
            return v;
        }
    }
    private String gettiem(long time){
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    public interface OnReltionListener {
        void onRelationPhoneNumber(CallInfo callinfo);
    }

}
