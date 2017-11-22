package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.OrderDetaiActivityl;
import com.hemaapp.dtyw.model.GoodsItems;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/11/1.
 * 订单adapter
 */
public class OrderDetaiAdapter extends HemaAdapter {
    private OrderDetaiActivityl activityl;
    private ArrayList<GoodsItems> goodsItemses;
    private Context mContext;
    private String keytype;

    public OrderDetaiAdapter(Context mContext, OrderDetaiActivityl activityl, ArrayList<GoodsItems> goodsItemses, String keytype) {
        super(mContext);
        this.mContext = mContext;
        this.goodsItemses = goodsItemses;
        this.mContext = mContext;
        this.keytype = keytype;
        this.activityl = activityl;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public void setGoodsItemses(ArrayList<GoodsItems> goodsItemses) {
        this.goodsItemses = goodsItemses;
    }

    @Override
    public boolean isEmpty() {
        return goodsItemses == null || goodsItemses.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : goodsItemses.size();
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
        LinearLayout din_layout;
        LinearLayout zong_view;
        TextView tui_ping_text;
        TextView add_view_guige;
        TextView tui_two_text;
    }

    private void findView(ViewHolder holder, View view) {
        holder.number_text = (TextView) view.findViewById(R.id.contain_text);
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
        holder.zong_view = (LinearLayout) view.findViewById(R.id.zong_view);
        holder.din_layout = (LinearLayout) view.findViewById(R.id.din_layout);
        holder.tui_ping_text = (TextView) view.findViewById(R.id.tui_ping_text);
        holder.add_view_guige = (TextView) view.findViewById(R.id.add_view_guige);
        holder.tui_two_text = (TextView) view.findViewById(R.id.tui_two_text);
    }

    private void setData(ViewHolder holder, int position) {
        final GoodsItems goodsItems = goodsItemses.get(position);
        holder.zong_view.setVisibility(View.GONE);
        holder.din_layout.setVisibility(View.GONE);
        holder.tui_ping_text.setVisibility(View.GONE);
        holder.tui_two_text.setVisibility(View.GONE);
        //商品图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(goodsItems.getGoodsimgurl(), holder.commod_img, options);
        //商品名称
        holder.commod_name.setText(goodsItems.getGoodsname());
        //商品价格
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        holder.money_text_com.setText(String.valueOf(df.format(Double.valueOf(goodsItems.getPrice()))));
        if (Double.valueOf(goodsItems.getPrice())==0)
        {
            holder.money_text_com.setText("0.00");
        }
        //商品数量
        holder.number_text_com.setText(goodsItems.getGoodsnum());
        //添加商品规格
        holder.add_view_guige.setVisibility(View.VISIBLE);
        String conent = "";
        if (!isNull(goodsItems.getGoodspropertyname()))
            conent = goodsItems.getGoodspropertyname();
        holder.add_view_guige.setText(conent);
        // //安装服务判断
        if ("0".equals(goodsItems.getInstall()))
            holder.anzhuang_layout.setVisibility(View.GONE);
        else {
            holder.anzhuang_layout.setVisibility(View.VISIBLE);
            //服务店名称
            holder.fuwu_name.setText(goodsItems.getInstallname());
            //服务店电话
            holder.phone_text.setText(goodsItems.getInstallphone());
            //服务店地址
            holder.address_text.setText(goodsItems.getInstalladdress());
            //服务价值
            holder.money_text.setText(goodsItems.getInstallprice());
        }
        //判断是否显示退款 评价
        if ("2".equals(keytype)) {
            holder.tui_ping_text.setVisibility(View.VISIBLE);
            holder.tui_ping_text.setText("退款");
//            if (goodsItems.getReply().equals("0")) {
//                holder.tui_ping_text.setVisibility(View.VISIBLE);
//
//            } else {
//                holder.tui_ping_text.setVisibility(View.GONE);
//            }
            //显示状态
            //申请中
            if ("1".equals(goodsItems.getAccountreturn())) {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_ping_text.setText("待退款");
                holder.tui_ping_text.setEnabled(false);
            }
            //同意退款
            else if ("2".equals(goodsItems.getAccountreturn())) {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_ping_text.setText("待退款");
                holder.tui_ping_text.setEnabled(false);
            }
            //已退款
            else if (("3".equals(goodsItems.getAccountreturn()))) {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_ping_text.setText("已退款");
                holder.tui_ping_text.setEnabled(false);
            } else {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_ping_text.setEnabled(true);
            }
        }
        //待收货
        else if ("3".equals(keytype)) {
            holder.tui_two_text.setVisibility(View.VISIBLE);
            holder.tui_ping_text.setVisibility(View.GONE);
            //显示状态
            //申请中
            if ("1".equals(goodsItems.getAccountreturn())) {
                holder.tui_two_text.setVisibility(View.VISIBLE);
                holder.tui_two_text.setText("待退款");
                holder.tui_two_text.setEnabled(false);
            }
            //同意退款
            else if ("2".equals(goodsItems.getAccountreturn())) {
                holder.tui_two_text.setVisibility(View.VISIBLE);
                holder.tui_two_text.setText("待退款");
                holder.tui_two_text.setEnabled(false);
            }
            //已退款
            else if (("3".equals(goodsItems.getAccountreturn()))) {
                holder.tui_two_text.setVisibility(View.VISIBLE);
                holder.tui_two_text.setText("已退款");
                holder.tui_two_text.setEnabled(false);
            } else {
                holder.tui_two_text.setVisibility(View.VISIBLE);
                holder.tui_two_text.setEnabled(true);
            }

        }
        //待评价
        else if ("4".equals(keytype)) {
            holder.tui_ping_text.setVisibility(View.VISIBLE);
            holder.tui_two_text.setVisibility(View.VISIBLE);
            holder.tui_ping_text.setText("评价");
            if (goodsItems.getReply().equals("0")) {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_two_text.setVisibility(View.GONE);
                //显示状态
                //申请中
//                if ("1".equals(goodsItems.getAccountreturn())) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("待退款");
//                    holder.tui_two_text.setEnabled(false);
//                   holder.tui_ping_text.setEnabled(false);
//                }
//                //同意退款
//                else if ("2".equals(goodsItems.getAccountreturn())) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("待退款");
//                   holder.tui_ping_text.setEnabled(true);
//                    holder.tui_two_text.setEnabled(false);
//                }
//                //已退款
//                else if (("3".equals(goodsItems.getAccountreturn()))) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("已退款");
//                    holder.tui_two_text.setEnabled(false);
//                    holder.tui_ping_text.setEnabled(false);
//                } else {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setEnabled(true);
//                   holder.tui_ping_text.setEnabled(true);
//                }

            } else {
                holder.tui_ping_text.setVisibility(View.VISIBLE);
                holder.tui_ping_text.setText("已评价");
                holder.tui_ping_text.setEnabled(false);
                holder.tui_ping_text.setTextColor(activityl.getResources().getColor(R.color.white));
                holder.tui_ping_text.setBackgroundResource(R.color.white_p);
                holder.tui_two_text.setVisibility(View.GONE);
                //申请中
//                if ("1".equals(goodsItems.getAccountreturn())) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("待退款");
//                    holder.tui_two_text.setEnabled(false);
//                 //   holder.tui_ping_text.setEnabled(true);
//                }
//                //同意退款
//                else if ("2".equals(goodsItems.getAccountreturn())) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("待退款");
//                //    holder.tui_ping_text.setEnabled(true);
//                    holder.tui_two_text.setEnabled(false);
//                }
//                //已退款
//                else if (("3".equals(goodsItems.getAccountreturn()))) {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setText("已退款");
//                    holder.tui_two_text.setEnabled(false);
//                    holder.tui_ping_text.setEnabled(false);
//                } else {
//                    holder.tui_two_text.setVisibility(View.VISIBLE);
//                    holder.tui_two_text.setEnabled(false);
//               //     holder.tui_ping_text.setEnabled(true);
//                }


            }

        } else {
            holder.tui_ping_text.setVisibility(View.GONE);
        }
        //退款，评论点击事件
        holder.tui_ping_text.setTag(R.id.TAG, goodsItems);
        holder.tui_ping_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoodsItems goodsItems1 = (GoodsItems) v.getTag(R.id.TAG);
                activityl.goTorP(goodsItems1);
            }

        });
        //退款
        holder.tui_two_text.setTag(R.id.TAG, goodsItems);
        holder.tui_two_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsItems goodsItems1 = (GoodsItems) v.getTag(R.id.TAG);
                activityl.outGood(goodsItems1);
            }
        });
    }
}
