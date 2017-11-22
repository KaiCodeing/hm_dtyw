package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/9/29.
 * 账号管理
 */
public class UserManageActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout change_password_log;
    private LinearLayout change_tixian_password;
    private LinearLayout find_zhifu_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_manage);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {

    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {

    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        change_password_log = (LinearLayout) findViewById(R.id.change_password_log);
        change_tixian_password = (LinearLayout) findViewById(R.id.change_tixian_password);
        find_zhifu_password = (LinearLayout) findViewById(R.id.find_zhifu_password);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("账号管理");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //修改登录密码
        change_password_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChangeAllPasswordActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });
        //修改支付密码
        change_tixian_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ChangeAllPasswordActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
        //找回支付密码
        find_zhifu_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RegisterActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });
    }
}
