package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.model.Pricerange;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/21.
 * 筛选价格
 */
public class Brand4PriceAdapter extends HemaAdapter {
    private ArrayList<Pricerange> priceranges ;
    private BrandActivity activity;
    private Context mContext;
    public Brand4PriceAdapter(Context mContext,ArrayList<Pricerange> priceranges,BrandActivity activity) {
        super(mContext);
        this.priceranges = priceranges;
        this.mContext = mContext;
        this.activity = activity;
    }

    public void setPriceranges(ArrayList<Pricerange> priceranges) {
        this.priceranges = priceranges;
    }

    @Override
    public boolean isEmpty() {
        return priceranges==null || priceranges.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:priceranges.size();
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
    private void setData(ViewHolder holder,int position)
    {
        Pricerange pricerange = priceranges.get(position);
        holder.type_class_text.setText(pricerange.getDescription());
        if (pricerange.isCheck())
            holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        else
            holder.type_class_text.setTextColor(mContext.getResources().getColor(R.color.shaixuan));
        holder.type_class_text.setTag(R.id.TAG,position);
        holder.type_class_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = (int) v.getTag(R.id.TAG);
                activity.changePrice(a);
            }
        });
    }
}
