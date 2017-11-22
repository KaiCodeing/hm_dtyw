package com.hemaapp.dtyw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywFragment;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.activity.LoginActivity;
import com.hemaapp.dtyw.activity.MessageActivity;
import com.hemaapp.dtyw.activity.SearchActivity;
import com.hemaapp.dtyw.adapter.HomeAdapter;
import com.hemaapp.dtyw.adapter.TopAdAdapter;
import com.hemaapp.dtyw.model.AdList;
import com.hemaapp.dtyw.model.Brands;
import com.hemaapp.dtyw.model.Bulletin;
import com.hemaapp.dtyw.model.Goods;
import com.hemaapp.dtyw.model.NoticeCount;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.DtywViewPager;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/9/18.
 * 主页
 * HomeAdapter
 */
public class HomeFragment extends DtywFragment {
    private TextView search_log;
    private ImageView message_view;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ArrayList<AdList> adLists = new ArrayList<AdList>();
    private ArrayList<Bulletin> bulletins = new ArrayList<Bulletin>();
    private ArrayList<Goods> goodses = new ArrayList<Goods>();
    private ArrayList<Brands> brandses = new ArrayList<Brands>();
    private String type = "1";//1品牌推荐 2热销专区
    private Integer page = 0;
    private HomeAdapter adapter;
    private View view;
    private ViewHolder holder;
    private TopAdAdapter adAdapter;
    private ViewSwitcher.ViewFactory viewFactory;
    private Handler handler;
    private String url;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_home);
        super.onCreate(savedInstanceState);
        view = View.inflate(this.getActivity(), R.layout.adapter_home_start, null);
        //   listview.addHeaderView(view);
        inIt();
    }

    private void inIt() {
        getNetWorker().thirdUrl();

    }

    @Override
    public void onResume() {
        if (!isNull(XtomSharedPreferencesUtil.get(getActivity(), "username"))) {
            String token = DtywApplication.getInstance().getUser().getToken();
            getNetWorker().noreadNoticeCount(token);
        }
        super.onResume();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case AD_LIST:
                showProgressDialog("获取广告信息");
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case BULLETIN_LIST:
                showProgressDialog("获取公告信息");
                break;
            case BRANDS_LIST:
                showProgressDialog("获取品牌推荐信息");
                break;
            case GOODS_LIST:
                showProgressDialog("获取热销商品信息");
                break;
            case THIRD_URL:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case AD_LIST:
            case BULLETIN_LIST:
            case BRANDS_LIST:
            case GOODS_LIST:
                cancelProgressDialog();
                break;
            case THIRD_URL:
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case THIRD_URL:
                HemaArrayResult<String> resulturl = (HemaArrayResult<String>) hemaBaseResult;
                url = resulturl.getObjects().get(0);
                getNetWorker().adList("1");
                break;
            case AD_LIST:
                HemaArrayResult<AdList> result = (HemaArrayResult<AdList>) hemaBaseResult;
                adLists = result.getObjects();
                /**
                 * 获取公告
                 */
                getNetWorker().bulletinList();
                break;
            case BULLETIN_LIST:
                HemaArrayResult<Bulletin> result1 = (HemaArrayResult<Bulletin>) hemaBaseResult;
                bulletins = result1.getObjects();
                setHeaderView();
                //品牌推荐
                if ("1".equals(type)) {
                    getNetWorker().brandsList("1", String.valueOf(page));
                }
                //热销专区
                else {
                    getNetWorker().goodsList("2", "", "", "1", "0", "", "", "0");
                }
                break;
            case BRANDS_LIST:
                HemaPageArrayResult<Brands> result2 = (HemaPageArrayResult<Brands>) hemaBaseResult;
                ArrayList<Brands> brandses = result2.getObjects();
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.brandses.clear();
                    this.brandses.addAll(brandses);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (brandses.size() < 5) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (brandses.size() > 0)
                        this.brandses.addAll(brandses);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(getActivity(), "已经到最后啦");
                    }
                }
                freshData();
                break;
            case GOODS_LIST:
                HemaPageArrayResult<Goods> result3 = (HemaPageArrayResult<Goods>) hemaBaseResult;
                ArrayList<Goods> goodses = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.goodses.clear();
                    this.goodses.addAll(goodses);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (goodses.size() < 5) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (goodses.size() > 0)
                        this.goodses.addAll(goodses);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(getActivity(), "已经到最后啦");
                    }
                }
                freshData();
                break;
            case NOREAD_UNREAD:
                HemaArrayResult<NoticeCount> result4 = (HemaArrayResult<NoticeCount>) hemaBaseResult;
                NoticeCount count = result4.getObjects().get(0);
                int count1;
                if (isNull(count.getNum())) {
                    count1 = 0;
                    message_view.setImageResource(R.mipmap.message_img_out);
                } else {
                    count1 = Integer.valueOf(count.getNum());
                    if (count1!=0)
                    message_view.setImageResource(R.mipmap.message_img_on);
                    else
                        message_view.setImageResource(R.mipmap.message_img_out);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case AD_LIST:
            case BULLETIN_LIST:
            case BRANDS_LIST:
            case GOODS_LIST:
            case NOREAD_UNREAD:
            case THIRD_URL:
                showTextDialog(hemaBaseResult.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case THIRD_URL:
                showTextDialog("获取网址失败，请稍后重试");
                break;
            case AD_LIST:
                showTextDialog("获取广告信息失败，请稍后重试");
                break;
            case BULLETIN_LIST:
                showTextDialog("获取公告信息失败，请稍后重试");
                break;
            case BRANDS_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取品牌推荐失败，请稍后重试");
                break;
            case GOODS_LIST:
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取热销商品失败，请稍后重试");
                break;
            case NOREAD_UNREAD:
                showTextDialog("获取消息信息失败，请稍后重试");
                break;
            default:
                break;
        }
    }

    @Override
    protected void findView() {
        search_log = (TextView) findViewById(R.id.search_log);
        message_view = (ImageView) findViewById(R.id.message_view);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    @Override
    protected void setListener() {
        /**
         * 加载
         */
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {

            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout v) {
                // TODO Auto-generated method stub
                page = 0;

                inIt();

            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout v) {
                // TODO Auto-generated method stub
                page++;
                if ("1".equals(type)) {
                    getNetWorker().brandsList("1", String.valueOf(page));
                } else {
                    getNetWorker().goodsList("2", "", "", "1", "0", "", "",String.valueOf(page));
                }
            }
        });
        //搜索search_log
        search_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
            }
        });
        /**
         * 消息
         */
        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (isNull(XtomSharedPreferencesUtil.get(getActivity(), "username"))) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                getActivity().startActivity(intent);
            }
            }
        });

    }

    /**
     * 选择数据
     */
    public void setSelectData(String select) {
        type = select;
        page = 0;
        if ("1".equals(type)) {
            getNetWorker().brandsList("1", String.valueOf(page));
        } else {
            getNetWorker().goodsList("2", "", "", "1", "0", "", "",  String.valueOf(page));
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new HomeAdapter(HomeFragment.this, type,
                    adLists, bulletins, goodses, brandses, listview,url);

            adapter.setEmptyString("暂无数据");

            listview.setAdapter(adapter);
        } else {
            adapter.setAdLists(adLists);
            adapter.setBrandses(brandses);
            adapter.setBulletins(bulletins);
            adapter.setGoodses(goodses);
            adapter.setUrl(url);
            adapter.setType(type);
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }

    private class ViewHolder {
        DtywViewPager adviewpager;
        RadioGroup radiogroup;
        ViewSwitcher index_viewSwitcher;
        ImageView pinpai_image;
        ImageView chanping_image;
        ImageView ershou_image;
        ImageView pintai_image;
        RadioButton button1;
        RadioButton button2;
        android.view.View view1;
        android.view.View view2;
        FrameLayout vp_top;
        RadioGroup radiogroup_select;
    }

    private void setHeaderView() {
        holder = new ViewHolder();
        holder.adviewpager = (DtywViewPager) view.findViewById(R.id.adviewpager);
        holder.radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        holder.index_viewSwitcher = (ViewSwitcher) view.findViewById(R.id.index_viewSwitcher);
        holder.pinpai_image = (ImageView) view.findViewById(R.id.pinpai_image);
        holder.chanping_image = (ImageView) view.findViewById(R.id.chanping_image);
        holder.ershou_image = (ImageView) view.findViewById(R.id.ershou_image);
        holder.pintai_image = (ImageView) view.findViewById(R.id.pintai_image);
        holder.button1 = (RadioButton) view.findViewById(R.id.button1);
        holder.button2 = (RadioButton) view.findViewById(R.id.button2);
        holder.view1 = view.findViewById(R.id.view1);
        holder.view2 = view.findViewById(R.id.view2);
        holder.vp_top = (FrameLayout) view.findViewById(R.id.vp_top);
        holder.radiogroup_select = (RadioGroup) view.findViewById(R.id.radiogroup_select);
        setData(holder);

    }

    private void setData(final ViewHolder holder) {
        /**
         * 广告页
         */
        adAdapter = new TopAdAdapter(this, holder.radiogroup, holder.vp_top, adLists);
        holder.adviewpager.setAdapter(adAdapter);
        holder.adviewpager.setOnPageChangeListener(new PageChangeListener());
        /**
         * 公告
         */
        holder.index_viewSwitcher.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.in));
        holder.index_viewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.out));

        handler = new Handler() {
            int i = 1;

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (i >= bulletins.size())
                    i = 0;
                ((TextView) holder.index_viewSwitcher.getNextView().findViewById(R.id.item_index_vs_tv1)).setText(bulletins.get(i).getName());
                holder.index_viewSwitcher.getNextView().setTag(bulletins.get(i));
                if (bulletins.size() > 1)
                    holder.index_viewSwitcher.showNext();
                i++;
                if (i >= bulletins.size())
                    i = 0;

                handler.sendEmptyMessageDelayed(i, 4000);
            }
        };
        if (viewFactory == null) {
            viewFactory = new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_index_viewswitcher, null);
                    TextView tv = ((TextView) itemView.findViewById(R.id.item_index_vs_tv1));
                    // log_d("position==="+position);
                    tv.setText(bulletins.get(0).getName());
                    itemView.setTag(bulletins.get(0));
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bulletin a = (Bulletin) v.getTag();
                            // TODO Auto-generated method stub
