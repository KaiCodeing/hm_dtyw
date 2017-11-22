package com.hemaapp.dtyw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.alipay.PayResult;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.model.AlipayTrade;
import com.hemaapp.dtyw.model.UnionTrade;
import com.hemaapp.dtyw.model.WxPayment;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2016/9/28.
 * 结算
 */
public class Pay1Activity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private TextView all_money_text;
    private CheckBox yue_check;
    private TextView money_text;
    private TextView buy_go_text;
    private LinearLayout zhifubao_layout;
    private LinearLayout yinlian_layout;
    private LinearLayout weixin_layout;
    private String keytype;
    private String keyid;
    private String total_fee;
    private ViewHolder holder;
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pay);
        super.onCreate(savedInstanceState);
        Intent intent1 = new Intent();
        intent1.setAction("hemaapp.dtyw.buy.car.data");
        sendBroadcast(intent1);
        inIt();
        registerMyBroadcast();
    }

    @Override
    protected void onDestroy() {
        unregisterMyBroadcast();
        super.onDestroy();
    }

    private void registerMyBroadcast() {
        // 注册广播接收
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("hemaapp.dtyw.buy.congzhi.infor"); // 只有持有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.congzhi.infor".equals(intent.getAction())) {
                int err = intent.getIntExtra("res",-1);
                //成功
                if (0==err)
                {
                    showTextDialog("支付成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
                }
                else if(-1==err)
                {
                    showTextDialog("支付错误");

                }
                else
                {
                    showTextDialog("取消支付");

                }

            }
        }
    }


    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
    }
    //初始化
    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().getFeeaccount(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            Intent intent1 = new Intent();
          //  intent1.setAction("hemaapp.dtyw.buy.dd.free");
            intent1.setAction("hemaapp.dtyw.buy.keytype.change");
            sendBroadcast(intent1);
            Intent intent = new Intent();
            intent.setAction("hemaapp.dtyw.buy.dd.free");
         //   intent.setAction("hemaapp.dtyw.buy.keytype.change");
            sendBroadcast(intent);
            msg = "支付成功！";
            // refresh();
            showTextDialog(msg);
            next_button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },1000);
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
            showTextDialog(msg);
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "您取消了支付";
            showTextDialog(msg);
        }


    }


    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                showProgressDialog("获取账户余额");
                break;
            case FEEACCOUNT_REMOVE:
                showProgressDialog("余额支付...");
                break;
            case ALIPAY:
                showProgressDialog("请稍后");
                break;
            case UNIONPAY:
                showProgressDialog("请稍后");
                break;
            case WXPAYMENT:
                showProgressDialog("请稍后");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
            case UNIONPAY:
            case WXPAYMENT:
            case ALIPAY:
            case FEEACCOUNT_REMOVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String feeaccount = result.getObjects().get(0);
                money_text.setText("(" + feeaccount + "元)");
                break;
            case FEEACCOUNT_REMOVE:
                Intent intent = new Intent();
           //     intent.setAction("hemaapp.dtyw.buy.dd.free");
                intent.setAction("hemaapp.dtyw.buy.keytype.change");
                sendBroadcast(intent);
                Intent intent1 = new Intent();
                intent1.setAction("hemaapp.dtyw.buy.dd.free");
              //  intent1.setAction("hemaapp.dtyw.buy.keytype.change");
                sendBroadcast(intent1);
                showTextDialog("余额支付成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            case ALIPAY:
                HemaArrayResult<AlipayTrade> aResult = (HemaArrayResult<AlipayTrade>) hemaBaseResult;
                AlipayTrade trade = aResult.getObjects().get(0);
                String orderInfo = trade.getAlipaysign();
                new AlipayThread(orderInfo).start();
                break;
            case UNIONPAY:
                HemaArrayResult<UnionTrade> uResult1 = (HemaArrayResult<UnionTrade>) hemaBaseResult;
                UnionTrade uTrade = uResult1.getObjects().get(0);
                String uInfo = uTrade.getTn();
                UPPayAssistEx.startPayByJAR(mContext, PayActivity.class, null,
                        null, uInfo, DtywConfig.UNIONPAY_TESTMODE);


                break;
            case WXPAYMENT:
                HemaArrayResult<WxPayment> wResult = (HemaArrayResult<WxPayment>) hemaBaseResult;
                WxPayment wTrade = wResult.getObjects().get(0);
                goWeixin(wTrade);
                break;
        }
    }

    private void goWeixin(WxPayment trade) {
        msgApi.registerApp(DtywConfig.APPID_WEIXIN);

        PayReq request = new PayReq();
        request.appId = trade.getAppid();
        request.partnerId = trade.getPartnerid();
        request.prepayId = trade.getPrepayid();
        request.packageValue = trade.getPackageS();
        request.nonceStr = trade.getNoncestr();
        request.timeStamp = trade.getTimestamp();
        request.sign = trade.getSign();
        msgApi.sendReq(request);

    }

    private class AlipayThread extends Thread {
        String orderInfo;
        AlipayHandler alipayHandler;

        public AlipayThread(String orderInfo) {
            this.orderInfo = orderInfo;
            alipayHandler = new AlipayHandler(Pay1Activity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(Pay1Activity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        Pay1Activity activity;

        public AlipayHandler(Pay1Activity activity) {
            this.activity = activity;
        }

        public void handleMessage(android.os.Message msg) {
            if (msg == null) {
                activity.showTextDialog("支付失败");
                return;
            }
            PayResult result = new PayResult((String) msg.obj);
            String staus = result.getResultStatus();
            switch (staus) {
                case "9000":
                    activity.showTextDialog("恭喜\n支付成功");
                    Intent intent = new Intent();
                //    intent.setAction("hemaapp.dtyw.buy.dd.free");
                    intent.setAction("hemaapp.dtyw.buy.keytype.change");
                    activity.sendBroadcast(intent);
                    Intent intent1 = new Intent();
                    intent1.setAction("hemaapp.dtyw.buy.dd.free");
                  //  intent1.setAction("hemaapp.dtyw.buy.keytype.change");
                    activity.sendBroadcast(intent1);
                    postAtTime(new Runnable() {

                        @Override
                        public void run() {
                            activity.finish();
                        }
                    }, 1500);
                    break;
                case "8000":
                    activity.showTextDialog("支付结果确认中");
                    break;
                default:
                    activity.showTextDialog("您取消了支付");
                    break;
            }
        }

        ;
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
            case FEEACCOUNT_REMOVE:
            case ALIPAY:
            case UNIONPAY:
            case WXPAYMENT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GET_FEEACCOUNT:
                showTextDialog("获取余额失败，请稍后重试");
                break;
            case FEEACCOUNT_REMOVE:
                showTextDialog("余额支付失败，请稍后重试");
                break;
            case ALIPAY:
                showTextDialog("支付宝支付失败，请稍后重试");
                break;
            case UNIONPAY:
                showTextDialog("银联支付失败，请稍后重试");
                break;
            case WXPAYMENT:
                showTextDialog("微信支付失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);

        all_money_text = (TextView) findViewById(R.id.all_money_text);
        yue_check = (CheckBox) findViewById(R.id.yue_check);
        money_text = (TextView) findViewById(R.id.money_text);
        buy_go_text = (TextView) findViewById(R.id.buy_go_text);
        zhifubao_layout = (LinearLayout) findViewById(R.id.zhifubao_layout);
        yinlian_layout = (LinearLayout) findViewById(R.id.yinlian_layout);
        weixin_layout = (LinearLayout) findViewById(R.id.weixin_layout);


    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        keyid = mIntent.getStringExtra("keyid");
        total_fee = mIntent.getStringExtra("total_fee");
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("提交订单");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //总价
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        all_money_text.setText(String.valueOf(df.format(Double.valueOf(total_fee))));
        if ("0".equals(total_fee))
            all_money_text.setText("0.00");
        //确认余额支付
        buy_go_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yue_check.isChecked()) {
                    //输入密码
                    showView();
                } else {
                    showTextDialog("请选择余额支付");
                }
            }
        });
        //支付宝
        zhifubao_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().alipayGet(token, "1", "6", keyid, total_fee);
            }
        });
        //微信
        weixin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().wxpayment(token, "3", "6", keyid, total_fee);
            }
        });
        //银联
        yinlian_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().unionpay(token, "2", "6", keyid, total_fee);
            }
        });
    }

    private class ViewHolder {
        EditText input_pwd;
        TextView close_pop;
        TextView yas_pop;
        TextView iphone_number;
    }

    private void showView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_password_zhifu, null);
        holder = new ViewHolder();
        holder.close_pop = (TextView) view.findViewById(R.id.close_pop);
        holder.input_pwd = (EditText) view.findViewById(R.id.input_pwd);
        holder.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        holder.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
        holder.iphone_number.setText("初次支付密码为注册时的登陆密码");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //确定
        holder.yas_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(holder.input_pwd.getText().toString().trim())) {
                    showTextDialog("请填写支付密码");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().feeaccountRemove(token,total_fee, keyid, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(holder.input_pwd.getText().toString().trim())));
                popupWindow.dismiss();
            }
        });
        holder.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
