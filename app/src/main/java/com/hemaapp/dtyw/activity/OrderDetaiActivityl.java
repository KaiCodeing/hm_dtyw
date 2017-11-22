package com.hemaapp.dtyw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.OrderDetaiAdapter;
import com.hemaapp.dtyw.model.GoodsItems;
import com.hemaapp.dtyw.model.OrderGet;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/11/1.
 * 订单详情
 */
public class OrderDetaiActivityl extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    //   private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private LinearLayout layout_show;
    private TextView text_tijiao;//提交订单
    private TextView text_shanchu;//删除订单
    private String orderid;//订单id
    private String keytype;//订单状态
    private OrderGet orderGet;
    private OrderDetaiAdapter adapter;
    private View view;
    private View view2;
    private DeleteView deleteView;
    private ViewHolder holder;
    private ReceiveBroadCast receiveBroadCast; // 广播实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_affirm);
        super.onCreate(savedInstanceState);
        registerMyBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterMyBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    //加表头
    private void addHolder() {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_add_address, null);
            ViewAddress address = new ViewAddress();
            findAddress(address, view);
            setAddress(address);
            listview.addHeaderView(view);
        }
    }

    //加表尾
    private void addFooter() {
        if (view2 == null) {
            view2 = LayoutInflater.from(mContext).inflate(R.layout.item_order_detai_footer, null);
            ViewFooter footer = new ViewFooter();
            view2.setTag(R.id.TAG,footer);
            findFooter(footer, view2);
            setFooter(footer);
            listview.addFooterView(view2);
        }
        else {
            ViewFooter footer = (ViewFooter) view2.getTag(R.id.TAG);
            String type = "";
            if ("1".equals(orderGet.getStatus()))
                type = "待付款";
            else if ("2".equals(orderGet.getStatus()))
                type = "待发货";
            else if ("3".equals(orderGet.getStatus()))
                type = "待收货";
            else if ("4".equals(orderGet.getStatus()))
                type = "待评价";
            else if ("5".equals(orderGet.getStatus()))
                type = "已完成";
            else if ("6".equals(orderGet.getStatus()))
                type = "申请退款";
            else if ("7".equals(orderGet.getStatus()))
                type = "退款完成";
            else if ("8".equals(orderGet.getStatus()))
                type = "已关闭";
            footer.order_content.setText("订单号:" + orderGet.getOrdernum() + "\n"
                    + "订单时间:" + orderGet.getTime() + "\n" + "订单状态:" + type);
        }
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().orderGet(token, orderid);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
                showProgressDialog("获取订单信息...");
                break;
            case ORDER_CONFIRM:
                showProgressDialog("确认收货...");
                break;
            case ORDER_SAVEOPERATE:
                showProgressDialog("操作订单...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case ORDER_CONFIRM:
            case ORDER_SAVEOPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
                HemaArrayResult<OrderGet> result = (HemaArrayResult<OrderGet>) hemaBaseResult;

                orderGet = result.getObjects().get(0);

                if (orderGet == null || isNull(orderGet.getId())) {
                    showTextDialog("订单已删除");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                    break;
                }
                setdingdan();
                addHolder();
                addFooter();
                freshData();
                break;
            case ORDER_CONFIRM:
                showTextDialog("确认收货成功");
                Intent intent1 = new Intent();
                intent1.setAction("hemaapp.dtyw.buy.dd.more");
                sendBroadcast(intent1);
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case ORDER_SAVEOPERATE:
                showTextDialog("操作订单成功");
                Intent intent = new Intent();
                intent.setAction("hemaapp.dtyw.buy.dd.more");
                sendBroadcast(intent);
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new OrderDetaiAdapter(mContext, OrderDetaiActivityl.this, orderGet.getGoodsItem(), keytype);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("暂无数据");
            adapter.setKeytype(keytype);
            adapter.setGoodsItemses(orderGet.getGoodsItem());
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
            case ORDER_CONFIRM:
            case ORDER_SAVEOPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_GET:
                showTextDialog("获取订单详情失败，请稍后重试");
                break;
            case ORDER_CONFIRM:
                showTextDialog("确认收货失败，请稍后重试");
                break;
            case ORDER_SAVEOPERATE:
                showTextDialog("操作订单失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        //     refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
        text_tijiao = (TextView) findViewById(R.id.text_tijiao);
        text_shanchu = (TextView) findViewById(R.id.text_shanchu);
    }

    @Override
    protected void getExras() {
        orderid = mIntent.getStringExtra("orderid");
        keytype = mIntent.getStringExtra("keytype");
    }

    //判断订单状态
    private void setdingdan() {
        //判断状态\
        //待付款
        if ("1".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.VISIBLE);
            text_shanchu.setVisibility(View.VISIBLE);
            text_shanchu.setText("取消订单");
            text_tijiao.setText("去付款");
        }
        //待发货
        else if ("2".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.GONE);
        }
        //待收货
        else if ("3".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.VISIBLE);
            text_tijiao.setVisibility(View.VISIBLE);
            text_tijiao.setText("确定收货");

        }
        //待评价
        else if ("4".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.VISIBLE);
            text_tijiao.setVisibility(View.GONE);
            text_shanchu.setVisibility(View.VISIBLE);
            text_shanchu.setText("删除订单");
        }
        //已完成
        else if ("5".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.VISIBLE);
            text_tijiao.setVisibility(View.GONE);
            text_shanchu.setVisibility(View.VISIBLE);
            text_shanchu.setText("删除订单");
        }
        //已关闭
        else if ("8".equals(orderGet.getStatus())) {
            layout_show.setVisibility(View.VISIBLE);
            text_tijiao.setVisibility(View.GONE);
            text_shanchu.setVisibility(View.VISIBLE);
            text_shanchu.setText("删除订单");
        }

    }

    @Override
    protected void setListener() {
        back_button.setImageResource(R.mipmap.back_img);
        title_text.setText("订单详情");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setVisibility(View.INVISIBLE);

        //删除订单，
        text_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待付款取消订单
                if ("1".equals(keytype)) {
                    showDelete();
                }
                //已关闭删除订单
                else if ("8".equals(keytype)) {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().orderSaveoperate(token, "2", orderid);
                }
                //已完成删除订单
                else if ("5".equals(keytype)) {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().orderSaveoperate(token, "2", orderid);
                }
                //待评价删除订单
                else if ("4".equals(keytype)) {
                    int m = 0;
                    for (int i = 0; i < orderGet.getGoodsItem().size(); i++) {
                        if ("1".equals(orderGet.getGoodsItem().get(i).getAccountreturn()) || "2".equals(orderGet.getGoodsItem().get(i).getAccountreturn())) {
                            m++;
                        }
                    }
                    if (m == 0) {
                        String token = DtywApplication.getInstance().getUser().getToken();
                        getNetWorker().orderSaveoperate(token, "2", orderid);
                    } else
                        showTextDialog("您有商品正在退款处理中");
                }
            }
        });
        //提交订单
        text_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待付款
                if ("1".equals(keytype)) {
                    Intent intent = new Intent(OrderDetaiActivityl.this, Pay1Activity.class);
                    intent.putExtra("keyid", orderid);
                    intent.putExtra("total_fee", orderGet.getMoney());
                    startActivity(intent);
                }
                //待收货
                else if ("3".equals(keytype)) {
                    int m = 0;
                    for (int i = 0; i < orderGet.getGoodsItem().size(); i++) {
                        if ("1".equals(orderGet.getGoodsItem().get(i).getAccountreturn()) || "2".equals(orderGet.getGoodsItem().get(i).getAccountreturn())) {
                            m++;
                        }
                    }
                    log_i("SSSSSSSSSSSSSSSSSSSS" + m);
                    if (m == 0) {
                        String token = DtywApplication.getInstance().getUser().getToken();
                        getNetWorker().orderConfirm(token, orderid);
                    } else
                        showTextDialog("您有商品正在退款处理中");
                }
            }
        });
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);

        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        deleteView.text.setText("确定要取消此订单？");
        deleteView.iphone_number.setText("一旦删除将不能找回");
        deleteView.iphone_number.setVisibility(View.GONE);

        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        deleteView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        deleteView.yas_pop.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      String token = DtywApplication.getInstance().getUser().getToken();
                                                      getNetWorker().orderSaveoperate(token, "1", orderid);
                                                      popupWindow.dismiss();
                                                  }


                                              }

        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new

                BitmapDrawable()

        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 表头
     */
    private class ViewAddress {
        LinearLayout add_address_show_view;//添加地址
        LinearLayout select_address_layout;//地址详情
        ImageView jian_img;//箭头
        TextView name_nick_text;//用户昵称
        TextView user_iphone;//用户手机号
        TextView user_address;//收货地址
    }

    private void findAddress(ViewAddress viewAddress, View view) {
        viewAddress.add_address_show_view = (LinearLayout) view.findViewById(R.id.add_address_show_view);
        viewAddress.select_address_layout = (LinearLayout) view.findViewById(R.id.select_address_layout);
        viewAddress.jian_img = (ImageView) view.findViewById(R.id.jian_img);
        viewAddress.name_nick_text = (TextView) view.findViewById(R.id.name_nick_text);
        viewAddress.user_iphone = (TextView) view.findViewById(R.id.user_iphone);
        viewAddress.user_address = (TextView) view.findViewById(R.id.user_address);
    }

    private void setAddress(ViewAddress viewAddress) {
        viewAddress.add_address_show_view.setVisibility(View.GONE);
        viewAddress.select_address_layout.setVisibility(View.VISIBLE);
        viewAddress.jian_img.setVisibility(View.INVISIBLE);
        viewAddress.name_nick_text.setText(orderGet.getClientname());
        viewAddress.user_address.setText(orderGet.getAddress());
        viewAddress.user_iphone.setText(orderGet.getTel());
    }

    /**
     * 表尾
     */
    private class ViewFooter {
        TextView yunfei_text;//运费
        TextView money_all;//总计
        LinearLayout fapiao_layout;//发票栏
        TextView invoice_type;//发票类型
        TextView invoice_content;//发票内容
        LinearLayout wuliu_layout;//物流栏
        TextView wuliu_content;//物流信息
        TextView order_content;//订单信息
    }

    private void findFooter(ViewFooter footer, View view) {
        footer.yunfei_text = (TextView) view.findViewById(R.id.yunfei_text);
        footer.money_all = (TextView) view.findViewById(R.id.money_all);
        footer.fapiao_layout = (LinearLayout) view.findViewById(R.id.fapiao_layout);
        footer.invoice_type = (TextView) view.findViewById(R.id.invoice_type);
        footer.invoice_content = (TextView) view.findViewById(R.id.invoice_content);
        footer.wuliu_layout = (LinearLayout) view.findViewById(R.id.wuliu_layout);
        footer.wuliu_content = (TextView) view.findViewById(R.id.wuliu_content);
        footer.order_content = (TextView) view.findViewById(R.id.order_content);
    }

    private void setFooter(ViewFooter footer) {
        //运费
        footer.yunfei_text.setText(orderGet.getShipment());
        //总计
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

        footer.money_all.setText(String.valueOf(Double.valueOf(orderGet.getMoney())));
        if ("0".equals(orderGet.getMoney()))
            footer.money_all.setText("0.00");
        //判断是否显示发票信息
        if ("1".equals(orderGet.getStatus()) || "2".equals(orderGet.getStatus()) || "3".equals(orderGet.getStatus()) ||
                "4".equals(orderGet.getStatus()) || "5".equals(orderGet.getStatus()) || "8".equals(orderGet.getStatus())) {
            footer.fapiao_layout.setVisibility(View.VISIBLE);
            //发票类型
            if ("0".equals(orderGet.getInvoice())) {
                footer.invoice_type.setText("不开发票");
                footer.invoice_content.setVisibility(View.GONE);
            } else if ("1".equals(orderGet.getInvoice())) {
                footer.invoice_type.setText("增值税普通发票");
                footer.invoice_content.setText("发票抬头:" + orderGet.getInvoiceheader() + "\n"
                        + "发票明细:" + orderGet.getInvoiceitem() + "\n" + "备注内容:" + orderGet.getInvoicedemo());
            } else {
                footer.invoice_type.setText("增值税专业发票");
                footer.invoice_content.setText("公司名称:" + orderGet.getCompany() + "\n"
                        + "地址:" + orderGet.getCompanyaddress() + "\n" + "电话:" + orderGet.getConmpanytel() + "\n"
                        + "开户行:" + orderGet.getBank() + "\n" + "账号:" + orderGet.getBanknum() + "\n" + "税号:" +
                        orderGet.getTaxnum() + "\n" + "备注内容:" + orderGet.getInvoicedemo());
            }
        } else {
            footer.fapiao_layout.setVisibility(View.GONE);
        }
        //判断是否显示物流信息
        if ("3".equals(orderGet.getStatus()) || "4".equals(orderGet.getStatus()) || "5".equals(orderGet.getStatus()) || "8".equals(orderGet.getStatus())) {
            footer.wuliu_layout.setVisibility(View.VISIBLE);
            if (isNull(orderGet.getExpress()) && isNull(orderGet.getExpressnum()))
                footer.wuliu_layout.setVisibility(View.GONE);
            else
                footer.wuliu_layout.setVisibility(View.VISIBLE);
            footer.wuliu_content.setText("快递公司:" + orderGet.getExpress() + "\n" + "快递单号:" + orderGet.getExpressnum());
        } else
            footer.wuliu_layout.setVisibility(View.GONE);
        //订单信息
        String type = "";
        if ("1".equals(orderGet.getStatus()))
            type = "待付款";
        else if ("2".equals(orderGet.getStatus()))
            type = "待发货";
        else if ("3".equals(orderGet.getStatus()))
            type = "待收货";
        else if ("4".equals(orderGet.getStatus()))
            type = "待评价";
        else if ("5".equals(orderGet.getStatus()))
            type = "已完成";
        else if ("6".equals(orderGet.getStatus()))
            type = "申请退款";
        else if ("7".equals(orderGet.getStatus()))
            type = "退款完成";
        else if ("8".equals(orderGet.getStatus()))
            type = "已关闭";
        footer.order_content.setText("订单号:" + orderGet.getOrdernum() + "\n"
                + "订单时间:" + orderGet.getTime() + "\n" + "订单状态:" + type);
    }

    //退款 评论
    public void goTorP(GoodsItems items) {
        //退款
        if ("2".equals(keytype) || "3".equals(keytype)) {
            if ("0".equals(items.getAccountreturn()) || "4".equals(items.getAccountreturn()))
                showView(items.getCartid(), items);
            else {
            }
        } else {
            Intent intent = new Intent(this, CommodityEvaluateActivity.class);
            intent.putExtra("order", items);
            intent.putExtra("id", orderid);
            startActivity(intent);
            ;
        }
    }

    //单独的退款
    public void outGood(GoodsItems items) {
        showView(items.getCartid(), items);
    }

    private class ViewHolder {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    public void showView(final String cartid, final GoodsItems goodsitems) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        holder = new ViewHolder();
        holder.close_pop = (TextView) view.findViewById(R.id.close_pop);
        holder.yas_pop = (TextView) view.findViewById(R.id.yas_pop);

        holder.text = (TextView) view.findViewById(R.id.text);
        holder.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        holder.text.setText("确定要进行退款申请？");
        holder.iphone_number.setText("申请退款后订单转移至退款订单中");

        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        holder.close_pop.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        holder.yas_pop.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent intent = new Intent(OrderDetaiActivityl.this, RefundActivity.class);
                                                  intent.putExtra("orderid", orderid);
                                                  intent.putExtra("cartid", cartid);
                                                  intent.putExtra("goodsitems", goodsitems);
                                                  intent.putExtra("orderget", orderGet);
                                                  startActivity(intent);
                                                  popupWindow.dismiss();
                                              }
                                          }
        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new

                BitmapDrawable()

        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.keytype.change"); // 只有持有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.keytype.change".equals(intent.getAction())) {
                layout_show.setVisibility(View.GONE);
                keytype = "2";
                inIt();
            }
        }
    }


    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }
}
