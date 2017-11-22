package com.hemaapp.dtyw.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

/**
 * Created by lenovo on 2016/9/30.
 * 钱包充值
 */
public class RechargeActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText money_input;
    private LinearLayout zhifubao_layout;
    private LinearLayout yinlian_layout;
    private LinearLayout weixin_layout;
    //  private TextView yes_layout;
    IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ReceiveBroadCast receiveBroadCast; // 广播实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge);
        registerMyBroadcast();
        super.onCreate(savedInstanceState);
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
        filter.addAction("hemaapp.dtyw.buy.congzhi.infor"); // 只持有有相同的action的接受者才能接收此广播

        registerReceiver(receiveBroadCast, filter);
    }

    public class ReceiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("hemaapp.dtyw.buy.congzhi.infor".equals(intent.getAction())) {
                int err = intent.getIntExtra("res", -1);
                log_i("SSSSS" + err);
                //成功
                if (0 == err) {
                    showTextDialog("支付成功");
                    next_button.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                } else if (-1 == err) {
                    showTextDialog("支付错误");

                } else {
                    showTextDialog("取消支付");

                }

            }
        }
    }

    private void unregisterMyBroadcast() {
        unregisterReceiver(receiveBroadCast);
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
            msg = "支付成功！";
            showTextDialog(msg);
            next_button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
            // refresh();
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

            case FEEACCOUNT_REMOVE:
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
            alipayHandler = new AlipayHandler(RechargeActivity.this);
        }

        @Override
        public void run() {
            // 构造PayTask 对象
            PayTask alipay = new PayTask(RechargeActivity.this);
            // 调用支付接口，获取支付结果
            String result = alipay.pay(orderInfo);

            log_i("result = " + result);
            Message msg = new Message();
            msg.obj = result;
            alipayHandler.sendMessage(msg);
        }
    }

    private static class AlipayHandler extends Handler {
        RechargeActivity activity;

        public AlipayHandler(RechargeActivity activity) {
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

        money_input = (EditText) findViewById(R.id.money_input);
        zhifubao_layout = (LinearLayout) findViewById(R.id.zhifubao_layout);
        yinlian_layout = (LinearLayout) findViewById(R.id.yinlian_layout);
        weixin_layout = (LinearLayout) findViewById(R.id.weixin_layout);
        //      yes_layout = (TextView) findViewById(R.id.yes_layout);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("充值");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //支付宝
        zhifubao_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDa()) {
                    String total_fee = money_input.getText().toString().trim();
//                total_fee = total_fee.substring(1,total_fee.length());
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().alipayGet(token, "1", "1", "0", total_fee);
                }
            }
        });
        //微信
        weixin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDa()) {
                    String total_fee = money_input.getText().toString().trim();
//                    total_fee = total_fee.substring(1, total_fee.length());
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().wxpayment(token, "3", "1", "0", total_fee);
                }
            }
        });
        //银联
        yinlian_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDa()) {
                    String total_fee = money_input.getText().toString().trim();
//                    total_fee = total_fee.substring(1, total_fee.length());
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().unionpay(token, "2", "1", "0", total_fee);
                }
            }
        });
//     |
    }

    private boolean checkDa() {
        String total_fee = money_input.getText().toString().trim();
//        total_fee = total_fee.substring(1,total_fee.length());
        if (isNull(total_fee)) {
            showTextDialog("请填写充值金额");
            return false;
        }
        return true;
    }
}
