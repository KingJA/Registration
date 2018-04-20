package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.VisitTypeModel;

import java.util.ArrayList;
import java.util.List;

public class PopVisit extends PopupWindow {

    private TextView TV_RelationPhoneNumber;
    private TextView TV_Call;
    private TextView TV_Visit_Result;
    private TextView TV_back;
    private List<VisitTypeModel> CDList = new ArrayList<VisitTypeModel>();
    private Activity mActivity;
    private OnPopCallBack mOnPopCallBack;
    private View contentView;


    public PopVisit(Activity A, OnPopCallBack OPC) {
        mActivity = A;
        mOnPopCallBack = OPC;
        initview();
        setPop();
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
        setAnimationStyle(R.style.AnimationBottomFade);
    }

    private void initview() {
        contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_visit, null);

        TV_RelationPhoneNumber = (TextView) contentView.findViewById(R.id.TV_RelationPhoneNumber);
        TV_Call = (TextView) contentView.findViewById(R.id.TV_Call);
        TV_Visit_Result = (TextView) contentView.findViewById(R.id.TV_Visit_Result);
        TV_back = (TextView) contentView.findViewById(R.id.TV_back);
        TV_RelationPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPopCallBack!=null){
                    mOnPopCallBack.onRelationPhoneNumber();
                }
            }
        });
        TV_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPopCallBack!=null){
                    mOnPopCallBack.onCall();
                }
            }
        });
        TV_Visit_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPopCallBack!=null){
                    mOnPopCallBack.onResult();
                }
            }
        });
        TV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnPopCallBack!=null){
                    mOnPopCallBack.onBack();
                }
            }
        });

    }

    public void show() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        mActivity.getWindow().setAttributes(lp);
        showAtLocation(mActivity.getLayoutInflater().inflate(R.layout.activity_2_visit, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public interface OnPopCallBack {
        void onRelationPhoneNumber();
        void onResult();
        void onCall();
        void onBack();

    }

}
