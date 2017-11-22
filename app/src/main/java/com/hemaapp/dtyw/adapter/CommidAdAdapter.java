package com.hemaapp.dtyw.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.dtyw.DtywUtil;
import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.model.Image;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * Created by lenovo on 2016/10/18.
 * 商品详情广告页
 */
public class CommidAdAdapter extends PagerAdapter {
    private CommodityInforActivity activity;
    private RadioGroup mIndicator;
    private View view;
    private int size;
    private XtomImageWorker imgWorker;
    private ArrayList<Image> indexads;

    public CommidAdAdapter(CommodityInforActivity activity, RadioGroup mIndicator,
                        View view, ArrayList<Image> indexads) {
        this.activity = activity;
        this.mIndicator = mIndicator;
        this.view = view;
        this.indexads = indexads;
        imgWorker = new XtomImageWorker(activity);
        init();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (indexads==null ||indexads.size()==0)
            return 0;
        else
        return indexads.size();
    }

    private void init() {
        float density = activity.getResources().getDisplayMetrics().density;
        size = (int) (density * 8);
        mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
        mIndicator.removeAllViews();
        if (getCount() > 0)
            for (int i = 0; i < getCount(); i++) {
                RadioButton button = new RadioButton(activity);
                button.setId(i);
                button.setClickable(false);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        size, size);
                params.leftMargin = (int) (3 * density);
                params.rightMargin = (int) (3 * density);
                if (i == 0)
                    button.setChecked(true);
                button.setButtonDrawable(android.R.color.transparent);
                button.setBackgroundResource(R.drawable.indicator_show);
                mIndicator.addView(button, params);
            }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void notifyDataSetChanged() {
        init();
        super.notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mView;
        final Image ad = indexads.get(position);
        if (container.getChildAt(position) == null) {
            mView = new ImageView(activity);


            mView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

//            mView.setLayoutParams(params);
          //  LinearLayout.LayoutParams params1 =  mView.getLayoutParams();
            Log.i("ssssssssss",mView.toString());
            double width= DtywUtil.getScreenWidth(activity);
            double height=width/1*1;
            params.width = (int) width;
            params.height=(int) height;
            mView.setLayoutParams(params);
            container.addView(mView, position);
            mView.setImageResource(R.mipmap.default_bg2);
            try {
                XtomImageTask task = new XtomImageTask(mView, new URL(
                        ad.getImgurl()), activity);
                imgWorker.loadImage(task);
            } catch (MalformedURLException e) {
               // mView.setImageBitmap(null);
                mView.setImageResource(R.mipmap.default_bg2);
            }
            mView.setTag(R.id.TAG, position);
            mView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    if (DtywApplication.getInstance().getUser() == null)
//                        return;
                    int p = (int) v.getTag(R.id.TAG);
                    Intent it = new Intent(activity, ShowLargePicActivity.class);
                    it.putExtra("position", p);
                    it.putExtra("images", indexads);
                    it.putExtra("titleAndContentVisible", false);
                    activity.startActivity(it);
                }
            });
        } else
            mView = (ImageView) container.getChildAt(position);
        return mView;
    }

    public ViewGroup getIndicator() {
        return mIndicator;
    }

}
