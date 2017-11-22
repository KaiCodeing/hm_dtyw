package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.ProductClassAdapter;
import com.hemaapp.dtyw.model.Brands;
import com.hemaapp.dtyw.model.BrandsTw;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/18.
 * 产品分类
 */
public class ProductClassActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ArrayList<BrandsTw> brandsTws = new ArrayList<BrandsTw>();
    private ProductClassAdapter adapter;
    private LinearLayout layout_show;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_class_product);
        super.onCreate(savedInstanceState);
        getNetWorker().brandsTw();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BRANDS_TW:
                showProgressDialog("获取品牌信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BRANDS_TW:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BRANDS_TW:
                HemaArrayResult<BrandsTw> result = (HemaArrayResult<BrandsTw>) hemaBaseResult;
                brandsTws= result.getObjects();
                freshData();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new ProductClassAdapter(ProductClassActivity.this,brandsTws,type);

            adapter.setEmptyString("暂无数据");

            listview.setAdapter(adapter);
        } else {

            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BRANDS_TW:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BRANDS_TW:
                showTextDialog("获取品牌信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
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
        title_text.setText("品牌分类");
        next_button.setVisibility(View.INVISIBLE);
        layout_show.setVisibility(View.GONE);
    }
    public  void finishA(Brands brands)
    {
        mIntent.putExtra("brandName",brands.getName());

        setResult(RESULT_OK, mIntent);
        finish();
    }
}
