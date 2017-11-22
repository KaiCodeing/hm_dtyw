package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.fragment.ProduceSortationFragment;
import com.hemaapp.dtyw.model.Type;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/22.
 * 筛选的二级列表
 */
public class ShaiXuanExpandAdapter extends HemaAdapter {
    private ArrayList<Type> types;
    private ProduceSortationFragment fragment;
    private Context mContext;
    public ShaiXuanExpandAdapter(ArrayList<Type> types,ProduceSortationFragment fragment,Context mContext)
    {
        super(mContext);
        this.mContext = mContext;
        this.types = types;
        this.fragment = fragment;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    @Override
    public boolean isEmpty() {
        return types==null || types.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:types.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_brand_2_type, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }
    private class ViewHolder {
        TextView type_class_text;
        FlowLayout add_view_guige;
    }

    private void findView(ViewHolder holder, View view) {
        holder.add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
        holder.type_class_text = (TextView) view.findViewById(R.id.type_class_text);
    }
    private void setData(ViewHolder holder, int position) {
        Type type = types.get(position);
        holder.type_class_text.setText(type.getName());
        holder.add_view_guige.removeAllViews();
        boolean bz=false;
        for (int i = 0; i < type.getChildren().size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_item_type, null);
            TextView type_name = (TextView) view.findViewById(R.id.type_name);
            type_name.setText(type.getChildren().get(i).getName());
            if (type.getChildren().get(i).isCheck()) {
                type_name.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
                bz=true;
            } else
                type_name.setTextColor(mContext.getResources().getColor(R.color.shaixuan));
            //选择
            type_name.setTag(R.id.TAG, position);
            type_name.setTag(R.id.TAG_VIEWHOLDER, i);
            type_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = (int) v.getTag(R.id.TAG);
                    int b = (int) v.getTag(R.id.TAG_VIEWHOLDER);
                  fragment.changeTypeOneName(a, b);
                }
            });
            holder.add_view_guige.addView(view);
        }
        if (type.isCheck()) {
            holder.add_view_guige.setVisibility(View.VISIBLE);
            holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        } else {
            holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.shaixuan));
            holder.add_view_guige.setVisibility(View.GONE);
        }

        //选择
        holder.type_class_text.setTag(R.id.TAG, position);
        holder.type_class_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = (int) v.getTag(R.id.TAG);
             fragment.changeTypeName(a);
            }
        });

    }
}
