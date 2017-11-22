package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.AddAddressActivity;
import com.hemaapp.dtyw.activity.SelectAddressActivity;
import com.hemaapp.dtyw.model.Shipaddress;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/22.
 */
public class SelectAddressAdapter extends HemaAdapter {
    private ArrayList<Shipaddress> shipaddresses;
    private SelectAddressActivity activity;
    private Context mContext;

    public SelectAddressAdapter(Context mContext, ArrayList<Shipaddress> shipaddresses, SelectAddressActivity activity) {
        super(mContext);
        this.mContext = mContext;
        this.shipaddresses = shipaddresses;
        this.activity = activity;
    }

    public void setShipaddresses(ArrayList<Shipaddress> shipaddresses) {
        this.shipaddresses = shipaddresses;
    }

    @Override
    public boolean isEmpty() {
        return shipaddresses == null || shipaddresses.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : shipaddresses.size();

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_address_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * item_add_address_view
     */
    private class ViewHolder {
        TextView username_text;
        TextView address_text;
        TextView delete_address_text;
        TextView change_address_text;
        LinearLayout add_address_layout;
        LinearLayout text_show;
        ImageView check_address_img;
    }

    private void findView(ViewHolder holder, View view) {
        holder.username_text = (TextView) view.findViewById(R.id.username_text);
        holder.address_text = (TextView) view.findViewById(R.id.address_text);
        holder.delete_address_text = (TextView) view.findViewById(R.id.delete_address_text);
        holder.change_address_text = (TextView) view.findViewById(R.id.change_address_text);
        holder.add_address_layout = (LinearLayout) view.findViewById(R.id.add_address_layout);
        holder.text_show = (LinearLayout) view.findViewById(R.id.text_show);
        holder.check_address_img = (ImageView) view.findViewById(R.id.check_address_img);
    }

    private void setData(ViewHolder holder, int position) {
        if (shipaddresses == null || shipaddresses.size() == 0) {
            holder.text_show.setVisibility(View.GONE);
            holder.add_address_layout.setVisibility(View.VISIBLE);
        } else {
            //判断是否是最后一个
            final Shipaddress shipaddress = shipaddresses.get(position);
            //最后一个
            if (position == shipaddresses.size() - 1) {
                holder.text_show.setVisibility(View.VISIBLE);
                holder.add_address_layout.setVisibility(View.VISIBLE);

            } else {
                holder.text_show.setVisibility(View.VISIBLE);
                holder.add_address_layout.setVisibility(View.GONE);
            }
            if (!isNull(shipaddress.getClientname()))
            {
                if (!isNull(shipaddress.getTel()))
                holder.username_text.setText(shipaddress.getClientname()+"   "+shipaddress.getTel());
                else
                    holder.username_text.setText(shipaddress.getClientname());
            }
            if (!isNull(shipaddress.getAddress()))
                holder.address_text.setText(shipaddress.getAddress());
            //是否选择
            if (isNull(shipaddress.getDefaultadd()) || shipaddress.getDefaultadd().equals("0"))
                holder.check_address_img.setImageResource(R.mipmap.check_anzhuang_on);
            else
                holder.check_address_img.setImageResource(R.mipmap.check_anzhuang_off);
            //选择操作
            holder.check_address_img.setTag(R.id.TAG,shipaddress);
            holder.check_address_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Shipaddress shipaddress1 = (Shipaddress) v.getTag(R.id.TAG);
                    if (shipaddress.getDefaultadd().equals("1"))
                        return;
                    else
                        activity.changeAdd(shipaddress1.getId());
                }
            });
            //编辑
            holder.change_address_text.setTag(R.id.TAG,shipaddress);
            holder.change_address_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Shipaddress shipaddress1 = (Shipaddress) v.getTag(R.id.TAG);
                    Intent intent = new Intent(activity, AddAddressActivity.class);
                    intent.putExtra("address",shipaddress1);
                    activity.startActivity(intent);
                }
            });
            //删除
            holder.delete_address_text.setTag(R.id.TAG,shipaddress);
            holder.delete_address_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Shipaddress shipaddress1 = (Shipaddress) v.getTag(R.id.TAG);
                    activity.deleteAdd(shipaddress1.getId());
                }
            });
        }
        //添加地址
        holder.add_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddAddressActivity.class);
                activity.startActivity(intent);
            }
        });
    }
}
