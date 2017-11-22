package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.model.BrandsTw;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/20.
 */
public class Brand1ClassAdpater extends HemaAdapter {
    private Context mContext;
    private ArrayList<BrandsTw> brandses;
    private BrandActivity activity;

    public Brand1ClassAdpater(Context mContext, ArrayList<BrandsTw> brandses, BrandActivity activity) {
        super(mContext);
        this.mContext = mContext;
        this.brandses = brandses;
        this.activity = activity;
    }

    public void setBrandses(ArrayList<BrandsTw> brandses) {
        this.brandses = brandses;
    }

    @Override
    public boolean isEmpty() {
        return brandses == null || brandses.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : brandses.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_product_class, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    private class ViewHolder {
        TextView zimu_text;
        // DtywGridView gridview_class;
        FlowLayout add_view_guige;
    }

    private void findView(ViewHolder holder, View view) {
        holder.zimu_text = (TextView) view.findViewById(R.id.zimu_text);
        // holder.gridview_class = (DtywGridView) view.findViewById(R.id.gridview_class);
        holder.add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
    }

    private void setData(ViewHolder holder, int position) {
        //item_product_class
        final BrandsTw brands = brandses.get(position);
        holder.zimu_text.setText(brands.getCharindex());
        //添加品牌
        holder.add_view_guige.removeAllViews();
        for (int i = 0; i < brands.getBrands().size(); i++) {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.add_guige_view_text, null);
            TextView guige_text = (TextView) view1.findViewById(R.id.guige_text);
            guige_text.setText(brands.getBrands().get(i).getName());
            if (brands.getBrands().get(i).isCheck()) {
                guige_text.setTextColor(activity.getResources().getColor(R.color.white));
                guige_text.setBackgroundResource(R.drawable.buy_commonid_bg);
            } else {
                guige_text.setTextColor(activity.getResources().getColor(R.color.color_text));
                guige_text.setBackgroundResource(R.drawable.select_check_shai);
            }
            guige_text.setTag(R.id.TAG, i);
            guige_text.setTag(R.id.TAG_VIEWHOLDER, position);
            //选择品牌
            guige_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = (int) v.getTag(R.id.TAG_VIEWHOLDER);
                    int b = (int) v.getTag(R.id.TAG);
                    activity.changePP(a,b);
                    notifyDataSetChanged();
                }
            });
            holder.add_view_guige.addView(view1);
        }
    }
}
