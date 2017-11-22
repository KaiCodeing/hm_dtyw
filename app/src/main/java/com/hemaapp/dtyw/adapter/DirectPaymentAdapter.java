package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.DirectPaymentActivity;
import com.hemaapp.dtyw.activity.SelectAddressActivity;
import com.hemaapp.dtyw.model.Defaultadd;
import com.hemaapp.dtyw.model.GoodsGet;
import com.hemaapp.dtyw.model.Installservice;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/30.
 * 直接支付
 */
public class DirectPaymentAdapter extends HemaAdapter {
    private GoodsGet goodsGet;
    private ArrayList<Installservice> installservices;
    private DirectPaymentActivity activity;
    private Context mContext;
    private Defaultadd defaultadd;
    private String price;
    private String anvalue;
    private String peopery;
    private String stock;
    public DirectPaymentAdapter(Context mContext, GoodsGet goodsGet, ArrayList<Installservice> installservices,
                                DirectPaymentActivity activity, Defaultadd defaultadd, String price,String anvalue,String peopery,
                                String stock) {
        super(mContext);
        this.mContext = mContext;
        this.goodsGet = goodsGet;
        this.installservices = installservices;
        this.price = price;
        this.defaultadd = defaultadd;
        this.activity = activity;
        this.anvalue = anvalue;
        this.peopery = peopery;
        this.stock = stock;
    }

    public void setDefaultadd(Defaultadd defaultadd) {
        this.defaultadd = defaultadd;
    }

    public void setGoodsGet(GoodsGet goodsGet) {
        this.goodsGet = goodsGet;
    }

    public void setPeopery(String peopery) {
        this.peopery = peopery;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int getCount() {
        return 2;
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
            switch (position) {
                case 0:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_address, null);
                    ViewHolder holder = new ViewHolder();
                    findView(holder, convertView);
                    convertView.setTag(R.id.TAG, holder);
                    break;
                case 1:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_addview_affirm, null);
                    AddViewHolder holder2 = new AddViewHolder();
                    findViewAdd(holder2, convertView);
                    convertView.setTag(R.id.TAG, holder2);
                    break;

            }
        }
        switch (position) {
            case 0:
                ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
                setData(holder, position);
                break;
            case 1:
                AddViewHolder viewHolder = (AddViewHolder) convertView.getTag(R.id.TAG);
                setAddView(viewHolder);
                break;

        }
        return convertView;
    }

    /**
     * 添加的view
     */
    private class AddViewHolder {
        LinearLayout add_view_affirm;
    }

    private void findViewAdd(AddViewHolder viewHolder, View view) {
        viewHolder.add_view_affirm = (LinearLayout) view.findViewById(R.id.add_view_affirm);
    }

    private void setAddView(AddViewHolder viewHolder) {
        //动态添加商品
        viewHolder.add_view_affirm.removeAllViews();

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mai_shanping_2view, null);
        ViewHolder6 holder6 = new ViewHolder6();
        findView6(holder6, view);
        setData6(holder6);
        viewHolder.add_view_affirm.addView(view);
        for (int i = 0; i < installservices.size(); i++) {
            if (installservices.get(i).isCheck()) {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_anzhuang_view, null);
                ViewHolder3 holder3 = new ViewHolder3();
                findView3(holder3, view1);
                setData(holder3, installservices.get(i));
                viewHolder.add_view_affirm.addView(view1);
            }
        }
//        //运费
//        double yfNum = Double.valueOf(goodsGet.getShipment());
//        //商品数量
//        int spNumber = 0;
//        //商品总价
//        double spMoney = 0;
//        for (int i = 0; i < cars.size(); i++) {
//            spNumber = Integer.valueOf(cars.get(i).getBuycount()) + spNumber;
//            spMoney = ((Double.valueOf(cars.get(i).getBuycount())) * Double.valueOf(cars.get(i).getPrice())) + spMoney;
//            if (!isNull(cars.get(i).getInstallprice()) && !cars.get(i).getInstallid().equals("0"))
//                spMoney = Double.valueOf(cars.get(i).getInstallprice()) + spMoney;
//            if (Double.valueOf(cars.get(i).getShipment()) > yfNum)
//                yfNum = Double.valueOf(cars.get(i).getShipment());
//        }
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.item_yunfei_view, null);
        ViewHolder4 holder4 = new ViewHolder4();
        findView4(holder4, view2);
        setData4(holder4);
        viewHolder.add_view_affirm.addView(view2);

    }

    /**
     * 地址 item_add_address
     */
    private class ViewHolder {
        LinearLayout add_address_show_view;
        LinearLayout add_address_layout;
        LinearLayout select_address_layout;
        TextView name_nick_text;
        TextView user_iphone;
        TextView user_address;
    }

    private void findView(ViewHolder holder, View view) {
        holder.add_address_show_view = (LinearLayout) view.findViewById(R.id.add_address_show_view);
        holder.add_address_layout = (LinearLayout) view.findViewById(R.id.add_address_layout);
        holder.select_address_layout = (LinearLayout) view.findViewById(R.id.select_address_layout);
        holder.name_nick_text = (TextView) view.findViewById(R.id.name_nick_text);
        holder.user_iphone = (TextView) view.findViewById(R.id.user_iphone);
        holder.user_address = (TextView) view.findViewById(R.id.user_address);
    }

    private void setData(ViewHolder holder, int position) {
        if (defaultadd.getId().equals("0")) {
            holder.select_address_layout.setVisibility(View.GONE);
            holder.add_address_show_view.setVisibility(View.VISIBLE);
        } else {
            holder.select_address_layout.setVisibility(View.VISIBLE);
            holder.add_address_show_view.setVisibility(View.GONE);
            if (!isNull(defaultadd.getClientname()))
                holder.name_nick_text.setText(defaultadd.getClientname());
            if (!isNull(defaultadd.getAddress()))
                holder.user_address.setText(defaultadd.getAddress());
            if (!isNull(defaultadd.getTel()))
                holder.user_iphone.setText(defaultadd.getTel());
        }
        //选择地址
        holder.select_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectAddressActivity.class);
                mContext.startActivity(intent);
            }
        });
        //添加地址
        holder.add_address_show_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AddAddressActivity.class);