//                                Intent intent=new Intent(getActivity(),JpDetailEndActivity.class);
//                                intent.putExtra("goodid", ads.get(position).getNum_id());
//                                startActivity(intent);
                        }
                    });
                    return itemView;
                }
            };
            holder.index_viewSwitcher.setFactory(viewFactory);
            handler.sendEmptyMessageDelayed(1, 4000);
        }
        /**
         * 四个按钮
         */
        //品牌分类
        holder.pinpai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //所有产品
        holder.chanping_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //二手配件
        holder.ershou_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //平台招商
        holder.pintai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * 选择类型显示
         */
        holder.radiogroup_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:// 策略库
                        setSelectData("1");
                        holder.view1.setVisibility(View.VISIBLE);
                        holder.view2.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.button2:// 名师榜
                        setSelectData("2");
                        holder.view2.setVisibility(View.VISIBLE);
                        holder.view1.setVisibility(View.INVISIBLE);
                        break;

                }
            }
        });

    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int arg0) {
            if (adAdapter != null) {
                ViewGroup indicator = adAdapter.getIndicator();
                if (indicator != null) {
                    RadioButton rbt = (RadioButton) indicator.getChildAt(arg0);
                    if (rbt != null)
                        rbt.setChecked(true);
                }
            }
        }

    }
}
