package com.hemaapp.dtyw.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.dtyw.model.BankInfo;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.unionpay.mobile.android.plugin.BaseActivity;

import java.util.ArrayList;

/**
 * 银行卡列表
 */
public class BankAdapter extends HemaAdapter implements View.OnClickListener {

    private ArrayList<BankInfo> data = null;

    public BankAdapter(Context mContext, ArrayList<BankInfo> data) {
        super(mContext);
        this.data = data;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    public void setData(ArrayList<BankInfo> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null || data.size() == 0 ? 1 : data.size();
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
        if (isEmpty()) {
            return getEmptyView(parent);
        }
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.bank_item, null);
            holder = new ViewHolder();
            holder.divider = (View) convertView.findViewById(R.id.line);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        BankInfo bankInfo = data.get(position);
        holder.name.setText(bankInfo.getName());
        convertView.setTag(R.id.TAG_VIEWHOLDER, bankInfo);

        if (getCount() == 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            if (getCount() - 1 == position) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        BankInfo bankInfo = (BankInfo) v.getTag(R.id.TAG_VIEWHOLDER);
        ImageView opt = (ImageView) v.findViewById(R.id.opt);
        if (bankInfo != null) {
            opt.setImageResource(R.mipmap.check_anzhuang_off);
            Intent intent = new Intent();
            intent.putExtra("bank", bankInfo);
            ((BaseActivity) mContext).setResult(Activity.RESULT_OK, intent);
            ((BaseActivity) mContext).finish();
        }
    }

    static class ViewHolder {
        private TextView name = null;
        private View divider = null;
    }
}