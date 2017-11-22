package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2016/9/29.
 * 我的钱包
 */
public class MyWalletActivity extends DtywActivity {
    private TextView money_number;
    private LinearLayout jiaoyi_layout;
    private LinearLayout chongzhi_layout;
    private LinearLayout tixian_layout;
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    ViewHolder holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_wallet);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    private void inIt()
    {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().getFeeaccount(token);
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GET_FEEACCOUNT:
                showProgressDialog("获取账户余额...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GET_FEEACCOUNT:
              cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GET_FEEACCOUNT:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                String money = result.getObjects().get(0);
                money_number.setText("¥ "+money);
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GET_FEEACCOUNT:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case GET_FEEACCOUNT:
                showTextDialog("获取余额失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        money_number = (TextView) findViewById(R.id.money_number);
        jiaoyi_layout = (LinearLayout) findViewById(R.id.jiaoyi_layout);
        chongzhi_layout = (LinearLayout) findViewById(R.id.chongzhi_layout);
        tixian_layout = (LinearLayout) findViewById(R.id.tixian_layout);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        title_text.setText("我的钱包");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    //充值
        chongzhi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,RechargeActivity.class);
                startActivity(intent);
            }
        });
        //提现
        tixian_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShow();
            }
        });
        //交易记录
        jiaoyi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,TransactionActivity.class);
                startActivity(intent);
            }
        });
    }
    private class ViewHolder{
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }
    private void setShow()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_camera,null);
        holder = new ViewHolder();
        holder.camera_text = (TextView) view.findViewById(R.id.camera_text);
        holder.album_text = (TextView) view.findViewById(R.id.album_text);
        holder.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        holder.camera_text.setText("支付宝提现");
        holder.album_text.setText("银行卡提现");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        holder.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        //支付宝提现
        holder.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWalletActivity.this,WithdrawalsActivity.class);
                intent.putExtra("keytype","0");
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //银行卡提现
        holder.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWalletActivity.this,WithdrawalsActivity.class);
                intent.putExtra("keytype","1");
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
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
}
