package com.hemaapp.dtyw.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.CommodityInforAdapter;
import com.hemaapp.dtyw.model.CityChildren;
import com.hemaapp.dtyw.model.CitySan;
import com.hemaapp.dtyw.model.GoodsGet;
import com.hemaapp.dtyw.model.Installservice;
import com.hemaapp.dtyw.model.Reply;
import com.hemaapp.dtyw.model.SysInitInfo;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.AreaDialog;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import xtom.frame.XtomObject;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/9/18.
 * 商品详情
 */
public class CommodityInforActivity extends DtywActivity implements PlatformActionListener {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ImageView go_top_image;
    private FrameLayout car_image;
    private TextView view_show;
    private TextView add_car_text;
    private TextView go_money_text;
    private String id;
    private ArrayList<Installservice> installservices = new ArrayList<Installservice>();
    private ArrayList<Reply> replies = new ArrayList<Reply>();
    private GoodsGet goodsGet;
    private Integer page = 0;
    private String type = "2";
    private CommodityInforAdapter adapter;
    private AreaDialog areaDialog;
    private String cityName;
    private ShareHolder shareHolder;
    private OnekeyShare oks;
    private String sys_plugins;
    private String pathWX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_infor_commodity);
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        SysInitInfo initInfo = getApplicationContext()
                .getSysInitInfo();
        sys_plugins = initInfo.getSys_plugins();
        pathWX = sys_plugins + "share/sdk.php?id=" + id+"&keytype=1";
        log_i("++++++++"+pathWX);
    }

    @Override
    protected void onResume() {
        inIt();
        super.onResume();
    }
    private void inIt() {
        String token = null;
        if (DtywApplication.getInstance().getUser() != null)
            token = DtywApplication.getInstance().getUser().getToken();
        else {
        }
        getNetWorker().goodsGet(token, id);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                showProgressDialog("获取商品详情");

                break;
            case INSTALLSERVICE_LIST:
                showProgressDialog("获取商品安装服务");
                break;
            case REPLY_LIST:
                showProgressDialog("获取评论信息");
                break;
            case GOODS_OPERATE:
                showProgressDialog("保存收藏操作");
                break;
            case ADDLISTALL_LIST:
                showProgressDialog("获取地区信息");
                break;
            case CART_ADD:
                showProgressDialog("添加到购物车");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case INSTALLSERVICE_LIST:
            case REPLY_LIST:
            case GOODS_OPERATE:
            case ADDLISTALL_LIST:
            case CART_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ADDLISTALL_LIST:
                HemaArrayResult<CityChildren> result1 = (HemaArrayResult<CityChildren>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                citySan.setChildren(result1.getObjects());
                getApplicationContext().setCityInfo(citySan);
                showCity();
                break;
            case GOODS_GET:
                log_i("+++++++++++++++++第二次");
                HemaArrayResult<GoodsGet> result = (HemaArrayResult<GoodsGet>) hemaBaseResult;
                goodsGet = result.getObjects().get(0);
                if (goodsGet.getPropery() == null || goodsGet.getPropery().size() == 0) {
                } else {
                    for (int i = 0; i < goodsGet.getPropery().size(); i++) {
                        goodsGet.getPropery().get(i).getValue().get(0).setCheck(true);
                    }
                }
                //安装接口
                String username = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");
                //登录
                if (!isNull(username)) {
                    String address = DtywApplication.getInstance().getUser().getDistrict_name();
                    String token = DtywApplication.getInstance().getUser().getToken();
                    cityName = address;
                    //getNetWorker().installserviceList(token, address, id);
                    getNetWorker().getCartgoodsnum(token);
                    freshData();
                } else
                //未登录
                {
                    view_show.setVisibility(View.INVISIBLE);
                    refreshLoadmoreLayout.setLoadmoreable(false);
//                    getNetWorker().installserviceList(null, "");
                    freshData();
                }
                refreshLoadmoreLayout.refreshSuccess();
                break;
            case INSTALLSERVICE_LIST:
                log_i("+++++++++++++++++第二次数据库");
                HemaArrayResult<Installservice> result2 = (HemaArrayResult<Installservice>) hemaBaseResult;
                ArrayList<Installservice> installservices = result2.getObjects();
                this.installservices.clear();
                this.installservices.addAll(installservices);
//                User user = DtywApplication.getInstance().getUser();
//                if (user==null)
//                refreshLoadmoreLayout.setLoadmoreable(false);
                String username2 = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");
                //登录
                if (!isNull(username2)) {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    getNetWorker().getCartgoodsnum(token);
                }
                freshData();

                break;
            case REPLY_LIST:
                HemaPageArrayResult<Reply> result3 = (HemaPageArrayResult<Reply>) hemaBaseResult;
                ArrayList<Reply> replies = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.replies.clear();
                    this.replies.addAll(replies);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (replies.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (replies.size() > 0)
                        this.replies.addAll(replies);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case GOODS_OPERATE:
                String keytype = hemaNetTask.getParams().get("keytype");
                ShouCang cang = new ShouCang(CommodityInforActivity.this);
                if ("2".equals(keytype)) {
                    cang.setOpen(2);
                    cang.show();
                    goodsGet.setCollect("2");
                } else {
                    cang.setOpen(1);
                    cang.show();
                    goodsGet.setCollect("1");
                }
                freshData();
                break;
            case GET_CARTGOODSNUM:
                log_i("购物车数量.....");
                HemaArrayResult<String> result4 = (HemaArrayResult<String>) hemaBaseResult;
                String num = result4.getObjects().get(0);
                if (num.equals("0"))
                    view_show.setVisibility(View.GONE);
                else {
                    view_show.setVisibility(View.VISIBLE);
                    view_show.setText(num);
                }
                refreshLoadmoreLayout.refreshSuccess();
                break;
            case CART_ADD:
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().getCartgoodsnum(token);
                Intent intent = new Intent();
                intent.setAction("hemaapp.dtyw.buy.car.data");
                sendBroadcast(intent);
                showTextDialog("添加购物车成功!");
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
            case INSTALLSERVICE_LIST:
            case REPLY_LIST:
            case ADDLISTALL_LIST:
            case GET_CARTGOODSNUM:
            case CART_ADD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_GET:
                refreshLoadmoreLayout.refreshFailed();
                showTextDialog("获取商品详情失败，请稍后重试");
                break;
            case INSTALLSERVICE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取安装服务失败，请稍后重试");
                break;
            case REPLY_LIST:
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取评论信息失败，请稍后重试");
                break;
            case ADDLISTALL_LIST:

                showTextDialog("获取地区信息失败，请稍后重试");

                break;
            case GET_CARTGOODSNUM:
                refreshLoadmoreLayout.refreshFailed();
                showTextDialog("获取购物车信息失败，请稍后重试");
                break;
            case CART_ADD:
                showTextDialog("添加购物车失败，请稍后重试");
                break;

        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new CommodityInforAdapter(mContext, this,
                    goodsGet, type, replies, installservices, id, cityName);

            adapter.setEmptyString("暂无数据");

            listview.setAdapter(adapter);
        } else {
            adapter.setGoodsGet(goodsGet);
            adapter.setInstallservices(installservices);
            adapter.setReplies(replies);
            adapter.setType(type);
            adapter.setCityName(cityName);
            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        go_top_image = (ImageView) findViewById(R.id.go_top_image);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        car_image = (FrameLayout) findViewById(R.id.car_image);
        view_show = (TextView) findViewById(R.id.view_show);
        add_car_text = (TextView) findViewById(R.id.add_car_text);
        go_money_text = (TextView) findViewById(R.id.go_money_text);

    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {

        view_show.setVisibility(View.INVISIBLE);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setImageResource(R.mipmap.share_img);
        title_text.setText("商品详情");
        //分享
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();
            }
        });
        //刷新
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                log_i("TTTTTTTTTTTTTTTTTTTTTTTTTTT" + type);
                page++;
                if (type.equals("2"))
                    refreshLoadmoreLayout.setLoadmoreable(false);
                else if (type.equals("1")) {
                    //安装接口
                    String username = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");
                    if (!isNull(username)) {
                        String token = DtywApplication.getInstance().getUser().getToken();
                        String address = cityName;
                        getNetWorker().installserviceList(token, address, id);
                    } else
                        refreshLoadmoreLayout.setLoadmoreable(false);
                } else
                    setReply();
            }
        });
        //      refreshLoadmoreLayout.setRefreshable(false);
        //跳转到头
        refreshLoadmoreLayout.setLoadmoreable(false);
        go_top_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview.setSelection(0);
                adapter.notifyDataSetInvalidated();
            }
        });
        //加入购物车
        add_car_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");
                //登录
                if (isNull(username)) {
                    Intent intent = new Intent(CommodityInforActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                //加入购物车
                else {
                    String token = DtywApplication.getInstance().getUser().getToken();
                    //规格组合id

                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < goodsGet.getPropery().size(); i++) {
                        for (int j = 0; j < goodsGet.getPropery().get(i).getValue().size(); j++) {
                            if (goodsGet.getPropery().get(i).getValue().get(j).isCheck()) {
                                buffer.append(goodsGet.getPropery().get(i).getValue().get(j).getId() + ",");
                            }
                        }

                    }
                    String keyid = null;
                    for (int i = 0; i < goodsGet.getPrice().size(); i++) {
                        if (goodsGet.getPrice().get(i).getPath().equals(buffer.substring(0, buffer.length() - 1)))
                            keyid = goodsGet.getPrice().get(i).getId();
                    }
                    //安装服务id
                    String anid = "0";
                    for (int i = 0; i < installservices.size(); i++) {
                        if (installservices.get(i).isCheck())
                            anid = installservices.get(i).getId();
                    }
                    getNetWorker().cartAdd(token, "1", id, keyid, anid);
                }
            }
        });
        //立即购买
        go_money_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");
                //登录
                if (isNull(username)) {
                    Intent intent = new Intent(CommodityInforActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    StringBuffer buffer = new StringBuffer();
                    StringBuffer peopery = new StringBuffer();
                    for (int i = 0; i < goodsGet.getPropery().size(); i++) {
                        for (int j = 0; j < goodsGet.getPropery().get(i).getValue().size(); j++) {
                            if (goodsGet.getPropery().get(i).getValue().get(j).isCheck()) {
                                buffer.append(goodsGet.getPropery().get(i).getValue().get(j).getId() + ",");
                                peopery.append(goodsGet.getPropery().get(i).getProperyname() + ":" + goodsGet.getPropery().get(i).getValue().get(j).getName() + "   ");
                            }
                        }

                    }
                    String keyid = "";
                    String value = "";
                    String stock = "0";
                    for (int i = 0; i < goodsGet.getPrice().size(); i++) {
                        if (goodsGet.getPrice().get(i).getPath().equals(buffer.substring(0, buffer.length() - 1))) {
                            keyid = goodsGet.getPrice().get(i).getPrice();
                            value = goodsGet.getPrice().get(i).getId();
                            stock = goodsGet.getPrice().get(i).getStock();
                        }
                    }
                    if (isNull(stock) || "0".equals(stock))
                    {
                        showTextDialog("库存不足");
                        return;
                    }
                    //安装服务id
                    String anid = "0";
                    String anvalue = "";
                    for (int i = 0; i < installservices.size(); i++) {
                        if (installservices.get(i).isCheck()) {
                            anid = installservices.get(i).getId();
                            anvalue = installservices.get(i).getPrice();
                        }
                    }
                    Intent intent = new Intent(mContext, DirectPaymentActivity.class);
                    intent.putExtra("good", goodsGet);
                    intent.putExtra("anid", anid);
                    intent.putExtra("goodId", id);
                    intent.putExtra("anvalue", anvalue);
                    intent.putExtra("price", keyid);
                    intent.putExtra("stock",stock);
                    intent.putExtra("peopery", String.valueOf(peopery));
                    // intent.putExtra("valueId",buffer.substring(0, buffer.length() - 1));
                    intent.putExtra("valueId", value);
                    intent.putExtra("installservice", installservices);
                    startActivity(intent);
                }

            }
        });
        //购物车
        car_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // XtomActivityManager.finishAll();
                Intent intent = new Intent(CommodityInforActivity.this, BuyCarActivity.class);

                startActivity(intent);
            }
        });
    }

    //评价列表
    public void setReply() {
        getNetWorker().replyList("1", id, String.valueOf(page));
    }

    /**
     * 安装服务
     */
    public void setCity() {
        if (DtywApplication.getInstance().getCityInfo() == null) {
            getNetWorker().addListAllList();
        } else
            showCity();
    }

    /**
     * 收藏
     */
    public void setShouCang() {
        String token = DtywApplication.getInstance().getUser().getToken();
        if (goodsGet.getCollect().equals("1"))
            getNetWorker().goodsOperate(token, "2", id);
        else
            getNetWorker().goodsOperate(token, "1", id);
    }

    /**
     * 选择详情网页，评论，安装服务
     *
     * @param /i
     * @param /j
     */
    public void setType(String type1) {
        type = type1;
        String username = XtomSharedPreferencesUtil.get(CommodityInforActivity.this, "username");

        if (!isNull(username) && type.equals("1")) {
            page = 0;
            String token = DtywApplication.getInstance().getUser().getToken();
            String address = DtywApplication.getInstance().getUser().getDistrict_name();
            getNetWorker().installserviceList(token, address, id);
            refreshLoadmoreLayout.setLoadmoreable(true);
        } else if (type.equals("2")) {
            refreshLoadmoreLayout.setLoadmoreable(false);
            freshData();
        } else if (type.equals("3")) {
            refreshLoadmoreLayout.setLoadmoreable(true);
            page = 0;
            setReply();
        } else {
            freshData();
            refreshLoadmoreLayout.setLoadmoreable(false);
        }
    }

    //更改规格
    public void setGuiGe(int i, int j) {
        for (int m = 0; m < goodsGet.getPropery().get(i).getValue().size(); m++) {
            if (j == m)
                goodsGet.getPropery().get(i).getValue().get(m).setCheck(true);
            else
                goodsGet.getPropery().get(i).getValue().get(m).setCheck(false);
        }
        freshData();
    }

    /**
     * 选择安装服务
     */
    public void setAnZhuang(int i) {
        for (int j = 0; j < installservices.size(); j++) {
            if (i == j) {
                if (installservices.get(j).isCheck())
                    installservices.get(j).setCheck(false);
                else
                    installservices.get(j).setCheck(true);
            } else
                installservices.get(j).setCheck(false);
        }
        freshData();
    }

    // 获取软件Logo文件地址
    private String getLogoImagePath() {
        String imagePath;
        try {
            String cachePath_internal = XtomFileUtil.getCacheDir(mContext)
                    + "/images/";// 获取缓存路径
            File dirFile = new File(cachePath_internal);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            imagePath = cachePath_internal + "icondd.png";
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
                Bitmap pic = BitmapFactory.decodeResource(
                        mContext.getResources(), R.mipmap.ic_launcher);
                FileOutputStream fos = new FileOutputStream(file);
                pic.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            imagePath = null;
        }
        log_i("imagePath=" + imagePath);
        return imagePath;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是微信
            handler.sendEmptyMessage(1);
        }
        if (platform.getName().equals(Wechat.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(2);
        }
        if (platform.getName().equals(QZone.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(3);
        }
        if (platform.getName().equals(SinaWeibo.NAME)) {// 判断成功的平台是不是微信朋友圈
            handler.sendEmptyMessage(4);
        }
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        arg2.printStackTrace();
        Message msg = new Message();
        msg.what = 6;
        msg.obj = arg2.getMessage();
        handler.sendMessage(msg);

    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(5);
    }

    public class ShouCang extends XtomObject {
        private Dialog mDialog;
        private TextView mTextView;
        private TextView shoucang_text2;
        private Runnable cancelRunnable = new Runnable() {

            @Override
            public void run() {
                cancel();
            }
        };

        public ShouCang(Context context) {
            mDialog = new Dialog(context, R.style.toast);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popwind_shoucang_view, null);
            mTextView = (TextView) view.findViewById(R.id.shoucang_text1);
            shoucang_text2 = (TextView) view.findViewById(R.id.shoucang_text2);
            mDialog.setCancelable(true);
            mDialog.setContentView(view);
            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    mTextView.removeCallbacks(cancelRunnable);
                }
            });
            mDialog.show();
        }

        public void setOpen(int i) {
            if (i == 1) {
                mTextView.setVisibility(View.VISIBLE);
                shoucang_text2.setVisibility(View.GONE);
            } else {
                mTextView.setVisibility(View.GONE);
                shoucang_text2.setVisibility(View.VISIBLE);
            }
        }

        public void setText(String text) {
            mTextView.setText(text);
        }

        public void setText(int textID) {
            mTextView.setText(textID);
        }

        public void show() {
            mDialog.show();
            mTextView.postDelayed(cancelRunnable, 2000);
        }

        public void cancel() {
            mDialog.cancel();
        }


    }

    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);

            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.dtyw.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            // city_text.setText(areaDialog.getText());
            cityName = areaDialog.getText();
            String token = DtywApplication.getInstance().getUser().getToken();
            page = 0;
            getNetWorker().installserviceList(token, cityName, id);
            //  cityName = city_text.getText().toString();
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
//            String[] cityid = areaDialog.getId().split(",");
//            homecity = cityid[1];
//            homecounty = cityid[2];
            areaDialog.cancel();
        }

    }

    //分享
    private class ShareHolder {
        TextView textView1_camera;
        TextView radiobutton3;
        TextView radiobutton1;
        TextView radiobutton2;
        TextView radiobutton0;
    }

    private void showSelect() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwind_share_view, null);
        shareHolder = new ShareHolder();
        shareHolder.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        shareHolder.radiobutton3 = (TextView) view.findViewById(R.id.radiobutton3);
        shareHolder.radiobutton1 = (TextView) view.findViewById(R.id.radiobutton1);
        shareHolder.radiobutton2 = (TextView) view.findViewById(R.id.radiobutton2);
        shareHolder.radiobutton0 = (TextView) view.findViewById(R.id.radiobutton0);
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        shareHolder.radiobutton0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QQ.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(Wechat.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(QZone.NAME);
                popupWindow.dismiss();
            }
        });
        shareHolder.radiobutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(SinaWeibo.NAME);
                popupWindow.dismiss();
            }
        });

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //   popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showShare(String platform) {
        if (oks == null) {
            oks = new OnekeyShare();
            oks.setTitleUrl(pathWX); // 标题的超链接
            oks.setTitle("梯配网");
            oks.setText(goodsGet.getName());
            oks.setImageUrl("");
            oks.setFilePath(pathWX);
          //  oks.setImageUrl(goodsGet.getImgurl());
            oks.setImagePath(getLogoImagePath());
            oks.setUrl(pathWX);
            oks.setSiteUrl(pathWX);
            oks.setCallback(this);
        }
        oks.setPlatform(platform);
        oks.show(mContext);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "空间分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "微博分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
    //显示图片
    public void showImage(String url)
    {
        ShowLargeImageView imageView = new ShowLargeImageView(CommodityInforActivity.this,listview);
        imageView.setImageURL(url);
        imageView.show();
    }
}
