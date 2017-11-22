package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2016/9/30.
 * 提现
 */
public class WithdrawalsActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout bank_ly;
    private TextView bank_name;
    private TextView user_name;
    private TextView back_card_number;
    private TextView yu_text;
    private EditText money_text;
    private EditText tixian_password;
    private LinearLayout add_activity_button;
    private String keytype;
    private String openBank;
    private TextView wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_withdrawals);
        super.onCreate(savedInstanceState);
        inIt();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        //   getNetWorker().getFeeaccount(token);
        String id = DtywApplication.getInstance().getUser().getId();
        getNetWorker().clientGet(token, id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;

        }
        switch (requestCode) {
            case 1:
                String content = data.getStringExtra("context");
                back_card_number.setVisibility(View.VISIBLE);
                back_card_number.setText(content);
                break;
            case 2:
                String bankName = data.getStringExtra("bankName");
                String bankUser = data.getStringExtra("bankUser");
                String bankNum = data.getStringExtra("bankNum");
                openBank = data.getStringExtra("bankOpen");
                user_name.setVisibility(View.VISIBLE);
                back_card_number.setVisibility(View.VISIBLE);
                user_name.setText(bankUser);
                back_card_number.setText(bankNum);
                bank_name.setText(bankName);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                break;
            case CASH_ADD:
                showProgressDialog("提交提现申请");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                break;
            case CASH_ADD:
                cancelProgressDialog();
                break;

        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                User user = result.getObjects().get(0);
                DtywApplication.getInstance().setUser(user);

                //余额
                yu_text.setText("¥"+user.getFeeaccount());
                if ("0".equals(keytype)) {
                    //支付宝
                    back_card_number.setVisibility(View.VISIBLE);
                    back_card_number.setText(user.getLevel_exp());
                } else { //银行
                    if (!isNull(user.getLevel_name())) {
                        bank_name.setText(user.getLevel_name());
                    }
                    if (!isNull(user.getLevel_imgurl())) {  //真实姓名
                        user_name.setText(user.getLevel_imgurl());
                        user_name.setVisibility(View.VISIBLE);
                    }
                    if (!isNull(user.getSigninflag())) { //银行卡号
                        back_card_number.setText(user.getSigninflag());
                        back_card_number.setVisibility(View.VISIBLE);
                    }
                    openBank = user.getFollowflag();
                }

                break;
            case CASH_ADD:
                showTextDialog("提现申请发送成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
            case CASH_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;

        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_GET:
                showTextDialog("获取用户余额失败，请稍后重试");
                break;
            case CASH_ADD:
                showTextDialog("提现申请发送失败，请稍后重试");
                break;

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);

        bank_ly = (LinearLayout) findViewById(R.id.bank_ly);
        bank_name = (TextView) findViewById(R.id.bank_name);
        user_name = (TextView) findViewById(R.id.user_name);
        back_card_number = (TextView) findViewById(R.id.back_card_number);
        yu_text = (TextView) findViewById(R.id.yu_text);
        money_text = (EditText) findViewById(R.id.money_text);
        tixian_password = (EditText) findViewById(R.id.tixian_password);
        add_activity_button = (LinearLayout) findViewById(R.id.add_activity_button);
        wallet = (TextView) findViewById(R.id.wallet);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        if ("0".equals(keytype)) {
            title_text.setText("支付宝提现");
            bank_name.setText("支付宝账号");
        } else {
            title_text.setText("银行卡提现");
            bank_name.setText("提现银行");
        }
        user_name.setVisibility(View.GONE);
        back_card_number.setVisibility(View.GONE);
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //跳转银行或支付宝
        bank_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付宝
                if ("0".equals(keytype)) {
                    String content = back_card_number.getText().toString().trim();
                    Intent intent = new Intent(WithdrawalsActivity.this, InputWordActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("content", content);
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(WithdrawalsActivity.this, EditorBankActivity.class);

                    if (bank_name.getText().toString().trim().equals("提现银行"))
                        intent.putExtra("bankname", "");
                    else
                    intent.putExtra("bankname", bank_name.getText().toString().trim());

                    intent.putExtra("bank", openBank);
                    intent.putExtra("bankcard", back_card_number.getText().toString().trim());
                    intent.putExtra("bankuser", user_name.getText().toString().trim());
                    startActivityForResult(intent, 2);
                }
            }
        });
        //提交申请
        add_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                //支付宝
                if ("0".equals(keytype)) {
                    //判断
                    if (isNull(back_card_number.getText().toString().trim())) {
                        showTextDialog("请输入支付宝账号");
                        return;
                    }
                    if (isNull(money_text.getText().toString().trim())) {
                        showTextDialog("请输入提现金额");
                        return;
                    }
                    int m = Integer.valueOf(money_text.getText().toString().trim());
                    if (Integer.valueOf(money_text.getText().toString().trim())==0)
                    {
                        showTextDialog("提现金额需为100的整数倍");
                        return;
                    }
                    int a = Integer.valueOf(money_text.getText().toString().trim())%100;

                    if (a!=0)
                    {
                        showTextDialog("提现金额需为100的整数倍");
                        return;
                    }
                    if (isNull(tixian_password.getText().toString().trim())) {
                        showTextDialog("请输入提现密码");
                        return;
                    }

                    getNetWorker().cashAdd(token, "1", money_text.getText().toString().trim(),
                            Md5Util.getMd5(XtomConfig.DATAKEY
                                    + Md5Util.getMd5(tixian_password.getText().toString().trim()))
                    );
                } else {
                    //判断
                    if (bank_name.getText().toString().trim().equals("提现银行")) {
                        showTextDialog("请选择提现银行");
                        return;
                    }
                    if (isNull(user_name.getText().toString().trim())) {
                        showTextDialog("请填写用户姓名");
                        return;
                    }
                    //判断
                    if (isNull(back_card_number.getText().toString().trim())) {
                        showTextDialog("请输入银行卡号");
                        return;
                    }
                    if (isNull(money_text.getText().toString().trim())) {
                        showTextDialog("请输入提现金额");
                        return;
                    }
                    if (Integer.valueOf(money_text.getText().toString().trim())==0)
                    {
                        showTextDialog("提现金额需为100的整数倍");
                        return;
                    }
                    int a = Integer.valueOf(money_text.getText().toString().trim())%100;
                    if (a!=0)
                    {
                        showTextDialog("提现金额需为100的整数倍");
                        return;
                    }
                    if (isNull(tixian_password.getText().toString().trim())) {
                        showTextDialog("请输入提现密码");
                        return;
                    }
                    getNetWorker().cashAdd(token, "2", money_text.getText().toString().trim(),
                            Md5Util.getMd5(XtomConfig.DATAKEY
                                    + Md5Util.getMd5(tixian_password.getText().toString().trim())));
                }
            }
        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WithdrawalsActivity.this, WebviewActivity.class);
                intent.putExtra("type", "5");
                startActivity(intent);
            }
        });
    }
}
