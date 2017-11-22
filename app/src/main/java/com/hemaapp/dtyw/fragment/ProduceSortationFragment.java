package com.hemaapp.dtyw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywFragment;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.adapter.ShaiXuanExpandAdapter;
import com.hemaapp.dtyw.model.Type;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/22.
 * 产品分类
 */
public class ProduceSortationFragment extends DtywFragment {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
 //   private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ArrayList<Type> types = new ArrayList<Type>();
    private ShaiXuanExpandAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_sortation_produce);
        super.onCreate(savedInstanceState);
        inIt();
    }
    //初始化
    private void inIt()
    {
        getNetWorker().typeList();
    }
    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALLTYPE_LIST:
                showProgressDialog("获取分类信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALLTYPE_LIST:
                progressbar.setVisibility(View.GONE);
          //      refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALLTYPE_LIST:
                HemaArrayResult<Type> result = (HemaArrayResult<Type>) hemaBaseResult;
                types.clear();
                types = result.getObjects();
                freshData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALLTYPE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case ALLTYPE_LIST:

                showTextDialog("获取分类信息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        //refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void setListener() {
        back_button.setVisibility(View.INVISIBLE);
        title_text.setText("产品分类");
        next_button.setVisibility(View.INVISIBLE);

    }

    private void freshData() {
        if (adapter == null) {
            adapter = new ShaiXuanExpandAdapter(types,ProduceSortationFragment.this, getContext());

            adapter.setEmptyString("暂无数据");

            listview.setAdapter(adapter);
        } else {

            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }

    //改变类型主题状态
    public void changeTypeName(int a) {
        for (int i = 0; i < types.size(); i++) {
            if (a == i) {
                types.get(i).setCheck(true);
            } else
                types.get(i).setCheck(false);
        }
        adapter.setTypes(types);
        adapter.notifyDataSetChanged();
    }
    //改变类型状态二
    public void changeTypeOneName(int a, int b) {
        String brandName = "";
        String brandid ="";
        for (int i = 0; i < types.size(); i++) {
            for (int j = 0; j < types.get(i).getChildren().size(); j++) {
                if (a == i) {
                    types.get(i).setCheck(true);
                    if (j == b) {
                        types.get(i).getChildren().get(j).setCheck(true);
                        brandName = types.get(i).getChildren().get(j).getName();
                        brandid = types.get(i).getChildren().get(j).getId();
                    } else
                        types.get(i).getChildren().get(j).setCheck(false);
                } else {
                    types.get(i).setCheck(false);
                    types.get(i).getChildren().get(j).setCheck(false);
                }
            }
        }
        adapter.setTypes(types);
        adapter.notifyDataSetChanged();
        //跳转
        Intent intent = new Intent(getActivity(), BrandActivity.class);
        intent.putExtra("brandName",brandName);
        intent.putExtra("typekey","7");
        intent.putExtra("typeid",brandid);
        getActivity().startActivity(intent);
    }

}
