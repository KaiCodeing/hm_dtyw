package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.HemaWebView;

/**
 * Created by lenovo on 2016/9/18.
 * 内部链接
 */
public class WebviewActivity extends DtywActivity {
    private ImageButton back_button;//差号
    private TextView title_text;
    private Button next_button;
    private HemaWebView webview_aboutwe;
    private String type;
    private String id ;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
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
        webview_aboutwe = (HemaWebView) findViewById(R.id.webview_aboutwe);
    }

    @Override
    protected void getExras() {
    type = mIntent.getStringExtra("type");
        id = mIntent.getStringExtra("id");
        url = mIntent.getStringExtra("url");
    }

    @Override
    protected void setListener() {
    back_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

        String sys_web_service = getApplicationContext().getSysInitInfo()
                .getSys_web_service();
        back_button.setImageResource(R.mipmap.back_img);
        /**
         * 注册协议
         */
        if ("1".equals(type)) {
            String path = sys_web_service + "webview/parm/protocal";
            webview_aboutwe.loadUrl(path);
            title_text.setText("注册协议");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("2".equals(type))
        {
            String path = sys_web_service + "webview/parm/merchants";
            webview_aboutwe.loadUrl(path);
            title_text.setText("平台招商");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("3".equals(type))
        {
            String path = sys_web_service + "webview/parm/instructions";
            webview_aboutwe.loadUrl(path);
            title_text.setText("关于我们");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("4".equals(type))
        {
            String path = sys_web_service + "webview/parm/softinfo";
            webview_aboutwe.loadUrl(path);
            title_text.setText("帮助中心");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("5".equals(type))
        {
            String path = sys_web_service + "webview/parm/wallet";
            webview_aboutwe.loadUrl(path);
            title_text.setText("钱包使用说明");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("6".equals(type))
        {
            String path = sys_web_service + "webview/parm/ad/id/"+id;
            webview_aboutwe.loadUrl(path);
            title_text.setText("图文详情");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("7".equals(type))
        {
            String path = sys_web_service + "webview/parm/bulletin/id/"+id;
            webview_aboutwe.loadUrl(path);
            title_text.setText("图文详情");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("8".equals(type))
        {
            String path = sys_web_service + "webview/parm/refund";
            webview_aboutwe.loadUrl(path);
            title_text.setText("退款规则");
            next_button.setVisibility(View.INVISIBLE);
            return;
        }
        if ("9".equals(type))
        {
            webview_aboutwe.loadUrl(type);
        }
    }
}
