package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.model.AddressOne;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/21.
 * \筛选地区
 */
public class Brand3AddressAdapter extends HemaAdapter {
    private BrandActivity activity;
    private ArrayList<AddressOne> addressOnes;
    private Context mContext;
    private String tit;
    public Brand3AddressAdapter(Context mContext,BrandActivity activity, ArrayList<AddressOne> addressOnes,String tit) {
        super(mContext);
        this.activity = activity;
        this.addressOnes = addressOnes;
        this.mContext = mContext;
        this.tit = tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public void setAddressOnes(ArrayList<AddressOne> addressOnes) {
        this.addressOnes = addressOnes;
    }

    @Override
    public boolean isEmpty() {
        return addressOnes==null || addressOnes.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:addressOnes.size();
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
            ViewGroupHolder holder = new ViewGroupHolder();
            findGroupView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewGroupHolder holder = (ViewGroupHolder) convertView.getTag(R.id.TAG);
        setGrupView(holder, position);
        return convertView;
    }
    //组的view
    private class ViewGroupHolder {
        TextView type_class_text;
        FlowLayout add_view_guige;
    }

    private void findGroupView(ViewGroupHolder holder, View view) {
        holder.type_class_text = (TextView) view.findViewById(R.id.type_class_text);
        holder.add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
    }

    private void setGrupView(ViewGroupHolder holder, int position) {
        AddressOne add = addressOnes.get(position);
        holder.type_class_text.setText(add.getName());
    //    holder.type_class_text.setText(type.getName());
        holder.add_view_guige.removeAllViews();
        boolean bz=false;
        for (int i = 0; i < add.getChildren().size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_item_type, null);
            TextView type_name = (TextView) view.findViewById(R.id.type_name);
            type_name.setText(add.getChildren().get(i).getName());
            if (add.getChildren().get(i).isCheck()) {
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
                    activity.changeAddressOneName(a, b);
                }
            });
            holder.add_view_guige.addView(view);
        }

        if (!tit.equals("2")) {
            if (add.isCheck()) {
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
                activity.changeAddressName(a);
            }
        });

    }

}
