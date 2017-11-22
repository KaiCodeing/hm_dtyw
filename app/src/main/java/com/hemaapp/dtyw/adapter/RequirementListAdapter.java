package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.model.Requirement;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/2/28.
 * 需求汇总
 */
public class RequirementListAdapter extends HemaAdapter {
    private Context mContext;
    private ArrayList<Requirement> requirements;
    private String keytype;
    public RequirementListAdapter(Context mContext,ArrayList<Requirement> requirements,String keytype) {
        super(mContext);
        this.mContext = mContext;
        this.requirements = requirements;
        this.keytype = keytype;
    }

    @Override
    public boolean isEmpty() {
        return requirements==null || requirements.size()==0;
    }

    @Override
    public int getCount() {
        return isEmpty()?1:requirements.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_requirment, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setData(holder, position);
        return convertView;
    }
    private class ViewHolder{
        ImageView requset_img;
        TextView requset_title;
        TextView brand_name;
        TextView type_text;
        ImageView call_tel;
        View view_show;
    }
    private void findView(ViewHolder holder,View view)
    {
        holder.requset_img = (ImageView) view.findViewById(R.id.requset_img);
        holder.requset_title = (TextView) view.findViewById(R.id.requset_title);
        holder.brand_name = (TextView) view.findViewById(R.id.brand_name);
        holder.type_text = (TextView) view.findViewById(R.id.type_text);
        holder.call_tel = (ImageView) view.findViewById(R.id.call_tel);
        holder.view_show = view.findViewById(R.id.view_show);
    }
    private void setData(ViewHolder holder,int position)
    {
        final Requirement requirement = requirements.get(position);
        //商品图片
        String path = requirement.getImgurl();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(path, holder.requset_img, options);
        holder.requset_title.setText(requirement.getName());
        holder.brand_name.setText(requirement.getBrand());
        holder.type_text.setText(requirement.getProperty());
        //打电话
        holder.call_tel.setTag(R.id.TAG,requirement);
        holder.call_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requirement requirement1 = (Requirement) v.getTag(R.id.TAG);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + requirement1.getPhone());
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });
        if ("2".equals(keytype)) {
            holder.call_tel.setVisibility(View.GONE);
            holder.view_show.setVisibility(View.GONE);
        }
            else {
            holder.call_tel.setVisibility(View.VISIBLE);
            holder.view_show.setVisibility(View.VISIBLE);
        }
        if (isNull(XtomSharedPreferencesUtil.get(mContext,"username")))
        {}
        else
        {
            String userid = DtywApplication.getInstance().getUser().getId();
            if (userid.equals(requirement.getCid()))
            {
                holder.call_tel.setVisibility(View.GONE);
                holder.view_show.setVisibility(View.GONE);
            }
            else
            {
                holder.call_tel.setVisibility(View.VISIBLE);
                holder.view_show.setVisibility(View.VISIBLE);
            }
        }
    }
}
