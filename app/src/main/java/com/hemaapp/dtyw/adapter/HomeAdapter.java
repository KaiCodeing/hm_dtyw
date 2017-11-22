package com.hemaapp.dtyw.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hemaapp.dtyw.DtywUtil;
import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.activity.IssueDemandActivity;
import com.hemaapp.dtyw.activity.OnlyShangpActivity;
import com.hemaapp.dtyw.activity.ProductClassActivity;
import com.hemaapp.dtyw.activity.RequsetListActivity;
import com.hemaapp.dtyw.activity.WebviewActivity;
import com.hemaapp.dtyw.fragment.HomeFragment;
import com.hemaapp.dtyw.model.AdList;
import com.hemaapp.dtyw.model.Brands;
import com.hemaapp.dtyw.model.Bulletin;
import com.hemaapp.dtyw.model.Goods;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.DtywViewPager;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;
import xtom.frame.view.XtomListView;

/**
 * Created by lenovo on 2016/9/18.
 * 首页的adapter
 * adapter_home_start
 * adapter_home_end_right
 */
public class HomeAdapter extends HemaAdapter {
    private String type;
    private ArrayList<AdList> adLists;
    private ArrayList<Bulletin> bulletins;
    private ArrayList<Goods> goodses;
    private ArrayList<Brands> brandses;
    private XtomListView listView;
    private HomeFragment mContext;
    private TopAdAdapter adAdapter;
    private ViewSwitcher.ViewFactory viewFactory;
    private ViewSwitcher viewSwitcher;
    private Handler handler;
    private String url;
    public HomeAdapter(HomeFragment mContext, String type, ArrayList<AdList> adLists, ArrayList<Bulletin> bulletins,
                       ArrayList<Goods> goodses, ArrayList<Brands> brandses, XtomListView listView,String url) {
        super(mContext);
        this.mContext = mContext;
        this.adLists = adLists;
        this.bulletins = bulletins;
        this.goodses = goodses;
        this.brandses = brandses;
        this.listView = listView;
        this.type = type;
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAdLists(ArrayList<AdList> adLists) {
        this.adLists = adLists;
    }

    public void setBrandses(ArrayList<Brands> brandses) {
        this.brandses = brandses;
    }

    public void setBulletins(ArrayList<Bulletin> bulletins) {
        this.bulletins = bulletins;
    }

    public void setGoodses(ArrayList<Goods> goodses) {
        this.goodses = goodses;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isEmpty() {
        if ("1".equals(type))
            return brandses == null || brandses.size() == 0;
        else
            return goodses == null || goodses.size() == 0;

    }

    @Override
    public int getCount() {
        if ("1".equals(type))
            return isEmpty() ? 1 : brandses.size() + 1;
        else
            return isEmpty() ? 1 : goodses.size() + 1;

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
    public int getItemViewType(int position) {
        //广告页
        if (position == 0)
            return 1;
        else {
            if ("1".equals(type)) {
                //品牌推荐
                return 2;
            } else {
                //热销推荐
                return 3;
            }
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.adapter_home_start, null);
                    ViewHolder holder = new ViewHolder();
                    findView1(holder, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, holder);

                    break;
                case 2:
                    convertView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.adapter_home_end_left, null);
                    ViewHolder3 hloder3 = new ViewHolder3();
                    findView3(hloder3, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, hloder3);

                    break;
                case 3:
                    convertView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.adapter_home_end_right, null);
                    ViewHloder1 hloder1 = new ViewHloder1();
                    findView2(hloder1, convertView);
                    convertView.setTag(R.id.TAG_VIEWHOLDER, hloder1);

                    break;
            }
        }
        switch (type) {
            case 1:
                ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData(holder, position);
                break;
            case 2:
                ViewHolder3 holder3 = (ViewHolder3) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData3(holder3, position);
                break;
            case 3:
                ViewHloder1 hloder1 = (ViewHloder1) convertView.getTag(R.id.TAG_VIEWHOLDER);
                setData2(hloder1, position);
                break;
        }
        return convertView;
    }

    /**
     * 头
     */
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
        ImageView zhineng_image;
        ImageView demand_image;
        ImageView demand_fa_image;
    }

    private void findView1(ViewHolder holder, View view) {
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
        holder.zhineng_image = (ImageView) view.findViewById(R.id.zhineng_image);
        holder.demand_image = (ImageView) view.findViewById(R.id.demand_image);
        holder.demand_fa_image = (ImageView) view.findViewById(R.id.demand_fa_image);
        holder.radiogroup_select = (RadioGroup) view.findViewById(R.id.radiogroup_select);
        viewSwitcher = holder.index_viewSwitcher;
    }

    private void setData(final ViewHolder holder, int position) {
        /**
         * 广告页
         */
        adAdapter = new TopAdAdapter(mContext, holder.radiogroup, holder.vp_top, adLists);
        holder.adviewpager.setAdapter(adAdapter);
        holder.adviewpager.setOnPageChangeListener(new PageChangeListener());
        /**
         * 公告
         */
        holder.index_viewSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.in));
        holder.index_viewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext.getActivity(), R.anim.out));

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
                    View itemView = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.item_index_viewswitcher, null);
                    TextView tv = ((TextView) itemView.findViewById(R.id.item_index_vs_tv1));
                    // log_d("position==="+position);
                    tv.setText(bulletins.get(0).getName());
                    itemView.setTag(bulletins.get(0));
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bulletin aa = (Bulletin) v.getTag();
                            // TODO Auto-generated method stub
