package com.hemaapp.dtyw.fragment;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywFragment;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.activity.AffirmActivity;
import com.hemaapp.dtyw.activity.LoginActivity;
import com.hemaapp.dtyw.adapter.BuyCarAdapter;
import com.hemaapp.dtyw.model.Car;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/9/22.
 */
public class BuyCarFragment extends DtywFragment {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ImageView all_buy_img;
    private TextView money_text;
    private TextView yunfei_text;
    private TextView buy_text;
    private LinearLayout view;
    private FrameLayout colose_view;
    private TextView login_text;
    private ArrayList<Car> cars = new ArrayList<Car>();
    private BuyCarAdapter adapter;
    private DeleteView deleteView;
    private String type = "0";
    private ReceiveBroadCast receiveBroadCast; // 广播实例

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_car_buy);
        super.onCreate(savedInstanceState);
        inIt();
        registerMyBroadcast();
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        if (!hidden) {
//            inIt();
//            Intent intent = new Intent();
//            intent.setAction("com.hemaapp.car.number");
//            getActivity().sendBroadcast(intent);
//          //  all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
//        }
//        super.onHiddenChanged(hidden);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterMyBroadcast();
    }

    //初始化
    private void inIt() {
        if (isNull(XtomSharedPreferencesUtil.get(getContext(), "username"))) {
            view.setVisibility(View.GONE);
            colose_view.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            colose_view.setVisibility(View.GONE);
            String token = DtywApplication.getInstance().getUser().getToken();
            getNetWorker().cartList(token);
            all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
            type = "0";
            yunfei_text.setText("（运费0元）");
            money_text.setText("0.00");
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                showProgressDialog("获取购物车信息");
                break;
            case CART_SAVEOPERATE:
             //   showProgressDialog("保存购物车信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case CART_SAVEOPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                refreshLoadmoreLayout.refreshSuccess();
                HemaArrayResult<Car> result = (HemaArrayResult<Car>) hemaBaseResult;
                cars.clear();
                cars = result.getObjects();

                Intent intent1 = new Intent();
                intent1.setAction("com.hemaapp.car.number");
                getActivity().sendBroadcast(intent1);
                freshData();
                break;
            case CART_SAVEOPERATE:
                //String token = DtywApplication.getInstance().getUser().getToken();
                //getNetWorker().cartList(token);
                String carId = hemaNetTask.getParams().get("cartid");
                String keytype = hemaNetTask.getParams().get("keytype");
                String num = hemaNetTask.getParams().get("num");
                if ("1".equals(keytype))
                {  cars.clear();
                    Intent intent = new Intent();
                    intent.setAction("com.hemaapp.car.number");
                    getActivity().sendBroadcast(intent);
                    all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
                    yunfei_text.setText("（运费0元）");
                    money_text.setText("0.00");
                    type = "0";
                }
                else if ("2".equals(keytype)) {
                    for (int i = 0; i < cars.size(); i++) {
                        if (cars.get(i).getId().equals(carId))
                            cars.remove(i);
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.hemaapp.car.number");
                    getActivity().sendBroadcast(intent);
                } else {
                    for (int i = 0; i < cars.size(); i++) {
                        if (cars.get(i).getId().equals(carId))
                            cars.get(i).setBuycount(num);
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.hemaapp.car.number");
                    getActivity().sendBroadcast(intent);

                }
                if ("1".equals(type)) {
                    showP();
                }
                freshData();

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
            case CART_SAVEOPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CART_LIST:
                showTextDialog("获取购物车信息失败，请稍后重试");
                break;
            case CART_SAVEOPERATE:
                showTextDialog("保存购物车信息失败，请稍后重试");
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new BuyCarAdapter(getContext(), BuyCarFragment.this, cars);

            adapter.setEmptyString("您还未添加任何商品");

            listview.setAdapter(adapter);
        } else {
            adapter.setCars(cars);
            adapter.setEmptyString("您还未添加任何商品");
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        all_buy_img = (ImageView) findViewById(R.id.all_buy_img);
        money_text = (TextView) findViewById(R.id.money_text);
        yunfei_text = (TextView) findViewById(R.id.yunfei_text);
        buy_text = (TextView) findViewById(R.id.buy_text);
        view = (LinearLayout) findViewById(R.id.view);
        colose_view = (FrameLayout) findViewById(R.id.colose_view);
        login_text = (TextView) findViewById(R.id.login_text);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);

    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("购物车");
        next_button.setText("清空");
        //刷新
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                inIt();
                all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
                yunfei_text.setText("（运费0元）");
                money_text.setText("0.00");
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {

            }
        });
        refreshLoadmoreLayout.setLoadmoreable(false);
        //登录
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });
        //全选
        all_buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cars == null || cars.size() == 0)
                    return;
                ;
                if (type.equals("0")) {
                    for (int i = 0; i < cars.size(); i++) {
                        cars.get(i).setCheck(true);
                    }
                    all_buy_img.setImageResource(R.mipmap.check_anzhuang_off);
                    type = "1";
                } else {
                    for (int i = 0; i < cars.size(); i++) {
                        cars.get(i).setCheck(false);
                    }
                    all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
                    type = "0";
                }
                freshData();
                showP();
            }
        });
        //清空g
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelete(null);
            }
        });
        //购买
        buy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Car> cars1 = new ArrayList<Car>();
                if (cars==null || cars.size()==0)
                {
                    return;
                }
                for (int i = 0; i < cars.size(); i++) {
                    if (cars.get(i).isCheck()) {
                        cars1.add(cars.get(i));
                    }
                }
                if (cars1 == null || cars1.size() == 0) {
                    showTextDialog("请选择要购买的商品");
                    return;
                }
                Intent intent = new Intent(getActivity(), AffirmActivity.class);
                intent.putExtra("cars", cars1);
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * 购物车操作
     * 1:清空购物车
     * 2:删除购物车的某条记录
     * 3:更改购物车中某条记录的商品数量
     */
    public void changeNum(String carId, String num, String type) {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().cartSaveoperate(token, type, carId, num);
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    public void showDelete(final String id) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        deleteView.text = (TextView) view.findViewById(R.id.text);
        deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        if (!isNull(id)) {
            deleteView.text.setText("确定要删除此商品？");
            deleteView.iphone_number.setText("一旦删除将不能找回");
            deleteView.iphone_number.setVisibility(View.GONE);
        } else {
            deleteView.text.setText("确定要清空所有商品？");
            deleteView.iphone_number.setText("一旦清空将不能找回");
            deleteView.iphone_number.setVisibility(View.VISIBLE);
        }
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
                                                      if (!isNull(id))
                                                          changeNum(id, "", "2");
                                                      else {
                                                          changeNum("", "", "1");
                                                      }
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

    //更改选中状态
    public void selectCart(int carid) {
        for (int i = 0; i < cars.size(); i++) {
            if (Integer.valueOf(cars.get(i).getId()) == carid) {
                if (cars.get(i).isCheck())
                    cars.get(i).setCheck(false);
                else
                    cars.get(i).setCheck(true);
            }
        }
        freshData();
        showP();
    }

    //购物车商品总价值
    private void showP() {
        double pro = 0;
        double yf = 0;
        int m =0;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).isCheck()) {
                pro = (Double.valueOf(cars.get(i).getPrice()) * Double.valueOf(cars.get(i).getBuycount())) + pro;
                if (!isNull(cars.get(i).getInstallprice()) && !cars.get(i).getInstallid().equals("0"))
                    pro = Double.valueOf(cars.get(i).getInstallprice())+pro;
                if (Double.valueOf(cars.get(i).getShipment()) > yf)
                    yf = Double.valueOf(cars.get(i).getShipment());
                m++;
            }
        }
        if (m==cars.size() && m!=0)
        {
            all_buy_img.setImageResource(R.mipmap.check_anzhuang_off);
            type = "1";
        }
        else {
            all_buy_img.setImageResource(R.mipmap.check_anzhuang_on);
            type = "0";
        }
        yunfei_text.setText("（运费" + yf + "元）");
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        money_text.setText(String.valueOf(df.format(pro+yf)));
        if (0==pro && 0==yf)
            money_text.setText("0.00");
    }


    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.car.data"); // 只有持有相同的action的接受者才能接收此广播

        getActivity().registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.car.data".equals(intent.getAction())) {
                inIt();

            }
        }
    }


    private void unregisterMyBroadcast() {
        getActivity().unregisterReceiver(receiveBroadCast);
    }
}
