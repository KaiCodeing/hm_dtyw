package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/10/8.
 * 编辑银行卡
 */
public class EditorBankActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout bank_layout;
    private LinearLayout name_layout;
    private LinearLayout bank_number_layout;
    private LinearLayout bank_kaihu_layout;
    private TextView bank_name;//银行名称
    private TextView bank_user_name;//户主姓名
    private TextView bank_number;//银行卡号
    private TextView bank_open_name;//开户行
    private String bankname ="";
    private String bank="";
    private String bankcard="";
    private String bankuser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_editor_bank);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode)
        {
            case 1:
                String bankName = data.getStringExtra("bankName");
                bank_name.setText(bankName);
                break;
            //xingming
            case 2:
                String bankUer =  data.getStringExtra("context");
                bank_user_name.setText(bankUer);
                break;
            case 3:
                String banknum =  data.getStringExtra("context");
                bank_number.setText(banknum);
                break;
            case 4:
                String bankopen = data.getStringExtra("context");
                bank_open_name.setText(bankopen);
                break;
        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_SAVE:
                showProgressDialog("保存银行卡信息");
                break;
        }

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_SAVE:
               cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_SAVE:
                showTextDialog("保存银行卡信息成功");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //判断是否全部填写
                        String bankName = bank_name.getText().toString().trim();
                        String bankUser = bank_user_name.getText().toString().trim();
                        String bankNum = bank_number.getText().toString().trim();
                        String bankOpen = bank_open_name.getText().toString().trim();
                        //返回
                        mIntent.putExtra("bankName", bankName);
                        mIntent.putExtra("bankUser", bankUser);
                        mIntent.putExtra("bankNum", bankNum);
                        mIntent.putExtra("bankOpen", bankOpen);
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
            case BANK_SAVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_SAVE:
                showTextDialog("保存银行卡信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        bank_layout = (LinearLayout) findViewById(R.id.bank_layout);
        name_layout = (LinearLayout) findViewById(R.id.name_layout);
        bank_number_layout = (LinearLayout) findViewById(R.id.bank_number_layout);
        bank_kaihu_layout = (LinearLayout) findViewById(R.id.bank_kaihu_layout);
        bank_name = (TextView) findViewById(R.id.bank_name);
        bank_user_name = (TextView) findViewById(R.id.bank_user_name);
        bank_number = (TextView) findViewById(R.id.bank_number);
        bank_open_name = (TextView) findViewById(R.id.bank_open_name);

    }

    @Override
    protected void getExras() {
        bankname = mIntent.getStringExtra("bankname");
        bank = mIntent.getStringExtra("bank");
        bankcard = mIntent.getStringExtra("bankcard");
        bankuser = mIntent.getStringExtra("bankuser");

    }

    @Override
    protected void setListener() {
        bank_name.setText(bankname);
        bank_user_name.setText(bankuser);
        bank_number.setText(bankcard);
        bank_open_name.setText(bank);
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("编辑银行卡");
        next_button.setText("确定");
        //提交银行卡
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否全部填写
                String bankName = bank_name.getText().toString().trim();
                String bankUser = bank_user_name.getText().toString().trim();
                String bankNum = bank_number.getText().toString().trim();
                String bankOpen = bank_open_name.getText().toString().trim();
                if (isNull(bankName))
                {
                    showTextDialog("请选择银行");
                    return;
                }
                if (isNull(bankUser))
                {
                    showTextDialog("请填写真实姓名");
                    return;
                }
                if (isNull(bankNum))
                {
                    showTextDialog("请填写银行卡号");
                    return;
                }
                if (isNull(bankOpen))
                {
                    showTextDialog("请填写开户行名称");
                    return;
                }
                //提交
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().bankSave(token,bankUser,bankName,bankNum,bankOpen);


            }
        });
        //选择银行卡
        bank_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditorBankActivity.this,SelectBankActivity.class);
                startActivityForResult(intent,1);
            }
        });
        //填写真实姓名
        name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = bank_user_name.getText().toString().trim();
                Intent intent =new Intent(EditorBankActivity.this,InputWordActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("content",userName);
                startActivityForResult(intent,2);
            }
        });
        //银行卡号
        bank_number_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankNum= bank_number.getText().toString().trim();
                Intent intent =new Intent(EditorBankActivity.this,InputWordActivity.class);
                intent.putExtra("type","3");
                intent.putExtra("content",bankNum);
                startActivityForResult(intent,3);
            }
        });
        //开户行
        bank_kaihu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankNum= bank_open_name.getText().toString().trim();
                Intent intent =new Intent(EditorBankActivity.this,InputWordActivity.class);
                intent.putExtra("type","4");
                intent.putExtra("content",bankNum);
                startActivityForResult(intent,4);
            }
        });
    }
}
