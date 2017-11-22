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
import com.hemaapp.dtyw.adapter.SelectBankAdapter;
import com.hemaapp.dtyw.model.Bank;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/10/8.
 * 选择银行
 */
public class SelectBankActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private Integer page = 0;
    private ArrayList<Bank> banks = new ArrayList<>();
    private SelectBankAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_bank);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt()
    {
        getNetWorker().bankList(String.valueOf(page));
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                showProgressDialog("获取银行卡列表");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
               cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                HemaPageArrayResult<Bank> result3 = (HemaPageArrayResult<Bank>) hemaBaseResult;
                ArrayList<Bank> banks = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.banks.clear();
                    this.banks.addAll(banks);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (banks.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (banks.size() > 0)
                        this.banks.addAll(banks);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
        }
    }
    private void freshData() {
        if (adapter == null) {
            adapter = new SelectBankAdapter(mContext,SelectBankActivity.this,banks);
            adapter.setEmptyString("暂无数据");
            listview.setAdapter(adapter);
        } else {
            adapter.setBanks(banks);
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }
    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case BANK_LIST:
                showTextDialog("获取银行卡列表失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void getExras() {

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
        title_text.setText("选择银行");
        next_button.setText("保存");
        //保存
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkon())
                {
                    String bankName = "";
                    for (Bank bank : banks)
                    {
                        if (bank.isCheck())
                            bankName = bank.getName();
                    }
                    mIntent.putExtra("bankName", bankName);
                    setResult(RESULT_OK, mIntent);
//                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
//                            0);
                    finish();
                }
            }
        });

    }
    //判断是否选中
    private boolean checkon()
    {
        if (banks==null || banks.size()==0)
            return false;
        else
        {
            int a = 0;
            for (Bank bank : banks)
            {
                if (bank.isCheck())
                    a++;
            }
            if (a==0) {
                showTextDialog("请选择银行");
                return false;
            }
            else
                return  true;
        }
    }
}
