package com.tdr.kingja.view.dialog;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.tdr.registration.R;

import java.util.List;

/**
 * Description：TODO
 * Create Time：2017/1/21 11:11
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public abstract class BaseListDialog<T> extends BaseDialog implements AdapterView.OnItemClickListener{
    private List<T> data;

    protected BaseListDialog(Context context, List<T>data) {
        super(context);
        this.data = data;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_list;
    }

    @Override
    public void initView() {
        ListView lv = (ListView)findViewById(R.id.lv);
        DialogSelectAdapter<T> dialogSelectAdapter = new DialogSelectAdapter<T>(context, data) {
            @Override
            protected void fillData(List<T> list, int position, TextView tv) {
                fillLvData( list,  position,  tv);
            }
        };
        lv.setAdapter(dialogSelectAdapter);
        lv.setOnItemClickListener(this);
    }
    protected abstract void fillLvData(List<T> list, int position, TextView tv);
    @Override
    public void initData() {

    }

    @Override
    public void settData() {

    }

    @Override
    public void childClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        T t = (T) parent.getItemAtPosition(position);
        this.dismiss();
        onItemSelect(t);
    }

    protected abstract void onItemSelect( T t);
}
