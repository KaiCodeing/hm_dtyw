package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywUtil;
import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.activity.LoginActivity;
import com.hemaapp.dtyw.model.GoodsGet;
import com.hemaapp.dtyw.model.Installservice;
import com.hemaapp.dtyw.model.Reply;
import com.hemaapp.dtyw.model.User;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.DtywGridView;
import com.hemaapp.dtyw.view.DtywViewPager;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.dtyw.view.MyWebView;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.model.Image;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/18.
 * 商品详情 (没有写layout)
 * adapter_infor_commodity
 */
public class CommodityInforAdapter extends HemaAdapter {
    private Context mContext;
    private CommodityInforActivity activity;
    private GoodsGet goodsGet;
    private String type;//1安装 2//详情3//评价
    private ArrayList<Reply> replies;
    private ArrayList<Installservice> installservices;
    private String id;
    private CommidAdAdapter adAdapter;
    private String cityName;

    public CommodityInforAdapter(Context mContext, CommodityInforActivity activity, GoodsGet goodsGet, String type,
                                 ArrayList<Reply> replies, ArrayList<Installservice> installservices, String id, String cityName) {
        super(mContext);
        this.mContext = mContext;
        this.activity = activity;
        this.goodsGet = goodsGet;
        this.type = type;
        this.replies = replies;
        this.installservices = installservices;
        this.id = id;
        this.cityName = cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setGoodsGet(GoodsGet goodsGet) {
        this.goodsGet = goodsGet;
    }

    public void setInstallservices(ArrayList<Installservice> installservices) {
        this.installservices = installservices;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isEmpty() {
        if (type.equals("1")) {
            return installservices == null || installservices.size() == 0;
        } else if (type.equals("2")) {
            return true;
        } else {
            return replies == null || replies.size() == 0;
        }
    }

    @Override
    public int getCount() {
        if (type.equals("1")) {
            return isEmpty() ? 2 : 1 + (installservices.size() >= 3 ? 3 : installservices.size());
        } else if (type.equals("2")) {
            return 2;
        } else {
            return isEmpty() ? 2 : replies.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        //头
        if (position == 0)
            return 1;
        else {
            if (type.equals("1")) {
                return 2;
            } else if (type.equals("2")) {
                return 3;
            } else
                return 4;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = LayoutInflater.from(activity).inflate(R.layout.item_commodity_infor_top, null);
                    ViewHolder holder = new ViewHolder();
                    findView(holder, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
                    break;
                case 3:
                    convertView = LayoutInflater.from(activity).inflate(R.layout.item_webview_, null);
                    ViewHolder3 holder3 = new ViewHolder3();
                    findView3(holder3, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder3);
                    break;
                case 2:
                    convertView = LayoutInflater.from(activity).inflate(R.layout.item_commod_infor_view1, null);
                    ViewHolder2 holder2 = new ViewHolder2();
                    findView2(holder2, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder2);
                    break;
                case 4:
                    convertView = LayoutInflater.from(activity).inflate(R.layout.item_commod_infor_view3, null);
                    ViewHolder4 holder4 = new ViewHolder4();
                    findView4(holder4, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder4);
                    break;
            }
        }
        switch (type) {
            case 1:
                ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData(holder, position);
                break;
            case 2:
                ViewHolder2 holder2 = (ViewHolder2) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData2(holder2, position);
                break;
            case 3:
                ViewHolder3 holder3 = (ViewHolder3) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData3(holder3, position);
                break;
            case 4:
                ViewHolder4 holder4 = (ViewHolder4) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData(holder4, position);
                break;
        }
        return convertView;
    }

    /**
     * 顶部
     */
    private class ViewHolder {
        DtywViewPager adviewpager;
        RadioGroup radiogroup;
        TextView commod_name;
        TextView number_money;
        TextView xiaoliang_text;
        TextView kc_text;
        TextView yf_text;
        ImageView shoucang_img;
        LinearLayout add_guige_view;
        TextView miaoshu_text;
        RadioButton button1;
        RadioButton button2;
        RadioButton button3;
        View view1;
        View view2;
        FrameLayout vp_top;
        View view3;
        RadioGroup radiogroup_san;
    }

    private void findView(ViewHolder holder, View view) {
        holder.adviewpager = (DtywViewPager) view.findViewById(R.id.adviewpager);
        holder.radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.number_money = (TextView) view.findViewById(R.id.number_money);
        holder.xiaoliang_text = (TextView) view.findViewById(R.id.xiaoliang_text);
        holder.kc_text = (TextView) view.findViewById(R.id.kc_text);
        holder.yf_text = (TextView) view.findViewById(R.id.yf_text);
        holder.shoucang_img = (ImageView) view.findViewById(R.id.shoucang_img);
        holder.add_guige_view = (LinearLayout) view.findViewById(R.id.add_guige_view);
        holder.miaoshu_text = (TextView) view.findViewById(R.id.miaoshu_text);
        holder.button1 = (RadioButton) view.findViewById(R.id.button1);
        holder.button2 = (RadioButton) view.findViewById(R.id.button2);
        holder.button3 = (RadioButton) view.findViewById(R.id.button3);
        holder.view1 = view.findViewById(R.id.view1);
        holder.view2 = view.findViewById(R.id.view2);
        holder.view3 = view.findViewById(R.id.view3);
        holder.vp_top = (FrameLayout) view.findViewById(R.id.vp_top);
        holder.radiogroup_san = (RadioGroup) view.findViewById(R.id.radiogroup_san);
    }

    private void setData(final ViewHolder holder, int position) {
        //add_guige_view
        //商品详情广告页
        double width= DtywUtil.getScreenWidth(activity);
        double height=width/1*1;
        ViewGroup.LayoutParams params1 =  holder.adviewpager.getLayoutParams();
        params1.width = (int) width;
        params1.height=(int) height;
        holder.adviewpager.setLayoutParams(params1);
        adAdapter = new CommidAdAdapter(activity, holder.radiogroup, holder.vp_top, goodsGet.getImgItems());
        holder.adviewpager.setAdapter(adAdapter);
        holder.adviewpager.setOnPageChangeListener(new PageChangeListener());
        if (!isNull(goodsGet.getName())) {
            holder.commod_name.setText(goodsGet.getName());
        }
        //收藏
        if (goodsGet.getCollect().equals("1")) {
            holder.shoucang_img.setImageResource(R.mipmap.yishoucang_img);
        } else
            holder.shoucang_img.setImageResource(R.mipmap.weishoucang_img);
        //判断是否登录
        holder.shoucang_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = XtomSharedPreferencesUtil.get(activity, "username");
                //登录
                if (isNull(username)) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                } else {
                    activity.setShouCang();
                }
            }
        });
        //销量
        if (!isNull(goodsGet.getSalses()))
            holder.xiaoliang_text.setText("销量" + goodsGet.getSalses());
        if (!isNull(goodsGet.getShipment()))
            holder.yf_text.setText("运费" + goodsGet.getShipment());
        //价格
        if (goodsGet.getPropery() == null || goodsGet.getPropery().size() == 0) {
            holder.number_money.setText(goodsGet.getPrice().get(0).getPrice());
        } else {
            String title = "";
            for (int i = 0; i < goodsGet.getPropery().size(); i++) {
                for (int j = 0; j < goodsGet.getPropery().get(i).getValue().size(); j++) {
                    if (goodsGet.getPropery().get(i).getValue().get(j).isCheck()) {
                        title = title + goodsGet.getPropery().get(i).getValue().get(j).getId() + ",";
                    }
                }

            }
            //获取价格
            String name = title.substring(0, title.length() - 1);
            for (int i = 0; i < goodsGet.getPrice().size(); i++) {
                if (goodsGet.getPrice().get(i).getPath().equals(name)) {
                    holder.number_money.setText(goodsGet.getPrice().get(i).getPrice());
                    holder.kc_text.setText("库存" + goodsGet.getPrice().get(i).getStock());
                }
            }
        }
        //填写规格
        holder.add_guige_view.removeAllViews();
        for (int i = 0; i < goodsGet.getPropery().size(); i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.add_guige_view, null);
            TextView guige_name_text = (TextView) view.findViewById(R.id.guige_name_text);
            LinearLayout city_layout = (LinearLayout) view.findViewById(R.id.city_layout);
            FlowLayout add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
            TextView guige_content_text = (TextView) view.findViewById(R.id.guige_content_text);

