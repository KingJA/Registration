package com.tdr.registration.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;

/**
 * Created by Administrator on 2017/5/18.
 */

public class MyRadioButton extends LinearLayout  {
    View v;
    ImageView IV_PayTypeRB;
    ImageView IV_PayTypeImg;
    TextView TV_PayTypeName;
    boolean isSelected=false;

    public MyRadioButton(Context context) {
        super(context);
    }
    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        v = LayoutInflater.from(context).inflate(R.layout.item_pay_type, null);
        IV_PayTypeRB=(ImageView) v.findViewById(R.id.IV_PayTypeRB);
        IV_PayTypeImg=(ImageView) v.findViewById(R.id.IV_PayTypeImg);
        TV_PayTypeName=(TextView) v.findViewById(R.id.TV_PayTypeName);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelected){
                    isSelected=false;
                    IV_PayTypeRB.setBackgroundResource(R.mipmap.radiobutton_off);
                }else{
                    isSelected=true;
                    IV_PayTypeRB.setBackgroundResource(R.mipmap.radiobutton_on);
                }
            }
        });
        int resourceId = -1;
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioView);

        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MyRadioView_Oriental:
                    resourceId = typedArray.getInt(
                            R.styleable.MyRadioView_Oriental, 0);
                    this.setOrientation(resourceId == 1 ? LinearLayout.HORIZONTAL
                            : LinearLayout.VERTICAL);
                    break;
                case R.styleable.MyRadioView_Text:
                    resourceId = typedArray.getResourceId(
                            R.styleable.MyRadioView_Text, 0);
                    TV_PayTypeName.setText(resourceId > 0 ? typedArray.getResources().getText(
                            resourceId) : typedArray
                            .getString(R.styleable.MyRadioView_Text));
                    break;
                case R.styleable.MyRadioView_Src:
                    resourceId = typedArray.getResourceId(
                            R.styleable.MyRadioView_Src, 0);
                    IV_PayTypeImg.setImageResource(resourceId > 0 ?resourceId:R.mipmap.ic_launcher);
                    break;
            }
        }
        addView(v);
        typedArray.recycle();
    }
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        if(isSelected){
            isSelected=false;
            IV_PayTypeRB.setBackgroundResource(R.mipmap.radiobutton_off);
        }else{
            isSelected=true;
            IV_PayTypeRB.setBackgroundResource(R.mipmap.radiobutton_on);
        }
    }



}
