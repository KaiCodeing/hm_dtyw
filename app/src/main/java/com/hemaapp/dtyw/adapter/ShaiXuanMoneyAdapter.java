package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;

/**
 * Created by lenovo on 2016/9/22.
 * 筛选价格
 */
public class ShaiXuanMoneyAdapter extends HemaAdapter {

    public ShaiXuanMoneyAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
    private class ViewHolder{
        TextView money_text;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.money_text = (TextView) view.findViewById(R.id.money_text);
    }
    private void setData(ViewHolder holder,int position)
    {
        //item_shaixuan_money_view
    }
}