            guige_content_text.setVisibility(View.GONE);
            add_view_guige.setVisibility(View.VISIBLE);
            add_view_guige.removeAllViews();
            guige_name_text.setText(goodsGet.getPropery().get(i).getProperyname());
            for (int j = 0; j < goodsGet.getPropery().get(i).getValue().size(); j++) {
                View view1 = LayoutInflater.from(activity).inflate(R.layout.add_guige_view_text, null);
                TextView guige_text = (TextView) view1.findViewById(R.id.guige_text);
                if (goodsGet.getPropery().get(i).getValue().get(j).isCheck()) {
                    guige_text.setTextColor(activity.getResources().getColor(R.color.white));
                    guige_text.setBackgroundResource(R.drawable.buy_commonid_bg);
                } else {
                    guige_text.setTextColor(activity.getResources().getColor(R.color.color_text));
                    guige_text.setBackgroundResource(R.drawable.select_check_shai);
                }
                guige_text.setText(goodsGet.getPropery().get(i).getValue().get(j).getName());
                //规格点击事件
                guige_text.setTag(R.id.TAG, i);
                guige_text.setTag(R.id.TAG_VIEWHOLDER, j);
                guige_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int a = (int) v.getTag(R.id.TAG);
                        int b = (int) v.getTag(R.id.TAG_VIEWHOLDER);
                        activity.setGuiGe(a, b);
                    }
                });
                add_view_guige.addView(view1);
            }
            holder.add_guige_view.addView(view);
        }
        //产地
        View view = LayoutInflater.from(activity).inflate(R.layout.add_guige_view, null);
        TextView guige_name_text = (TextView) view.findViewById(R.id.guige_name_text);
        FlowLayout add_view_guige = (FlowLayout) view.findViewById(R.id.add_view_guige);
        TextView guige_content_text = (TextView) view.findViewById(R.id.guige_content_text);
        LinearLayout city_layout = (LinearLayout) view.findViewById(R.id.city_layout);
        add_view_guige.setVisibility(View.GONE);
        city_layout.setVisibility(View.GONE);
        guige_name_text.setText("产地");
        if (!isNull(goodsGet.getPlace()))
            guige_content_text.setText(goodsGet.getPlace());
        holder.add_guige_view.addView(view);
        //描述
        holder.miaoshu_text.setVisibility(View.VISIBLE);
        if (!isNull(goodsGet.getDescription()))
            holder.miaoshu_text.setText(goodsGet.getDescription());
        //选择评论，详情，安装地址
        holder.radiogroup_san.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        if (DtywApplication.getInstance().getUser()!=null)
                        {
                            activity.setType("1");
                            holder.view1.setVisibility(View.VISIBLE);
                            holder.view2.setVisibility(View.INVISIBLE);
                            holder.view3.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Intent intent = new Intent(activity,LoginActivity.class);
                            activity.startActivity(intent);
                            if ("2".equals(type))
                                holder.button2.setChecked(true);
                            else
                                holder.button3.setChecked(true);

                        }
                        break;
                    case R.id.button2:
                        activity.setType("2");
                        holder.view2.setVisibility(View.VISIBLE);
                        holder.view1.setVisibility(View.INVISIBLE);
                        holder.view3.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.button3:
                        activity.setType("3");
                        holder.view3.setVisibility(View.VISIBLE);
                        holder.view2.setVisibility(View.INVISIBLE);
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

    /**
     * item_commod_infor_view1
     * 安装服务
     */
    private class ViewHolder2 {
        ImageView check_anzhuang;
        TextView fuwu_name;//服务店名称
        TextView fuwu_city_text;//服务店地址
        TextView fuwu_phone_text;//服务电话
        TextView money_anzhuang_text;//服务费用
        LinearLayout view_layout;
        LinearLayout select_city_layout;
        TextView city_name_text;
    }

    private void findView2(ViewHolder2 holder2, View view) {
        holder2.check_anzhuang = (ImageView) view.findViewById(R.id.check_anzhuang);
        holder2.fuwu_name = (TextView) view.findViewById(R.id.fuwu_name);
        holder2.fuwu_city_text = (TextView) view.findViewById(R.id.fuwu_city_text);
        holder2.fuwu_phone_text = (TextView) view.findViewById(R.id.fuwu_phone_text);
        holder2.money_anzhuang_text = (TextView) view.findViewById(R.id.money_anzhuang_text);
        holder2.view_layout = (LinearLayout) view.findViewById(R.id.view_layout);
        holder2.select_city_layout = (LinearLayout) view.findViewById(R.id.select_city_layout);
        holder2.city_name_text = (TextView) view.findViewById(R.id.city_name_text);
    }

    private void setData2(ViewHolder2 holder2, int position) {

        if (installservices == null || installservices.size() == 0) {
            holder2.view_layout.setVisibility(View.GONE);
            holder2.city_name_text.setText("请选择地址");
            User user = DtywApplication.getInstance().getUser();
            if (user == null)
                holder2.select_city_layout.setVisibility(View.GONE);
            else {
                holder2.select_city_layout.setVisibility(View.VISIBLE);
                holder2.city_name_text.setText(cityName);
            }
        } else {
            holder2.view_layout.setVisibility(View.VISIBLE);
            Installservice installservice = installservices.get(position - 1);
            if (position == 1) {
                holder2.select_city_layout.setVisibility(View.VISIBLE);
                holder2.city_name_text.setText(DtywApplication.getInstance().getUser().getDistrict_name());
            } else
                holder2.select_city_layout.setVisibility(View.GONE);
            //店名称
            holder2.fuwu_name.setText(installservice.getName());
            holder2.fuwu_city_text.setText(installservice.getAddress());
            holder2.fuwu_phone_text.setText(installservice.getPhone());
            holder2.money_anzhuang_text.setText(installservice.getPrice());
            //是否选中
            if (installservice.isCheck()) {
                holder2.check_anzhuang.setImageResource(R.mipmap.check_anzhuang_off);
            } else
                holder2.check_anzhuang.setImageResource(R.mipmap.check_anzhuang_on);
            holder2.view_layout.setTag(R.id.TAG, position - 1);
            //选择
            holder2.view_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag(R.id.TAG);
                    activity.setAnZhuang(i);
                }
            });
            holder2.city_name_text.setText(cityName);
        }
