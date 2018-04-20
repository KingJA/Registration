package com.tdr.registration.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.base.BaseDialogAlert;
import com.tdr.registration.fragment.BusinessFragment;
import com.tdr.registration.model.BikeCode;

import java.util.List;


/**
 * 备案登记对话框选项
 * Created by Linus_Xie on 2016/9/18.
 */
public class DialogRegistration extends Dialog {


    private OnClickListener onClickListener;
    private ListView LL_CarType;

    public DialogRegistration(Context context, BusinessFragment.myAdapter adapter, View view) {
        super(context);
        LL_CarType =(ListView) view.findViewById(R.id.LL_CarType);
        LL_CarType.setAdapter(adapter);
        LL_CarType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickListener.onRegisterationClick(position);
            }
        });
        setContentView(view);
//        setFullWidth();
    }



    private void setFullWidth() {
        Window win = this.getWindow();
        this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onRegisterationClick(int position);
    }
}

