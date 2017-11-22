package com.hemaapp.dtyw.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hemaapp.dtyw.activity.BrandActivity;
import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.activity.WebviewActivity;
import com.hemaapp.dtyw.fragment.HomeFragment;
import com.hemaapp.dtyw.model.AdList;
import com.hemaapp.dtyw.myapplication.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.image.load.XtomImageWorker;


public class TopAdAdapter extends PagerAdapter {
	private HomeFragment fragment;
	private RadioGroup mIndicator;
	private View view;
	private int size;
	private XtomImageWorker imgWorker;
	private ArrayList<AdList> indexads;

	public TopAdAdapter(HomeFragment fragment, RadioGroup mIndicator,
			View view, ArrayList<AdList> indexads) {
		this.fragment = fragment;
		this.mIndicator = mIndicator;
		this.view = view;
		this.indexads = indexads;
		imgWorker = new XtomImageWorker(fragment.getActivity());
		init();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return indexads.size();
	}

	private void init() {
		float density = fragment.getResources().getDisplayMetrics().density;
		size = (int) (density * 8);
		mIndicator = (RadioGroup) view.findViewById(R.id.radiogroup);
		mIndicator.removeAllViews();
		if (getCount() > 0)
			for (int i = 0; i < getCount(); i++) {
				RadioButton button = new RadioButton(fragment.getActivity());
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
		if (container.getChildAt(position) == null) {
			mView = new ImageView(fragment.getActivity());
			mView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			mView.setLayoutParams(params);
			container.addView(mView, position);
			AdList ad = indexads.get(position);
			mView.setImageResource(R.mipmap.default_bg2);
			try {
				XtomImageTask task = new XtomImageTask(mView, new URL(
						ad.getImgurl()), fragment.getActivity());
				imgWorker.loadImage(task);
			} catch (MalformedURLException e) {
			//	mView.setImageBitmap(null);
				mView.setImageResource(R.mipmap.default_bg2);
			}
			mView.setTag(R.id.TAG, ad);
			mView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if (DtywApplication.getInstance().getUser() == null)
//						return;
					AdList aa = (AdList) v.getTag(R.id.TAG);
					/**
					 * 内部链接
					 */
					if (aa.getJump_type().equals("1")) {
						Intent intent = new Intent(fragment.getActivity(), CommodityInforActivity.class);
						intent.putExtra("id", aa.getRelative_content());
						fragment.getActivity().startActivity(intent);

					}
					//外部链接
					else if (aa.getJump_type().equals("2")) {
						String sub = aa.getTypeurl().substring(0, 7);
						Uri uri;
						if (sub.equals("http://")) {
							uri = Uri.parse(aa.getTypeurl());
						} else {
							uri = Uri.parse("http://" + aa.getTypeurl());
						}

						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						fragment.getActivity().startActivity(it);
					}
					//图文广告
					else if (aa.getJump_type().equals("3")) {
						Intent intent = new Intent(fragment.getActivity(),
								WebviewActivity.class);
						intent.putExtra("id", aa.getId());
						intent.putExtra("type", "6");
					//	intent.putExtra("id", aa.getId());
						fragment.getActivity().startActivity(intent);
					}
					else if(aa.getJump_type().equals("4"))
					{
						Intent intent = new Intent(fragment.getActivity(), BrandActivity.class);
						intent.putExtra("brandId",aa.getRelative_content());
						intent.putExtra("brandName",aa.getBrandname());
						intent.putExtra("typekey","3");
						fragment.getActivity().startActivity(intent);
					}
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
