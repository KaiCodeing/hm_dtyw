package com.hemaapp.dtyw.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.activity.ProductClassActivity;
import com.hemaapp.dtyw.model.Brands;
import com.hemaapp.dtyw.model.BrandsTw;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/21.
 * 产品分类的adapter
 */
public class ProductClassAdapter extends HemaAdapter {
    private ProductClassActivity mContext;
    private ArrayList<BrandsTw> brandses;
    private String type;

    public ProductClassAdapter(ProductClassActivity mContext, ArrayList<BrandsTw> brandses, String type) {
        super(mContext);
        this.mContext = mContext;
        this.brandses = brandses;
        this.type = type;
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
            guige_text.setTag(R.id.TAG, brands.getBrands().get(i));
            //选择品牌
            guige_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Brands brands1 = (Brands) v.getTag(R.id.TAG);
                    if (isNull(type)) {
                        Intent intent = new Intent(mContext, BrandActivity.class);
                        intent.putExtra("brandId", brands1.getId());
                        intent.putExtra("brandName", brands1.getName());
                        intent.putExtra("typekey", "3");
                        mContext.startActivity(intent);
                    }else {
                        mContext.finishA(brands1);
                    }
                }
            });
            holder.add_view_guige.addView(view1);
        }
    }
}
