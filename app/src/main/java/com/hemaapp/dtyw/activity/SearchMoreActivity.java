package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

/**
 * Created by lenovo on 2017/3/1.
 * 搜索框
 */
public class SearchMoreActivity extends DtywActivity {
    private EditText search_log;
    private TextView message_view;
    private String keytype;
    private String brandId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_more);
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
        search_log = (EditText) findViewById(R.id.search_log);
        message_view = (TextView) findViewById(R.id.message_view);
    }

    @Override
    protected void getExras() {
        keytype = mIntent.getStringExtra("keytype");
        brandId = mIntent.getStringExtra("brandId");
    }

    @Override
    protected void setListener() {
        //文字变化改变
        search_log.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String word = search_log.getText().toString().trim();
                if (isNull(word))
                    message_view.setText("取消");
                else
                    message_view.setText("搜索");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //搜索
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("取消".equals(message_view.getText().toString())) {
                    finish();
                } else {
                    Intent intent = new Intent(SearchMoreActivity.this, SearchResultActivity.class);
                    intent.putExtra("keytype", keytype);
                    intent.putExtra("brandId", brandId);
                    intent.putExtra("word",search_log.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
