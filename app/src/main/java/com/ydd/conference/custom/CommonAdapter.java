package com.ydd.conference.custom;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by admin on 2015/4/10.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    private List<T> mList;
    private Context mContext;
    private int mItemLayout;


    public CommonAdapter(Context context, List<T> list, int itemLayout) {
        mContext = context;
        mList = list;
        mItemLayout = itemLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, mItemLayout);

        convertView(viewHolder, getItem(position),position);

        return viewHolder.getConvertView();
    }

    public abstract void convertView(ViewHolder viewHolder, T item,int position);
}