//                mContext.startActivity(intent);
                Intent intent = new Intent(mContext, SelectAddressActivity.class);
                mContext.startActivity(intent);
            }
        });
    }


    /**
     * 安装服务 item_anzhuang_view
     */
    private class ViewHolder3 {
        TextView fuwu_name;
        TextView address_text;
        TextView phone_text;
        TextView money_text;
        TextView anzhuang_text;
    }

    private void findView3(ViewHolder3 holder3, View view) {
        holder3.fuwu_name = (TextView) view.findViewById(R.id.fuwu_name);
        holder3.address_text = (TextView) view.findViewById(R.id.address_text);
        holder3.phone_text = (TextView) view.findViewById(R.id.phone_text);
        holder3.money_text = (TextView) view.findViewById(R.id.money_text);
        holder3.anzhuang_text = (TextView) view.findViewById(R.id.anzhuang_text);
    }

    private void setData(ViewHolder3 holder3, Installservice installservice) {

        holder3.anzhuang_text.setVisibility(View.VISIBLE);
        if (!isNull(installservice.getName()))
            holder3.fuwu_name.setText(installservice.getName());
        if (!isNull(installservice.getAddress()))
            holder3.address_text.setText(installservice.getAddress());
        if (!isNull(installservice.getPhone()))
            holder3.phone_text.setText(installservice.getPhone());
        if (!isNull(installservice.getPrice()))
            holder3.money_text.setText(installservice.getPrice());

    }

    /**
     * 运费 全部费用 item_yunfei_view
     */
    private class ViewHolder4 {
        TextView yunfei_text;
        TextView all_number;
        TextView money_all;
        TextView contain_text;
    }

    private void findView4(ViewHolder4 holder4, View view) {
        holder4.yunfei_text = (TextView) view.findViewById(R.id.yunfei_text);
        holder4.all_number = (TextView) view.findViewById(R.id.all_number);
        holder4.money_all = (TextView) view.findViewById(R.id.money_all);
        holder4.contain_text = (TextView) view.findViewById(R.id.contain_text);
    }

    private void setData4(ViewHolder4 holder4) {
        holder4.yunfei_text.setText(goodsGet.getShipment());
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        holder4.money_all.setText(String.valueOf( df.format(Double.valueOf(goodsGet.getNumber())*Double.valueOf(price)+Double.valueOf(anvalue)+Double.valueOf(goodsGet.getShipment()))));
        holder4.contain_text.setText("（含运费" + goodsGet.getShipment() + "元）");
        holder4.all_number.setText("共" + goodsGet.getNumber() + "件商品");
    }

    /**
     * 直接购买的时候的商品列表  item_mai_chanping_2view
     */
    private class ViewHolder6 {
        LinearLayout shangp_layout;
        ImageView commod_img;
        TextView commod_name;
        TextView money_text;
        FlowLayout flowlayout;
        ImageView subtarct;
        TextView add_or_subtract;
        ImageView add;
        TextView add_view_guige;
    }

    private void findView6(ViewHolder6 holder6, View view) {
        holder6.shangp_layout = (LinearLayout) view.findViewById(R.id.shangp_layout);
        holder6.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder6.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder6.money_text = (TextView) view.findViewById(R.id.money_text);
        holder6.flowlayout = (FlowLayout) view.findViewById(R.id.flowlayout);
        holder6.add_or_subtract = (TextView) view.findViewById(R.id.add_or_subtract);
        holder6.subtarct = (ImageView) view.findViewById(R.id.subtarct);
        holder6.add = (ImageView) view.findViewById(R.id.add);
        holder6.add_view_guige = (TextView) view.findViewById(R.id.add_view_guige);
    }

    private void setData6(ViewHolder6 holder6) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(goodsGet.getImgurl(), holder6.commod_img, options);
        holder6.commod_name.setText(goodsGet.getName());
        holder6.money_text.setText(price);
        holder6.add_or_subtract.setText(goodsGet.getNumber());
        if (isNull(peopery))
            holder6.add_view_guige.setVisibility(View.GONE);
        else
        {
            holder6.add_view_guige.setVisibility(View.VISIBLE);
            holder6.add_view_guige.setText(peopery);
        }
        //减
        holder6.subtarct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(goodsGet.getNumber()) == 1) {
                } else {
                    goodsGet.setNumber(String.valueOf(Integer.valueOf(goodsGet.getNumber()) - 1));
                }
                notifyDataSetChanged();
            }
        });
        //jia
        holder6.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(goodsGet.getNumber())<Integer.valueOf(stock))
                {
                goodsGet.setNumber(String.valueOf(Integer.valueOf(goodsGet.getNumber()) + 1));
                notifyDataSetChanged();
                }
                else
                {
                    activity.showTextDialog("库存不足");
                }
            }
        });
    }
}
