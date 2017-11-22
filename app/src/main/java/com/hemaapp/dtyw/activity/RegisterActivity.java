package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaUtil;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;

/**
 * Created by lenovo on 2016/9/18.
 * 注册第一步（找回密码）
 */
public class RegisterActivity extends DtywActivity {
    private ImageButton back_button;//差号
    private TextView title_text;
    private Button next_button;
    private EditText input_phone;//输入电话号码
    private TextView phone;//显示号码
    private EditText yanzheng_text;//验证码输入
    private Button send_button;//发送验证码
    private TextView second;//秒
    private EditText input_password;//输入密码
    private CheckBox check_zu;//复选框
    private TextView xieyi_text;//注册协议
    private TextView yes_button;//确定
    private String type;//空的时候是注册，非空的时候是找回密码 1 找回登录密码 2 找回支付密码
    private String username;
    private TimeThread timeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showProgressDialog("正在验证手机号");
                break;
            case CODE_GET:
                showProgressDialog("正在发送验证码");
                break;
            case CODE_VERIFY:
                showProgressDialog("正在验证验证码");
                break;
            case PASSWORD_RESET:
                showProgressDialog("正在修改密码");
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
            case CODE_GET:
            case CODE_VERIFY:
            case PASSWORD_RESET:
                cancelProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                if (isNull(type)) {
                    showTextDialog("手机号已被注册");
                } else {
                    String username = hemaNetTask.getParams().get("username");
                    getNetWorker().codeGet(username);
                }
                break;
            case CODE_GET:
                username = hemaNetTask.getParams().get("username");
                phone.setText("验证码已发送到 "
                        + HemaUtil.hide(hemaNetTask.getParams().get("username"), "1"));
                phone.setVisibility(View.VISIBLE);
                timeThread = new TimeThread(new TimeHandler(this));
                timeThread.start();
                break;
            case CODE_VERIFY:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String temp_token = result.getObjects().get(0);
                log_i("接口返回的token++"+temp_token);
                if (!isNull(type))
                {
                    getNetWorker().passwordReset(temp_token,type, Md5Util.getMd5(XtomConfig.DATAKEY
                            + Md5Util.getMd5(input_password.getText().toString())));
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this,
                        Register1Activity.class);
                String username = input_phone.getText().toString();
                if (send_button.getVisibility()==View.GONE)
                {

                }
                else
                {
                    showTextDialog("请获取验证码");
                    return;
                }
                if (isNull(this.username)) {
                    showTextDialog("请填写手机号");
                    return;
                }
                if (this.username.equals(username)) {
                    intent.putExtra("type", type);
                    intent.putExtra("username", username);
                    intent.putExtra("password",input_password.getText().toString());
                    intent.putExtra("tempToken", temp_token);
                    startActivity(intent);
                } else {
                    showTextDialog("两次输入手机号码不一致，\n请确认");
                    return;
                }

                break;
            case PASSWORD_RESET:
                showTextDialog("密码修改成功");
                next_button.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                if (isNull(type)) {
                    String username = hemaNetTask.getParams().get("username");
                    getNetWorker().codeGet(username);
                } else {
                    showTextDialog("手机号未注册！");
                }
                break;
            case CODE_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            case CODE_VERIFY:
                if (hemaBaseResult.getError_code() == 103) {
                    showTextDialog("输入的验证码不正确！");
                } else {
                    showTextDialog(hemaBaseResult.getMsg());
                }

                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_VERIFY:
                showTextDialog("验证手机号失败");
                break;
            case CODE_GET:
                showTextDialog("发送验证码失败");
                break;
            case CODE_VERIFY:
                showTextDialog("校验验证码失败");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        send_button = (Button) findViewById(R.id.send_button);
        input_phone = (EditText) findViewById(R.id.input_phone);
        yanzheng_text = (EditText) findViewById(R.id.yanzheng_text);
        second = (TextView) findViewById(R.id.second);
        input_password = (EditText) findViewById(R.id.input_password);
        check_zu = (CheckBox) findViewById(R.id.check_zu);
        xieyi_text = (TextView) findViewById(R.id.xieyi_text);
        yes_button = (TextView) findViewById(R.id.yes_button);
        phone = (TextView) findViewById(R.id.phone_show);
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
        phone.setVisibility(View.INVISIBLE);
        if (type == null || type.equals("")) {
            title_text.setText("注册");
            next_button.setText("完成");
            next_button.setVisibility(View.INVISIBLE);
        }
        else if (type.equals("1"))
        {
            title_text.setText("忘记密码");
            next_button.setVisibility(View.INVISIBLE);
            check_zu.setVisibility(View.INVISIBLE);
            xieyi_text.setVisibility(View.INVISIBLE);
            next_button.setText("提交");
        }
       else if ("2".equals(type))
        {
            title_text.setText("找回提现/支付密码");
            next_button.setVisibility(View.INVISIBLE);
            check_zu.setVisibility(View.INVISIBLE);
            xieyi_text.setVisibility(View.INVISIBLE);
        }
        /**
         * 发送验证码
         */
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = input_phone.getText().toString();
                if (isNull(username)) {
                    showTextDialog("请输入手机号");
                    return;
                }

                // String mobile = "^[1][3-8]+\\d{9}";
                String mobile = "\\d{11}";// 只判断11位
                if (!username.matches(mobile)) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                getNetWorker().clientVerify(username);
            }
        });
        yes_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isNull(input_phone.getText().toString())) {
                    showTextDialog("您输入的手机号不正确");
                    return;
                }
                if (isNull(yanzheng_text.getText().toString())) {
                    showTextDialog("输入的验证码不能为空");
                    return;
                }
                if (!check_zu.isChecked() && isNull(type)) {
                    showTextDialog("请同意注册说明");
                    return;
                }
                if (isNull(input_password.getText().toString())) {
                    showTextDialog("请输入密码");
                    return;
                }
                if (input_password.getText().toString().trim().length() >= 6 && input_password.getText().toString().trim().length() <= 20) {

                } else {
                    showTextDialog("密码输入不正确\n请输入6-20位密码");
                    return;
                }
                String codeString = yanzheng_text.getText().toString();
                getNetWorker().codeVerify(input_phone.getText().toString(),
                        codeString);
            }
        });
        /**
         * 注册协议
         */
        xieyi_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, WebviewActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
    }

    private class TimeThread extends Thread {
        private int curr;

        private TimeHandler timeHandler;

        public TimeThread(TimeHandler timeHandler) {
            this.timeHandler = timeHandler;
        }

        void cancel() {
            curr = 0;
        }

        @Override
        public void run() {
            curr = 60;
            while (curr > 0) {
                timeHandler.sendEmptyMessage(curr);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // ignore
                }
                curr--;
            }
            timeHandler.sendEmptyMessage(-1);
        }
    }

    private static class TimeHandler extends Handler {
        RegisterActivity activity;

        public TimeHandler(RegisterActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:

                    activity.send_button.setText("重新发送");
                    activity.send_button.setVisibility(View.VISIBLE);
                    break;
                default:
                    activity.send_button.setVisibility(View.GONE);
                    activity.second.setText("" + msg.what);
                    break;
            }
        }
    }

}
