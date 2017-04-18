package com.ydd.conference.custom;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by admin on 2015/4/10.
 */
public class ViewHolder {

    private View mConvertView;

    private SparseArray<View> mViews;

    public ViewHolder(Context context, int itemLayout) {

        mViews = new SparseArray<View>();

        mConvertView = LayoutInflater.from(context).inflate(itemLayout, null);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, int itemLayout) {
        if (convertView == null) {
            return new ViewHolder(context, itemLayout);
        }
        return (ViewHolder) convertView.getTag();
    }


    public <T extends View> T getView(int id) {

        View view = mViews.get(id);
        if (view == null) {
            view = mConvertView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public void setTextView(int id, String text) {
        TextView textView = getView(id);
        if (text != null)
            textView.setText(text);
    }

    public void setImageView(int id, int resourceId) {
        ImageView imageView = getView(id);
        imageView.setImageResource(resourceId);
    }

    public void loadImageView(int id, String url) {
        ImageView imageView = getView(id);

        //ViewUtil.loadImageView(url, imageView);

        /*RequestClient.LoaderImage(url, imageView);*/

    }

    public View getConvertView() {
        return mConvertView;
    }
}
