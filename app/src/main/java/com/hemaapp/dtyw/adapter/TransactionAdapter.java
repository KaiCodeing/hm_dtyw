package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.dtyw.model.PayList;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/30.
 * 交易记录
 */
public class TransactionAdapter extends HemaAdapter {
    private ArrayList<PayList> payLists;
    private Context mContext;

    public TransactionAdapter(Context mContext, ArrayList<PayList> payLists) {
        super(mContext);
        this.payLists = payLists;
        this.mContext = mContext;
    }

    @Override
    public boolean isEmpty() {
        return payLists == null || payLists.size() == 0;

    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : payLists.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_transaction_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * item_transaction_view
     */
    private class ViewHolder {
        ImageView type_img;
        TextView type_name;
        TextView type_time;
        TextView type_number;
    }

    private void findView(ViewHolder holder, View view) {
        holder.type_img = (ImageView) view.findViewById(R.id.type_img);
        holder.type_name = (TextView) view.findViewById(R.id.type_name);
        holder.type_time = (TextView) view.findViewById(R.id.type_time);
        holder.type_number = (TextView) view.findViewById(R.id.type_number);
    }

    private void setData(ViewHolder holder, int position) {
        PayList payList = payLists.get(position);
        //时间
        holder.type_time.setText(payList.getRegdate());
        //购买商品
        if ("2".equals(payList.getKeytype())) {
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText(prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.jiaoyi_jian));
            holder.type_img.setImageResource(R.mipmap.goumai_img_jy);
            holder.type_name.setText("购买商品");
        }
        //支付宝充值
        else if ("1".equals(payList.getKeytype())) {
            holder.type_img.setImageResource(R.mipmap.zhifubao_img_jy);
            holder.type_name.setText("支付宝充值");
                String prelce ="0";
                if (isNull(payList.getAmount()))
                    prelce = "0";
                else
                    prelce = payList.getAmount();
            holder.type_number.setText("+"+prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));

        }
        //微信充值
        else if ("3".equals(payList.getKeytype())) {
            holder.type_img.setImageResource(R.mipmap.weixin_img_jy);
            holder.type_name.setText("微信充值");
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText("+"+prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        }
        //已，银联充值
        else if ("4".equals(payList.getKeytype())) {
            holder.type_img.setImageResource(R.mipmap.yinlian_img_jy);
            holder.type_name.setText("银联充值");
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText("+"+prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        }
        //支付宝提现
        else if ("5".equals(payList.getKeytype())) {
            holder.type_img.setImageResource(R.mipmap.zhifubao_img_jy);
            holder.type_name.setText("支付宝提现");
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText(prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.jiaoyi_jian));
        }
        //银行卡提现
        else if ("6".equals(payList.getKeytype())) {
            holder.type_img.setImageResource(R.mipmap.yinlian_img_jy);
            holder.type_name.setText("银联卡提现");
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText(prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.jiaoyi_jian));
        }
        //退款
        else if ("7".equals(payList.getKeytype()))
        {
            holder.type_img.setImageResource(R.mipmap.tuikuan_img);
            holder.type_name.setText("退款");
            String prelce ="0";
            if (isNull(payList.getAmount()))
                prelce = "0";
            else
                prelce = payList.getAmount();
            holder.type_number.setText("+"+prelce);
            holder.type_number.setTextColor(mContext.getResources().getColor(R.color.title_backgroid));
        }
    }
}