//        if (cityName != null) {
//            holder2.view_layout.setVisibility(View.GONE);
//
//        }
        //选择地点
        holder2.select_city_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCity();
            }
        });
    }

    /**
     * 网页
     * activity_webview
     */
    private class ViewHolder3 {
        MyWebView webview_aboutwe;
        View view_title;
    }

    private void findView3(ViewHolder3 holder3, View view) {
        holder3.webview_aboutwe = (MyWebView) view.findViewById(R.id.webview_aboutwe);
        holder3.view_title = view.findViewById(R.id.view_title);
    }

    private void setData3(ViewHolder3 holder3, int position) {
        holder3.view_title.setVisibility(View.GONE);
//        WebSettings settings = holder3.webview_aboutwe.getSettings();
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
    //    DtywUtil.initWebview(holder3.webview_aboutwe);
        String sys_web_service = DtywApplication.getInstance().getSysInitInfo()
                .getSys_web_service();
        String path = sys_web_service + "webview/parm/goods/id/" + id;
        log_i("++++++++++++++++++++++"+path);
     //   holder3.webview_aboutwe.setWebViewClient(new MyWebViewClient(mContext, holder3.webview_aboutwe));
     //   holder3.webview_aboutwe.addJavascriptInterface(new JavascriptInterface(mContext),"imagelistner");
        holder3.webview_aboutwe.loadUrl(path);
        holder3.webview_aboutwe.setOnImageClickListener(new MyWebView.OnImageClickListener() {
            @Override
            public void clickImage(String imageUrl) {
                activity.showImage(imageUrl);
            }
        });
     //   holder3.webview_aboutwe.getSettings().setJavaScriptEnabled(true);
    }
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            System.out.println(img);
            log_i("-------------------"+img);
            Image image = new Image("","","","",img,"","");
            Intent intent = new Intent();
