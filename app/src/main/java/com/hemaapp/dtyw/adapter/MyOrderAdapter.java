package com.hemaapp.dtyw.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.MyOrderActivity;
import com.hemaapp.dtyw.activity.OrderDetaiActivityl;
import com.hemaapp.dtyw.activity.Pay1Activity;
import com.hemaapp.dtyw.model.OrderList;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/9.
 */
public class MyOrderAdapter extends HemaAdapter {
    private MyOrderActivity activity;
    private ArrayList<OrderList> orderLists;
    private String keytype;
    public MyOrderAdapter(MyOrderActivity activity, ArrayList<OrderList> orderLists,String keytype) {
        super(activity);
        this.activity = activity;
        this.orderLists = orderLists;
        this.keytype = keytype;
    }

    public ArrayList<OrderList> getOrderLists() {
        return orderLists;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setOrderLists(ArrayList<OrderList> orderLists) {
        this.orderLists = orderLists;
    }

    @Override
    public boolean isEmpty() {
        return orderLists == null || orderLists.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : orderLists.size();
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_order_my_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder, position);
        return convertView;
    }

    /**
     * item_order_my_view
     */
    private class ViewHolder {
        TextView number_text;
        TextView shouhuo_type;
        ImageView iamge1;
        ImageView iamge2;
        ImageView iamge3;
        TextView all_number;
        LinearLayout look_sp;
        TextView money_text;
        TextView delete_dj;
        TextView go_pj_dj;
        LinearLayout pop_view;
        ImageView check_img;
        LinearLayout item_layout;
    }

    private void findView(ViewHolder holder, View view) {
        holder.number_text = (TextView) view.findViewById(R.id.number_text);
        holder.shouhuo_type = (TextView) view.findViewById(R.id.shouhuo_type);
        holder.iamge1 = (ImageView) view.findViewById(R.id.iamge1);
        holder.iamge2 = (ImageView) view.findViewById(R.id.iamge2);
        holder.iamge3 = (ImageView) view.findViewById(R.id.iamge3);
        holder.all_number = (TextView) view.findViewById(R.id.all_number);
        holder.look_sp = (LinearLayout) view.findViewById(R.id.look_sp);
        holder.money_text = (TextView) view.findViewById(R.id.money_text);
        holder.delete_dj = (TextView) view.findViewById(R.id.delete_dj);
        holder.go_pj_dj = (TextView) view.findViewById(R.id.go_pj_dj);
        holder.check_img = (ImageView) view.findViewById(R.id.check_img);
        holder.pop_view = (LinearLayout) view.findViewById(R.id.pop_view);
        holder.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
    }

