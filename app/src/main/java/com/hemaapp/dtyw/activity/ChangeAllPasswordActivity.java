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

import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/29.
 * 修改密码
 */
public class ChangeAllPasswordActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText old_password;
    private EditText new_password;
    private EditText yes_new_password;
    private TextView on_change;
    private String type; //1 登录密码 2 支付密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_changeall_password);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
                showProgressDialog("修改密码...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
                showTextDialog("修改成功");
                if (type.equals("1")) {
                    XtomSharedPreferencesUtil.save(mContext, "password", hemaNetTask.getParams().get("new_password"));
                }
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
            case PASSWORD_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case PASSWORD_SAVE:
                showTextDialog("修改密码失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        yes_new_password = (EditText) findViewById(R.id.yes_new_password);
        on_change = (TextView) findViewById(R.id.on_change);


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
        next_button.setVisibility(View.INVISIBLE);
        if ("1".equals(type))
            title_text.setText("修改登录密码");
        else
            title_text.setText("修改提现/支付密码");
        //修改
        on_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPsw()) {
                    String oldPwd = old_password.getText().toString().trim();
                    String newPwd = new_password.getText().toString().trim();
                    String newPwd1 = yes_new_password.getText().toString().trim();
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().passwordSave(token, type,
                            Md5Util.getMd5(XtomConfig.DATAKEY
                                    + Md5Util.getMd5(oldPwd)),
                            Md5Util.getMd5(XtomConfig.DATAKEY
                                    + Md5Util.getMd5(newPwd)));
                }
            }
        });
    }

    //检测
    private boolean checkPsw() {
        String oldPwd = old_password.getText().toString().trim();
        String newPwd = new_password.getText().toString().trim();
        String newPwd1 = yes_new_password.getText().toString().trim();
        if (isNull(oldPwd)) {
            showTextDialog("请填写原密码");
            return false;
        }
        if (isNull(newPwd)) {
            showTextDialog("请填写新密码");
            return false;
        }
        if (isNull(newPwd1)) {
            showTextDialog("请填写确认密码");
            return false;
        }
        if (!newPwd.equals(newPwd1)) {
            showTextDialog("请保持两次输入密码一致");
            return false;
        }
        if (newPwd.length() < 6 || newPwd.length() > 20) {
            showTextDialog("请填写6-20位密码");
            return false;
        }
        return true;
    }
}
