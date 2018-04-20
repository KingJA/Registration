package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.ConditionListAdapter;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.mLog;

import java.util.ArrayList;
import java.util.List;

public class PopVisitType extends PopupWindow implements View.OnClickListener {

    private RecyclerView RL_ConditionList;
    private TextView TV_Reset;
    private TextView TV_Confim;
    private Activity mActivity;
    private OnPopCallBack mOnPopCallBack;
    private View contentView;
    private ConditionListAdapter CLAdapter;
    private List<VisitTypeModel> CDList = new ArrayList<VisitTypeModel>();
    private TextView TV_title;

    public PopVisitType(Activity A, OnPopCallBack OPC) {
        mActivity = A;
        mOnPopCallBack = OPC;
        initview();
        setPop();
    }

    public void update(List<VisitTypeModel> List) {
        CDList = List;
        CLAdapter.update(CDList);
    }

    private void setPop() {
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击外面popupWindow消失
        setOutsideTouchable(false);
        //popupWindow获取焦点
        setFocusable(false);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
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
    }

    private void initview() {
        contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_visit_type, null);

        RL_ConditionList = (RecyclerView) contentView.findViewById(R.id.RL_ConditionList);
        TV_title = (TextView) contentView.findViewById(R.id.TV_title);
        TV_Reset = (TextView) contentView.findViewById(R.id.TV_Reset);
        TV_Confim = (TextView) contentView.findViewById(R.id.TV_Confim);
        TV_Reset.setOnClickListener(this);
        TV_Confim.setOnClickListener(this);
        RL_ConditionList.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));

        CLAdapter = new ConditionListAdapter(mActivity, CDList);
        CLAdapter.setItemClickLitener(new ConditionListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(List<VisitTypeModel> conditionlist) {
                CDList = conditionlist;
//                for (VisitTypeModel conditionList : conditionlist) {
//                    mLog.e("Name=" + conditionList.getNAME());
//                    mLog.e("Select=" + conditionList.isSelect());
//                }
            }
        });
        RL_ConditionList.addItemDecoration(new RVItemDecoration());
        RL_ConditionList.setAdapter(CLAdapter);

    }

    public void show(View v) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);
        showAsDropDown(v);
    }

    public void setTitle(String s) {
        TV_title.setText(s);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TV_Reset:
                for (VisitTypeModel conditionList : CDList) {
                    conditionList.setSelect(false);
                }
                CLAdapter.update(CDList);
                break;
            case R.id.TV_Confim:
                mOnPopCallBack.onBack(CDList);
                dismiss();
                break;
        }


    }

    public interface OnPopCallBack {
        void onBack(List<VisitTypeModel> CDL);
    }

    class RVItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //设定底部边距为1px
            outRect.set(10, 10, 10, 10);
        }

    }
}
