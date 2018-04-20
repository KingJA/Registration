package com.tdr.registration.view.popwindow;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.activity.Visit_1_Activity;
import com.tdr.registration.model.VisitTypeModel;
import com.tdr.registration.util.AppUtil;
import com.tdr.registration.util.Utils;
import com.tdr.registration.view.niftydialog.NiftyDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class PopVisitResult extends PopupWindow {

    private EditText ET_MSG ;
    private ListView LV_VisitList;
    private View V;
    private Activity mActivity;
    private OnPopCallBack mOnPopCallBack;
    private View contentView;
    private MyAdapter mAdapter;
    private List<VisitTypeModel> CDList = new ArrayList<VisitTypeModel>();
    private TextView IV_Determine;
    private int Index = -1;
    private String MSG="";


    public PopVisitResult(Activity A, OnPopCallBack OPC) {
        mActivity = A;
        mOnPopCallBack = OPC;
        initview();
        setPop();
    }

    public void update(List<VisitTypeModel> List) {
        CDList = List;
        mAdapter.notifyDataSetChanged();
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
        contentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_visit_result, null);
        LV_VisitList = (ListView) contentView.findViewById(R.id.LV_VisitList);
        IV_Determine = (TextView) contentView.findViewById(R.id.IV_Determine);
        V = (ImageView) contentView.findViewById(R.id.IV_Back);
        V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        IV_Determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Index==-1){
                    Utils.showToast("请选择一个回访结果");
                 return;
                }
                if(CDList.get(Index).getCASE_STATUS().equals("2")){//车辆报废
                    dialogShow(1,"回访结果");
                }else{
                    if (mOnPopCallBack != null) {
                        mOnPopCallBack.onBack(CDList.get(Index),MSG);

                    }
                    dismiss();
                }
            }
        });
        mAdapter = new MyAdapter();
        LV_VisitList.setAdapter(mAdapter);
        LV_VisitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == CDList.size()) {
                    dialogShow(0,"输入备注");
                }else{
                    Index = position;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private NiftyDialogBuilder dialogBuilder;
    private NiftyDialogBuilder.Effectstype effectstype;

    private void dialogShow(int type,String msg) {
        if (dialogBuilder != null && dialogBuilder.isShowing())
            return;
        dialogBuilder = NiftyDialogBuilder.getInstance(mActivity);
        effectstype = NiftyDialogBuilder.Effectstype.Fadein;
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if(type==0){
            View color_view = mInflater.inflate(R.layout.dialog_visit_type_item, null);
            ET_MSG = (EditText) color_view.findViewById(R.id.ET_MSG);

            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(color_view, mActivity);
            dialogBuilder.withTitle(msg)
                    .withTitleColor("#333333")
                    .withMessage(null)
                    .withEffect(effectstype)
                    .withButton1Text("取消")
                    .withButton2Text("确定")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MSG= ET_MSG.getText().toString().trim();
                            mAdapter.notifyDataSetChanged();
                            dialogBuilder.dismiss();
                        }
                    })
                    .show();
        }else if(type==1){
            TextView TV=new TextView(mActivity);
            TV.setText("请确定车辆已报废？");
            TV.setTextSize(16);
            TV.setTextColor(Color.parseColor("#333333"));
            TV.setGravity(Gravity.CENTER);
            TV.setMinimumHeight(AppUtil.dp2px(60));
            dialogBuilder.isCancelable(false);
            dialogBuilder.setCustomView(TV, mActivity);
            dialogBuilder.withTitle(msg)
                    .withTitleColor("#333333")
                    .withMessage(null)
                    .withEffect(effectstype)
                    .withButton1Text("取消")
                    .withButton2Text("确定")
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    })
                    .setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                            if (mOnPopCallBack != null) {
                                mOnPopCallBack.onBack(CDList.get(Index),MSG);
                            }
                            dismiss();
                        }
                    })
                    .show();
        }

    }

    public void show() {
        MSG="";
        showAtLocation(mActivity.getLayoutInflater().inflate(R.layout.activity_2_visit, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public interface OnPopCallBack {
        void onBack(VisitTypeModel CDL,String msg);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return CDList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return CDList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if (position == CDList.size()) {
                v = LayoutInflater.from(mActivity).inflate(R.layout.item_visit_type2, null);
                TextView TV_MSG = (TextView) v.findViewById(R.id.TV_MSG);
                if(!MSG.equals("")){
                    TV_MSG.setText(MSG);
                }
            } else {
                v = LayoutInflater.from(mActivity).inflate(R.layout.item_visit_type, null);
                TextView TV_Visit_Type = (TextView) v.findViewById(R.id.TV_Visit_Type);
                ImageView IV_Tick = (ImageView) v.findViewById(R.id.IV_Tick);
                TV_Visit_Type.setText(CDList.get(position).getNAME());
                if (Index >= 0 && Index == position) {
                    IV_Tick.setVisibility(View.VISIBLE);
                } else {
                    IV_Tick.setVisibility(View.GONE);
                }

            }
            return v;
        }
    }

}
