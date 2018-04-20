package com.tdr.registration.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.ItemModel;
import com.tdr.registration.util.ItemClickListener;
import com.tdr.registration.util.mLog;

import java.util.List;

/**
 * Recycler适配器
 * Created by Linus_Xie on 2016/9/17.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<ItemModel> list;
    private ItemClickListener mItemClickListener;

    public RecyclerAdapter(Context mContext, List<ItemModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 添加数据，制定其位置
     *
     * @param model
     * @param position
     */
    public void addData(ItemModel model, int position) {
        list.add(position, model);
        notifyItemInserted(position);
    }

    /**
     * 添加数据到最后面添加
     *
     * @param model
     */
    public void addData(ItemModel model) {
        list.add(model);
        notifyDataSetChanged();
    }

    /**
     * 删除数据，指定其位置
     *
     * @param position
     */
    public void deleteData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 某一位置开始，有itemCount个Item的数据删除
     *
     * @param positionStart
     * @param itemCount
     */
    public void itemRangeRemoved(int positionStart, int itemCount) {
        for (int i = positionStart; i < itemCount; i++) {
            list.remove(positionStart);
        }
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * 某一位置开始，有itemCount个Item的数据插入
     *
     * @param model
     * @param positionStart
     * @param itemCount
     */
    public void itemRangeInserted(ItemModel model, int positionStart, int itemCount) {
        for (int i = positionStart; i < itemCount; i++) {
            list.add(i, model);
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_item, null);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.textFuncName.setText(list.get(position).getItemName());
        holder.imageFuncIcon.setImageResource(list.get(position).getItemBitResc());

        if (list.get(position).getSize() > 0 && list.get(position).getSize() < 10){
            holder.relativeEllipse.setVisibility(View.GONE);
            holder.relativeCircular.setVisibility(View.VISIBLE);
            holder.textCircularNum.setText(String.valueOf(list.get(position).getSize()));
        } else if (list.get(position).getSize() >= 10){
            holder.relativeCircular.setVisibility(View.GONE);
            holder.relativeEllipse.setVisibility(View.VISIBLE);
            if (list.get(position).getSize() >= 99){
                holder.textEllipseNum.setText("99");
            } else{
                holder.textEllipseNum.setText(String.valueOf(list.get(position).getSize()));
            }
        }

        //为image添加监听回调
      /*  holder.imageFuncIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemSubViewClick(holder.imageFuncIcon, position);
                }
            }
        });*/
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageFuncIcon;
        public TextView textFuncName;
        public RelativeLayout relativeCircular;
        public TextView textCircularNum;
        public RelativeLayout relativeEllipse;
        public TextView textEllipseNum;

        public RecyclerViewHolder(final View view) {
            super(view);
            this.imageFuncIcon = (ImageView) view.findViewById(R.id.image_funcIcon);
            this.textFuncName = (TextView) view.findViewById(R.id.text_funcName);
            this.relativeCircular = (RelativeLayout) view.findViewById(R.id.relative_circular);
            this.textCircularNum = (TextView) view.findViewById(R.id.text_circularNum);
            this.relativeEllipse = (RelativeLayout) view.findViewById(R.id.relative_ellipse);
            this.textEllipseNum = (TextView) view.findViewById(R.id.text_ellipseNum);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, getPosition());
                    }
                }
            });
        }
    }
}
