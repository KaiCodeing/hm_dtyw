package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.model.Type;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/20.
 * 类型列表
 */
public class Brand2TypeAdapter extends HemaAdapter {
    private ArrayList<Type> types;
    private Context mContext;
    private BrandActivity activity;
    private String tit;
    public Brand2TypeAdapter(Context mContext, ArrayList<Type> types, BrandActivity activity,String title) {
        super(mContext);
        this.types = types;
        this.mContext = mContext;
        this.activity = activity;
        this.tit =title;
    }

    public void setTypes(ArrayList<Type> types) {
        this.types = types;
    }

    public void setTit(String title) {
        this.tit = title;
    }

    @Override
    public boolean isEmpty() {
        return types == null || types.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : types.size();
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
        if (isEmpty())
            return getEmptyView(parent);
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
                    activity.changeTypeOneName(a, b);
                }
            });
            holder.add_view_guige.addView(view);
        }

        if (!tit.equals("2")) {
            if (type.isCheck()) {
                holder.add_view_guige.setVisibility(View.VISIBLE);
                holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
            } else {
                holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.shaixuan));
                holder.add_view_guige.setVisibility(View.GONE);
            }
        }
        else
        {
            if (bz)
            {
                holder.add_view_guige.setVisibility(View.VISIBLE);
                holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
            }
            else
            {
                holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.shaixuan));
                holder.add_view_guige.setVisibility(View.GONE);
            }
        }
        //选择
        holder.type_class_text.setTag(R.id.TAG, position);
        holder.type_class_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = (int) v.getTag(R.id.TAG);
                activity.changeTypeName(a);
            }
        });

    }
}
