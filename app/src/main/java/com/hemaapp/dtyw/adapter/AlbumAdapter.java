package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hemaapp.dtyw.activity.AlbumActivity;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.hemaapp.hm_FrameWork.album.ImageLoader;
import com.hemaapp.hm_FrameWork.album.ImageLoader.Type;

import java.util.ArrayList;

import xtom.frame.util.XtomWindowSize;


public class AlbumAdapter extends HemaAdapter {
	private AlbumActivity mContext;
	private ArrayList<String> imgs;
	private int limit;
	private int count = 0;
	public String imgPath;

	private int mScreenWidth;
	private int item_width;

	public AlbumAdapter(Context mContext, ArrayList<String> imgs, int limit) {
		super(mContext);
		this.mContext = (AlbumActivity) mContext;
		this.imgs = imgs;
		this.limit = limit;

		mScreenWidth = XtomWindowSize.getWidth(mContext);
		item_width = (int) ((mScreenWidth / 4.0 + 0.5f));
	}

	@Override
	public int getCount() {
		return imgs.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.griditem_album, null);
			holder = new ViewHolder();
			findView(holder, convertView);
			convertView.setTag(R.id.TAG_VIEWHOLDER, holder);
		} else
			holder = (ViewHolder) convertView.getTag(R.id.TAG_VIEWHOLDER);
		String path = imgs.get(position);
		setData(holder, path);
		return convertView;
	}

	private void setData(final ViewHolder h, String path) {
		ImageLoader.getInstance(3, Type.LIFO).loadImage(path, h.img);
		h.img.setTag(R.id.TAG, path);
		h.img.setOnClickListener(new OnClickListener() {
			boolean isshow = false;

			@Override
			public void onClick(View v) {
				imgPath = (String) v.getTag(R.id.TAG);
				if (isshow) {
					h.box.setImageResource(R.mipmap.box_album_n);
					;
					count--;
					isshow = !isshow;
					mContext.changeHsv(imgPath, false);
				} else {
					if (count == limit) {
						mContext.showTextDialog("只能选择" + limit + "张图片");
					} else {
						h.box.setImageResource(R.mipmap.box_album_y);
						;
						count++;
						isshow = !isshow;
						mContext.changeHsv(imgPath, true);
					}
				}
			}
		});
	}

	private void findView(ViewHolder h, View v) {
		h.img = (ImageView) v.findViewById(R.id.album_img);
		h.box = (ImageView) v.findViewById(R.id.album_box);
		h.img.getLayoutParams().height = item_width;
	}

	private static class ViewHolder {
		ImageView img;
		ImageView box;
	}
}
