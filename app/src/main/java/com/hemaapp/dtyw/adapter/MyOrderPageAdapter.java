package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTaskExecuteListener;
import com.hemaapp.dtyw.DtywNetWorker;
import com.hemaapp.dtyw.activity.MyOrderActivity;
import com.hemaapp.dtyw.model.OrderList;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/10/31.
 */
public class MyOrderPageAdapter extends PagerAdapter {
    private ArrayList<Params> paramses;
    private MyOrderActivity activity;
    //    public MyOrderAdapter adapter;
    private ArrayList<OrderList> orderLists;
    //    DtywNetWorker netWorker;
    private ArrayList<RefreshLoadmoreLayout> layouts = new ArrayList<>();
    private ArrayList<ArrayList<OrderList>> orlist = new ArrayList<>();
    public MyOrderAdapter myadapter;

    public MyOrderPageAdapter(MyOrderActivity activity, ArrayList<Params> paramses) {
        this.activity = activity;
        this.paramses = paramses;
    }

    @Override
    public int getCount() {
        return paramses == null ? 0 : paramses.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        Params params = paramses.get(position);
        if (params.isFirstSetPrimary) {
            View view = (View) object;
            RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            layout.getOnStartListener().onStartRefresh(layout);
            params.isFirstSetPrimary = false;

        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
//        View view = container.getChildAt(position);
//        if (view == null) {
        Log.i("ss", "position---" + position);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.pageritem_rllistview_progress,
                null);
        RefreshLoadmoreLayout layout = (RefreshLoadmoreLayout) view
                .findViewById(R.id.refreshLoadmoreLayout);
        layouts.add(layout);
        Params params = paramses.get(position);
        DtywNetWorker netWorker = new DtywNetWorker(activity);
        netWorker.setOnTaskExecuteListener(new OnTaskExecuteListener(
                activity, view, params));
        layout.setOnStartListener(new OnStartListener(params, netWorker,
                view));
        container.addView(view);
//        }
        return view;
    }

    private class OnStartListener implements
            xtom.frame.view.XtomRefreshLoadmoreLayout.OnStartListener {

        private Integer current_page = 0;
        private Params params;
        private DtywNetWorker netWorker;
        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        // private GridView gridview;
        private ProgressBar progressBar;
        private LinearLayout layout_show;
        public OnStartListener(Params params, DtywNetWorker netWorker, View v) {
            this.params = params;
            this.netWorker = netWorker;
            this.layout = (XtomRefreshLoadmoreLayout) v
                    .findViewById(R.id.refreshLoadmoreLayout);
            this.progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
            this.listView = (XtomListView) layout.findViewById(R.id.listview);

        }

        @Override
        public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page = 0;
            DtywApplication application = DtywApplication.getInstance();
            String token = application.getUser().getToken();
            netWorker.orderList(token, params.keytype, String.valueOf(current_page));
        }

        @Override
        public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
            // TODO Auto-generated method stub
            current_page++;
            DtywApplication application = DtywApplication.getInstance();
            String token = application.getUser().getToken();
            netWorker.orderList(token, params.keytype, String.valueOf(current_page));
        }

    }

    private class OnTaskExecuteListener extends DtywNetTaskExecuteListener {

        private XtomRefreshLoadmoreLayout layout;
        private XtomListView listView;
        private ProgressBar progressBar;
        private Params params;
        // private GridView gridview;
        private ViewPager vp;
        private TextView content;

        private MyOrderAdapter adapter;
        private ArrayList<OrderList> orderLists = new ArrayList<>();

        // private View allitem;
        public OnTaskExecuteListener(Context context, View view, Params params) {
            super(context);
            // TODO Auto-generated constructor stub
            layout = (XtomRefreshLoadmoreLayout) view
                    .findViewById(R.id.refreshLoadmoreLayout);
            listView = (XtomListView) view.findViewById(R.id.listview);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
            this.params = params;
            orderLists = new ArrayList<OrderList>();
        }

        @Override
        public void onPreExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPostExecute(HemaNetWorker netWorker, HemaNetTask netTask) {
            // TODO Auto-generated method stub
            DtywHttpInformation information = (DtywHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case ORDER_LIST:
                    progressBar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onServerSuccess(HemaNetWorker netWorker,
                                    HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            DtywHttpInformation information = (DtywHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case ORDER_LIST:
                    HemaPageArrayResult<OrderList> resultblog = (HemaPageArrayResult<OrderList>) baseResult;
                    String pageblog = netTask.getParams().get("page");
                    if ("0".equals(pageblog)) {
                        layout.refreshSuccess();
                        orderLists.clear();
                        orderLists.addAll(resultblog.getObjects());
                        if (resultblog.getObjects().size() < DtywApplication
                                .getInstance().getSysInitInfo().getSys_pagesize()) {
                            layout.setLoadmoreable(false);
                        } else {
                            layout.setLoadmoreable(true);
                        }

                    } else { // 更多
                        layout.loadmoreSuccess();
                        if (resultblog.getObjects().size() > 0) {
                            orderLists.addAll(resultblog.getObjects());
                        } else {
                            layout.setLoadmoreable(false);
                            XtomToastUtil.showShortToast(activity, "已经到最后啦");
                        }
                    }
                    String keytpe = netTask.getParams().get("keytype");
                    orlist.add(orderLists);
                    if (adapter == null) {
                        adapter = new MyOrderAdapter(activity, orderLists,keytpe);
                        adapter.setEmptyString("没有任何订单");
                        listView.setAdapter(adapter);
                        if (params.keytype.equals("2"))
                        {
                            myadapter =adapter;
                        }
                    } else {
                        adapter.setOrderLists(orderLists);
                        adapter.setKeytype(keytpe);
                        adapter.setEmptyString("没有任何商品");
                        adapter.notifyDataSetChanged();
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onServerFailed(HemaNetWorker netWorker,
                                   HemaNetTask netTask, HemaBaseResult baseResult) {
            // TODO Auto-generated method stub
            DtywHttpInformation information = (DtywHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case ORDER_LIST:
                    activity.showTextDialog(baseResult.getMsg());
                    String page12 = netTask.getParams().get("page");
                    if ("0".equals(page12)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onExecuteFailed(HemaNetWorker netWorker,
                                    HemaNetTask netTask, int failedType) {
            // TODO Auto-generated method stub
            DtywHttpInformation information = (DtywHttpInformation) netTask
                    .getHttpInformation();
            switch (information) {
                case ORDER_LIST:
                    activity.showTextDialog("获取订单列表失败，请稍后重试");
                    String page12 = netTask.getParams().get("page");
                    if ("0".equals(page12)) { // 刷新
                        layout.refreshFailed();
                    } else { // 更多
                        layout.loadmoreFailed();
                    }
                    break;

                default:
                    break;
            }
        }

    }

    public static class Params extends XtomObject {
        boolean isFirstSetPrimary = true;// 第一次显示时需要自动加载数据
        String keytype;
        String keyid;

        public Params(String keytype, String keyid) {
            super();
            this.keytype = keytype;
            this.keyid = keyid;
        }
    }

    public void freshAll() {
        for (RefreshLoadmoreLayout layout : layouts) {
            if (layout != null)
                layout.getOnStartListener().onStartRefresh(layout);
        }
    }

    public ArrayList<OrderList> changeData() {
        return orlist.get(0);
    }


}
