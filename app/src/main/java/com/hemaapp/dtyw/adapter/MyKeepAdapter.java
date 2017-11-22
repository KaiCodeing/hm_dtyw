package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.activity.MyKeepActivity;
import com.hemaapp.dtyw.model.Collect;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/10/9.
 * 我的收藏
 */
public class MyKeepAdapter extends HemaAdapter {
    private ArrayList<Collect> collects;
    private String type;
    private MyKeepActivity activity;
    private Context mContext;

    public MyKeepAdapter(Context mContext, ArrayList<Collect> collects, String type, MyKeepActivity activity) {
        super(mContext);
        this.mContext = mContext;
        this.collects = collects;
        this.type = type;
        this.activity = activity;

    }

    public void setCollects(ArrayList<Collect> collects) {
        this.collects = collects;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean isEmpty() {
        return collects == null || collects.size() == 0;

    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : collects.size();

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
        if (isEmpty())
            return getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_keep_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }

    /**
     * item_my_keep_view
     */
    private class ViewHolder {
        LinearLayout go_layout;
        TextView commod_name;
        ImageView delete_check;
        ImageView chanp_img;
        LinearLayout view_star;
        TextView money_text;
        TextView buy_number;
    }

    private void findView(ViewHolder holder, View view) {
        holder.go_layout = (LinearLayout) view.findViewById(R.id.go_layout);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.delete_check = (ImageView) view.findViewById(R.id.delete_check);
        holder.chanp_img = (ImageView) view.findViewById(R.id.chanp_img);
        holder.view_star = (LinearLayout) view.findViewById(R.id.view_star);
        holder.money_text = (TextView) view.findViewById(R.id.money_text);
        holder.buy_number = (TextView) view.findViewById(R.id.buy_number);
    }

    private void setData(ViewHolder holder, int position) {
        final Collect collect = collects.get(position);

        //查看详情
        holder.go_layout.setTag(R.id.TAG, collect);
        holder.go_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(type)) {
                    Collect goods1 = (Collect) v.getTag(R.id.TAG);
                    Intent intent = new Intent(mContext, CommodityInforActivity.class);
                    intent.putExtra("id", goods1.getId());
                    mContext.startActivity(intent);
                }
                else
                {}

            }
        });
        //商品图片
        String path = collect.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.chanp_img, options);
        //商品名称
        if (!isNull(collect.getName()))
            holder.commod_name.setText(collect.getName());
        if (!isNull(collect.getPrice()))
            holder.money_text.setText(collect.getPrice());
        if (!isNull(collect.getSalses()))
            holder.buy_number.setText("销量:" + collect.getSalses());
        //星星
        holder.view_star.removeAllViews();
        int starNum;
        if (isNull(collect.getStarnum()))
            starNum = 0;
        else
            starNum = Integer.valueOf(collect.getStarnum());
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.star_add_view, null);
            ImageView star = (ImageView) view.findViewById(R.id.star_img_off);
            ImageView star_on = (ImageView) view.findViewById(R.id.star_img_on);
            if (i < starNum)
                star_on.setVisibility(View.GONE);
            else
                star.setVisibility(View.GONE);
            holder.view_star.addView(view);
        }
        //是否展示
        if (type.equals("0")) {
            holder.delete_check.setVisibility(View.GONE);
        } else
            holder.delete_check.setVisibility(View.VISIBLE);
        //判断是否选中
        if (collect.isCheck())
            holder.delete_check.setImageResource(R.mipmap.check_anzhuang_off);
        else
            holder.delete_check.setImageResource(R.mipmap.check_anzhuang_on);
        //选中的操作
        holder.delete_check.setTag(R.id.TAG, position);
        holder.delete_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = (int) v.getTag(R.id.TAG);
                for (int i = 0; i < collects.size(); i++) {
                    if (m == i) {
                        if (collects.get(i).isCheck())
                            collects.get(i).setCheck(false);
                        else
                            collects.get(i).setCheck(true);
                    }
                }
                activity.changeData();
                notifyDataSetChanged();
            }
        });
    }
}
