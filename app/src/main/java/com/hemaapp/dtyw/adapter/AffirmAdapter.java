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

import com.hemaapp.dtyw.activity.AffirmActivity;
import com.hemaapp.dtyw.activity.SelectAddressActivity;
import com.hemaapp.dtyw.model.Car;
import com.hemaapp.dtyw.model.Defaultadd;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/28.
 * 订单详情
 */
public class AffirmAdapter extends HemaAdapter {
    private ArrayList<Car> cars;

    private AffirmActivity activity;
    private Context mContext;
    private Defaultadd defaultadd;

    public AffirmAdapter(Context mContext, ArrayList<Car> cars, AffirmActivity activity, Defaultadd defaultadd) {
        super(mContext);
        this.mContext = mContext;
        this.cars = cars;

        this.activity = activity;
        this.defaultadd = defaultadd;
    }

    public void setDefaultadd(Defaultadd defaultadd) {
        this.defaultadd = defaultadd;
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
    public int getViewTypeCount() {
        return 5;
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
        for (int i = 0; i < cars.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mai_chanping_view, null);
            ViewHolder2 holder2 = new ViewHolder2();
            findView2(holder2, view);
            setData2(holder2, i);
            viewHolder.add_view_affirm.addView(view);
            if (cars.get(i).getInstallid()!= null && !cars.get(i).getInstallid().equals("0")) {
                View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_anzhuang_view, null);
                ViewHolder3 holder3 = new ViewHolder3();
                findView3(holder3, view1);
                setData(holder3, 0, i);
                viewHolder.add_view_affirm.addView(view1);
            }
        }
        int bz = 0;
        for (int i = 0; i < cars.size(); i++) {

        }
        //运费
        double yfNum = 0;
        //商品数量
        int spNumber = 0;
        //商品总价
        double spMoney = 0;
        for (int i = 0; i < cars.size(); i++) {
            spNumber = Integer.valueOf(cars.get(i).getBuycount()) + spNumber;
            spMoney = ((Double.valueOf(cars.get(i).getBuycount())) * Double.valueOf(cars.get(i).getPrice())) + spMoney;
            if (!isNull(cars.get(i).getInstallprice()) && !cars.get(i).getInstallid().equals("0"))
                spMoney = Double.valueOf(cars.get(i).getInstallprice())+spMoney;
            if (Double.valueOf(cars.get(i).getShipment()) > yfNum)
                yfNum = Double.valueOf(cars.get(i).getShipment());
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_yunfei_view, null);
        ViewHolder4 holder4 = new ViewHolder4();
        findView4(holder4, view);
        setData4(holder4, 1, yfNum, spNumber, spMoney+yfNum);
        viewHolder.add_view_affirm.addView(view);

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
     * 商品列表 item_mai_chanping_view
     */
    private class ViewHolder2 {
        LinearLayout shangp_layout;
        ImageView commod_img;
        TextView commod_name;
        TextView money_text;
        FlowLayout flowlayout;
        TextView number_text;
        TextView content_commid;
    }

    private void findView2(ViewHolder2 holder2, View view) {
        holder2.shangp_layout = (LinearLayout) view.findViewById(R.id.shangp_layout);
        holder2.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder2.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder2.money_text = (TextView) view.findViewById(R.id.money_text);
        holder2.flowlayout = (FlowLayout) view.findViewById(R.id.flowlayout);
        holder2.number_text = (TextView) view.findViewById(R.id.number_text);
        holder2.content_commid = (TextView) view.findViewById(R.id.content_commid);
    }

    private void setData2(ViewHolder2 holder2, int position) {
        Car car = cars.get(position);
        //商品图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(car.getImgurl(), holder2.commod_img, options);
        holder2.commod_name.setText(car.getGoodsname());
        holder2.money_text.setText(car.getPrice());
        holder2.number_text.setText(car.getBuycount());
        if (!isNull(car.getPropertyname())) {
            holder2.content_commid.setVisibility(View.VISIBLE);
            holder2.content_commid.setText(car.getPropertyname());
        }
        else
            holder2.content_commid.setVisibility(View.GONE);
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

    private void setData(ViewHolder3 holder3, int on, int position) {
        Car car = cars.get(position);

            holder3.anzhuang_text.setVisibility(View.VISIBLE);

        if (!isNull(car.getName()))
            holder3.fuwu_name.setText(car.getName());
        if (!isNull(car.getSeraddress()))
            holder3.address_text.setText(car.getSeraddress());
        if (!isNull(car.getPhone()))
            holder3.phone_text.setText(car.getPhone());
        if (!isNull(car.getInstallprice()))
            holder3.money_text.setText(car.getInstallprice());
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

    private void setData4(ViewHolder4 holder4, int position, double yfNum, int spNumber, double spMoney) {
        holder4.yunfei_text.setText(String.valueOf(yfNum));
        holder4.money_all.setText(String.valueOf(spMoney));
        holder4.contain_text.setText("（含运费" + String.valueOf(yfNum) + "元）");
        holder4.all_number.setText("共" + String.valueOf(spNumber) + "件商品");
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
    }

    private void setData6(ViewHolder6 holder6, int position) {

    }
}


