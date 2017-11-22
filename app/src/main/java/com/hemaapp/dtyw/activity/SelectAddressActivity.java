package com.hemaapp.dtyw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.SelectAddressAdapter;
import com.hemaapp.dtyw.model.Shipaddress;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/22.
 * 选择地址
 */
public class SelectAddressActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    //  private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ArrayList<Shipaddress> shipaddresses = new ArrayList<Shipaddress>();
    private SelectAddressAdapter addressAdapter;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_select);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        inIt();
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().shipaddressList(token);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SHIPADDRESS_LIST:
                showProgressDialog("获取收货地址信息");
                break;
            case SETDEFAULTADD:
                showProgressDialog("保存收货地址");
                break;
            case SHIPADDRESS_REMOVE:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SHIPADDRESS_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                break;
            case SETDEFAULTADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SHIPADDRESS_LIST:
                HemaArrayResult<Shipaddress> result = (HemaArrayResult<Shipaddress>) hemaBaseResult;
                shipaddresses = result.getObjects();
                freshData();
                break;
            case SETDEFAULTADD:
            case SHIPADDRESS_REMOVE:
                inIt();
                break;
        }
    }
    private void freshData() {
        if (addressAdapter == null) {
            addressAdapter = new SelectAddressAdapter(mContext, shipaddresses, this);



            listview.setAdapter(addressAdapter);
        } else {
            addressAdapter.setShipaddresses(shipaddresses);

            addressAdapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SHIPADDRESS_LIST:
            case SETDEFAULTADD:
            case SHIPADDRESS_REMOVE:
                showTextDialog(hemaBaseResult.getMsg());
                break;


        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case SHIPADDRESS_LIST:
                showTextDialog("获取收货地址失败，请稍后重试");
                break;
            case SETDEFAULTADD:
                showTextDialog("设置收货地址失败，请稍后重试");
                break;
            case SHIPADDRESS_REMOVE:
                showTextDialog("删除收货地址失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        //   refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {
        type = mIntent.getStringExtra("type");
    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        if (isNull(type))
        title_text.setText("选择收货地址");
        else
        title_text.setText("收货地址");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //设置收货地址
    public void changeAdd(String addId)
    {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().setDefaultadd(token,addId);
    }
    //删除收货地址
    public void deleteAdd(String addId)
    {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().shipaddressRemove(token,addId);
    }
}