//                                Intent intent=new Intent(getActivity(),JpDetailEndActivity.class);
//                                intent.putExtra("goodid", ads.get(position).getNum_id());
//                                startActivity(intent);
                            if (aa.getJumptype().equals("1")) {
                                Intent intent = new Intent(mContext.getActivity(), CommodityInforActivity.class);
                                intent.putExtra("id", aa.getJumpid());
                                mContext.getActivity().startActivity(intent);

                            }
                            //外部链接
                            else if (aa.getJumptype().equals("2")) {
                                String sub = aa.getJumpurl().substring(0, 7);
                                Uri uri;
                                if (sub.equals("http://")) {
                                    uri = Uri.parse(aa.getJumpurl());
                                } else {
                                    uri = Uri.parse("http://" + aa.getJumpurl());
                                }

                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                mContext.getActivity().startActivity(it);
                            }
                            //图文广告
                            else if (aa.getJumptype().equals("3")) {
                                Intent intent = new Intent(mContext.getActivity(),
                                        WebviewActivity.class);
                                intent.putExtra("id", aa.getId());
                                intent.putExtra("type", "7");
                                //	intent.putExtra("id", aa.getId());
                                mContext.getActivity().startActivity(intent);
                            }
                        }
                    });
                    return itemView;
                }
            };
            viewSwitcher.setFactory(viewFactory);
            handler.sendEmptyMessageDelayed(1, 4000);
        }
        /**
         * 四个按钮
         */
        //品牌分类
        holder.pinpai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isNull(XtomSharedPreferencesUtil.get(mContext.getActivity(),"username")))
//                {
//                    Intent intent = new Intent(mContext.getActivity(), LoginActivity.class);
//                    mContext.getActivity().startActivity(intent);
//                }
//                else {
                Intent intent = new Intent(mContext.getActivity(), ProductClassActivity.class);
                mContext.getActivity().startActivity(intent);
//                }
            }
        });
        //所有产品
        holder.chanping_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getActivity(), BrandActivity.class);
                intent.putExtra("typekey", "5");
                intent.putExtra("brandName", "所有产品");
                mContext.getActivity().startActivity(intent);
            }
        });
        //二手配件
        holder.ershou_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getActivity(), BrandActivity.class);
                intent.putExtra("typekey", "4");
                intent.putExtra("brandName", "二手配件");
                mContext.getActivity().startActivity(intent);
            }
        });
        //平台招商
        holder.pintai_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getActivity(), WebviewActivity.class);
                intent.putExtra("type", "2");
                mContext.getActivity().startActivity(intent);
            }
        });
        //智能物联
        holder.zhineng_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext.getActivity(), WebviewActivity.class);
//                intent.putExtra("type","9");
//                intent.putExtra("url",url);
//                mContext.getActivity().startActivity(intent);
                Intent intent = new Intent();
//Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                mContext.getActivity().startActivity(intent);
            }
        });
        //需求汇总
        holder.demand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getActivity(), RequsetListActivity.class);
                intent.putExtra("keytype","1");
                mContext.getActivity().startActivity(intent);
            }
        });
        //需求发布
        holder.demand_fa_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNull(XtomSharedPreferencesUtil.get(mContext.getActivity(),"username")))
                {
                    mContext.showTextDialog("请先登录!");
                    return;
                }
                Intent intent = new Intent(mContext.getActivity(), IssueDemandActivity.class);
                mContext.getActivity().startActivity(intent);
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
                        mContext.setSelectData("1");
                        holder.view1.setVisibility(View.VISIBLE);
                        holder.view2.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.button2:// 名师榜
                        mContext.setSelectData("2");
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

    /**
     * 左边
     */
    private class ViewHloder1 {
        ImageView commod_img;
        TextView commod_name;
        LinearLayout view_star;
        TextView money_text;
        TextView number_text;
        LinearLayout item_layout;
    }

    private void findView2(ViewHloder1 holder1, View view) {
        holder1.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder1.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder1.view_star = (LinearLayout) view.findViewById(R.id.view_star);
        holder1.money_text = (TextView) view.findViewById(R.id.money_text);
        holder1.number_text = (TextView) view.findViewById(R.id.number_text);
        holder1.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
    }

    private void setData2(ViewHloder1 hloder1, int position) {
        final Goods goods = goodses.get(position - 1);
        //查看详情
        hloder1.item_layout.setTag(R.id.TAG, goods);
        hloder1.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goods goods1 = (Goods) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext.getActivity(), CommodityInforActivity.class);
                intent.putExtra("id", goods1.getId());
                mContext.getActivity().startActivity(intent);

            }
        });
        //商品图片
        String path = goods.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, hloder1.commod_img, options);
        //商品名称
        if (!isNull(goods.getName()))
            hloder1.commod_name.setText(goods.getName());
        if (!isNull(goods.getPrice()))
            hloder1.money_text.setText(goods.getPrice());
        if (!isNull(goods.getSalses()))
            hloder1.number_text.setText("销量:" + goods.getSalses());
        //星星
        hloder1.view_star.removeAllViews();
        int starNum;
        if (isNull(goods.getStarnum()))
            starNum = 0;
        else
            starNum = Integer.valueOf(goods.getStarnum());
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(mContext.getActivity()).inflate(R.layout.star_add_view, null);
            ImageView star = (ImageView) view.findViewById(R.id.star_img_off);
            ImageView star_on = (ImageView) view.findViewById(R.id.star_img_on);
            if (i < starNum)
                star_on.setVisibility(View.GONE);
            else
                star.setVisibility(View.GONE);
            hloder1.view_star.addView(view);
        }
    }

    /**
     * 右边
     */
    private class ViewHolder3 {
        ImageView show_view;
        TextView number_commod;
        TextView name_commod;
    }

    private void findView3(ViewHolder3 holder3, View view) {
        holder3.name_commod = (TextView) view.findViewById(R.id.name_commod);
        holder3.number_commod = (TextView) view.findViewById(R.id.number_commod);
        holder3.show_view = (ImageView) view.findViewById(R.id.show_view);
    }

    private void setData3(ViewHolder3 holder3, int position) {
        Brands brand = brandses.get(position - 1);
        //商品图片
        String path = brand.getImgurl();
        double width= DtywUtil.getScreenWidth(mContext.getActivity());
        double height=width/8*5;
        ViewGroup.LayoutParams params1 = holder3.show_view.getLayoutParams();
        params1.width = (int) width;
        params1.height=(int) height;
        holder3.show_view.setLayoutParams(params1);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder3.show_view, options);
        /**
         * 名称
         */
        if (!isNull(brand.getName()))
            holder3.name_commod.setText(brand.getName());
        /**
         * 详细信息
         */
        holder3.show_view.setTag(R.id.TAG, brand);
        holder3.show_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Brands brands1 = (Brands) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext.getActivity(), OnlyShangpActivity.class);
                intent.putExtra("briandId", brands1.getId());
                intent.putExtra("briandName", brands1.getName());
                intent.putExtra("type", "1");
                mContext.startActivity(intent);
            }
        });
    }
}
