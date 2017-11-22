package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;

/**
 * Created by lenovo on 2016/9/20.
 * 消息盒子
 */
public class message_adapter extends HemaAdapter {
    public message_adapter(Context mContext) {
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
        RoundedImageView user_img;
        View show_message;
        TextView user_name;
        TextView message_content;
        TextView time_text;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.user_img = (RoundedImageView) view.findViewById(R.id.user_img);
        holder.show_message = view.findViewById(R.id.show_message);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.message_content = (TextView) view.findViewById(R.id.message_content);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
    }
    private void setData(ViewHolder holder,int position)
    {

    }
}
