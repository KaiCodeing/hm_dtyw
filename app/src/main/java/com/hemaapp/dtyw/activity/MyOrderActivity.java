package com.hemaapp.dtyw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.MyOrderPageAdapter;
import com.hemaapp.dtyw.model.OrderList;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/8.
 * 我的订单
 */
public class MyOrderActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RadioGroup radio_selete;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private View view6;
    private LinearLayout layout_show;
    private ImageView all_delete;
    private TextView all_money;
    private TextView back_delete;
    private ViewPager viewpager;
    private MyOrderPageAdapter pageAdapter;
    private RadioButton all_sp;//全部
    private RadioButton wait_buy_sp;//代付款
    private RadioButton wait_go_sp;//待发货
    private RadioButton wait_out_sp;//待收货
    private RadioButton wait_pj_sp;//待评价
    private RadioButton over_sp;//已完成
    private String type = "0";
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    private String alltype = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_order);
        super.onCreate(savedInstanceState);
        inIt(Integer.valueOf(type));
        registerMyBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterMyBroadcast();
    }

    /**
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    private void inIt(int type) {
        pageAdapter = new MyOrderPageAdapter(this, getParams());
        viewpager.setAdapter(pageAdapter);
        viewpager.setCurrentItem(type);
    }

    private ArrayList<MyOrderPageAdapter.Params> getParams() {
        ArrayList<MyOrderPageAdapter.Params> ps = new ArrayList<MyOrderPageAdapter.Params>();
        ps.add(new MyOrderPageAdapter.Params("1", ""));
        ps.add(new MyOrderPageAdapter.Params("2", ""));
        ps.add(new MyOrderPageAdapter.Params("3", ""));
        ps.add(new MyOrderPageAdapter.Params("4", ""));
        ps.add(new MyOrderPageAdapter.Params("5", ""));
        ps.add(new MyOrderPageAdapter.Params("6", ""));
        return ps;
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_SAVEOPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                if ("1".equals(keytype))
                    showProgressDialog("取消订单中...");
                else
                    showProgressDialog("删除订单中...");
                break;
            case ORDER_CONFIRM:
                showProgressDialog("确认收货中...");
                break;

        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_SAVEOPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                cancelProgressDialog();
                break;
            case ORDER_CONFIRM:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_SAVEOPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                if ("1".equals(keytype))
                    showTextDialog("取消订单成功");
                else
                    showTextDialog("删除订单成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageAdapter.freshAll();
                    }
                }, 1000);

                break;
            case ORDER_CONFIRM:
                showTextDialog("确认收货成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageAdapter.freshAll();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_SAVEOPERATE:
            case ORDER_CONFIRM:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ORDER_SAVEOPERATE:
                showTextDialog("订单操作失败，请稍后重试");
                break;
            case ORDER_CONFIRM:
                showTextDialog("确认收货失败，稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
        all_delete = (ImageView) findViewById(R.id.all_delete);
        all_money = (TextView) findViewById(R.id.all_money);
        back_delete = (TextView) findViewById(R.id.back_delete);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        radio_selete = (RadioGroup) findViewById(R.id.radio_selete);
        all_sp = (RadioButton) findViewById(R.id.all_sp);
        wait_buy_sp = (RadioButton) findViewById(R.id.wait_buy_sp);
        wait_go_sp = (RadioButton) findViewById(R.id.wait_go_sp);
        wait_out_sp = (RadioButton) findViewById(R.id.wait_out_sp);
        wait_pj_sp = (RadioButton) findViewById(R.id.wait_pj_sp);
        over_sp = (RadioButton) findViewById(R.id.over_sp);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
    }

    @Override
    protected void setListener() {
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("我的订单");
        next_button.setText("退款订单");
        //退款订单
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RefundmentListActivity.class);
                startActivity(intent);
            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio_selete.check(R.id.all_sp);
                        break;
                    case 1:
                        radio_selete.check(R.id.wait_buy_sp);
                        break;
                    case 2:
                        radio_selete.check(R.id.wait_go_sp);
                        break;
                    case 3:
                        radio_selete.check(R.id.wait_out_sp);
                        break;
                    case 4:
                        radio_selete.check(R.id.wait_pj_sp);
                        break;
                    case 5:
                        radio_selete.check(R.id.over_sp);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(6);
//        int t = viewpager.getCurrentItem();

        //选择操作
        radio_selete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.all_sp:
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        view5.setVisibility(View.INVISIBLE);
                        view6.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(0);
                        layout_show.setVisibility(View.GONE);
                        break;
                    case R.id.wait_buy_sp:
                        view2.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        view5.setVisibility(View.INVISIBLE);
                        view6.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(1);
                        layout_show.setVisibility(View.VISIBLE);
                        break;
                    case R.id.wait_go_sp:
                        view3.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        view5.setVisibility(View.INVISIBLE);
                        view6.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(2);
                        layout_show.setVisibility(View.GONE);
                        break;
                    case R.id.wait_out_sp:
                        view4.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view5.setVisibility(View.INVISIBLE);
                        view6.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(3);
                        layout_show.setVisibility(View.GONE);
                        break;
                    case R.id.wait_pj_sp:
                        view5.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view6.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(4);
                        layout_show.setVisibility(View.GONE);
                        break;
                    case R.id.over_sp:
                        view6.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        view5.setVisibility(View.INVISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        viewpager.setCurrentItem(5);
                        layout_show.setVisibility(View.GONE);
                        break;
                }
            }
        });
        //全选
        all_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double price = 0;
                if (alltype.equals("0")) {
                    all_delete.setImageResource(R.mipmap.check_anzhuang_off);
                    ArrayList<OrderList> orderLists = pageAdapter.myadapter.getOrderLists();
                    if (orderLists == null || orderLists.size() == 0) {
                    } else {
                        for (int i = 0; i < orderLists.size(); i++) {
                            orderLists.get(i).setCheck(true);
                            price = Double.valueOf(orderLists.get(i).getMoney()) + price;
                        }
                    }
                    alltype = "1";
                } else {
                    alltype = "0";
                    all_delete.setImageResource(R.mipmap.check_anzhuang_on);
                    ArrayList<OrderList> orderLists = pageAdapter.changeData();

                    if (orderLists == null || orderLists.size() == 0) {
                    } else {
                        for (int i = 0; i < orderLists.size(); i++) {
                            orderLists.get(i).setCheck(false);
                        }
                    }
                }
                all_money.setText(String.valueOf(price));
                pageAdapter.myadapter.notifyDataSetChanged();
            }
        });
        //去付款
        back_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<OrderList> orderLists = pageAdapter.myadapter.getOrderLists();
                double price = 0;
                int m = 0;
                StringBuffer orderId = new StringBuffer();
                if (orderLists == null || orderLists.size() == 0) {
                } else {
                    for (int i = 0; i < orderLists.size(); i++) {
                        if (orderLists.get(i).isCheck()) {
                            price = Double.valueOf(orderLists.get(i).getMoney()) + price;
                            m++;
                            orderId.append(orderLists.get(i).getId() + ",");
                        }
                    }
                    if (m == 0) {
                        showTextDialog("请选择要结算的商品");
                        return;
                    }
                    Intent intent = new Intent(MyOrderActivity.this, Pay1Activity.class);
                    intent.putExtra("keytype", "6");
                    intent.putExtra("keyid", String.valueOf(orderId.substring(0, orderId.length() - 1)));
                    intent.putExtra("total_fee", String.valueOf(price));
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 删除订单，或取消订单
     *
     * @param list
     */
    public void saveData(OrderList list) {
        String token = DtywApplication.getInstance().getUser().getToken();
        if (list.getStatus().equals("1")) {
            //取消订单
            getNetWorker().orderSaveoperate(token, "1", list.getId());
        } else {
            //删除订单
            int m = 0;
            for (int i = 0; i < list.getGoodsItems().size(); i++) {
                if ("1".equals(list.getGoodsItems().get(i).getAccountreturn()) || "2".equals(list.getGoodsItems().get(i).getAccountreturn())) {
                    m++;
                }
            }
            log_i("+++++++++++SSSS" + m);
            if (m == 0) {
                //   String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().orderSaveoperate(token, "2", list.getId());
            } else {
                showTextDialog("您有商品正在退款处理中");
            }

        }
    }

    /**
     * 确认收货
     *
     * @param id
     */
    public void yesC(String id, OrderList list) {
        int m = 0;
        for (int i = 0; i < list.getGoodsItems().size(); i++) {
            if ("1".equals(list.getGoodsItems().get(i).getAccountreturn()) || "2".equals(list.getGoodsItems().get(i).getAccountreturn())) {
                m++;
            }
        }
        log_i("+++++++++++SSSS" + m);
        if (m == 0) {
            String token = DtywApplication.getInstance().getUser().getToken();
            getNetWorker().orderConfirm(token, id);
        } else {
            showTextDialog("您有商品正在退款处理中");
        }
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.dd.free".equals(intent.getAction())) {
                showTextDialog("付款成功,订单转移至待发货");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageAdapter.freshAll();
                    }
                }, 1000);
                inputPlice();
            } else if ("hemaapp.dtyw.buy.dd.more".equals(intent.getAction())) {
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageAdapter.freshAll();
                    }
                }, 1000);
            }
        }
    }

    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.dd.free"); // 只有持有相同的action的接受者才能接收此广播
        filter.addAction("hemaapp.dtyw.buy.dd.more");
        registerReceiver(receiveBroadCast, filter);
    }

    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }

    /**
     * 改变总价
     */
    public void inputPlice() {
        ArrayList<OrderList> orderLists = pageAdapter.myadapter.getOrderLists();
        double price = 0;
        int m = 0;
        if (orderLists == null || orderLists.size() == 0) {
        } else {
            for (int i = 0; i < orderLists.size(); i++) {
                if (orderLists.get(i).isCheck()) {
                    price = Double.valueOf(orderLists.get(i).getMoney()) + price;
                    m++;
                }
            }
            //判断是否到全部了
            if (m == orderLists.size()) {
                all_delete.setImageResource(R.mipmap.check_anzhuang_off);
                alltype = "1";
            } else {
                all_delete.setImageResource(R.mipmap.check_anzhuang_on);
                alltype = "0";
            }
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

            all_money.setText(String.valueOf(df.format(price)));
            if (price == 0)
                all_money.setText("0.00");
        }

    }
}
