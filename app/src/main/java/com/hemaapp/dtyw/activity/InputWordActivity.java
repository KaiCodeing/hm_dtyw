package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/10/8.
 * 输入名字，账号等信息
 */
public class InputWordActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_text;
    private String type;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_word);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALIPAY_SAVE:
                showProgressDialog("保存支付宝信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALIPAY_SAVE:
              cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALIPAY_SAVE:
                showTextDialog("支付宝信息提交成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIntent.putExtra("context", input_text.getText().toString().trim());

                        setResult(RESULT_OK, mIntent);

                        finish();
                    }
                },1000);

                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALIPAY_SAVE:
               showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALIPAY_SAVE:
                showTextDialog("提交支付宝信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_text = (EditText) findViewById(R.id.input_text);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        content = mIntent.getStringExtra("content");
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
        if ("nickname".equals(type)) {
            title_text.setText("昵称");
            input_text.setHint("请填写昵称");

        }
        if ("1".equals(type)) {
            title_text.setText("支付宝账号");
            input_text.setHint("请填写支付宝账号");
        }
        if ("2".equals(type)) {
            title_text.setText("真实姓名");
            input_text.setHint("请填写真实姓名");
        }
        if ("3".equals(type)) {
            title_text.setText("银行卡号");
            input_text.setHint("请填写银行卡号");
        }
        if ("4".equals(type)) {
            title_text.setText("开户银行");
            input_text.setHint("请填写开户银行");
        }
        if (!isNull(content)) {
            input_text.setText(content);
        }

        next_button.setText("确定");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(input_text.getText().toString().trim())) {
                    if ("nickname".equals(type)) {
                        showTextDialog("请输入昵称");
                        return;
                    }
                    if ("1".equals(type)) {
                        showTextDialog("请输入支付宝账号");
                        return;
                    }
                    if ("2".equals(type)) {
                        showTextDialog("请输入真实姓名");
                        return;
                    }
                    if ("3".equals(type)) {
                        showTextDialog("请输入银行卡号");
                        return;
                    }
                    if ("4".equals(type)) {
                        showTextDialog("请输入开户银行");
                        return;
                    }
                }
                if ("1".equals(type)) {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().alipaySave(token,input_text.getText().toString().trim());
                } else {
                    mIntent.putExtra("context", input_text.getText().toString().trim());

                    setResult(RESULT_OK, mIntent);
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                            0);
                    finish();
                }

            }
        });

    }
}