    private void setData(ViewHolder holder, int position) {
        OrderList orderList = orderLists.get(position);
        //待付款
        if ("2".equals(keytype)) {
            holder.check_img.setVisibility(View.VISIBLE);
        } else {
            holder.check_img.setVisibility(View.GONE);
        }
        //订单状态
        if ("1".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("待付款");
            holder.delete_dj.setText("取消订单");
            holder.delete_dj.setVisibility(View.VISIBLE);
            holder.go_pj_dj.setText("去付款");
            holder.go_pj_dj.setVisibility(View.VISIBLE);
        } else if ("2".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("待发货");
            holder.delete_dj.setVisibility(View.INVISIBLE);
            holder.go_pj_dj.setVisibility(View.INVISIBLE);
        } else if ("3".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("待收货");
            holder.delete_dj.setVisibility(View.INVISIBLE);
            holder.go_pj_dj.setText("确认收货");
            holder.go_pj_dj.setVisibility(View.VISIBLE);
        } else if ("4".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("待评价");
            holder.delete_dj.setText("删除订单");
            holder.delete_dj.setVisibility(View.VISIBLE);
            holder.go_pj_dj.setText("去评价");
            holder.go_pj_dj.setVisibility(View.VISIBLE);
        } else if ("5".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("已完成");
            holder.go_pj_dj.setVisibility(View.GONE);
            holder.delete_dj.setText("删除订单");
            holder.delete_dj.setVisibility(View.VISIBLE);
        } else if ("8".equals(orderList.getStatus())) {
            holder.shouhuo_type.setText("已关闭");
            holder.go_pj_dj.setVisibility(View.GONE);
            holder.delete_dj.setText("删除订单");
            holder.delete_dj.setVisibility(View.VISIBLE);
        }
        //订单号
        holder.number_text.setText("订单号:  " + orderList.getOrdernum());
        //实付款
        holder.money_text.setText(orderList.getMoney());
        //照片
        if (orderList.getGoodsItems() == null || orderList.getGoodsItems().size() == 0) {
               holder.look_sp.setVisibility(View.GONE);
            holder.iamge1.setImageResource(R.mipmap.pinpai_def_img);
        } else {
            holder.pop_view.setVisibility(View.VISIBLE);
            if (orderList.getGoodsItems().size() == 1) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.pinpai_def_img)
                        .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                        .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(0).getGoodsimgurl(), holder.iamge1, options);
                holder.iamge1.setVisibility(View.VISIBLE);
                holder.iamge2.setVisibility(View.INVISIBLE);
                holder.iamge3.setVisibility(View.VISIBLE);
                holder.look_sp.setVisibility(View.INVISIBLE);
            } else if (orderList.getGoodsItems().size() == 2) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.pinpai_def_img)
                        .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                        .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(0).getGoodsimgurl(), holder.iamge1, options);
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(1).getGoodsimgurl(), holder.iamge2, options);
                holder.iamge1.setVisibility(View.VISIBLE);
                holder.iamge2.setVisibility(View.VISIBLE);
                holder.iamge3.setVisibility(View.INVISIBLE);
                holder.look_sp.setVisibility(View.INVISIBLE);
            } else if (orderList.getGoodsItems().size() >= 3) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.pinpai_def_img)
                        .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                        .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(0).getGoodsimgurl(), holder.iamge1, options);
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(1).getGoodsimgurl(), holder.iamge2, options);
                ImageLoader.getInstance().displayImage(orderList.getGoodsItems().get(2).getGoodsimgurl(), holder.iamge3, options);
                holder.iamge1.setVisibility(View.VISIBLE);
                holder.iamge2.setVisibility(View.VISIBLE);
                holder.iamge3.setVisibility(View.VISIBLE);
                if (orderList.getGoodsItems().size() > 3) {
                    holder.look_sp.setVisibility(View.VISIBLE);
                    holder.all_number.setText("共" + orderList.getGoodsItems().size() + "件");
                } else {
                    holder.look_sp.setVisibility(View.INVISIBLE);
                }

            }
            //订单详情
            holder.iamge1.setTag(R.id.TAG,orderList);
            holder.iamge1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderList list = (OrderList) v.getTag(R.id.TAG);
                    Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                    intent.putExtra("orderid",list.getId());
                    intent.putExtra("keytype",list.getStatus());
                    activity.startActivity(intent);
                }
            });
            holder.iamge2.setTag(R.id.TAG,orderList);
            holder.iamge2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderList list = (OrderList) v.getTag(R.id.TAG);
                    Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                    intent.putExtra("orderid",list.getId());
                    intent.putExtra("keytype",list.getStatus());
                    activity.startActivity(intent);
                }
            });
            holder.iamge3.setTag(R.id.TAG,orderList);
            holder.iamge3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderList list = (OrderList) v.getTag(R.id.TAG);
                    Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                    intent.putExtra("orderid",list.getId());
                    intent.putExtra("keytype",list.getStatus());
                    activity.startActivity(intent);
                }
            });
            holder.look_sp.setTag(R.id.TAG,orderList);
            holder.look_sp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderList list = (OrderList) v.getTag(R.id.TAG);
                    Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                    intent.putExtra("orderid",list.getId());
                    intent.putExtra("keytype",list.getStatus());
                    activity.startActivity(intent);
                }
            });
        }
        //订单详情
        holder.item_layout.setTag(R.id.TAG,orderList);
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList list = (OrderList) v.getTag(R.id.TAG);
                Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                intent.putExtra("orderid",list.getId());
                intent.putExtra("keytype",list.getStatus());
                activity.startActivity(intent);
            }
        });
        //取消订单，删除订单
        holder.delete_dj.setTag(R.id.TAG,orderList);
        holder.delete_dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList orderList1 = (OrderList) v.getTag(R.id.TAG);
                activity.saveData(orderList1);
            }
        });
        //去付款。确认收货。去评价
        holder.go_pj_dj.setTag(R.id.TAG,orderList);
        holder.go_pj_dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList list = (OrderList) v.getTag(R.id.TAG);
                //去付款
                if (list.getStatus().equals("1"))
                {
                    Intent intent = new Intent(activity, Pay1Activity.class);
                    intent.putExtra("keytype","6");
                    intent.putExtra("keyid",list.getId());
                    intent.putExtra("total_fee",list.getMoney());
                    activity.startActivity(intent);
                }
                //确认收货
                if (list.getStatus().equals("3"))
                    activity.yesC(list.getId(),list);
                //去评价
                if ("4".equals(list.getStatus()))
                {
                    Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                    intent.putExtra("orderid",list.getId());
                    intent.putExtra("keytype",list.getStatus());
                    activity.startActivity(intent);
                }
            }
        });
        //判断是否选中
        if (orderList.isCheck())
            holder.check_img.setImageResource(R.mipmap.check_anzhuang_off);
        else
            holder.check_img.setImageResource(R.mipmap.check_anzhuang_on);
        //选中操作
        holder.check_img.setTag(R.id.TAG,orderList);
        holder.check_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderList list = (OrderList) v.getTag(R.id.TAG);
                if (list.isCheck())
                    list.setCheck(false);
                else
                    list.setCheck(true);
                activity.inputPlice();
                notifyDataSetChanged();
            }
        });
    }
}
