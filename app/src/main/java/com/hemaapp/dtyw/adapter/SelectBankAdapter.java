package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.SelectBankActivity;
import com.hemaapp.dtyw.model.Bank;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/8.
 * 选择银行的adapter
 */
public class SelectBankAdapter extends HemaAdapter {
    private SelectBankActivity activity;
    private Context mContext;
    private ArrayList<Bank> banks;
    public SelectBankAdapter(Context mContext,SelectBankActivity activity,ArrayList<Bank> banks) {
        super(mContext);
        this.activity = activity;
        this.mContext = mContext;
        this.banks = banks;
    }

    public void setBanks(ArrayList<Bank> banks) {
        this.banks = banks;
    }

    @Override
    public boolean isEmpty() {
        return banks==null || banks.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?0:banks.size();
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
        if (convertView == null)
        {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_select_bank_view,null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder,position);
        return convertView;
    }
    /**
     * item_select_bank_view
     */
    private class ViewHolder{
        TextView bank_name;
        ImageView check_imgview;
        View line;
        LinearLayout bank_layout;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.bank_name = (TextView) view.findViewById(R.id.bank_name);
        holder.check_imgview = (ImageView) view.findViewById(R.id.check_imgview);
        holder.line = view.findViewById(R.id.line);
        holder.bank_layout = (LinearLayout) view.findViewById(R.id.bank_layout);

    }
    private void setData(ViewHolder holder,int position)
    {
        final Bank bank = banks.get(position);
        //名称
        holder.bank_name.setText(bank.getName());
        //是否选择
        if (bank.isCheck())
            holder.check_imgview.setImageResource(R.mipmap.check_anzhuang_off);
        else
            holder.check_imgview.setImageResource(R.mipmap.check_anzhuang_on);
        //选择
        holder.bank_layout.setTag(R.id.TAG,bank);
        holder.bank_layout.setTag(R.id.TAG_VIEWHOLDER,position);
        holder.bank_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bank bank1 = (Bank) v.getTag(R.id.TAG);
                int a = (int) v.getTag(R.id.TAG_VIEWHOLDER);


                for (int i =0;i<banks.size();i++)
                {
                    if (a==i)
                    {
                        if (bank1.isCheck())
                            bank1.setCheck(false);
                        else
                            bank1.setCheck(true);
                    }
                    else
                        banks.get(i).setCheck(false);
                }
                notifyDataSetChanged();
            }
        });
    }
}
