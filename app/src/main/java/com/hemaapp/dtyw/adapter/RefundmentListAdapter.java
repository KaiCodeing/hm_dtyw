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

import com.hemaapp.dtyw.activity.RefundmentActivity;
import com.hemaapp.dtyw.model.OrderReturn;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/10.
 * 退款订单列表
 */
public class RefundmentListAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<OrderReturn> orderReturns;

    public RefundmentListAdapter(Context mContext, ArrayList<OrderReturn> orderReturns) {
        super(mContext);
        this.mContext = mContext;
        this.orderReturns = orderReturns;
    }

    public void setOrderReturns(ArrayList<OrderReturn> orderReturns) {
        this.orderReturns = orderReturns;
    }

    @Override
    public boolean isEmpty() {
        return orderReturns == null || orderReturns.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : orderReturns.size();

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_refundment_list, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * item_refundment_list
     */
    private class ViewHolder {
        TextView number_text;
        TextView type_text;
        LinearLayout anzhuang_layout;
        LinearLayout add_view;
        TextView fuwu_name;
        TextView address_text;
        TextView phone_text;
        TextView money_text;
        TextView money_all;
        TextView contain_text;
        ImageView commod_img;
        TextView commod_name;
        TextView money_text_com;
        TextView number_text_com;
        LinearLayout shangp_layout;
        TextView tui_ping_text;
        TextView add_view_guige;
    }

    private void findView(ViewHolder holder, View view) {
        holder.number_text = (TextView) view.findViewById(R.id.number_text);
        holder.type_text = (TextView) view.findViewById(R.id.type_text);
        holder.anzhuang_layout = (LinearLayout) view.findViewById(R.id.anzhuang_layout);
        holder.add_view = (LinearLayout) view.findViewById(R.id.add_view);
        holder.fuwu_name = (TextView) view.findViewById(R.id.fuwu_name);
        holder.address_text = (TextView) view.findViewById(R.id.address_text);
        holder.phone_text = (TextView) view.findViewById(R.id.phone_text);
        holder.money_text = (TextView) view.findViewById(R.id.money_text);
        holder.money_all = (TextView) view.findViewById(R.id.money_all);
        holder.contain_text = (TextView) view.findViewById(R.id.contain_text);
        holder.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.money_text_com = (TextView) view.findViewById(R.id.money_text_com);
        holder.number_text_com = (TextView) view.findViewById(R.id.number_text_com);
        holder.shangp_layout = (LinearLayout) view.findViewById(R.id.shangp_layout);
        holder.tui_ping_text = (TextView) view.findViewById(R.id.tui_ping_text);
        holder.add_view_guige = (TextView) view.findViewById(R.id.add_view_guige);
    }

    private void setData(ViewHolder holder, int position) {
        final OrderReturn orderReturn = orderReturns.get(position);
        holder.tui_ping_text.setVisibility(View.GONE);
        //订单号number_text
        holder.number_text.setText("订单号:"+orderReturn.getOrdernum());
        //type_text审核状态
        if ("1".equals(orderReturn.getAccountreturn()))
        {
        holder.type_text.setText("退款申请中");
        }
        else if("2".equals(orderReturn.getAccountreturn()))
        {
            holder.type_text.setText("同意退款");
        }
        else if("3".equals(orderReturn.getAccountreturn()))
            holder.type_text.setText("已退款");
        else
            holder.type_text.setText("退款失败");
        //商品图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(orderReturn.getGoodsimgurl(), holder.commod_img, options);
        //商品名称
        holder.commod_name.setText(orderReturn.getGoodsname());
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        //商品价格
        holder.money_text_com.setText(String.valueOf(df.format(Double.valueOf(orderReturn.getPrice()))));
        //商品数量
        holder.number_text_com.setText(orderReturn.getGoodsnum());
        //商品规格
        holder.add_view_guige.setVisibility(View.VISIBLE);
        if (isNull(orderReturn.getGoodspropertyname()))
            holder.add_view_guige.setText("");
        else
            holder.add_view_guige.setText(orderReturn.getGoodspropertyname());
        //安装服务判断
        if ("0".equals(orderReturn.getInstall()))
            holder.anzhuang_layout.setVisibility(View.GONE);
        else {
            holder.anzhuang_layout.setVisibility(View.VISIBLE);
            //服务店名称
            holder.fuwu_name.setText(orderReturn.getInstallname());
            //服务店电话
            holder.phone_text.setText(orderReturn.getInstallphone());
            //服务店地址
            holder.address_text.setText(orderReturn.getInstalladdress());
            //服务价值
            holder.money_text.setText(orderReturn.getInstallprice());
        }

        //总价
        holder.money_all.setText(String.valueOf(df.format(Double.valueOf(orderReturn.getRefundmoney()))));
        //vcontain_text运费
        holder.contain_text.setText("（含运费"+orderReturn.getShipment()+"元）");
        //跳转到退款详情
        holder.shangp_layout.setTag(R.id.TAG,orderReturn);
        holder.shangp_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderReturn orderReturn1 = (OrderReturn) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, RefundmentActivity.class);
                intent.putExtra("orderid",orderReturn1.getOrderid());
                intent.putExtra("cartid",orderReturn1.getCartid());
                intent.putExtra("goodsid",orderReturn1.getGoodsid());
                mContext.startActivity(intent);
            }
        });
    }
}
