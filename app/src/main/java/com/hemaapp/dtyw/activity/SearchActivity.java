package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.adapter.SearchAdapter;
import com.hemaapp.dtyw.db.SearchDBClient;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/18.
 * 主页搜索
 */
public class SearchActivity extends DtywActivity {
    private EditText search_log;
    private TextView message_view;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private SearchDBClient client;
    private SearchAdapter adapter;
    private ArrayList<String> search_strs;
    private ViewHolder holder;
    private TextView select_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        client = SearchDBClient.get(mContext);
    }

    @Override
    protected void onResume() {
        getHistoryList();
        super.onResume();
    }

    private void getHistoryList() {
        new LoadDBTask().execute();
    }

    private class LoadDBTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // XtomProcessDialog.show(mContext, R.string.loading);
        }

        @Override
        protected Void doInBackground(Void... params) {
            search_strs = client.select();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //   getHistoryList_done();
            refreshLoadmoreLayout.setVisibility(View.VISIBLE);
            progressbar.setVisibility(View.GONE);
            adapter = new SearchAdapter(mContext, search_strs, SearchActivity.this);
            listview.setAdapter(adapter);
        }
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
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        select_type = (TextView) findViewById(R.id.select_type);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        refreshLoadmoreLayout.setLoadmoreable(false);
        refreshLoadmoreLayout.setRefreshable(false);
/**
 * 搜索框的改变
 */
        search_log.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (search_log.getText().toString().trim().equals("")
                        || search_log.getText().toString() == null) {
                    message_view.setText("取消");
                } else {
                    message_view.setText("搜索");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        //取消
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message_view.getText().toString().equals("取消")) {
                    mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    finish();
                } else {
                    String search = search_log.getText().toString().trim();
                    select_search_str(search);
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("word", search_log.getText().toString().trim());
                    if ("所有商品".equals(select_type.getText().toString()))
                    intent.putExtra("keytype","8");
                    else if("二手配件".equals(select_type.getText().toString()))
                        intent.putExtra("keytype","10");
                    else
                        intent.putExtra("keytype","9");
                    startActivity(intent);
                }
            }
        });
        //选择类型
        select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(SearchActivity.this).inflate(
                        R.layout.search_view_select, null);
                holder = new ViewHolder();
                holder.es_text = (TextView) view.findViewById(R.id.es_text);
                holder.pt_text = (TextView) view.findViewById(R.id.pt_text);
                holder.rx_text = (TextView) view.findViewById(R.id.rx_text);
                final PopupWindow popupWindow = new PopupWindow(view,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //二手
                holder.es_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_type.setText("二手配件");
                        popupWindow.dismiss();
                    }
                });
                //热销
                holder.rx_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_type.setText("热销商品");
                        popupWindow.dismiss();
                    }
                });
                //所有
                holder.pt_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_type.setText("所有商品");
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(findViewById(R.id.select_type));
            }
        });
    }

    /**
     * 删除
     */
    public void Clear_HistoryList() {
        client.clear();
        search_strs.clear();
        adapter.notifyDataSetChanged();
    }
    /**
     * 添加
     */

    public void select_search_str(String str) {

            boolean found = false;
            if (search_strs != null && search_strs.size() > 0) {
                for (String hstr : search_strs) {
                    if (hstr.equals(str)) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                if (search_strs == null)
                    search_strs = new ArrayList<String>();
                search_strs.add(0, str);
                client.insert(str);
            }
    }
    private class ViewHolder{
        TextView pt_text;
        TextView rx_text;
        TextView es_text;
    }
}
