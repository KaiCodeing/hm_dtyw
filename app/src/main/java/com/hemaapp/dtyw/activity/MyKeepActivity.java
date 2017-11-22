package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.MyKeepAdapter;
import com.hemaapp.dtyw.model.Collect;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/10/9.
 * 我的收藏
 */
public class MyKeepActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private XtomListView listview;
    private ProgressBar progressbar;
    //    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private LinearLayout layout_show;
    private ImageView all_delete;
    private TextView back_delete;
    private ArrayList<Collect> collects = new ArrayList<Collect>();
    private String type = "0";
    private MyKeepAdapter adapter;
    private String check = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_class_product);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().collectList(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COLLECT_LIST:
                showProgressDialog("获取我的收藏信息");
                break;
            case GOODS_OPERATE:
                showProgressDialog("取消对商品的收藏...");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COLLECT_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case GOODS_OPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COLLECT_LIST:
                HemaArrayResult<Collect> result = (HemaArrayResult<Collect>) hemaBaseResult;
                collects = result.getObjects();
                freshData();
                break;
            case GOODS_OPERATE:
                showTextDialog("取消收藏成功");
                check = "0";
                all_delete.setImageResource(R.mipmap.check_anzhuang_on);
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inIt();
                    }
                }, 1000);

                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MyKeepAdapter(mContext, collects, type, this);
            adapter.setEmptyString("您还未收藏任何商品");
            listview.setAdapter(adapter);
        } else {
            adapter.setEmptyString("您还未收藏任何商品");
            adapter.setType(type);
            adapter.setCollects(collects);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COLLECT_LIST:
            case GOODS_OPERATE:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case COLLECT_LIST:
                showTextDialog("获取收藏信息失败，请稍后重试");
                break;
            case GOODS_OPERATE:
                showTextDialog("取消收藏失败，请稍后重试");
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
        //     refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        layout_show = (LinearLayout) findViewById(R.id.layout_show);
        all_delete = (ImageView) findViewById(R.id.all_delete);
        back_delete = (TextView) findViewById(R.id.back_delete);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        layout_show.setVisibility(View.GONE);
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("我的收藏");
        next_button.setText("编辑");
        //编辑
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0")) {
                    type = "1";
                    layout_show.setVisibility(View.VISIBLE);
                    next_button.setText("保存");
                } else {
                    type = "0";
                    layout_show.setVisibility(View.GONE);
                    next_button.setText("编辑");
                }
                freshData();
            }
        });
        //全选
        all_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collects == null || collects.size() == 0)
                    return;
                if (check.equals("0")) {
                    for (int i = 0; i < collects.size(); i++) {
                        collects.get(i).setCheck(true);
                    }
                    check = "1";
                    all_delete.setImageResource(R.mipmap.check_anzhuang_off);
                } else {
                    for (int i = 0; i < collects.size(); i++) {
                        collects.get(i).setCheck(false);
                    }
                    check = "0";
                    all_delete.setImageResource(R.mipmap.check_anzhuang_on);
                }
                freshData();
            }
        });
        //全部取消收藏
        back_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collects == null || collects.size() == 0)
                    return;
                int a = 0;

                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < collects.size(); i++) {
                    if (collects.get(i).isCheck()) {
                        a++;
                        buffer.append(collects.get(i).getId() + ",");
                    }
                }
                if (a == 0) {
                    showTextDialog("请选择商品");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().goodsOperate(token, "2", buffer.substring(0, buffer.length() - 1));
            }
        });
    }

    public void changeData() {
        int m = 0;
        for (int i = 0; i < collects.size(); i++) {
            if (!collects.get(i).isCheck()) {
                m++;
            }
        }
        if (m == 0) {
            check = "1";
            all_delete.setImageResource(R.mipmap.check_anzhuang_off);
        } else {
            check = "0";
            all_delete.setImageResource(R.mipmap.check_anzhuang_on);
        }
    }
}
