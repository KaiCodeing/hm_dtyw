package com.hemaapp.dtyw.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hemaapp.dtyw.activity.RefundActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;

import java.io.File;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;

/**
 * Created by lenovo on 2016/11/2.
 */
public class AddimgRefundAdapter extends HemaAdapter {
    private RefundActivity mContext;
    private ArrayList<String> imgs;
    private View view;
    //	private viewHolder holder;
    private static final int TYPE_ADD = 0;
    private static final int TYPE_IMAGE = 1;
    private String poster;
    //private static final int TYPE_POSTER = 2;
    private XtomImageWorker imgWorker;
    private ShowLargeImageView mView;
    public AddimgRefundAdapter(RefundActivity mContext, ArrayList<String> imgs, View view) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.imgs = imgs;
        this.view = view;
        imgWorker = new XtomImageWorker(mContext);
    }

    public String getPoster() {
        return poster;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        int count;
        int size = imgs == null ? 0 : imgs.size();
        if (size < 4)
            count = size + 1;
        else
            count = 4;
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        int size = imgs == null ? 0 : imgs.size();
        int count = getCount();

        if (size < 4) {
            if (position == count - 1)
                return TYPE_ADD;
            else
                return TYPE_IMAGE;
        } else
            return TYPE_IMAGE;

    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        int type = getItemViewType(position);
        viewHolder holder;
        if (convertView==null) {
            switch (type) {
                case TYPE_ADD:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_sendblog_add, null);
                    holder = new viewHolder();
                    holder.button = (ImageView) convertView.findViewById(R.id.button);
                    convertView.setTag(R.id.TAG,holder);
                    break;
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.griditem_sendblog_image, null);
                    holder = new viewHolder();
                    holder.delete = (ImageButton) convertView.findViewById(R.id.delete);
                    holder.imageview = (RoundedImageView) convertView.findViewById(R.id.imageview);
                    convertView.setTag(R.id.TAG,holder);
                    break;
                default:
                    break;
            }
        }
        holder = (viewHolder) convertView.getTag(R.id.TAG);
        switch (type) {
            case TYPE_ADD:
                setAdd(holder);
                break;
            case TYPE_IMAGE:
                setImage(holder, position);
                break;
            default:
                break;
        }
        return convertView;
    }
    private class viewHolder{
        ImageView button;//���
        RoundedImageView imageview;//ͼƬ
        ImageButton delete;//ɾ��
    }
    /**
     *
     * @方法名称: setAdd
     * @功能描述: TODO添加照片
     * @返回值: void
     */
    private void setAdd(viewHolder holder)
    {
        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mContext.showImageWay(1);
                //mContext.mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                InputMethodManager inputMethodManager = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken()
                        ,InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
    }
    /**
     *
     * @方法名称: setImage
     * @功能描述: TODO点击照片 查看或者删除
     * @param holder
     * @param position
     * @返回值: void
     */
    private void setImage(viewHolder holder,int position)
    {
        String path = imgs.get(position);
        imgWorker.loadImage(new XtomImageTask(holder.imageview, path, mContext));
        holder.imageview.setTag(R.id.TAG, path);
        holder.imageview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = (String) v.getTag(R.id.TAG);
                mView = new ShowLargeImageView(mContext, view);
                mView.setImagePath(s);
                mView.show();
            }
        });
        holder.delete.setTag(R.id.TAG, path);
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ss = (String) v.getTag(R.id.TAG);
                File file = new File(ss);
                file.delete();
                imgs.remove(ss);
                notifyDataSetChanged();
            }
        });
    }
}
