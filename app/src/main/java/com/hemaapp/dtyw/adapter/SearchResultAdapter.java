package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.model.Goods;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/20.
 * 搜索结果
 */
public class SearchResultAdapter extends HemaAdapter {
    private ArrayList<Goods> goodses ;
    private Context mContext;
    public SearchResultAdapter(Context mContext,ArrayList<Goods> goodses) {
        super(mContext);
        this.goodses = goodses;
        this.mContext = mContext;
    }

    @Override
    public boolean isEmpty() {
        return goodses==null || goodses.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:goodses.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty()) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_no_search_result, null);

            int width = parent.getWidth();
            int height = parent.getHeight();
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(width, height);
            view.setLayoutParams(params);
            return view;
        }
        if (convertView==null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_home_end_right,null);
            ViewHloder1 hloder1 = new ViewHloder1();
            findView2(hloder1,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,hloder1);

        }
        ViewHloder1 hloder1 = (ViewHloder1) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData2(hloder1,position);
        return convertView;
    }
    /**
     * 左边
     */
    private class ViewHloder1{
        ImageView commod_img;
        TextView commod_name;
        LinearLayout view_star;
        TextView money_text;
        TextView number_text;
        LinearLayout item_layout;
    }
    private void findView2(ViewHloder1 holder1,View view)
    {
        holder1.commod_img = (ImageView) view.findViewById(R.id.commod_img);
        holder1.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder1.view_star = (LinearLayout) view.findViewById(R.id.view_star);
        holder1.money_text = (TextView) view.findViewById(R.id.money_text);
        holder1.number_text = (TextView) view.findViewById(R.id.number_text);
        holder1.item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
    }
    private void setData2(ViewHloder1 hloder1,int position)
    {
        Goods goods = goodses.get(position);
        //查看详情
        hloder1.item_layout.setTag(R.id.TAG,goods);
        hloder1.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goods goods1 = (Goods) v.getTag(R.id.TAG);
                Intent intent = new Intent(mContext, CommodityInforActivity.class);
                intent.putExtra("id",goods1.getId());
                mContext.startActivity(intent);
            }
        });
        //商品图片
        String path = goods.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, hloder1.commod_img,options);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.star_add_view, null);
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
     * 没有数据的时候显示
     * item_no_search_result
     */
}
