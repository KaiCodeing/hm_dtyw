package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.MessageActivity;
import com.hemaapp.dtyw.activity.OrderDetaiActivityl;
import com.hemaapp.dtyw.activity.RefundmentActivity;
import com.hemaapp.dtyw.model.Notice;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.view.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import xtom.frame.util.XtomTimeUtil;

/**
 * Created by lenovo on 2016/9/18.
 * 消息
 */
public class MessageAdapter extends HemaAdapter {
    private ArrayList<Notice> notices;
    private MessageActivity activity;
    private Context mContext;
    private String type;

    public MessageAdapter(Context mContext, ArrayList<Notice> notices, MessageActivity activity, String type) {
        super(mContext);
        this.mContext = mContext;
        this.notices = notices;
        this.activity = activity;
        this.type = type;
    }

    public void setNotices(ArrayList<Notice> notices) {
        this.notices = notices;
    }

    @Override
    public boolean isEmpty() {
        return notices == null || notices.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : notices.size();
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
            return  getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message, null);
            ViewHolder holder = new ViewHolder();
            findView(holder,convertView);
            convertView.setTag(R.id.TAG_VIEWHOLDER,holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
        setData(holder,position);
        return convertView;
    }

    private class ViewHolder {
        LinearLayout layout_in;
        RoundedImageView user_img;
        TextView user_name;
        TextView time_text;
        TextView message_content;
        View show_message;
    }

    private void findView(ViewHolder holder, View view) {
        holder.layout_in = (LinearLayout) view.findViewById(R.id.layout_in);
        holder.user_img = (RoundedImageView) view.findViewById(R.id.user_img);
        holder.user_name = (TextView) view.findViewById(R.id.user_name);
        holder.time_text = (TextView) view.findViewById(R.id.time_text);
        holder.message_content = (TextView) view.findViewById(R.id.message_content);
        holder.show_message = view.findViewById(R.id.show_message);
    }

    private void setData(ViewHolder holder, int position) {
        final Notice notice = notices.get(position);
        String path = notice.getAvatar();
        holder.user_img.setCornerRadius(100);
        DisplayImageOptions options;
        //系统消息
        if ("1".equals(type)) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.system_message_img)
                    .showImageForEmptyUri(R.mipmap.system_message_img)
                    .showImageOnFail(R.mipmap.system_message_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();
        } else {
            //  订单消息
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.dingdan_message_img)
                    .showImageForEmptyUri(R.mipmap.dingdan_message_img)
                    .showImageOnFail(R.mipmap.dingdan_message_img).cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565).build();

        }
        ImageLoader.getInstance().displayImage(path, holder.user_img, options);
        if (!isNull(notice.getNickname()))
            holder.user_name.setText(notice.getNickname());
        if (!isNull(notice.getRegdate()))
        {
            String regdate = XtomTimeUtil.TransTime(notice.getRegdate(),
                    "yyyy.MM.dd");
            holder.time_text.setText(regdate);
        }
        if (!isNull(notice.getContent()))
            holder.message_content.setText(notice.getContent());
        /**
         * 判断是否已读
         */
        if ("1".equals(notice.getLooktype()))
        {
            holder.show_message.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.show_message.setVisibility(View.GONE);
        }
        /**
         * 删除
         */
        holder.layout_in.setTag(R.id.TAG_VIEWHOLDER,notice);
        holder.layout_in.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Notice notice1 = (Notice) v.getTag(R.id.TAG_VIEWHOLDER);
                activity.deleteMessge(notice.getId());
                return false;
            }
        });
        /**
         * 详情
         */

        holder.layout_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Notice notice1 = (Notice) v.getTag(R.id.TAG_VIEWHOLDER);
                /**
                 * 判断是否已读
                 */
                if (notice.getLooktype().equals("1"))
                {
                    activity.yiduMessage(notice.getId());
                }
                else
                {
                    //跳转到详细信息
                  // activity.showTextDialog(notice.getId()+"跳转到详细信息");
                    //发货
                    log_i("+++++++++++++++++++++++"+notice1.getKeytype());
                    if ("2".equals(notice1.getKeytype()))
                    {
                        Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                        intent.putExtra("orderid",notice1.getKeyid());
                        intent.putExtra("keytype","3");
                        activity.startActivity(intent);
                    }
                    //退款完成
                    else if("5".equals(notice1.getKeytype()) || "3".equals(notice1.getKeytype()) || "4".equals(notice1.getKeytype()))
                    {
                        Intent intent = new Intent(activity, RefundmentActivity.class);
                        intent.putExtra("orderid",notice1.getKeyid());
                        intent.putExtra("cartid",notice.getCartid());
                        intent.putExtra("goodsid",notice.getGoodsid());
                        activity.startActivity(intent);
                    }
                    //确认收货
                    else if("8".equals(notice1.getKeytype()))
                    {
                        Intent intent = new Intent(activity, OrderDetaiActivityl.class);
                        intent.putExtra("orderid",notice1.getKeyid());
                        intent.putExtra("keytype","4");
                        activity.startActivity(intent);
                    }
                }
            }
        });
    }
}
