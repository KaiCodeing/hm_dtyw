package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetWorker;
import com.hemaapp.dtyw.db.SelectDBHelper;
import com.hemaapp.dtyw.model.Select;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import xtom.frame.XtomActivityManager;
import xtom.frame.XtomConfig;
import xtom.frame.util.Md5Util;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/14.
 * 登录
 */
public class LoginActivity extends DtywActivity {
    private ImageButton back_button;//差号
    private TextView title_text;
    private Button next_button;
    private EditText username_text;
    private EditText password_text;
    private TextView login_text;
    private TextView find_password;
    private SelectDBHelper helper;
    private String log;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        helper = SelectDBHelper.get(mContext);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

        // TODO Auto-generated method stub

        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:

                showProgressDialog("正在验证登录信息");
                break;
            case CLIENT_LOGINOUT:
                break;
            default:
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:

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
            case CLIENT_LOGIN:
                HemaArrayResult<User> userArrayResult = (HemaArrayResult<User>) hemaBaseResult;
                User user = userArrayResult.getObjects().get(0);

                getApplicationContext().setUser(user);
                String username = hemaNetTask.getParams().get("username");
                String password = hemaNetTask.getParams().get("password");
                //添加账号
                Select select = new Select(username,user.getNickname(),password,user.getAvatar());
                helper.insertOrUpdate(select);
                XtomSharedPreferencesUtil.save(mContext, "username", username);
                XtomSharedPreferencesUtil.save(mContext, "password", password);
                XtomActivityManager.finishAll();
                String token = user.getToken();
                //getNetWorker().positionSave(token, lng, lat, address);
                GotoMain();
                break;
            case CLIENT_LOGINOUT:
                DtywApplication.getInstance().setUser(null);
                XtomSharedPreferencesUtil.save(mContext, "username", "");// 清空用户名
                XtomSharedPreferencesUtil.save(mContext, "password", "");// 青空密码
                String username1 = username_text.getText().toString();
                String password1 = password_text.getText().toString();
                DtywNetWorker netWorker = getNetWorker();
                netWorker.clientLogin(username1, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password1)));
                break;
            default:
                break;
        }

    }
    /**
     *
     * @方法名称: GotoMain
     * @功能描述: TODO跳转到主界面
     * @返回值: void
     */
    private void GotoMain()
    {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);

    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                HemaArrayResult<User> result = (HemaArrayResult<User>) hemaBaseResult;
                log_i(result.getError_code() + "Error_code");
                if ("102".equals(result.getError_code()+"")) {
                    showTextDialog("手机号或密码输入不正确，请\n重新输入");
                }
                else if ("106".equals(hemaBaseResult.getError_code()+"")) {
                    showTextDialog("手机号不存在");
                }
                else {
                    showTextDialog(result.getMsg());
                }
              break;
            case CLIENT_LOGINOUT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case CLIENT_LOGIN:
                showTextDialog("登录失败，请稍后登录");
                break;
            case CLIENT_LOGINOUT:
                showTextDialog("退出登录失败，请稍后重试");
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
        username_text = (EditText) findViewById(R.id.username_text);
        password_text = (EditText) findViewById(R.id.password_text);
        login_text = (TextView) findViewById(R.id.login_text);
        find_password = (TextView) findViewById(R.id.find_password);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
        log = mIntent.getStringExtra("log");
    }

    @Override
    protected void setListener() {
        /**
         * 退出登录
         */
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNull(type))
                XtomActivityManager.finishAll();
                else
                    finish();
            }
        });
        title_text.setText("登录");
        next_button.setText("注册");
        if (!isNull(log))
        {
            title_text.setText("添加账号");
            next_button.setVisibility(View.INVISIBLE);
            back_button.setImageResource(R.mipmap.back_img);
            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            find_password.setVisibility(View.INVISIBLE);
        }
        /**
         * 注册
         */
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        /**
         * 登录
         */
        login_text.setOnClickListener (new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = username_text.getText().toString();
                String password = password_text.getText().toString();
                if (!isNull(XtomSharedPreferencesUtil.get(mContext,"username")) && XtomSharedPreferencesUtil.get(mContext,"username").equals(username))
                {
                    showTextDialog("您已经登录");
                    return;
                }
                if (isNull(username) || username.equals("")) {
                    showTextDialog("请填写手机号");
                    return;
                }
				String mobile = "\\d{11}";// 只判断11位
				if (!username.matches(mobile)) {
					showTextDialog("手机格式不正确，请重新输入");
					return;
				}
                if (isNull(password) || password.equals("")) {
                    showTextDialog("请填写登录密码");
                    return;
                }
                if (password.trim().length()>=6 && password.trim().length()<=20) {

                }
                else {
                    showTextDialog("密码输入不正确\n请输入6-16位密码");
                    return;
                }
                if (!isNull(log))
                {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().clientLoginout(token);
                }
                else
                {  DtywNetWorker netWorker = getNetWorker();
                         netWorker.clientLogin(username, Md5Util.getMd5(XtomConfig.DATAKEY
                        + Md5Util.getMd5(password)));}
            }
        });
        /**
         * 找回密码
         */
        find_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
    }



}