//            intent.putExtra("image", image);
            ArrayList<Image> images = new ArrayList<>();
            images.add(image);
            intent.putExtra("position", 1);
            intent.putExtra("images", images);
            intent.putExtra("titleAndContentVisible", false);
            intent.setClass(context, ShowLargePicActivity.class);
            context.startActivity(intent);
            System.out.println(img);
        }
    }
    /**
     * 评论
     */
    private class ViewHolder4 {
        TextView pl_number_text;
        ImageView user_img;
        TextView username_text;
        TextView content_time_text;
        TextView content_word_text;
        DtywGridView pl_gridview;
        LinearLayout select_city_layout;
        LinearLayout add_star_view;
        LinearLayout add_star_view_top;

    }

    private void findView4(ViewHolder4 holder4, View view) {
        holder4.user_img = (ImageView) view.findViewById(R.id.user_img);
        holder4.pl_number_text = (TextView) view.findViewById(R.id.pl_number_text);
        holder4.username_text = (TextView) view.findViewById(R.id.username_text);
        holder4.content_time_text = (TextView) view.findViewById(R.id.content_time_text);
        holder4.content_word_text = (TextView) view.findViewById(R.id.content_word_text);
        holder4.pl_gridview = (DtywGridView) view.findViewById(R.id.pl_gridview);
        holder4.select_city_layout = (LinearLayout) view.findViewById(R.id.select_city_layout);
        holder4.add_star_view = (LinearLayout) view.findViewById(R.id.add_star_view);
        holder4.add_star_view_top = (LinearLayout) view.findViewById(R.id.add_star_view_top);
    }

    private void setData(ViewHolder4 holder4, int position) {
        //评论数
        if (isNull(goodsGet.getReplycount()))
            holder4.pl_number_text.setText("评论（0）");
        else
            holder4.pl_number_text.setText("评论（" + goodsGet.getReplycount() + "）");
        if (position == 1) {
            holder4.select_city_layout.setVisibility(View.VISIBLE);
            holder4.add_star_view_top.removeAllViews();
            int starNumber = Integer.valueOf(goodsGet.getReplytype());
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(activity).inflate(R.layout.star_add_view, null);
                ImageView star = (ImageView) view.findViewById(R.id.star_img_off);
                ImageView star_on = (ImageView) view.findViewById(R.id.star_img_on);
                if (i < starNumber)
                    star_on.setVisibility(View.GONE);
                else
                    star.setVisibility(View.GONE);
                holder4.add_star_view_top.addView(view);
            }
        } else
            holder4.select_city_layout.setVisibility(View.GONE);
        if (replies == null || replies.size() == 0) {

        } else {
            Reply reply = replies.get(position - 1);
            //头像
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.user_img)
                    .showImageForEmptyUri(R.mipmap.user_img)
                    .showImageOnFail(R.mipmap.user_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance().displayImage(reply.getAvatar(), holder4.user_img, options);
            //名称
            holder4.username_text.setText(reply.getNickname());
            //时间
            holder4.content_time_text.setText(reply.getRegdate());
            //内容
            holder4.content_word_text.setText(reply.getContent());
            //照片
            if (reply.getImgItems() == null || reply.getImgItems().size() == 0)
                holder4.pl_gridview.setVisibility(View.GONE);
            else {
                holder4.pl_gridview.setVisibility(View.VISIBLE);
                int a = reply.getImgItems().size();
                BlogReplyImgAdapter imgAdapter = new BlogReplyImgAdapter(mContext,
                        reply.getImgItems());
                holder4.pl_gridview.setAdapter(imgAdapter);
            }
            //星星
            holder4.add_star_view.removeAllViews();
            int starNum;
            if (isNull(reply.getReplytype()))
                starNum = 0;
            else
                starNum = Integer.valueOf(reply.getReplytype());
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(activity).inflate(R.layout.star_add_view, null);
                ImageView star = (ImageView) view.findViewById(R.id.star_img_off);
                ImageView star_on = (ImageView) view.findViewById(R.id.star_img_on);
                if (i < starNum)
                    star_on.setVisibility(View.GONE);
                else
                    star.setVisibility(View.GONE);
                holder4.add_star_view.addView(view);
            }
        }
        //总的评论数

    }
}
