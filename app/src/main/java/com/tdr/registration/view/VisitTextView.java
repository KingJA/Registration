package com.tdr.registration.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.adapter.InsuranceAdapter;

/**
 * Created by Administrator on 2017/7/28.
 */

public class VisitTextView extends RelativeLayout{


    private final ImageView IV_BKG;
    private final ImageView IV_tick;
    private final TextView TV_Name;
    private boolean Check=false;
    public VisitTextView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.visitview, this);
        IV_BKG= (ImageView) findViewById(R.id.IV_BKG);
        IV_tick = (ImageView) findViewById(R.id.IV_tick);
        TV_Name = (TextView) findViewById(R.id.TV_Name);

        IsChecked(false);
    }
    public VisitTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.visitview, this);
        IV_BKG= (ImageView) findViewById(R.id.IV_BKG);
        IV_tick = (ImageView) findViewById(R.id.IV_tick);
        TV_Name = (TextView) findViewById(R.id.TV_Name);

        IsChecked(false);
    }
    public void setName(String name){
        TV_Name.setText(name);
    }
    public void IsChecked(boolean check){
        Check=check;
        if(check){
            IV_tick.setVisibility(VISIBLE);
            IV_BKG.setBackgroundResource(R.drawable.shape_textview_bkg_blue2);
            TV_Name.setTextColor(Color.parseColor("#4B9DF9"));
        }else{
            IV_tick.setVisibility(GONE);
            IV_BKG.setBackgroundResource(R.drawable.shape_textview_bkg_blue3);
            TV_Name.setTextColor(Color.parseColor("#A0A0A0"));
        }
    }
}
